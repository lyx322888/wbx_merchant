package com.wbx.merchant.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.alipay.sdk.app.PayTask;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wbx.merchant.R;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.base.BaseApplication;
import com.wbx.merchant.baseapp.AppConfig;
import com.wbx.merchant.baseapp.AppManager;
import com.wbx.merchant.bean.PayResult;
import com.wbx.merchant.bean.WxPayInfo;
import com.wbx.merchant.utils.FormatUtil;
import com.wbx.merchant.utils.SPUtils;
import com.wbx.merchant.utils.ToastUitl;
import com.wbx.merchant.widget.LoadingDialog;

import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import rx.Observable;

/**
 * Created by wushenghui on 2017/6/24.
 */

public class PayActivity extends BaseActivity {
    private IWXAPI msgApi;
    private PayReq request;
    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_CHECK_FLAG = 2;
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

                        if (isRenew || isDaDa) {
                            //续费支付||达达充值
                            finish();
                        } else {
                            getServiceEndTime();//支付成功 获取服务到期时间
                        }
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

        ;
    };
    private String payMode;
    @Bind(R.id.ali_pay_im)
    ImageView aliPayIm;
    @Bind(R.id.wx_pay_im)
    ImageView wxPayIm;
    @Bind(R.id.grade_name_tv)
    TextView gradeNameTv;
    @Bind(R.id.need_pay_tv)
    TextView needPayTv;
    private int gradeId;
    private double needPay;
    private boolean isRenew;
    @Bind(R.id.is_renew_layout)
    LinearLayout renewLayout;
    @Bind(R.id.service_date_end_time_tv)
    TextView endTimeTv;
    private boolean isDaDa;
    @Bind(R.id.input_money_edit_text)
    EditText moneyEditText;
    private int shopGradeId;

    @Override
    public int getLayoutId() {
        return R.layout.activity_pay;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        request = new PayReq();
        msgApi = WXAPIFactory.createWXAPI(mActivity, AppConfig.WX_APP_ID);
    }

    @Override
    public void fillData() {
        isDaDa = getIntent().getBooleanExtra("isDaDa", false);
        if (!isDaDa) {
            moneyEditText.setVisibility(View.GONE);
//            gradeId = getIntent().getIntExtra("gradeId", 0);
            isRenew = getIntent().getBooleanExtra("isRenew", false);
            shopGradeId = getIntent().getIntExtra("shopGradeId", 0);
            needPayTv.setText(String.format("¥%.2f", getIntent().getIntExtra("select_money", 1) / 100.00));
            gradeNameTv.setText(getIntent().getStringExtra("gradeName"));
            if (isRenew) {
                endTimeTv.setText(FormatUtil.stampToDate(userInfo.getEnd_date() + ""));
                renewLayout.setVisibility(View.VISIBLE);
            }
            AppConfig.RESULT_PAY_TYPE = isRenew;
            if (shopGradeId == 0) {
                getStoreType();
            }
        } else {
            //达达充值
            AppConfig.RESULT_PAY_TYPE = true;
            gradeNameTv.setVisibility(View.GONE);
            needPayTv.setVisibility(View.GONE);
        }
    }

    private void getStoreType() {
        LoadingDialog.showDialogForLoading(mActivity, "加载中...", true);
        new MyHttp().doPost(Api.getDefault().getShopCate(SPUtils.getSharedIntData(mContext, "gradeId")), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                JSONObject data = result.getJSONObject("data");
                JSONObject shopGradeData = JSONObject.parseObject(data.getString("shop_grade"));
                gradeNameTv.setText(shopGradeData.getString("grade_name"));
                needPayTv.setText("¥" + shopGradeData.getIntValue("money") / 100.00);
                needPay = shopGradeData.getIntValue("money") / 100.00;
//                cateInfoList.addAll(JSONArray.parseArray(data.getString("cates"), CateInfo.class)) ;
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
                if (TextUtils.isEmpty(payMode)) {
                    showShortToast("请选择支付方式");
                    return;
                }
                if (isDaDa) {
                    if (TextUtils.isEmpty(moneyEditText.getText().toString())) {
                        ToastUitl.showShort("请输入金额");
                        return;
                    }
                    double money = Double.valueOf(moneyEditText.getText().toString());
//                    if(money<20){
//                        showShortToast("充值金额不能小于20元");
//                        return;
//                    }
                    goPay(Api.getDefault().daDaRecharge(userInfo.getSj_login_token(), payMode, money));
                } else {
                    goPay(Api.getDefault().goPay(userInfo.getSj_login_token(), SPUtils.getSharedIntData(mContext, "gradeId"), payMode, isRenew ? AppConfig.PAY_TYPE.RENEW : AppConfig.PAY_TYPE.APPLY, getIntent().getIntExtra("shopGradeId", 0)));
                }

                break;
        }
    }

    //支付
    private void goPay(Observable<JSONObject> observable) {
        new MyHttp().doPost(observable, new HttpListener() {
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
                AppManager.getAppManager().finishAllActivity();
                startActivity(new Intent(mContext, AuditingActivity.class));
            }

            @Override
            public void onError(int code) {

            }
        });

    }
}
