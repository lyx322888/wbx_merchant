package com.wbx.merchant.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.wbx.merchant.R;
import com.wbx.merchant.adapter.viewpageadapter.ThirdFragmentStateAdapter;
import com.wbx.merchant.base.BaseActivity;

import butterknife.Bind;

/**
 * Created by wushenghui on 2017/10/24.
 * //第三方配送订单
 */

public class ThirdDeliveryActivity extends BaseActivity {
    @Bind(R.id.order_lab_layout)
    TabLayout mTabLayout;
    @Bind(R.id.order_view_pager)
    ViewPager mOrderViewPager;
    private String[] mTitles = new String[] { "待接单","待取件","配送中","已完成","待处理" };
    private ThirdFragmentStateAdapter mPagerAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_third_delivery;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    @Override
    public void fillData() {
        mPagerAdapter = new ThirdFragmentStateAdapter(getSupportFragmentManager(),mTitles);
        mOrderViewPager.setAdapter(mPagerAdapter);
        mTabLayout.setupWithViewPager(mOrderViewPager);
    }

    @Override
    public void setListener() {

    }
}
