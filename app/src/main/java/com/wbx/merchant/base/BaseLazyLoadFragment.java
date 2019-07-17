package com.wbx.merchant.base;

import android.os.Bundle;

/**
 * Description: Fragment LazyLoad
 */
public abstract class BaseLazyLoadFragment extends BaseFragment {
    /**
     * UI初始化是否完成
     */
    private boolean isViewInitiated;
    /**
     * 当前UI是否可见
     */
    private boolean isVisibleToUser;
    /**
     * DATA初始化是否完成
     */
    private boolean isDataInitiated;

    /**
     * 获取数据方法
     */
    public abstract void fetchData();

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        // 当前UI是否可见
        this.isVisibleToUser = isVisibleToUser;
        prepareFetchData();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // UI初始化完成
        isViewInitiated = true;
        prepareFetchData();
    }

    /**
     * 准备获取数据
     */
    public void prepareFetchData() {
        if (isVisibleToUser && isViewInitiated && !isDataInitiated) {
            isDataInitiated = true;
            fetchData();
        }
    }
}