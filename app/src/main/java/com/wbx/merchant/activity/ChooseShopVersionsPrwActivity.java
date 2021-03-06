package com.wbx.merchant.activity;


import android.content.Intent;
import android.database.Observable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.alipay.sdk.app.PayTask;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wbx.merchant.MainActivity;
import com.wbx.merchant.R;
import com.wbx.merchant.adapter.ChooseShopVersionsAdapter;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.base.BaseApplication;
import com.wbx.merchant.baseapp.AppConfig;
import com.wbx.merchant.baseapp.AppManager;
import com.wbx.merchant.bean.ChooseShopVersionsBean;
import com.wbx.merchant.bean.PayResult;
import com.wbx.merchant.bean.WxPayInfo;
import com.wbx.merchant.common.LoginUtil;
import com.wbx.merchant.utils.SPUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChooseShopVersionsPrwActivity extends BaseActivity {

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.rv_shop_versions)
    RecyclerView rvShopVersions;
    @Bind(R.id.ali_pay_im)
    ImageView aliPayIm;
    @Bind(R.id.ali_pay_layout)
    LinearLayout aliPayLayout;
    @Bind(R.id.wx_pay_im)
    ImageView wxPayIm;
    @Bind(R.id.wx_pay_layout)
    LinearLayout wxPayLayout;
    @Bind(R.id.pay_btn)
    TextView payBtn;
    private ChooseShopVersionsAdapter chooseShopVersionsAdapter;
    private ChooseShopVersionsBean.DataBean dataBean;//选择项
    private String payMode;
    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_CHECK_FLAG = 2;
    private IWXAPI msgApi;
    private PayReq request;
    private Handler mHandler = new Handler() {
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

                            getServiceEndTime();//支付成功 获取服务到期时间
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。

                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            showShortToast("支付结果确认中");

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            showShortToast("支付失败");
                        }
                    }
                    break;
                }
                case SDK_CHECK_FLAG: {
                    showShortToast("检查结果为：" + msg.obj);
                    break;
                }
                default:
                    break;
            }
        }
    };
    @Override
    public int getLayoutId() {
        setStatubarColor(R.color.white);
        return R.layout.activity_choose_shop_versions_prw;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        request = new PayReq();
        msgApi = WXAPIFactory.createWXAPI(mActivity, AppConfig.WX_APP_ID);
        rvShopVersions.setLayoutManager(new LinearLayoutManager(mContext){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        chooseShopVersionsAdapter = new ChooseShopVersionsAdapter();
        rvShopVersions.setAdapter(chooseShopVersionsAdapter);
        chooseShopVersionsAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                chooseShopVersionsAdapter.chooseVersion(position);
                switch (position){
                    case 0:
                        payBtn.setText("确认支付");
                        break;
                    case 1:
                        payBtn.setText("确认支付 ￥" + chooseShopVersionsAdapter.getData().get(position).getMoney() / 100);
                        break;
                    case 2:
                        payBtn.setText("确认支付");
                        break;
                }
                dataBean = chooseShopVersionsAdapter.getItem(position);
            }
        });
    }

    @Override
    public void fillData() {
        new MyHttp().doPost(Api.getDefault().get_shop_grade_info_two(LoginUtil.getLoginToken()), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                ChooseShopVersionsBean bean = new Gson().fromJson(result.toString(), ChooseShopVersionsBean.class);
                chooseShopVersionsAdapter.setNewData(bean.getData());
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    @Override
    public void setListener() {

    }

    @OnClick({R.id.ali_pay_layout, R.id.wx_pay_layout, R.id.pay_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ali_pay_layout:
                payMode = AppConfig.PayMode.alipay;
                aliPayIm.setImageResource(R.drawable.ic_ok);
                wxPayIm.setImageResource(R.drawable.ic_round);
                break;
            case R.id.wx_pay_layout:
                payMode = AppConfig.PayMode.wxpay;
                aliPayIm.setImageResource(R.drawable.ic_round);
                wxPayIm.setImageResource(R.drawable.ic_ok);
                break;
            case R.id.pay_btn:
                if(dataBean==null){
                    showShortToast("请选择版本");
                    return;
                }
                if (dataBean.getGrade_type()==1){
                    SPUtils.setSharedBooleanData(mContext, AppConfig.LOGIN_STATE, true);
                    AppManager.getAppManager().finishAllActivity();
                    startActivity(new Intent(mContext, MainActivity.class));
                }else if (dataBean.getGrade_type()==4){
                    //支付码支付
                    if (!TextUtils.isEmpty(dataBean.getPayment_code())){
                        payment_code();
                    }else {
                        showShortToast("请输入支付码");
                    }
                }else {
                    if (TextUtils.isEmpty(payMode)) {
                        showShortToast("请选择支付方式");
                        return;
                    }
                    goPay();
                }

                break;
        }
    }

     private void payment_code(){
         HashMap<String,Object> hashMap = new HashMap<>();
         hashMap.put("sj_login_token",LoginUtil.getLoginToken());
         hashMap.put("type","apply");
         hashMap.put("shop_grade",dataBean.getShop_grade());
         hashMap.put("grade_type",dataBean.getGrade_type());
         hashMap.put("payment_code",dataBean.getPayment_code());
        new MyHttp().doPost(Api.getDefault().payment_code_pay(hashMap), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                getServiceEndTime();
            }

            @Override
            public void onError(int code) {

            }
        });
     }

    //支付
    private void goPay() {
        new MyHttp().doPost(Api.getDefault().goPayKdp(userInfo.getSj_login_token(), payMode,"apply", dataBean.getShop_grade(),dataBean.getGrade_type()), new HttpListener() {
            @Override
            public void onSuccess(final JSONObject result) {
                if (payMode.equals(AppConfig.PayMode.alipay)) {
                    //支付宝支付
                    Runnable payRunnable = new Runnable() {
                        @Override
                        public void run() {
                            PayTask alipay = new PayTask(mActivity);
                            Map<String, String> payResult = alipay.payV2(result.getString("data"), true);
                            Message msg = new Message();
                            msg.what = SDK_PAY_FLAG;
                            msg.obj = payResult;
                            mHandler.sendMessage(msg);
                        }
                    };
                    Thread payThread = new Thread(payRunnable);
                    payThread.start();

                } else {
                    WxPayInfo wxPayInfo = result.getObject("data", WxPayInfo.class);
                    startWxPay(wxPayInfo);
                }

            }

            @Override
            public void onError(int code) {

            }
        });
    }

    //微信支付
    private void startWxPay(WxPayInfo wxPayInfo) {
        genPayReq(wxPayInfo);
        //注册到微信
        msgApi.registerApp(AppConfig.WX_APP_ID);
        msgApi.sendReq(request);
    }

    private void genPayReq(WxPayInfo wxPayInfo) {
        request.appId = AppConfig.WX_APP_ID;
        request.partnerId = wxPayInfo.getMch_id();
        request.prepayId = wxPayInfo.getPrepay_id();
        request.packageValue = "Sign=WXPay";
        request.nonceStr = wxPayInfo.getNonce_str();
        request.timeStamp = wxPayInfo.getTime() + "";
        request.sign = wxPayInfo.getTwo_sign();
    }

    //支付成功，获取服务到期时间
    private void getServiceEndTime() {
        new MyHttp().doPost(Api.getDefault().getServiceEndTime(userInfo.getSj_login_token(), userInfo.getShop_id()), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                JSONObject data = result.getJSONObject("data");
                userInfo.setEnd_date(data.getIntValue(data.getString("end_date")));
                BaseApplication.getInstance().saveUserInfo(userInfo);
                SPUtils.setSharedBooleanData(mContext, AppConfig.LOGIN_STATE, true);
                AppManager.getAppManager().finishAllActivity();
                startActivity(new Intent(mContext, MainActivity.class));
            }

            @Override
            public void onError(int code) {

            }
        });

    }
}
