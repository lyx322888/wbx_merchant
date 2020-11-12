package com.wbx.merchant.fragment;

import android.os.Bundle;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.wbx.merchant.R;
import com.wbx.merchant.adapter.MoneyLogAdapter;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseFragment;
import com.wbx.merchant.baseapp.AppConfig;
import com.wbx.merchant.bean.MoneyLogBean;
import com.wbx.merchant.common.LoginUtil;
import com.wbx.merchant.utils.AdapterUtilsNew;
import com.wbx.merchant.widget.refresh.BaseRefreshListener;
import com.wbx.merchant.widget.refresh.PullToRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;


public class ItemNewCirclePutlnFragment extends BaseFragment {
    @Bind(R.id.order_recycler_view)
    RecyclerView orderRecyclerView;
    @Bind(R.id.refresh_layout)
    PullToRefreshLayout mRefreshLayout;

    String change_status;
    MoneyLogAdapter moneyLogAdapter;
    private int pageNum = 1;


    public static ItemNewCirclePutlnFragment newInstance(String change_status) {
        ItemNewCirclePutlnFragment fragment = new ItemNewCirclePutlnFragment();
        Bundle args = new Bundle();
        args.putString("change_status", change_status);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            change_status = getArguments().getString("change_status");
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_item_new_circle_putln;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    protected void initView() {
        moneyLogAdapter = new MoneyLogAdapter();
        orderRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        orderRecyclerView.setAdapter(moneyLogAdapter);
        moneyLogAdapter.setEmptyView(R.layout.layout_empty,orderRecyclerView);
        mRefreshLayout.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                pageNum = 1;
                fillData();
            }

            @Override
            public void loadMore() {
                pageNum += 1;
                fillData();
            }
        });
    }

    @Override
    protected void fillData() {
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("sj_login_token", LoginUtil.getLoginToken());
        hashMap.put("page",pageNum);
        hashMap.put("num", AppConfig.pageSize);
        hashMap.put("change_status",change_status);
        new MyHttp().doPost(Api.getDefault().list_draw_fans_money_log(hashMap), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                mRefreshLayout.finishRefresh();
                mRefreshLayout.finishLoadMore();
                MoneyLogBean moneyLogBean = new Gson().fromJson(result.toString(),MoneyLogBean.class);
                AdapterUtilsNew.setData(moneyLogAdapter,moneyLogBean.getData(),pageNum,10);

            }

            @Override
            public void onError(int code) {
                mRefreshLayout.finishRefresh();
                mRefreshLayout.finishLoadMore();
            }
        });
    }

    @Override
    protected void bindEven() {

    }
}