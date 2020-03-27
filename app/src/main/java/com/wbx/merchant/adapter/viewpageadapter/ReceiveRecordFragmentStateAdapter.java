package com.wbx.merchant.adapter.viewpageadapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.wbx.merchant.activity.ShopRedPacketReceiveRecordActivity;
import com.wbx.merchant.fragment.ShopRedPacketReceiveRecordFragment;

/**
 * Created by wushenghui on 2017/6/20.
 */

public class ReceiveRecordFragmentStateAdapter extends FragmentStatePagerAdapter {
    private String[] mTitles;

    public ReceiveRecordFragmentStateAdapter(FragmentManager fm, String[] mTitles) {
        super(fm);
        this.mTitles = mTitles;
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
                fragment = ShopRedPacketReceiveRecordFragment.newInstance(ShopRedPacketReceiveRecordActivity.TYPE_NO_USE);
                break;
            case 1:
                fragment = ShopRedPacketReceiveRecordFragment.newInstance(ShopRedPacketReceiveRecordActivity.TYPE_USED);
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }
}
