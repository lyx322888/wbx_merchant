package com.wbx.merchant.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.fastjson.JSONObject;
import com.wbx.merchant.R;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.utils.ToastUitl;
import com.wbx.merchant.widget.LoadingDialog;
import com.wbx.merchant.widget.PriceEditText;

import butterknife.Bind;
import butterknife.OnClick;

public class MemberSettingActivity extends BaseActivity {
    @Bind(R.id.et_condition)
    PriceEditText etCondition;

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, MemberSettingActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_member_setting;
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
        new MyHttp().doPost(Api.getDefault().getMemberCondition(userInfo.getSj_login_token()), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                JSONObject data = result.getJSONObject("data");
                if (data != null) {
                    String consumption_money = data.getString("consumption_money");
                    etCondition.setText(consumption_money);
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

    @OnClick({R.id.tv_delete, R.id.tv_complete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_delete:
                updateMemberCondition(1);
                break;
            case R.id.tv_complete:
                updateMemberCondition(0);
                break;
        }
    }

    private void updateMemberCondition(final int close) {
        if (TextUtils.isEmpty(etCondition.getText().toString()) && close == 0) {
            //增加会员条件时价格不能为空
            ToastUitl.showShort("请输入金额");
            return;
        }
        new MyHttp().doPost(Api.getDefault().setMemberCondition(userInfo.getSj_login_token(), etCondition.getText().toString(), close), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                ToastUitl.showShort(result.getString("msg"));
                if (close == 0) {
                    //添加成功
                    finish();
                    return;
                } else {
                    etCondition.setText("");
                }
            }

            @Override
            public void onError(int code) {

            }
        });
    }
}
