package com.wbx.merchant.adapter.viewpageadapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.wbx.merchant.activity.ScanOrderActivity;
import com.wbx.merchant.fragment.ScanOrderFragment;

/**
 * Created by wushenghui on 2017/6/20.
 */

public class ScanOrderFragmentStateAdapter extends FragmentStatePagerAdapter {
    private String[] mTitles;
    private FragmentManager mFm;

    public ScanOrderFragmentStateAdapter(FragmentManager fm, String[] mTitles) {
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
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = ScanOrderFragment.newInstance(ScanOrderActivity.POSITION_WAIT_PAY);
                break;
            case 1:
                fragment = ScanOrderFragment.newInstance(ScanOrderActivity.POSITION_COMPLETE);
                break;
        }
        return fragment;
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
