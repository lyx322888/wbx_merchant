package com.wbx.merchant.base;

import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDexApplication;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.squareup.leakcanary.LeakCanary;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;
import com.wbx.merchant.BuildConfig;
import com.wbx.merchant.baseapp.ThreadPoolManager;
import com.wbx.merchant.bean.UserInfo;
import com.wbx.merchant.chat.BaseManager;
import com.wbx.merchant.utils.LogUtils;
import com.wbx.merchant.widget.refresh.AppRefreshFoot;
import com.wbx.merchant.widget.refresh.AppRefreshHead;

import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import java.util.List;

import cn.jpush.android.api.JPushInterface;


/**
 * Created by wushenghui on 2017/6/20.
 */

public class BaseApplication extends MultiDexApplication {
    private static BaseApplication instance;
    private static long appInitTime = System.currentTimeMillis();

    //单例模式中获取唯一的MyApplication实例
    public static BaseApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        instance = this;
        init();
    }

    private void init() {
        ThreadPoolManager.execute(new Runnable() {
            @Override
            public void run() {
                ZXingLibrary.initDisplayOpinion(instance);
                BaseManager.initOpenHelper(instance);
            }
        });
        initInUiThread();
    }

    private void initInUiThread() {
//        LeakCanary.install(this);
        LogUtils.logInit(BuildConfig.LOG_DEBUG);
//        initHxChat();
        initJPush();
        initXunFei();
        initRefresh();
    }

    private void initRefresh() {
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @NonNull
            @Override
            public RefreshHeader createRefreshHeader(@NonNull Context context, @NonNull RefreshLayout layout) {
                AppRefreshHead appRefreshHead = new AppRefreshHead(context);
                return appRefreshHead;
            }
        });
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @NonNull
            @Override
            public RefreshFooter createRefreshFooter(@NonNull Context context, @NonNull RefreshLayout layout) {
                AppRefreshFoot appRefreshFoot = new AppRefreshFoot(context);
                return appRefreshFoot;
            }
        });
    }

    /**
     * 初始化讯飞
     */
    private void initXunFei() {
        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=5ad82bdf");
    }


    //初始化极光
    private void initJPush() {
        JPushInterface.setDebugMode(true);    // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);            // 初始化 JPush
    }

    public long getAppInitTime() {
        return appInitTime;
    }

    public String getVersion() {
        PackageManager manager = this.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(this.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return info.versionName;

    }

    /**
     * 读取保存的登陆用户
     *
     * @return
     */
    public UserInfo readLoginUser() {
        SharedPreferences preferences = getSharedPreferences("base64", MODE_PRIVATE);
        String productBase64 = preferences.getString("user", "");
        if (productBase64 == "") {
            return null;
        }

        // 读取字节
        byte[] base64 = Base64.decodeBase64(productBase64.getBytes());

        // 封装到字节流
        ByteArrayInputStream bais = new ByteArrayInputStream(base64);
        try {
            // 再次封装
            ObjectInputStream bis = new ObjectInputStream(bais);
            // 读取对象
            return (UserInfo) bis.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 保存的登陆用户
     *
     * @return
     */
    public void saveUserInfo(UserInfo user) {
        SharedPreferences preferences = getSharedPreferences("base64", MODE_PRIVATE);

        // 创建字节输出流
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            // 创建对象输出流，并封装字节流
            ObjectOutputStream oos = new ObjectOutputStream(baos);

            // 将对象写入字节流
            oos.writeObject(user);

            // 将字节流编码成base64的字符窜
            String productBase64 = new String(Base64.encodeBase64(baos.toByteArray()));

            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("user", productBase64);

            editor.commit();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }
}
