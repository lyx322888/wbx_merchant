package com.wbx.merchant.fragment;

import android.os.Bundle;

import com.wbx.merchant.R;
import com.wbx.merchant.base.BaseFragment;

/**
 * Created by wushenghui on 2017/10/24.
 */

public class ThirdDeliveryFragment extends BaseFragment {
    public static final String POSITION = "position";
    public static ThirdDeliveryFragment newInstance(int position)
    {
        ThirdDeliveryFragment tabFragment = new ThirdDeliveryFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(POSITION, position);
        tabFragment.setArguments(bundle);
        return tabFragment;
    }
    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_third_delivery;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void fillData() {

    }

    @Override
    protected void bindEven() {

    }
}
