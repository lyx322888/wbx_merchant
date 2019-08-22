package com.wbx.merchant.activity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.wbx.merchant.R;
import com.wbx.merchant.adapter.viewpageadapter.OrderFragmentStateAdapter;
import com.wbx.merchant.base.BaseActivity;

import butterknife.Bind;

/**
 * Created by wushenghui on 2017/6/20.
 */

public class OrderActivity extends BaseActivity {
    public static final int POSITION_WAIT_SEND = 0;//待配送
    public static final int POSITION_SENDING = 1;//配送中
    public static final int POSITION_SELF= 2;//待自提
    public static final int POSITION_WAIT_REFUND = 3;//待退款
    public static final int POSITION_REFUNDED = 4;//已退款
    public static final int POSITION_COMPLETED = 5;//已完成
    @Bind(R.id.order_lab_layout)
    TabLayout mTabLayout;
    @Bind(R.id.order_view_pager)
    ViewPager mOrderViewPager;
    private String[] mTitles = new String[]{"待配送", "配送中", "待自提","待退款", "已退款", "已完成"};
    private OrderFragmentStateAdapter mPagerAdapter;

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, OrderActivity.class);
        context.startActivity(intent);
    }

    public static void actionStart(Context context, int position) {
        Intent intent = new Intent(context, OrderActivity.class);
        intent.putExtra("position", position);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_order;
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
        mPagerAdapter = new OrderFragmentStateAdapter(getSupportFragmentManager(), mTitles);
        mOrderViewPager.setAdapter(mPagerAdapter);
        mTabLayout.setupWithViewPager(mOrderViewPager);
        mTabLayout.getTabAt(getIntent().getIntExtra("position", 0)).select();
    }

    @Override
    public void setListener() {

    }
}
