package com.wbx.merchant.base;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.huawei.HuaWeiRegister;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.alibaba.sdk.android.push.register.MeizuRegister;
import com.alibaba.sdk.android.push.register.MiPushRegister;
import com.alibaba.sdk.android.push.register.OppoRegister;
import com.alibaba.sdk.android.push.register.VivoRegister;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.commonsdk.UMConfigure;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;
import com.wbx.merchant.BuildConfig;
import com.wbx.merchant.R;
import com.wbx.merchant.baseapp.AppConfig;
import com.wbx.merchant.baseapp.ThreadPoolManager;
import com.wbx.merchant.bean.UserInfo;
import com.wbx.merchant.chat.BaseManager;
import com.wbx.merchant.utils.APPUtil;
import com.wbx.merchant.utils.LogUtils;
import com.wbx.merchant.utils.SystemUtils;
import com.wbx.merchant.widget.MediaLoader;
import com.wbx.merchant.widget.refresh.AppRefreshFoot;
import com.wbx.merchant.widget.refresh.AppRefreshHead;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumConfig;

import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

import cn.jpush.android.api.JPushInterface;
import me.jessyan.autosize.AutoSizeConfig;


/**
 * Created by wushenghui on 2017/6/20.
 */

public class BaseApplication extends MultiDexApplication {
    private static BaseApplication instance;
    private static long appInitTime = System.currentTimeMillis();
    public static boolean isxqwdb = false;

    //单例模式中获取唯一的MyApplication实例
    public static BaseApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        init();
        closeAndroidPDialog();

    }

    /**
     * 关闭Android P 使用没系统API弹窗提示
     */
    private void closeAndroidPDialog() {
        try {
            Class aClass = Class.forName("android.content.pm.PackageParser$Package");
            Constructor declaredConstructor = aClass.getDeclaredConstructor(String.class);
            declaredConstructor.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Class cls = Class.forName("android.app.ActivityThread");
            Method declaredMethod = cls.getDeclaredMethod("currentActivityThread");
            declaredMethod.setAccessible(true);
            Object activityThread = declaredMethod.invoke(null);
            Field mHiddenApiWarningShown = cls.getDeclaredField("mHiddenApiWarningShown");
            mHiddenApiWarningShown.setAccessible(true);
            mHiddenApiWarningShown.setBoolean(activityThread, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

        isxqwdb = TextUtils.equals(SystemUtils.getAppMetaData(this, AppConfig.UMENG_CHANNEL),"xqwdb");

        //屏幕适配
        AutoSizeConfig.getInstance()
                .setBaseOnWidth(true)
                .getUnitsManager()
                .setDesignHeight(667)
                .setSupportDP(true)
                .setSupportSP(true);
    }

    private void initInUiThread() {
        LogUtils.logInit(BuildConfig.LOG_DEBUG);
//        initJPush();
        initXunFei();
        initRefresh();
        initBugly();
        initAlbum();
        initUMeng();
        initCloudChannel(this);
    }
    /**
     * 初始化云推送通道
     * @param applicationContext
     */
    private void initCloudChannel(Context applicationContext) {
        createNotificationChannel();
        PushServiceFactory.init(applicationContext);
        CloudPushService pushService = PushServiceFactory.getCloudPushService();
        pushService.register(applicationContext, new CommonCallback() {
            @Override
            public void onSuccess(String response) {
                Log.d("dfdf", "init cloudchannel success");
                //注册华为推送
                HuaWeiRegister.register(BaseApplication.this);
                //小米
                MiPushRegister.register(BaseApplication.this, "2882303761517594729", "5161759426729");
                //GCM/FCM辅助通道注册
//                GcmRegister.register(this, sendId, applicationId); //sendId/applicationId为步骤获得的参数
                // OPPO通道注册
                OppoRegister.register(BaseApplication.this, "f22NrG5t1Hc0g84kWG4s0k80K", "2841d9Ac5Ded5791D6d82f8361B745da"); // appKey/appSecret在OPPO开发者平台获取
                // 魅族通道注册
                MeizuRegister.register(BaseApplication.this, "130756", "9476d8bd52b14dbb986ff6082a9da59d"); // appId/appkey在魅族开发者平台获取
                // VIVO通道注册
                VivoRegister.register(BaseApplication.this);
            }
            @Override
            public void onFailed(String errorCode, String errorMessage) {
                Log.d("dfdf", "init cloudchannel failed -- errorcode:" + errorCode + " -- errorMessage:" + errorMessage);
            }
        });

    }
    //初始化友盟
    private void initUMeng(){
        UMConfigure.setLogEnabled(true);
        UMConfigure.init(this, "5e1eb3e8570df3d4ce0004f8", "Umeng", UMConfigure.DEVICE_TYPE_PHONE, null);
    }
    //图片选择框架
    private void initAlbum() {
        Album.initialize(AlbumConfig.newBuilder(this)
                .setAlbumLoader(new MediaLoader()).build());
    }

    private void initBugly() {
        Context context = getApplicationContext();
        // 获取当前包名
        String packageName = context.getPackageName();
        // 获取当前进程名
        String processName = APPUtil.getProcessName(android.os.Process.myPid());
        // 设置是否为上报进程
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
        strategy.setUploadProcess(processName == null || processName.equals(packageName));
        strategy.setAppReportDelay(20000);
        // 初始化Bugly
        CrashReport.initCrashReport(context, "9cdab1f35e", false, strategy);
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
        JPushInterface.setDebugMode(false);    // 设置开启日志,发布时请关闭日志
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

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }


    //通知
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            // 通知渠道的id
            String id = "1";
            // 用户可以看到的通知渠道的名字.
            CharSequence name = "notification channel";
            // 用户可以看到的通知渠道的描述
            String description = "notification description";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(id, name, importance);
            // 配置通知渠道的属性
            mChannel.setDescription(description);
            // 设置通知出现时的闪灯（如果 android 设备支持的话）
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            // 设置通知出现时的震动（如果 android 设备支持的话）
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            //最后在notificationmanager中创建该通知渠道
            mNotificationManager.createNotificationChannel(mChannel);
        }
    }
}
