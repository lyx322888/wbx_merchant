package com.wbx.merchant.activity;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.wbx.merchant.R;
import com.wbx.merchant.adapter.NumberOrderAdapter;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.base.BaseApplication;
import com.wbx.merchant.baseapp.AppConfig;
import com.wbx.merchant.bean.NumberOrderBean;
import com.wbx.merchant.widget.refresh.BaseRefreshListener;
import com.wbx.merchant.widget.refresh.PullToRefreshLayout;
import com.wbx.merchant.widget.refresh.ViewStatus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class NumberOrderActivity extends BaseActivity implements BaseRefreshListener {
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.ptrl)
    PullToRefreshLayout ptrl;
    private NumberOrderAdapter adapter;
    private int currentPage = 1;
    private boolean canLoadMore = true;
    private List<NumberOrderBean> lstData = new ArrayList<>();

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, NumberOrderActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_number_order;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NumberOrderAdapter(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void fillData() {
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    private void loadData() {
        new MyHttp().doPost(Api.getDefault().getNumberOrderList(BaseApplication.getInstance().readLoginUser().getSj_login_token(), currentPage, 10), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                if (ptrl == null) {
                    return;
                }
                ptrl.finishLoadMore();
                ptrl.finishRefresh();
                ptrl.showView(ViewStatus.CONTENT_STATUS);
                List<NumberOrderBean> lstarray = JSONObject.parseArray(result.getString("data"), NumberOrderBean.class);
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
                if (ptrl == null) {
                    return;
                }
                ptrl.finishLoadMore();
                ptrl.finishRefresh();
                if (code == AppConfig.ERROR_STATE.NULLDATA) {
                    ptrl.showView(ViewStatus.EMPTY_STATUS);
                    ptrl.buttonClickNullData(NumberOrderActivity.this, "loadData");
                } else {
                    ptrl.showView(ViewStatus.ERROR_STATUS);
                    ptrl.buttonClickError(NumberOrderActivity.this, "loadData");
                }
            }
        });
    }

    @Override
    public void setListener() {
        ptrl.setRefreshListener(this);
    }

    @OnClick(R.id.tv_right)
    public void onViewClicked() {
        NumberOrderSettingActivity.actionStart(this);
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
            ptrl.finishLoadMore();
            showShortToast("没有更多数据了");
            return;
        }
        currentPage++;
        loadData();
    }
}
