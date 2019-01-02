package com.wbx.merchant.activity;

import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.wbx.merchant.R;
import com.wbx.merchant.base.BaseActivity;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by wushenghui on 2017/6/21.
 * 绑定支付宝
 */

public class BindALIActivity extends BaseActivity {
    @Bind(R.id.pay_account_edit)
    EditText payAccountEdit;
    @Bind(R.id.pay_account_tv)
    TextView payAccountTv;
    @Bind(R.id.get_code_btn)
    Button getCodeBtn;
    @Bind(R.id.phone_edit)
    EditText phoneEdit;
    @Override
    public int getLayoutId() {
        return R.layout.activity_bind_ali;
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
    @OnClick({R.id.get_code_btn})
    public void onClick(View view){
        switch (view.getId()){
            case  R.id.get_code_btn:
                if(TextUtils.isEmpty(phoneEdit.getText().toString())){
                  showShortToast("请输入手机号码");
                  return;
                }

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
}
