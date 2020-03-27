package com.wbx.merchant.adapter.viewpageadapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.wbx.merchant.activity.OrderActivity;
import com.wbx.merchant.fragment.OrderFragment;

/**
 * Created by wushenghui on 2017/6/20.
 */

public class OrderFragmentStateAdapter extends FragmentStatePagerAdapter {
    private String[] mTitles;
    private FragmentManager mFm;

    public OrderFragmentStateAdapter(FragmentManager fm, String[] mTitles) {
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
                fragment = OrderFragment.newInstance(OrderActivity.POSITION_WAIT_SEND);
                break;
            case 1:
                fragment = OrderFragment.newInstance(OrderActivity.POSITION_SENDING);
                break;
            case 2:
                fragment = OrderFragment.newInstance(OrderActivity.POSITION_SELF);
                break;
            case 3:
                fragment = OrderFragment.newInstance(OrderActivity.POSITION_WAIT_REFUND);
                break;
            case 4:
                fragment = OrderFragment.newInstance(OrderActivity.POSITION_REFUNDED);
                break;
            case 5:
                fragment = OrderFragment.newInstance(OrderActivity.POSITION_COMPLETED);
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
        mFm.beginTransaction().show(fragment).commitAllowingStateLoss();
        return fragment;
    }
}
