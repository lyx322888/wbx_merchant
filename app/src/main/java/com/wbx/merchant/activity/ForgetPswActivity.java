package com.wbx.merchant.activity;

import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.fastjson.JSONObject;
import com.wbx.merchant.R;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.utils.FormatUtil;
import com.wbx.merchant.utils.MD5;
import com.wbx.merchant.widget.LoadingDialog;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by wushenghui on 2017/6/22.
 */

public class ForgetPswActivity extends BaseActivity {
    @Bind(R.id.forget_phone_edit)
    EditText phoneEdit;
    @Bind(R.id.sms_code_edit)
    EditText codeEdit;
    @Bind(R.id.get_code_btn)
    Button getCodeBtn;
    @Bind(R.id.forget_psw_edit)
    EditText firstPswEdit;
    @Bind(R.id.forget_psw_again_edit)
    EditText secondPswEdit;

    @Override
    public int getLayoutId() {
        return R.layout.activity_forget_psw;
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

    @OnClick({R.id.get_code_btn, R.id.forget_submit_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.get_code_btn:
                getCode();
                break;
            case R.id.forget_submit_btn:
                submit();
                break;
        }
    }

    //提交
    private void submit() {
        String phone = phoneEdit.getText().toString();
        String code = codeEdit.getText().toString();
        String firstPsw = firstPswEdit.getText().toString();
        String secondPsw = secondPswEdit.getText().toString();
        String md5Psw = "";
        try {
            md5Psw = new MD5().EncoderByMd5(firstPsw);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (!canSubmit(phone, code, firstPsw, secondPsw)) return;
        LoadingDialog.showDialogForLoading(mActivity, "请稍后...", true);
        new MyHttp().doPost(Api.getDefault().forgetPsw(phone, code, md5Psw), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                showShortToast(result.getString("msg"));
                finish();
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    private boolean canSubmit(String phone, String code, String firstPsw, String secondPsw) {
        if (TextUtils.isEmpty(phone)) {
            showShortToast("请填写电话号码");
            return false;
        }
        if (!FormatUtil.isMobileNO(phone)) {
            showShortToast("请输入正确的手机号码");
            return false;
        }
        if (TextUtils.isEmpty(code)) {
            showShortToast("请输入验证码");
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
            showShortToast("两次输入的密码不相同");
            return false;
        }
        return true;
    }

    //获取验证码
    private void getCode() {
        String account = phoneEdit.getText().toString();
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
            getCodeBtn.setEnabled(false);
            getCodeBtn.setText((millisUntilFinished / 1000) + "秒后可重发");
        }

        @Override
        public void onFinish() {
            getCodeBtn.setEnabled(true);
            getCodeBtn.setText("获取验证码");
        }
    };
}
