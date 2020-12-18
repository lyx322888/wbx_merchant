package com.wbx.merchant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.wbx.merchant.R;
import com.wbx.merchant.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VideoGzsmActivity extends BaseActivity {

    @Bind(R.id.title_name_tv)
    TextView titleNameTv;
    @Bind(R.id.tv_fbsp)
    TextView tvFbsp;

    @Override
    public int getLayoutId() {
        return R.layout.activity_video_gzsm;
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

    @OnClick(R.id.tv_fbsp)
    public void onClick() {
        startActivity(new Intent(mContext,IssueVideoActivity.class));
        finish();
    }
}