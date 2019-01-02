package com.wbx.merchant.fragment;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wbx.merchant.R;
import com.wbx.merchant.adapter.ShopPacketReceiveRecordAdapter;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseFragment;
import com.wbx.merchant.baseapp.AppConfig;
import com.wbx.merchant.bean.RedPacketListBean;
import com.wbx.merchant.widget.refresh.BaseRefreshListener;
import com.wbx.merchant.widget.refresh.PullToRefreshLayout;
import com.wbx.merchant.widget.refresh.ViewStatus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class ShopRedPacketReceiveRecordFragment extends BaseFragment implements BaseRefreshListener {
    private static final String TYPE = "type";
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.ptrl)
    PullToRefreshLayout ptrl;
    private int type;
    private ShopPacketReceiveRecordAdapter mAdapter;
    private int currentPage = 1;
    private List<RedPacketListBean> lstData = new ArrayList<>();
    private MyHttp myHttp;

    public ShopRedPacketReceiveRecordFragment() {
    }

    public static ShopRedPacketReceiveRecordFragment newInstance(int type) {
        ShopRedPacketReceiveRecordFragment fragment = new ShopRedPacketReceiveRecordFragment();
        Bundle args = new Bundle();
        args.putInt(TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getInt(TYPE);
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_shop_red_packet_receive_record;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    protected void initView() {
        myHttp = new MyHttp();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new ShopPacketReceiveRecordAdapter(getContext());
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void fillData() {
        ptrl.showView(ViewStatus.LOADING_STATUS);
        loadData(false);
    }

    @Override
    protected void bindEven() {
        ptrl.setRefreshListener(this);
    }

    @Override
    public void refresh() {
        loadData(false);
    }

    @Override
    public void loadMore() {
        loadData(true);
    }

    private void loadData(final boolean isLoadMore) {
        if (isLoadMore) {
            currentPage++;
        } else {
            currentPage = 1;
        }
        myHttp.doPost(Api.getDefault().redPacketList(loginUser.getSj_login_token(), type, currentPage, 10), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                if (ptrl == null) {
                    //页面已销毁
                    return;
                }
                if (isLoadMore) {
                    ptrl.finishLoadMore();
                } else {
                    ptrl.finishRefresh();
                }
                List<RedPacketListBean> data = JSONArray.parseArray(result.getString("data"), RedPacketListBean.class);
                ptrl.showView(ViewStatus.CONTENT_STATUS);
                if (!isLoadMore) {
                    lstData.clear();
                }
                lstData.addAll(data);
                mAdapter.update(lstData);
            }

            @Override
            public void onError(int code) {
                if (ptrl == null) {
                    //页面已销毁
                    return;
                }
                if (isLoadMore) {
                    ptrl.finishLoadMore();
                } else {
                    ptrl.finishRefresh();
                }
                if (code == AppConfig.ERROR_STATE.NULLDATA && ptrl != null) {
                    if (isLoadMore) {
                        ptrl.setCanLoadMore(false);
                    } else {
                        ptrl.showView(ViewStatus.EMPTY_STATUS);
                        ptrl.buttonClickNullData(ShopRedPacketReceiveRecordFragment.this, "refresh");
                    }
                } else {
                    ptrl.showView(ViewStatus.ERROR_STATUS);
                    ptrl.buttonClickError(ShopRedPacketReceiveRecordFragment.this, "refresh");
                }
            }
        });
    }
}
