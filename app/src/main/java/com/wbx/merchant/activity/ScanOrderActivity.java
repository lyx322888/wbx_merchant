package com.wbx.merchant.activity;

import android.content.Context;
import android.content.Intent;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;

import com.wbx.merchant.R;
import com.wbx.merchant.adapter.viewpageadapter.ScanOrderFragmentStateAdapter;
import com.wbx.merchant.base.BaseActivity;

import butterknife.Bind;
import butterknife.OnClick;

public class ScanOrderActivity extends BaseActivity {
    public static final int POSITION_WAIT_PAY = 0;//待付款
    public static final int POSITION_COMPLETE = 1;//已完成
    @Bind(R.id.tab_layout)
    TabLayout tabLayout;
    @Bind(R.id.view_pager)
    ViewPager viewPager;
    private String[] mTitles = new String[]{"待付款", "已完成"};
    private ScanOrderFragmentStateAdapter mPagerAdapter;

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, ScanOrderActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_scan_order;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        mPagerAdapter = new ScanOrderFragmentStateAdapter(getSupportFragmentManager(), mTitles);
        viewPager.setAdapter(mPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void fillData() {

    }

    @Override
    public void setListener() {

    }

    @OnClick(R.id.title_right_tv)
    public void onViewClicked() {
        ScanOrderPayTypeSettingActivity.actionStart(this);
    }
}
