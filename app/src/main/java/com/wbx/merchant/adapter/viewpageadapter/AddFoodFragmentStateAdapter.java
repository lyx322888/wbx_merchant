package com.wbx.merchant.adapter.viewpageadapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.wbx.merchant.fragment.AddFoodFragment;

import java.util.List;

/**
 * Created by wushenghui on 2017/6/20.
 */

public class AddFoodFragmentStateAdapter extends FragmentStatePagerAdapter {
    private List<String> mTitles;
    private FragmentManager mFm;

    public AddFoodFragmentStateAdapter(FragmentManager fm, List<String> mTitles) {
        super(fm);
        this.mTitles = mTitles;
        this.mFm = fm;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = AddFoodFragment.newInstance(position);
        return fragment;
    }

    @Override
    public int getCount() {
        return mTitles.size();
    }

    @Override
    public Fragment instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container,
                position);
        mFm.beginTransaction().show(fragment).commit();
        return fragment;
    }
}
