package com.wbx.merchant.activity;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wbx.merchant.R;
import com.wbx.merchant.adapter.IntelligentReceiveAdapter;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.base.BaseApplication;
import com.wbx.merchant.baseapp.AppConfig;
import com.wbx.merchant.bean.IntelligentReceiveBean;
import com.wbx.merchant.widget.refresh.BaseRefreshListener;
import com.wbx.merchant.widget.refresh.PullToRefreshLayout;
import com.wbx.merchant.widget.refresh.ViewStatus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class IntelligentReceiveActivity extends BaseActivity implements BaseRefreshListener {
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.refresh_layout)
    PullToRefreshLayout refreshLayout;
    private IntelligentReceiveAdapter mAdapter;
    private List<IntelligentReceiveBean> lstData = new ArrayList<>();
    private int currentPage = 1;

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, IntelligentReceiveActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_intelligent_receive;
    }

    @Override
    public void initPresenter() {
    }

    @Override
    public void initView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new IntelligentReceiveAdapter(this);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void fillData() {
        refreshLayout.showView(ViewStatus.LOADING_STATUS);
        loadData(false);
    }

    @Override
    public void setListener() {
        refreshLayout.setRefreshListener(this);
    }

    @Override
    public void refresh() {
        loadData(false);
    }

    private void loadData(final boolean isLoadMore) {
        if (isLoadMore) {
            currentPage++;
        } else {
            currentPage = 1;
        }
        new MyHttp().doPost(Api.getDefault().getIntelligentReceiveList(BaseApplication.getInstance().readLoginUser().getSj_login_token(), currentPage, 10), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                if (isLoadMore) {
                    refreshLayout.finishLoadMore();
                } else {
                    refreshLayout.finishRefresh();
                }
                List<IntelligentReceiveBean> data = JSONArray.parseArray(result.getString("data"), IntelligentReceiveBean.class);
                if (!isLoadMore && (data == null || data.size() == 0)) {
                    refreshLayout.showView(ViewStatus.EMPTY_STATUS);
                    refreshLayout.buttonClickNullData(IntelligentReceiveActivity.this, "refresh");
                    return;
                }
                if (null != refreshLayout) {
                    refreshLayout.showView(ViewStatus.CONTENT_STATUS);
                }
                if (!isLoadMore) {
                    lstData.clear();
                }
                lstData.addAll(data);
                mAdapter.update(lstData);
            }

            @Override
            public void onError(int code) {
                if (isLoadMore) {
                    refreshLayout.finishLoadMore();
                } else {
                    refreshLayout.finishRefresh();
                }
                if (code == AppConfig.ERROR_STATE.NULLDATA && refreshLayout != null) {
                    if (isLoadMore) {
                        refreshLayout.setCanLoadMore(false);
                    } else {
                        refreshLayout.showView(ViewStatus.EMPTY_STATUS);
                        refreshLayout.buttonClickNullData(IntelligentReceiveActivity.this, "refresh");
                    }
                } else {
                    refreshLayout.showView(ViewStatus.ERROR_STATUS);
                    refreshLayout.buttonClickError(IntelligentReceiveActivity.this, "refresh");
                }
            }
        });
    }

    @Override
    public void loadMore() {
        loadData(true);
    }
}
