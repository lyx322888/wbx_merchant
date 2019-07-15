package com.wbx.merchant.activity;

import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.wbx.merchant.R;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.utils.BankUtils;
import com.wbx.merchant.utils.FormatUtil;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by wushenghui on 2017/11/17.
 */

public class BindUnionPayActivity extends BaseActivity {
    @Bind(R.id.scroll_view)
    ScrollView scrollView;
    @Bind(R.id.send_code_btn)
    TextView sendCodeBtn;
    @Bind(R.id.sms_code_edit)
    EditText smsCodeEdit;
    @Bind(R.id.bank_edit)
    EditText bankEdit;
    @Bind(R.id.bank_name_edit)
    EditText bankNameEdit;
    @Bind(R.id.real_name_edit)
    EditText realNameEdit;
    @Bind(R.id.subbranch_edit)
    EditText subbranchEdit;
    @Bind(R.id.mobile_edit)
    EditText mobileEdit;
    private HashMap<String, Object> mParams = new HashMap<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_bind_unionpay;
    }

    @Override
    public void initPresenter() {
    }

    @Override
    public void initView() {

    }

    @Override
    public void fillData() {
        new MyHttp().doPost(Api.getDefault().getBankInfo(userInfo.getSj_login_token()), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                JSONObject data = result.getJSONObject("data");
                try {
                    bankNameEdit.setText(data.getString("bank_name"));
                    bankEdit.setText(data.getString("bank_num"));
                    subbranchEdit.setText(data.getString("bank_branch"));
                    realNameEdit.setText(data.getString("bank_realname"));
                    mParams.put("bank_name", bankNameEdit.getText().toString());
                    mParams.put("bank_num", bankEdit.getText().toString());
                    mParams.put("bank_branch", subbranchEdit.getText().toString());
                    mParams.put("bank_realname", realNameEdit.getText().toString());
                    mParams.put("withdraw_id", data.getString("withdraw_id"));
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
        bankEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // 在输入数据时监听
                String backname = bankEdit.getText().toString();
                if (charSequence.length() >= 6) {
                    char[] cardNumber = backname.toCharArray();
                    String name = BankUtils.getNameOfBank(cardNumber, 0);// 获取银行卡的信息
                    bankNameEdit.setText(name);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @OnClick({R.id.send_code_btn, R.id.submit_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.send_code_btn:
                if (TextUtils.isEmpty(mobileEdit.getText().toString())) {
                    showShortToast("请输入手机号码");
                    return;
                }
                if (!FormatUtil.isMobileNO(mobileEdit.getText().toString())) {
                    showShortToast("请输入正确的手机号码");
                    return;
                }
                sendCode();
                break;
            case R.id.submit_btn:
                if (canSubmit()) submit();
                break;
        }
    }

    private void submit() {
        mParams.put("sj_login_token", userInfo.getSj_login_token());
        mParams.put("mobile", mobileEdit.getText().toString());
        mParams.put("code", smsCodeEdit.getText().toString());
        mParams.put("bank_name", bankNameEdit.getText().toString());
        mParams.put("bank_num", bankEdit.getText().toString());
        mParams.put("bank_branch", subbranchEdit.getText().toString());
        mParams.put("bank_realname", realNameEdit.getText().toString());
        new MyHttp().doPost(Api.getDefault().addBank(mParams), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                showShortToast("绑定成功");
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    private boolean canSubmit() {
        if (TextUtils.isEmpty(mobileEdit.getText().toString())) {
            showShortToast("请输入手机号码");
            return false;
        }
        if (!FormatUtil.isMobileNO(mobileEdit.getText().toString())) {
            showShortToast("请输入正确的手机号码");
            return false;
        }
        if (TextUtils.isEmpty(bankEdit.getText().toString())) {
            showShortToast("请输入银行账号");
            return false;
        }
        if (TextUtils.isEmpty(bankNameEdit.getText().toString())) {
            showShortToast("请输入所属银行");
            return false;
        }
        if (TextUtils.isEmpty(realNameEdit.getText().toString())) {
            showShortToast("请输入持卡人姓名");
            return false;
        }
        if (TextUtils.isEmpty(subbranchEdit.getText().toString())) {
            showShortToast("请输入开户支行");
            return false;
        }
        if (TextUtils.isEmpty(smsCodeEdit.getText().toString())) {
            showShortToast("请输入验证码");
            return false;
        }
        return true;
    }

    private void sendCode() {
        new MyHttp().doPost(Api.getDefault().sendCode(mobileEdit.getText().toString()), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                timer.start();
            }

            @Override
            public void onError(int code) {

            }
        });

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
}
