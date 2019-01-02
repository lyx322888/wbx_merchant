package com.wbx.merchant.adapter.viewpageadapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.wbx.merchant.fragment.SeckillFragment;

/**
 * Created by wushenghui on 2017/6/20.
 */

public class SeckillFragmentStateAdapter extends FragmentStatePagerAdapter {
    private String[] mTitles;
    private FragmentManager mFm;
    public SeckillFragmentStateAdapter(FragmentManager fm, String[] mTitles) {
        super(fm);
        this.mTitles = mTitles;
        this.mFm = fm;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }

    @Override
    public Fragment getItem(int position) {
        return SeckillFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }
    @Override
    public Fragment instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container,
                position);
        mFm.beginTransaction().show(fragment).commit();
        return fragment;
    }
}
