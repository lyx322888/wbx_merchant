package com.wbx.merchant.activity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.wbx.merchant.R;
import com.wbx.merchant.adapter.viewpageadapter.CustomerFragmentStateAdapter;
import com.wbx.merchant.base.BaseActivity;

import butterknife.Bind;

/**
 * Created by wushenghui on 2017/7/25.
 * 客户管理
 */

public class CustomerManagerActivity extends BaseActivity {
    @Bind(R.id.order_lab_layout)
    TabLayout mTabLayout;
    @Bind(R.id.order_view_pager)
    ViewPager mOrderViewPager;
    //    private String[] mTitles = new String[]{"最近消费", "次数最多", "金额最多", "购买过的客户", "关注过的客户"};
    private String[] mTitles = new String[]{"购买过的客户", "关注过的客户"};
    private CustomerFragmentStateAdapter mAdapter;

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, CustomerManagerActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_customer_manager;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
    }

    @Override
    public void fillData() {
        mAdapter = new CustomerFragmentStateAdapter(getSupportFragmentManager(), mTitles);
        mOrderViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mOrderViewPager);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    @Override
    public void setListener() {

    }
}
