package com.wbx.merchant.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.wbx.merchant.R;
import com.wbx.merchant.adapter.GoodsOrderAdapter;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.baseapp.AppConfig;
import com.wbx.merchant.bean.OrderGoodsBean;
import com.wbx.merchant.common.LoginUtil;
import com.wbx.merchant.utils.ToastUitl;
import com.wbx.merchant.widget.LoadingDialog;
import com.wbx.merchant.widget.refresh.BaseRefreshListener;
import com.wbx.merchant.widget.refresh.PullToRefreshLayout;
import com.wbx.merchant.widget.refresh.ViewStatus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class GoodsOrderActivity extends BaseActivity implements BaseRefreshListener {


    @Bind(R.id.goods_order_recycler_view)
    RecyclerView goodsOrderRecyclerView;
    @Bind(R.id.ptrl)
    PullToRefreshLayout ptrl;
    private List<OrderGoodsBean> lstData = new ArrayList<>();
    private GoodsOrderAdapter mAdapter;
    private boolean canLoadMore = true;//控制下次是否加载更多
    private int pageNum = AppConfig.pageNum;
    private int pageSize = AppConfig.pageSize;

    @Override
    public int getLayoutId() {
        return R.layout.activity_goods_order;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        ptrl.setRefreshListener(this);

        goodsOrderRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new GoodsOrderAdapter(lstData, mContext);
        goodsOrderRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        LoadingDialog.showDialogForLoading(this, "加载中...", true);
        new MyHttp().doPost(Api.getDefault().getListOrder(LoginUtil.getLoginToken(), pageNum, pageSize), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                ptrl.finishRefresh();
                ptrl.finishLoadMore();
                List<OrderGoodsBean> lstarray = JSONObject.parseArray(result.getString("data"), OrderGoodsBean.class);

                if (pageNum == 1) {
                    lstData.clear();
                }
                if (lstarray.size() < pageSize) {
                    //说明下次已经没有数据了
                    canLoadMore = false;
                }
                lstData.addAll(lstarray);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(int code) {
                if (ptrl == null) {
                    return;
                }
                ptrl.finishLoadMore();
                ptrl.finishRefresh();
                if (code == AppConfig.ERROR_STATE.NULLDATA) {
                    ptrl.showView(ViewStatus.EMPTY_STATUS);
                    ptrl.buttonClickNullData(GoodsOrderActivity.this, "loadData");
                } else {
                    ptrl.showView(ViewStatus.ERROR_STATUS);
                    ptrl.buttonClickError(GoodsOrderActivity.this, "loadData");
                }
            }
        });
    }

    @Override
    public void fillData() {

    }

    @Override
    public void setListener() {

    }

    @Override
    public void refresh() {
        canLoadMore = true;
        pageNum = 1;
        loadData();
    }

    @Override
    public void loadMore() {
        if (!canLoadMore) {
            ToastUitl.showShort("没有更多数据");
            ptrl.finishLoadMore();
            return;
        }
        pageNum++;
        loadData();
    }
}
