package com.wbx.merchant.adapter.viewpageadapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.wbx.merchant.fragment.GoodsManagerFragment;

/**
 * Created by wushenghui on 2017/6/20.
 */

public class GoodsFragmentStateAdapter extends FragmentStatePagerAdapter {
    private String[] mTitles;
    public GoodsFragmentStateAdapter(FragmentManager fm, String[] mTitles) {
        super(fm);
        this.mTitles = mTitles;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }

    @Override
    public Fragment getItem(int position) {
        return GoodsManagerFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }
}
