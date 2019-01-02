package com.wbx.merchant.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.wbx.merchant.R;
import com.wbx.merchant.adapter.MerchantSubsidyAdapter;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.base.BaseApplication;
import com.wbx.merchant.baseapp.AppConfig;
import com.wbx.merchant.bean.MerchantSubsidyBean;
import com.wbx.merchant.widget.refresh.BaseRefreshListener;
import com.wbx.merchant.widget.refresh.PullToRefreshLayout;
import com.wbx.merchant.widget.refresh.ViewStatus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class MerchantSubsidiesActivity extends BaseActivity implements BaseRefreshListener {
    @Bind(R.id.tv_total_balance)
    TextView tvTotalBalance;
    @Bind(R.id.tv_can_use_balance)
    TextView tvCanUseBalance;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.ptrl)
    PullToRefreshLayout ptrl;
    private List<MerchantSubsidyBean.ListBean> lstData = new ArrayList<>();
    private MerchantSubsidyAdapter mAdapter;
    private int mPageNum = AppConfig.pageNum;
    private int mPageSize = AppConfig.pageSize;
    private boolean canLoadMore = true;
    private MerchantSubsidyBean data;
    private Gson gson = new Gson();

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, MerchantSubsidiesActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_merchant_subsidies;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        ptrl.showView(ViewStatus.LOADING_STATUS);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MerchantSubsidyAdapter(lstData, this);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void fillData() {
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        new MyHttp().doPost(Api.getDefault().getMerchantSubsidy(BaseApplication.getInstance().readLoginUser().getSj_login_token(), mPageNum, mPageSize), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                data = gson.fromJson(result.getString("data"), MerchantSubsidyBean.class);
                tvTotalBalance.setText(String.format("%.2f", (float) data.getAll_money() / 100));
                tvCanUseBalance.setText(String.format("%.2f", (float) data.getShop_subsidy_money() / 100));
                ptrl.finishRefresh();
                ptrl.finishLoadMore();
                if ((null == data.getList() || data.getList().size() == 0) && mPageNum == AppConfig.pageNum) {
                    ptrl.showView(ViewStatus.EMPTY_STATUS);
                    ptrl.buttonClickNullData(MerchantSubsidiesActivity.this, "loadData");
                    return;
                }
                if (data.getList().size() < mPageSize) {
                    //说明下次已经没有数据了
                    canLoadMore = false;
                }
                if (mPageNum == AppConfig.pageNum) {
                    lstData.clear();
                }
                ptrl.showView(ViewStatus.CONTENT_STATUS);
                lstData.addAll(data.getList());
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(int code) {
                if (code == AppConfig.ERROR_STATE.NULLDATA) {
                    //无数据情况下  如果是第一次就是没有数据 直接显示没数据的view
                    if (mPageNum == AppConfig.pageNum) {
                        ptrl.showView(ViewStatus.EMPTY_STATUS);
                        ptrl.buttonClickNullData(MerchantSubsidiesActivity.this, "loadData");
                    } else {
                        //说明下次已经没有数据了
                        canLoadMore = false;
                    }
                } else if (code == AppConfig.ERROR_STATE.NO_NETWORK || code == AppConfig.ERROR_STATE.SERVICE_ERROR) {
                    ptrl.showView(ViewStatus.ERROR_STATUS);
                    ptrl.buttonClickError(MerchantSubsidiesActivity.this, "loadData");
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

    @OnClick({R.id.rl_right, R.id.tv_withdraw})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_right:
                SubsidyExplainActivity.actionStart(this);
                break;
            case R.id.tv_withdraw:
                Intent intentCash = new Intent(this, CashActivity.class);
                intentCash.putExtra("type", CashActivity.TYPE_REWARD);
                intentCash.putExtra("balance", data.getShop_subsidy_money());
                startActivity(intentCash);
                break;
        }
    }

    @Override
    public void refresh() {
        canLoadMore = true;
        mPageNum = AppConfig.pageNum;
        loadData();
    }

    @Override
    public void loadMore() {
        mPageNum++;
        if (!canLoadMore) {
            ptrl.finishLoadMore();
            showShortToast("没有更多数据了");
            return;
        }
        loadData();
    }
}
