package com.wbx.merchant.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.wbx.merchant.R;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.widget.LoadingDialog;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by wushenghui on 2017/6/20.
 * 收入管理
 */

public class InComeActivity extends BaseActivity {
    @Bind(R.id.user_balance_tv)
    TextView userBalanceTv;
    @Bind(R.id.cashing_tv)
    TextView cashingTv;
    @Bind(R.id.cashed_tv)
    TextView cashedTv;
    private JSONObject data;

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, InComeActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_income;
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
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        LoadingDialog.showDialogForLoading(mActivity, "加载中...", false);
        new MyHttp().doPost(Api.getDefault().getIncomeInfo(userInfo.getSj_login_token()), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                data = result.getJSONObject("data");
                userBalanceTv.setText(String.format("¥%.2f", data.getIntValue("gold") / 100.00));
                cashingTv.setText(String.format("¥%.2f", data.getIntValue("now_gold") / 100.00));
                cashedTv.setText(String.format("¥%.2f", data.getIntValue("already_gold") / 100.00));
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    @Override
    public void setListener() {

    }

    @OnClick({R.id.cash_tv, R.id.bind_account_layout, R.id.cash_record_layout, R.id.tv_rule})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cash_tv:
                if (data == null) {
                    return;
                }
                Intent intentCash = new Intent(mContext, CashActivity.class);
                intentCash.putExtra("balance", data.getIntValue("gold"));
                intentCash.putExtra("type", CashActivity.TYPE_SHOP);
                startActivity(intentCash);
                break;
            case R.id.bind_account_layout:
                startActivity(new Intent(mContext, BindAccountActivity.class));
                break;
            case R.id.cash_record_layout:
                if (data == null) {
                    return;
                }
                Intent intent = new Intent(mContext, CashRecordActivity.class);
                intent.putExtra("cashTotalPrice", data.getIntValue("already_gold"));
                startActivity(intent);
                break;
            case R.id.tv_rule:
                WebActivity.actionStart(this, "http://www.wbx365.com/Wbxwaphome/other/accounts_rull.html");
                break;
        }
    }
}
