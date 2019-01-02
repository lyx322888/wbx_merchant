package com.wbx.merchant.base;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDexApplication;

import com.bugtags.library.Bugtags;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.model.EaseNotifier;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.easeui.utils.UserDao;
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
import com.wbx.merchant.R;
import com.wbx.merchant.activity.ChatActivity;
import com.wbx.merchant.api.ApiConstants;
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
        //        Bugtags.start("d5c298ea42ab257e459d766eec29543b", this, Bugtags.BTGInvocationEventBubble);
        Bugtags.start("d5c298ea42ab257e459d766eec29543b", instance, ApiConstants.DEBUG ? Bugtags.BTGInvocationEventBubble : Bugtags.BTGInvocationEventNone);
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

    //初始化 环信
    private void initHxChat() {
        EaseUI instance = EaseUI.getInstance();

        EMOptions options = new EMOptions();
        // 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);
        options.setAutoLogin(true);
        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
        // 如果APP启用了远程的service，此application:onCreate会被调用2次
        // 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
        // 默认的APP会在以包名为默认的process name下运行，如果查到的process name不是APP的process name就立即返回
        if (processAppName == null || !processAppName.equalsIgnoreCase(this.getPackageName())) {
            // 则此application::onCreate 是被service 调用的，直接返回
            return;
        }
        //初始化
        instance.init(this, options);
        instance.setUserProfileProvider(new EaseUI.EaseUserProfileProvider() {
            @Override
            public EaseUser getUser(String username) {
                EaseUser easeUser = new EaseUser(username);

//
//                if(null!=userInfo){
//                    easeUser.setAvatar(userInfo.getFace());
//                    easeUser.setNickname(userInfo.getNickname());
//                }
                return getUserInfo(username);
            }
        });
        //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(false);

        EaseUI.getInstance().getNotifier().setNotificationInfoProvider(new EaseNotifier.EaseNotificationInfoProvider() {
            @Override
            public String getDisplayedText(EMMessage message) {
                String ticker = EaseCommonUtils.getMessageDigest(message, BaseApplication.instance);
                if (message.getType() == EMMessage.Type.TXT) {
                    ticker = ticker.replaceAll("\\[.{2,3}\\]", "[表情]");
                }
                EaseUser user = getUserInfo(message.getFrom());
                if (user != null) {
                    return getUserInfo(message.getFrom()).getNick() + ": " + ticker;
                } else {
                    return message.getFrom() + ": " + ticker;
                }
            }

            @Override
            public String getLatestText(EMMessage message, int fromUsersNum, int messageNum) {
                return String.format("用户%s发来%d条消息，请注意查收！", getUserInfo(message.getFrom()), messageNum);
            }

            @Override
            public String getTitle(EMMessage message) {
                return null;
            }

            @Override
            public int getSmallIcon(EMMessage message) {
                return R.drawable.ic_launcher;
            }

            @Override
            public Intent getLaunchIntent(EMMessage message) {
                Intent intent = new Intent(BaseApplication.instance, ChatActivity.class);
                intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
                intent.putExtra(EaseConstant.EXTRA_USER_ID, message.getFrom());
                return intent;
            }
        });
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

    private EaseUser getUserInfo(String username) {
        EaseUser easeUser = new EaseUser(username);
        UserInfo userInfo = readLoginUser();
        if (username.equals(userInfo.getHx_username())) {
            easeUser.setNickname(userInfo.getNickname());
            easeUser.setAvatar(userInfo.getFace());
        } else {
            UserDao userDao = new UserDao(getApplicationContext());
            userDao.openDataBase();
            easeUser = userDao.queryData(username);
        }
        return easeUser;
    }
}
