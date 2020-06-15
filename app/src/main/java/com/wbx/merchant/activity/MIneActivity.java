package com.wbx.merchant.activity;

import com.wbx.merchant.R;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.fragment.MineFragment;
import com.wbx.merchant.utils.FormatUtil;

public class MIneActivity extends BaseActivity {


    @Override
    public int getLayoutId() {
        FormatUtil.setStatubarColor(mActivity, R.color.app_color);
        return R.layout.activity_mine;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
       MineFragment  mineFragment = (MineFragment) getSupportFragmentManager().getFragments().get(0);
       mineFragment.hiddenTitle();
    }

    @Override
    public void fillData() {

    }

    @Override
    public void setListener() {

    }
}
