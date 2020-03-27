package com.wbx.merchant.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;

import com.wbx.merchant.R;
import com.wbx.merchant.adapter.viewpageadapter.ReceiveRecordFragmentStateAdapter;
import com.wbx.merchant.base.BaseActivity;

import butterknife.Bind;

public class ShopRedPacketReceiveRecordActivity extends BaseActivity {
    public String[] title = {"顾客未使用", "顾客已使用"};
    public static final int TYPE_NO_USE = 0;//未使用
    public static final int TYPE_USED = 1;//已使用
    @Bind(R.id.tab_layout)
    TabLayout tabLayout;
    @Bind(R.id.view_pager)
    ViewPager viewPager;

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, ShopRedPacketReceiveRecordActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_shop_red_packet_receive_record;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        ReceiveRecordFragmentStateAdapter adapter = new ReceiveRecordFragmentStateAdapter(getSupportFragmentManager(), title);
        viewPager.setAdapter(adapter);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.app_color));
        tabLayout.setTabTextColors(Color.parseColor("#333333"), getResources().getColor(R.color.app_color));
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void fillData() {

    }

    @Override
    public void setListener() {

    }
}
