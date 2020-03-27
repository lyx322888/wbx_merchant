package com.wbx.merchant.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.wbx.merchant.R;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.utils.FormatUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MyGylActivity extends BaseActivity {

    @Bind(R.id.tv_title)
    TextView tvTitle;

    public static void actionStart(Context context, String title) {
        Intent intent = new Intent(context, MyGylActivity.class);
        intent.putExtra("title", title);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        FormatUtil.setStatubarColor(mActivity, R.color.app_color);
        return R.layout.activity_my_gyl;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
     tvTitle.setText(getIntent().getStringExtra("title"));
    }

    @Override
    public void fillData() {

    }

    @Override
    public void setListener() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
