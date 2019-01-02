package com.wbx.merchant.activity;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wbx.merchant.R;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.baseapp.AppConfig;
import com.wbx.merchant.bean.WeChatBusEvent;
import com.wbx.merchant.utils.FormatUtil;
import com.wbx.merchant.utils.MyRxBus;
import com.wbx.merchant.utils.ToastUitl;
import com.wbx.merchant.widget.LoadingDialog;

import butterknife.Bind;
import butterknife.OnClick;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by wushenghui on 2017/11/7.
 * 绑定支付平台 提现到账账号
 */

public class BindPayPlatformActivity extends BaseActivity {
    @Bind(R.id.pay_account_tv)
    TextView payAccountTv;
    @Bind(R.id.pay_account_edit)
    EditText etAliAccount;
    @Bind(R.id.name_edit)
    EditText nameEdit;
    @Bind(R.id.phone_edit)
    EditText phoneEdit;
    @Bind(R.id.sms_code_edit)
    EditText smsCodeEdit;
    @Bind(R.id.get_code_btn)
    TextView getCodeBtn;
    private boolean isWXPay;//是否是添加微信账号
    private IWXAPI wxapi;
    @Bind(R.id.we_chat_accredit_layout)
    LinearLayout weChatAccreditLayout;
    @Bind(R.id.we_chat_nickname_tv)
    TextView weChatNickNameTv;
    private Subscription subscribe;
    @Bind(R.id.ali_layout)
    LinearLayout aliLayout;
    private String openid;
    private String withdraw_id;

    public static void actionStart(Context context, boolean isBindWx) {
        Intent intent = new Intent(context, BindPayPlatformActivity.class);
        intent.putExtra("isBindWx", isBindWx);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_bind_pay_plat_form;
    }

    @Override
    public void initPresenter() {
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        wxapi = WXAPIFactory.createWXAPI(this, AppConfig.WX_APP_ID, true);
        // 将该app注册到微信
        wxapi.registerApp(AppConfig.WX_APP_ID);
    }

    @Override
    public void initView() {
        isWXPay = getIntent().getBooleanExtra("isBindWx", false);
        if (isWXPay) {
            weChatAccreditLayout.setVisibility(View.VISIBLE);
            aliLayout.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        if (!subscribe.isUnsubscribed()) {
            subscribe.unsubscribe();
        }
        super.onDestroy();
    }

    @Override
    public void fillData() {
        subscribe = MyRxBus.getInstance().toObserverable(WeChatBusEvent.class)
                .subscribe(new Action1<WeChatBusEvent>() {
                    @Override
                    public void call(WeChatBusEvent weChatBusEvent) {
                        weChatNickNameTv.setText(weChatBusEvent.getNickName());
                        openid = weChatBusEvent.getOpenId();
                    }
                });
        getPayInfo();
    }

    //获取绑定账号信息
    private void getPayInfo() {
        LoadingDialog.showDialogForLoading(BindPayPlatformActivity.this, "获取数据中...", true);
        new MyHttp().doPost(isWXPay ? Api.getDefault().getWXPayInfo(userInfo.getSj_login_token())
                : Api.getDefault().getAliPayInfo(userInfo.getSj_login_token()), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                JSONObject data = result.getJSONObject("data");
                try {
                    String account = data.getString(isWXPay ? "nick_name" : "alipay_account");
                    if (isWXPay) {
                        openid = data.getString("openid");
                        weChatNickNameTv.setText(account);
                    } else {
                        etAliAccount.setText(account);
                    }
                    String name = data.getString(isWXPay ? "weixinpay_name" : "alipay_name");
                    nameEdit.setText(name);
                    withdraw_id = data.getString("withdraw_id");
                } catch (NullPointerException e) {

                }
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    @Override
    public void setListener() {

    }

    @OnClick({R.id.bind_submit_btn, R.id.get_code_btn, R.id.we_chat_accredit_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bind_submit_btn:
                if (canSubmit()) {
                    submit();
                }
                break;
            case R.id.get_code_btn:
                getSmsCode();
                break;
            case R.id.we_chat_accredit_layout:
                //微信授权
                SendAuth.Req req = new SendAuth.Req();
                req.scope = "snsapi_userinfo";
                req.state = "state";
                wxapi.sendReq(req);
                break;
        }
    }

    private CountDownTimer timer = new CountDownTimer(60000, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {
            getCodeBtn.setEnabled(false);
            getCodeBtn.setText((millisUntilFinished / 1000) + "秒后可重发");
        }

        @Override
        public void onFinish() {
            getCodeBtn.setEnabled(true);
            getCodeBtn.setText("获取验证码");
        }
    };

    //获取验证码
    private void getSmsCode() {
        if (TextUtils.isEmpty(phoneEdit.getText().toString())) {
            showShortToast("请输入电话号码！");
            return;
        }
        if (!FormatUtil.isMobileNO(phoneEdit.getText().toString())) {
            showShortToast("请输入正确的电话号码！");
            return;
        }
        new MyHttp().doPost(Api.getDefault().sendCode(phoneEdit.getText().toString()), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                timer.start();
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    //添加账号
    private void submit() {
        String name = nameEdit.getText().toString();
        String phone = phoneEdit.getText().toString();
        String code = smsCodeEdit.getText().toString();
        if (isWXPay) {
            new MyHttp().doPost(Api.getDefault().addWXPay(userInfo.getSj_login_token(), weChatNickNameTv.getText().toString(),
                    name, openid, phone, code, withdraw_id), new HttpListener() {
                @Override
                public void onSuccess(JSONObject result) {
                    showShortToast("添加成功");
                    finish();
                }

                @Override
                public void onError(int code) {

                }
            });
        } else {
            new MyHttp().doPost(Api.getDefault().addAliPay(userInfo.getSj_login_token(), etAliAccount.getText().toString(),
                    name, phone, code, withdraw_id), new HttpListener() {
                @Override
                public void onSuccess(JSONObject result) {
                    showShortToast("添加成功");
                    finish();
                }

                @Override
                public void onError(int code) {

                }
            });
        }
    }

    private boolean canSubmit() {
        if (TextUtils.isEmpty(etAliAccount.getText().toString()) && !isWXPay) {
            showShortToast("请输入账号！");
            return false;
        }
        if (TextUtils.isEmpty(nameEdit.getText().toString())) {
            showShortToast("请输入姓名！");
            return false;
        }
        if (TextUtils.isEmpty(phoneEdit.getText().toString())) {
            showShortToast("请输入电话号码！");
            return false;
        }
        if (!FormatUtil.isMobileNO(phoneEdit.getText().toString())) {
            showShortToast("请输入正确的电话号码！");
            return false;
        }
        if (TextUtils.isEmpty(smsCodeEdit.getText().toString())) {
            showShortToast("请输入验证码！");
            return false;
        }
        if (isWXPay && TextUtils.isEmpty(openid)) {
            ToastUitl.showShort("请先进行微信授权！");
            return false;
        }
        return true;
    }


}
