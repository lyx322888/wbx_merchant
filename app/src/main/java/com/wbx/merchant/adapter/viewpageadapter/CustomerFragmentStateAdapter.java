package com.wbx.merchant.adapter.viewpageadapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.wbx.merchant.fragment.MyMemberFragment;

/**
 * Created by wushenghui on 2017/6/20.
 */

public class CustomerFragmentStateAdapter extends FragmentStatePagerAdapter {
    private String[] mTitles;

    public CustomerFragmentStateAdapter(FragmentManager fm, String[] mTitles) {
        super(fm);
        this.mTitles = mTitles;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }

    @Override
    public Fragment getItem(int position) {
        return MyMemberFragment.newInstance(position + 1);
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }
}
