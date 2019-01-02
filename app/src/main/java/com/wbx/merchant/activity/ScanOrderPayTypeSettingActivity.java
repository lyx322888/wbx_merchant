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
import com.wbx.merchant.base.BaseApplication;
import com.wbx.merchant.utils.ToastUitl;
import com.wbx.merchant.widget.LoadingDialog;

import butterknife.Bind;
import butterknife.OnClick;

public class ScanOrderPayTypeSettingActivity extends BaseActivity {

    @Bind(R.id.tv_pay_first)
    TextView tvPayFirst;
    @Bind(R.id.tv_eat_first)
    TextView tvEatFirst;

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, ScanOrderPayTypeSettingActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_scan_order_pay_type_setting;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        tvEatFirst.setSelected(userInfo.getScan_order_type() == 1);
        tvPayFirst.setSelected(userInfo.getScan_order_type() == 2);
    }

    @Override
    public void fillData() {
    }

    @Override
    public void setListener() {

    }

    @OnClick({R.id.rl_right, R.id.tv_pay_first, R.id.tv_eat_first})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_right:
                save();
                break;
            case R.id.tv_pay_first:
                tvPayFirst.setSelected(true);
                tvEatFirst.setSelected(false);
                break;
            case R.id.tv_eat_first:
                tvPayFirst.setSelected(false);
                tvEatFirst.setSelected(true);
                break;
        }
    }

    private void save() {
        LoadingDialog.showDialogForLoading(this);
        new MyHttp().doPost(Api.getDefault().updateScanOrderType(userInfo.getSj_login_token(), tvEatFirst.isSelected() ? 1 : 2), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                ToastUitl.showShort(result.getString("msg"));
                userInfo.setScan_order_type(tvEatFirst.isSelected() ? 1 : 2);
                BaseApplication.getInstance().saveUserInfo(userInfo);
                finish();
            }

            @Override
            public void onError(int code) {

            }
        });
    }
}
