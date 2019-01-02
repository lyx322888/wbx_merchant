package com.wbx.merchant.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;

import com.alibaba.fastjson.JSONObject;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wbx.merchant.R;
import com.wbx.merchant.baseapp.AppConfig;
import com.wbx.merchant.bean.WeChatBusEvent;
import com.wbx.merchant.utils.MyRxBus;
import com.wbx.merchant.utils.ToastUitl;
import com.wbx.merchant.widget.LoadingDialog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by wushenghui on 2017/11/17.
 */

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private IWXAPI api;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wx_entry);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads().detectDiskWrites().detectNetwork()
                .penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
                .penaltyLog().penaltyDeath().build());
        api = WXAPIFactory.createWXAPI(this, AppConfig.WX_APP_ID);
        api.handleIntent(getIntent(), this);
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }
    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        switch (baseResp.getType()){
            case 1:
                //授权
                SendAuth.Resp re = ((SendAuth.Resp) baseResp);
                String code = re.code;
                if(BaseResp.ErrCode.ERR_OK==baseResp.errCode){
                    getOpenID(code);
                }else{
                    ToastUitl.showShort("授权失败");
                    finish();
                }
                break;
            case 2:
                //分享 收藏等
                if(baseResp.errCode== BaseResp.ErrCode.ERR_OK){
                    ToastUitl.showShort("成功");
                    finish();
                }else{
                    ToastUitl.showShort("失败");
                    finish();
                }
                break;
        }
    }

    private void getOpenID(String code) {
        LoadingDialog.showDialogForLoading(WXEntryActivity.this,"读取信息中...",true);
        BufferedReader in = null;
        final StringBuilder result = new StringBuilder();
        try {
            //GET请求直接在链接后面拼上请求参数
            String mPath = "https://api.weixin.qq.com/sns/oauth2/access_token?";
            HashMap<String,String> mData = new HashMap<>();
            mData.put("appid", AppConfig.WX_APP_ID);
            mData.put("secret",AppConfig.WX_APP_SECRET);
            mData.put("code",code);
            mData.put("grant_type","authorization_code");
            for(String key:mData.keySet()){
                mPath += key + "=" + mData.get(key) + "&";
            }
            URL url = new URL(mPath);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            //Get请求不需要DoOutPut
            conn.setDoOutput(false);
            conn.setDoInput(true);
            //设置连接超时时间和读取超时时间
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            //连接服务器
            conn.connect();
            // 取得输入流，并使用Reader读取
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //关闭输入流
        finally{
            try{
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        JSONObject jsonObject = JSONObject.parseObject(result.toString());
        String openid = jsonObject.getString("openid");
        String access_token = jsonObject.getString("access_token");
        getWeChatUserInfo(openid,access_token);

    }
    //获得微信用户信息
    private void getWeChatUserInfo(String openId, String accessToken) {
        BufferedReader in = null;
        final StringBuilder result = new StringBuilder();
        try {
            //GET请求直接在链接后面拼上请求参数
            String mPath = "https://api.weixin.qq.com/sns/userinfo?";
            HashMap<String,String> mData = new HashMap<>();
            mData.put("access_token",accessToken);
            mData.put("openid",openId);
            for(String key:mData.keySet()){
                mPath += key + "=" + mData.get(key) + "&";
            }
            URL url = new URL(mPath);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            //Get请求不需要DoOutPut
            conn.setDoOutput(false);
            conn.setDoInput(true);
            //设置连接超时时间和读取超时时间
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            //连接服务器
            conn.connect();
            // 取得输入流，并使用Reader读取
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //关闭输入流
        finally{
            try{
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
            LoadingDialog.cancelDialogForLoading();
            JSONObject jsonObject = JSONObject.parseObject(result.toString());
            String nickname = jsonObject.getString("nickname");
            MyRxBus.getInstance().post(new WeChatBusEvent(openId,nickname));
            finish();

        }
    }
}
