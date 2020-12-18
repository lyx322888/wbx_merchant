package com.wbx.merchant.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wbx.merchant.R;
import com.wbx.merchant.activity.PublishBusinessCircleActivity;
import com.wbx.merchant.adapter.MyBusinessCircleAdapterNew;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseFragment;
import com.wbx.merchant.baseapp.AppConfig;
import com.wbx.merchant.bean.BusinessCircleBean;
import com.wbx.merchant.common.LoginUtil;
import com.wbx.merchant.widget.refresh.BaseRefreshListener;
import com.wbx.merchant.widget.refresh.PullToRefreshLayout;
import com.wbx.merchant.widget.refresh.ViewStatus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 */
public class ItemBusinessDistrictFragment extends BaseFragment {
    @Bind(R.id.order_recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.refresh_layout)
    PullToRefreshLayout ptrl;

    private int mPageNum = AppConfig.pageNum;
    private int mPageSize = AppConfig.pageSize;
    private MyBusinessCircleAdapterNew mAdapter;


    public static ItemBusinessDistrictFragment newInstance() {
        ItemBusinessDistrictFragment tabFragment = new ItemBusinessDistrictFragment();
        Bundle bundle = new Bundle();
        tabFragment.setArguments(bundle);
        return tabFragment;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_item_business_district;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    protected void initView() {
        ptrl.showView(ViewStatus.LOADING_STATUS);
        ptrl.setCanLoadMore(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new MyBusinessCircleAdapterNew(loginUser);
        View emptyView = LayoutInflater.from(getContext()).inflate(R.layout.layout_empty_find, null);
        mAdapter.setEmptyView(emptyView);
        emptyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PublishBusinessCircleActivity.actionStart(getActivity(), String.valueOf(0));
            }
        });
        recyclerView.setAdapter(mAdapter);

        ptrl.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                fillData();
            }

            @Override
            public void loadMore() {

            }
        });
    }

    @Override
    protected void fillData() {
        new MyHttp().doPost(Api.getDefault().getDiscoveryList(LoginUtil.getLoginToken()), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                List<BusinessCircleBean> data = JSONArray.parseArray(result.getString("data"), BusinessCircleBean.class);
                if (data == null) {
                    data = new ArrayList<>();
                }
                ptrl.finishRefresh();
                mAdapter.setNewData(data);
                ptrl.showView(ViewStatus.CONTENT_STATUS);
            }

            @Override
            public void onError(int code) {
                if (code == AppConfig.ERROR_STATE.NULLDATA) {
                    //无数据情况下  如果是第一次就是没有数据 直接显示没数据的view
                    mAdapter.setNewData(new ArrayList<BusinessCircleBean>());
                    ptrl.finishRefresh();
                    ptrl.showView(ViewStatus.CONTENT_STATUS);
                } else if (code == AppConfig.ERROR_STATE.NO_NETWORK || code == AppConfig.ERROR_STATE.SERVICE_ERROR) {
                    ptrl.showView(ViewStatus.ERROR_STATUS);
                    ptrl.buttonClickError(ItemBusinessDistrictFragment.this, "fillData");
                } else {

                }
                ptrl.finishRefresh();
            }
        });
    }

    @Override
    protected void bindEven() {

    }

}