package com.wbx.merchant.activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.wbx.merchant.R;
import com.wbx.merchant.adapter.viewpageadapter.SeckillFragmentStateAdapter;
import com.wbx.merchant.base.BaseActivity;

import butterknife.Bind;

/**
 * Created by wushenghui on 2017/12/12.
 * 秒杀
 */

public class SeckillActivity extends BaseActivity {
    @Bind(R.id.tab_layout)
    TabLayout mTabLayout;
    @Bind(R.id.view_pager)
    ViewPager mViewPager;
    private String[] mTitles = new String[] { "全部","未开始","进行中","已结束"};
    private SeckillFragmentStateAdapter mPagerAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_seckill;
    }
    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {

    }

    @Override
    public void fillData() {
        mPagerAdapter = new SeckillFragmentStateAdapter(getSupportFragmentManager(),mTitles);
        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void setListener() {
     findViewById(R.id.add_seckill_btn).setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            startActivity(new Intent(mContext,AddSecKillActivity.class));
         }
     });
    }
}
