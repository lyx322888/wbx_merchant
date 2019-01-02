package com.wbx.merchant.activity;

import android.content.Context;
import android.content.Intent;

import com.wbx.merchant.R;
import com.wbx.merchant.base.BaseActivity;


public class SubsidyExplainActivity extends BaseActivity {

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, SubsidyExplainActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_subsidy_explain;
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
}
