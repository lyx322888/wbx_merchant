package com.wbx.merchant.receiver;

import android.content.Intent;
import android.os.Bundle;
import com.alibaba.sdk.android.push.AndroidPopupActivity;
import com.tencent.mm.opensdk.utils.Log;
import com.wbx.merchant.MainActivity;
import com.wbx.merchant.activity.SplashActivity;
import com.wbx.merchant.baseapp.AppManager;
import com.wbx.merchant.utils.TtsUtil;
import java.util.Map;
//阿里推送辅助弹窗
public class PopupPushActivity extends AndroidPopupActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onSysNoticeOpened(String title, String summary, Map<String, String> extMap) {

        processCustomMessage(summary);

        finish();
        //打开自定义的Activity
        if (!AppManager.getAppManager().isOpenActivity(MainActivity.class)){
            startActivity(new Intent(this,SplashActivity.class));
        }
        Log.e("MyMessageReceiver", "Receive notification, title: " + title + ", summary: " + summary + ", extraMap: " + extMap);
    }
    //send msg to MainActivity
    private void processCustomMessage( String message) {
        String content = "";
        if (message == null) {
            return;
        }
        if (message.contains("{")) {
            com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(message);
            content = jsonObject.getString("message");
        } else {
            content = message;
        }
        TtsUtil.getInstance(this).startSpeak(content+"                            "+content);
    }
}
