package com.wbx.merchant.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.wbx.merchant.R;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.baseapp.AppConfig;
import com.wbx.merchant.utils.ToastUitl;
import com.wbx.merchant.widget.LoadingDialog;

import butterknife.Bind;

/**
 * Created by wushenghui on 2017/11/16.
 * 选择提现/充值方式
 */

public class ChooseFinanceWayActivity extends BaseActivity {
    @Bind(R.id.ali_layout)
    LinearLayout aliLayout;
    @Bind(R.id.weixin_layout)
    LinearLayout weChatLayout;
    @Bind(R.id.bank_layout)
    LinearLayout bankLayout;
    @Bind(R.id.ali_pay_hint_tv)
    TextView aliPayHintTv;
    @Bind(R.id.scroll_view)
    ScrollView scrollView;
    @Bind(R.id.wx_pay_hint_tv)
    TextView wxPayHintTv;
    @Bind(R.id.bank_pay_hint_tv)
    TextView bankPayHintTv;
    @Bind(R.id.ali_check_im)
    ImageView aliCheckIm;
    @Bind(R.id.wx_check_im)
    ImageView wxCheckIm;
    @Bind(R.id.bank_check_im)
    ImageView bankCheckIm;
    @Bind(R.id.bank_name_tv)
    TextView bankNameTv;
    private String data;

    @Override
    public int getLayoutId() {
        return R.layout.activity_choose_finance_way;
    }

    @Override
    public void initPresenter() {
    }

    @Override
    public void initView() {

    }

    @Override
    public void fillData() {
        data = getIntent().getStringExtra("data");
        JSONObject jsonObject = JSONObject.parseObject(data);
        JSONObject userAliPay = jsonObject.getJSONObject("user_alipay");
        JSONObject userWeiXinPay = jsonObject.getJSONObject("user_weixinpay");
        JSONObject userBank = jsonObject.getJSONObject("user_bank");
        if (userAliPay != null) {
            aliLayout.setVisibility(View.VISIBLE);
            aliPayHintTv.setText(userAliPay.getString("depict"));
        }
        if (userWeiXinPay != null) {
            weChatLayout.setVisibility(View.VISIBLE);
            wxPayHintTv.setText(userWeiXinPay.getString("depict"));
        }
        if (userBank != null) {
            bankLayout.setVisibility(View.VISIBLE);
            bankPayHintTv.setText(userBank.getString("depict"));
            bankNameTv.setText(userBank.getString("bank_name"));
        }

        switch (CashActivity.cashType) {
            case AppConfig.CashCode.alipay:
                aliCheckIm.setImageResource(R.drawable.ic_ok);
                wxCheckIm.setImageResource(R.drawable.ic_round);
                bankCheckIm.setImageResource(R.drawable.ic_round);
                break;
            case AppConfig.CashCode.wxpay:
                aliCheckIm.setImageResource(R.drawable.ic_round);
                wxCheckIm.setImageResource(R.drawable.ic_ok);
                bankCheckIm.setImageResource(R.drawable.ic_round);
                break;
            case AppConfig.CashCode.bank:
                aliCheckIm.setImageResource(R.drawable.ic_round);
                wxCheckIm.setImageResource(R.drawable.ic_round);
                bankCheckIm.setImageResource(R.drawable.ic_ok);
                break;
        }
    }

    @Override
    public void setListener() {
        aliLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDefaultCashType(AppConfig.CashCode.alipay);
            }
        });
        weChatLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDefaultCashType(AppConfig.CashCode.wxpay);
            }
        });
        bankLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDefaultCashType(AppConfig.CashCode.bank);
            }
        });
    }

    private void setDefaultCashType(final String cashType) {
        LoadingDialog.showDialogForLoading(this);
        new MyHttp().doPost(Api.getDefault().setDefaultCashType(userInfo.getSj_login_token(), cashType), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                ToastUitl.showShort(result.getString("msg"));
                CashActivity.cashType = cashType;
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onError(int code) {

            }
        });
    }
}
