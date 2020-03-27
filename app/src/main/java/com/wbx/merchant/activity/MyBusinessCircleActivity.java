package com.wbx.merchant.activity;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wbx.merchant.R;
import com.wbx.merchant.adapter.MyBusinessCircleAdapter;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.base.BaseApplication;
import com.wbx.merchant.baseapp.AppConfig;
import com.wbx.merchant.bean.BusinessCircleBean;
import com.wbx.merchant.widget.refresh.BaseRefreshListener;
import com.wbx.merchant.widget.refresh.PullToRefreshLayout;
import com.wbx.merchant.widget.refresh.ViewStatus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class MyBusinessCircleActivity extends BaseActivity implements BaseRefreshListener {
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.ptrl)
    PullToRefreshLayout ptrl;
    private int mPageNum = AppConfig.pageNum;
    private int mPageSize = AppConfig.pageSize;
    private List<BusinessCircleBean> lstData = new ArrayList<>();
    private MyBusinessCircleAdapter mAdapter;
    private boolean canLoadMore = true;

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, MyBusinessCircleActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_business_circle;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        ptrl.showView(ViewStatus.LOADING_STATUS);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MyBusinessCircleAdapter(this, userInfo);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void fillData() {
        new MyHttp().doPost(Api.getDefault().getDiscoveryList(BaseApplication.getInstance().readLoginUser().getSj_login_token(), mPageNum, mPageSize), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                List<BusinessCircleBean> data = JSONArray.parseArray(result.getString("data"), BusinessCircleBean.class);
                ptrl.finishRefresh();
                ptrl.finishLoadMore();
                if ((null == data || data.size() == 0) && mPageNum == AppConfig.pageNum) {
                    ptrl.showView(ViewStatus.EMPTY_STATUS);
                    ptrl.buttonClickNullData(MyBusinessCircleActivity.this, "fillData");
                    return;
                }
                if (data.size() < mPageSize) {
                    //说明下次已经没有数据了
                    canLoadMore = false;
                }
                if (mPageNum == AppConfig.pageNum) {
                    lstData.clear();
                }
                ptrl.showView(ViewStatus.CONTENT_STATUS);
                lstData.addAll(data);
                mAdapter.update(lstData);
            }

            @Override
            public void onError(int code) {
                if (code == AppConfig.ERROR_STATE.NULLDATA) {
                    //无数据情况下  如果是第一次就是没有数据 直接显示没数据的view
                    if (mPageNum == AppConfig.pageNum) {
                        ptrl.showView(ViewStatus.EMPTY_STATUS);
                        ptrl.buttonClickNullData(MyBusinessCircleActivity.this, "fillData");
                    } else {
                        //说明下次已经没有数据了
                        canLoadMore = false;
                    }
                } else if (code == AppConfig.ERROR_STATE.NO_NETWORK || code == AppConfig.ERROR_STATE.SERVICE_ERROR) {
                    ptrl.showView(ViewStatus.ERROR_STATUS);
                    ptrl.buttonClickError(MyBusinessCircleActivity.this, "fillData");
                } else {

                }
                ptrl.finishRefresh();
                ptrl.finishLoadMore();
            }
        });
    }

    @Override
    public void setListener() {
        ptrl.setRefreshListener(this);
    }

    @OnClick(R.id.rl_release)
    public void onViewClicked() {
        PublishBusinessCircleActivity.actionStart(this);
        finish();
    }

    @Override
    public void refresh() {
        canLoadMore = true;
        mPageNum = AppConfig.pageNum;
        fillData();
    }

    @Override
    public void loadMore() {
        mPageNum++;
        if (!canLoadMore) {
            ptrl.finishLoadMore();
            showShortToast("没有更多数据了");
            return;
        }
        fillData();
    }
}
