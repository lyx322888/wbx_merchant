package com.wbx.merchant.fragment;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.wbx.merchant.R;
import com.wbx.merchant.adapter.ScanOrderAdapter;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseApplication;
import com.wbx.merchant.base.BaseFragment;
import com.wbx.merchant.baseapp.AppConfig;
import com.wbx.merchant.bean.ScanOrderBean;
import com.wbx.merchant.widget.refresh.BaseRefreshListener;
import com.wbx.merchant.widget.refresh.PullToRefreshLayout;
import com.wbx.merchant.widget.refresh.ViewStatus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class ScanOrderFragment extends BaseFragment implements BaseRefreshListener {
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.refresh_layout)
    PullToRefreshLayout refreshLayout;
    private int status;
    private ScanOrderAdapter adapter;
    private int currentPage = 1;
    private boolean canLoadMore = true;
    private List<ScanOrderBean> lstData = new ArrayList<>();

    public ScanOrderFragment() {
    }

    public static ScanOrderFragment newInstance(int status) {
        ScanOrderFragment fragment = new ScanOrderFragment();
        Bundle args = new Bundle();
        args.putInt("status", status);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            status = getArguments().getInt("status");
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_scan_order;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    protected void initView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ScanOrderAdapter(getActivity());
        adapter.setStatus(status);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    @Override
    protected void fillData() {
    }

    private void loadData() {
        new MyHttp().doPost(Api.getDefault().getScanOrderList(BaseApplication.getInstance().readLoginUser().getSj_login_token()
                , status, currentPage, AppConfig.pageSize), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                if (refreshLayout == null) {
                    return;
                }
                refreshLayout.finishLoadMore();
                refreshLayout.finishRefresh();
                refreshLayout.showView(ViewStatus.CONTENT_STATUS);
                List<ScanOrderBean> lstarray = JSONObject.parseArray(result.getString("data"), ScanOrderBean.class);
                if (currentPage == 1) {
                    lstData.clear();
                }
                lstData.addAll(lstarray);
                adapter.update(lstData);
                if (lstarray.size() < AppConfig.pageSize) {
                    //说明下次已经没有数据了
                    canLoadMore = false;
                }
            }

            @Override
            public void onError(int code) {
                if (refreshLayout == null) {
                    return;
                }
                refreshLayout.finishLoadMore();
                refreshLayout.finishRefresh();
                if (code == AppConfig.ERROR_STATE.NULLDATA) {
                    refreshLayout.showView(ViewStatus.EMPTY_STATUS);
                    refreshLayout.buttonClickNullData(ScanOrderFragment.this, "loadData");
                } else {
                    refreshLayout.showView(ViewStatus.ERROR_STATUS);
                    refreshLayout.buttonClickError(ScanOrderFragment.this, "loadData");
                }
            }
        });
    }

    @Override
    protected void bindEven() {
        refreshLayout.setRefreshListener(this);
    }

    @Override
    public void refresh() {
        currentPage = 1;
        canLoadMore = true;
        loadData();
    }

    @Override
    public void loadMore() {
        if (!canLoadMore) {
            refreshLayout.finishLoadMore();
            showShortToast("没有更多数据了");
            return;
        }
        currentPage++;
        loadData();
    }
}
