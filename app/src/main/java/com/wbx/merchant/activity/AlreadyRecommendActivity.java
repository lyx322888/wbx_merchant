package com.wbx.merchant.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.wbx.merchant.R;
import com.wbx.merchant.adapter.AlreadyRecommendAdapter;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.baseapp.AppConfig;
import com.wbx.merchant.bean.GoodsInfo;
import com.wbx.merchant.widget.LoadingDialog;
import com.wbx.merchant.widget.refresh.BaseRefreshListener;
import com.wbx.merchant.widget.refresh.PullToRefreshLayout;
import com.wbx.merchant.widget.refresh.ViewStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

//已推荐
public class AlreadyRecommendActivity extends BaseActivity {


    @Bind(R.id.rl_right)
    RelativeLayout rlRight;
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.refresh_layout)
    PullToRefreshLayout mRefreshLayout;
    private int mPageNum = AppConfig.pageNum;
    private int mPageSize = AppConfig.pageSize;
    private List<GoodsInfo> goodsInfoList = new ArrayList<>();
    private boolean canLoadMore = true;
    private AlreadyRecommendAdapter alreadyRecommendAdapter;
    @Override
    public int getLayoutId() {
        return R.layout.activity_already_recommend;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        alreadyRecommendAdapter = new AlreadyRecommendAdapter(R.layout.item_already_recommend, goodsInfoList);
        mRecyclerView.setAdapter(alreadyRecommendAdapter);
        alreadyRecommendAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()){
                    case R.id.iv_delete:
                        //删除已选
                        postDelete(alreadyRecommendAdapter.getData().get(position).getGoods_id());
                        break;
                }
            }
        });
    }
    //删除已选
    private void postDelete(int goods_id){
        LoadingDialog.showDialogForLoading(this);
        HashMap<String, Object> mParams = new HashMap<>();
        mParams.put("sj_login_token", userInfo.getSj_login_token());
        mParams.put("goods_id", goods_id);
        new MyHttp().doPost(Api.getDefault().getDeleteRecommned(mParams), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                showShortToast("已删除成功");
                canLoadMore = true;
                mPageNum = AppConfig.pageNum;
                fillData();
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    @Override
    public void fillData() {
        HashMap<String, Object> mParams = new HashMap<>();
        mParams.put("sj_login_token", userInfo.getSj_login_token());
        mParams.put("page", mPageNum);
        mParams.put("cate_id", 0);
        mParams.put("num", mPageSize);
        mParams.put("is_recommend", 1);
        new MyHttp().doPost(Api.getDefault().getRecommend_goods(mParams), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                List<GoodsInfo> dataList = JSONArray.parseArray(result.getString("data"), GoodsInfo.class);
                mRefreshLayout.finishRefresh();
                mRefreshLayout.finishLoadMore();
                if (dataList.size() < mPageSize) {
                    //说明下次已经没有数据了
                    canLoadMore = false;
                }
                if (mPageNum == AppConfig.pageNum) {
                    goodsInfoList.clear();
                }
                mRefreshLayout.showView(ViewStatus.CONTENT_STATUS);
                if (userInfo.getGrade_id() != AppConfig.StoreGrade.MARKET) {
                    //实体店数据赋值到菜市场 兼容
                    for (GoodsInfo goodsInfo : dataList) {
                        goodsInfo.setProduct_id(goodsInfo.getGoods_id());
                        goodsInfo.setProduct_name(goodsInfo.getTitle());
                        goodsInfo.setDesc(goodsInfo.getIntro());
                        goodsInfo.setCate_id(goodsInfo.getShopcate_id());
                    }
                } else {
                    for (GoodsInfo goodsInfo : dataList) {
                        goodsInfo.setGoods_id(goodsInfo.getProduct_id());
                    }
                }

                goodsInfoList.addAll(dataList);
                alreadyRecommendAdapter.notifyDataSetChanged();

            }

            @Override
            public void onError(int code) {
                if (code == AppConfig.ERROR_STATE.NULLDATA) {
                    //无数据情况下  如果是第一次就是没有数据 直接显示没数据的view
                    if (mPageNum == AppConfig.pageNum) {
                        goodsInfoList.clear();
                        alreadyRecommendAdapter.notifyDataSetChanged();
                        mRefreshLayout.showView(ViewStatus.EMPTY_STATUS);
                    } else {
                        //说明下次已经没有数据了
                        canLoadMore = false;
                    }

                } else if (code == AppConfig.ERROR_STATE.NO_NETWORK || code == AppConfig.ERROR_STATE.SERVICE_ERROR) {
                    mRefreshLayout.showView(ViewStatus.ERROR_STATUS);
                    mRefreshLayout.buttonClickError(AlreadyRecommendActivity.this, "fillData");
                } else {

                }
                mRefreshLayout.finishRefresh();
                mRefreshLayout.finishLoadMore();
            }
        });
    }

    @Override
    public void setListener() {
        mRefreshLayout.setRefreshListener(new BaseRefreshListener() {
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
                    mRefreshLayout.finishLoadMore();
                    showShortToast("没有更多数据了");
                    return;
                }
                fillData();
            }
        });
    }


}
