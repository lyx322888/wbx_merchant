package com.wbx.merchant.utils;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wbx.merchant.base.BaseApplication;
import com.wbx.merchant.baseapp.AppConfig;
import com.wbx.merchant.bean.PayResult;
import com.wbx.merchant.bean.WxPayInfo;


import java.lang.ref.WeakReference;
import java.util.Map;

//支付工具
public class PayUtils {
    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_CHECK_FLAG = 2;
    private MyHandler mHandler ;
    private WeakReference<Activity> mActivity;

    public PayUtils(Activity activity, OnAlipayListener onAlipayListener) {
        mActivity = new WeakReference<>(activity);
        mHandler =new MyHandler(onAlipayListener);

    }
    //微信支付
    public void startWxPay(WxPayInfo data){
        PayReq request = new PayReq();
        IWXAPI msgApi = WXAPIFactory.createWXAPI(BaseApplication.getInstance(), AppConfig.WX_APP_ID);
        request.appId = AppConfig.WX_APP_ID;
        request.partnerId = data.getMch_id();
        request.prepayId = data.getPrepay_id();
        request.packageValue = "Sign=WXPay";
        request.nonceStr = data.getNonce_str();
        request.timeStamp = data.getTime() + "";
        request.sign = data.getTwo_sign();
        //注册到微信
        msgApi.registerApp(AppConfig.WX_APP_ID);
        msgApi.sendReq(request);
    }

    //阿里支付
    public  void startAliPay(final String payV2){
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                if (mActivity!=null){
                    PayTask alipay = new PayTask(mActivity.get());
                    Map<String, String> payResult = alipay.payV2(payV2, true);
                    Message msg = new Message();
                    msg.what = SDK_PAY_FLAG;
                    msg.obj = payResult;
                    mHandler.sendMessage(msg);
                }
            }
        };
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    private static class MyHandler extends Handler {
        private OnAlipayListener onAlipayListener;
        public MyHandler(OnAlipayListener onAlipayListener) {
            this.onAlipayListener = onAlipayListener;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        if (onAlipayListener!=null){
                            onAlipayListener.onSuccess();
                        }
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            ToastUitl.showShort("支付结果确认中");
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            ToastUitl.showShort("支付失败");
                        }
                    }
                    break;

                }
                case SDK_CHECK_FLAG: {
                    ToastUitl.showShort("检查结果为：" + msg.obj);
                    break;
                }
                default:
                    break;
            }
        }
    }

    /**
     * 支付回调接口
     *
     * @author lenovo
     *
     */
    public abstract static class OnAlipayListener {
        /**
         * 支付成功
         */
        public abstract void onSuccess() ;

        /**
         * 支付取消
         */
        public void onCancel() {}

        /**
         * 等待确认
         */
        public void onWait() {}
    }

    //清除
    public void clear(){
        if (mHandler!=null){
            mHandler.removeCallbacksAndMessages(null);
        }
        mActivity = null;
    }
}
