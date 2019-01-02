package com.wbx.merchant.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.wbx.merchant.R;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.bean.CashInfoBean;
import com.wbx.merchant.widget.LoadingDialog;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by wushenghui on 2017/6/21.
 * 绑定账户 提现
 */

public class BindAccountActivity extends BaseActivity {

    @Bind(R.id.tv_bind_ali)
    TextView tvBindAli;
    @Bind(R.id.tv_bind_wx)
    TextView tvBindWx;
    @Bind(R.id.tv_bind_bank)
    TextView tvBindBank;

    @Override
    public int getLayoutId() {
        return R.layout.activity_bind_account;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {

    }

    @Override
    public void fillData() {
        LoadingDialog.showDialogForLoading(this);
        new MyHttp().doPost(Api.getDefault().getCashInfo(userInfo.getSj_login_token()), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                CashInfoBean data = result.getObject("data", CashInfoBean.class);
                if (data.getUser_alipay() != null) {
                    tvBindAli.setVisibility(View.VISIBLE);
                }
                if (data.getUser_weixinpay() != null) {
                    tvBindWx.setVisibility(View.VISIBLE);
                }
                if (data.getUser_bank() != null) {
                    tvBindBank.setVisibility(View.VISIBLE);
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

    @OnClick({R.id.ali_layout, R.id.weixin_layout, R.id.bank_layout})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.ali_layout:
                BindPayPlatformActivity.actionStart(mContext, false);
                break;
            case R.id.weixin_layout:
                BindPayPlatformActivity.actionStart(mContext, true);
                break;
            case R.id.bank_layout:
                intent = new Intent(mContext, BindUnionPayActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
