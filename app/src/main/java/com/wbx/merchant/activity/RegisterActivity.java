package com.wbx.merchant.activity;

import android.content.Intent;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.alibaba.fastjson.JSONObject;
import com.wbx.merchant.R;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.baseapp.AppConfig;
import com.wbx.merchant.utils.FormatUtil;
import com.wbx.merchant.utils.MD5;
import com.wbx.merchant.utils.SPUtils;
import com.wbx.merchant.widget.LoadingDialog;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by wushenghui on 2017/6/22.
 */

public class RegisterActivity extends BaseActivity {
    @Bind(R.id.register_phone_edit)
    EditText mobileEdit;
    @Bind(R.id.register_sms_edit)
    EditText codeEdit;
    @Bind(R.id.register_first_psw_edit)
    EditText firstPswEdit;
    @Bind(R.id.register_second_psw_edit)
    EditText secondPswEdit;
    @Bind(R.id.agree_cb)
    CheckBox agreeCb;
    @Bind(R.id.register_get_code_btn)
    Button sendCodeBtn;

    @Override
    public int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {

    }

    @Override
    public void fillData() {

    }

    @Override
    public void setListener() {

    }

    @OnClick({R.id.do_register_btn, R.id.register_get_code_btn, R.id.tv_register_protocol})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.do_register_btn:
                doRegister();
                break;
            case R.id.register_get_code_btn:
                sendCode();
                break;
            case R.id.tv_register_protocol:
                Intent intent = new Intent(this, WebActivity.class);
                intent.putExtra("url", "http://www.wbx365.com/wap/passport/fwxy.html");
                startActivity(intent);
                break;
        }

    }

    private void sendCode() {
        String account = mobileEdit.getText().toString();
        if (!codeSuccess(account)) {
            return;
        }
        new MyHttp().doPost(Api.getDefault().sendCode(account), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                timer.start();
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    private boolean codeSuccess(String account) {
        if (TextUtils.isEmpty(account)) {
            showShortToast("请输入账号");
            return false;
        }
        if (!FormatUtil.isMobileNO(account)) {
            showShortToast("请输入正确的手机号码");
            return false;
        }

        return true;
    }

    private CountDownTimer timer = new CountDownTimer(60000, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {
            sendCodeBtn.setEnabled(false);
            sendCodeBtn.setText((millisUntilFinished / 1000) + "秒后可重发");
        }

        @Override
        public void onFinish() {
            sendCodeBtn.setEnabled(true);
            sendCodeBtn.setText("获取验证码");
        }
    };

    private void doRegister() {
        String account = mobileEdit.getText().toString();
        String code = codeEdit.getText().toString();
        String firstPsw = firstPswEdit.getText().toString();
        String secondPsw = secondPswEdit.getText().toString();
        if (!isSuccess(account, firstPsw, secondPsw, code)) {
            return;
        }
        LoadingDialog.showDialogForLoading(mActivity, "注册中...", true);
        String md5Psw = "";
        try {
            md5Psw = MD5.EncoderByMd5(firstPsw);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        new MyHttp().doPost(Api.getDefault().register(account, code, md5Psw), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                SPUtils.setSharedStringData(mContext, AppConfig.LOGIN_MOBILE, mobileEdit.getText().toString());
                finish();
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    private boolean isSuccess(String account, String firstPsw, String secondPsw, String code) {
        if (TextUtils.isEmpty(account)) {
            showShortToast("请输入账号");
            return false;
        }
        if (TextUtils.isEmpty(firstPsw)) {
            showShortToast("请输入密码");
            return false;
        }
        if (TextUtils.isEmpty(secondPsw)) {
            showShortToast("请输入确认密码");
            return false;
        }
        if (!firstPsw.equals(secondPsw)) {
            showShortToast("两次输入的密码不对");
            return false;
        }
        if (TextUtils.isEmpty(code)) {
            showShortToast("请输入验证码");
            return false;
        }
        if (!FormatUtil.isMobileNO(account)) {
            showShortToast("请输入正确的手机号码");
            return false;
        }
        if (!agreeCb.isChecked()) {
            showShortToast("请先同意用户协议");
            return false;
        }
        return true;
    }
}
