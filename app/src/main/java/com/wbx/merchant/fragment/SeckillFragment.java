package com.wbx.merchant.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wbx.merchant.R;
import com.wbx.merchant.adapter.SeckillAdapter;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseAdapter;
import com.wbx.merchant.base.BaseFragment;
import com.wbx.merchant.baseapp.AppConfig;
import com.wbx.merchant.bean.GoodsInfo;
import com.wbx.merchant.utils.ShareUtils;
import com.wbx.merchant.widget.iosdialog.AlertDialog;
import com.wbx.merchant.widget.refresh.BaseRefreshListener;
import com.wbx.merchant.widget.refresh.PullToRefreshLayout;
import com.wbx.merchant.widget.refresh.ViewStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;

/**
 * Created by wushenghui on 2017/12/12.
 */
//秒杀
public class SeckillFragment extends BaseFragment implements BaseRefreshListener {
    @Bind(R.id.seckill_recycler_view)
    RecyclerView mRecyclerView;
    public static final String POSITION = "position";
    private List<GoodsInfo> goodsInfoList = new ArrayList<>();
    private SeckillAdapter mAdapter;
    private HashMap<String, Object> mParams = new HashMap<>();
    @Bind(R.id.refresh_layout)
    PullToRefreshLayout mRefreshLayout;
    private int goodsType;
    private int mPageNum = AppConfig.pageNum;
    private int mPageSize = AppConfig.pageSize;
    private boolean canLoadMore = true;
    @Bind(R.id.seckill_null_data_layout)
    RelativeLayout nullDataLayout;
    private GoodsInfo selectGoodsInfo;
    private Dialog shareDialog;
    private View shareInflate;

    public static SeckillFragment newInstance(int position) {
        SeckillFragment tabFragment = new SeckillFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(POSITION, position);
        tabFragment.setArguments(bundle);
        return tabFragment;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_seckill;
    }

    @Override
    public void initPresenter() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    @Override
    public void onResume() {
        super.onResume();
        canLoadMore = true;
        mPageNum = AppConfig.pageNum;
        getServiceData();
    }

    private void getServiceData() {
        goodsType = getArguments().getInt(POSITION, 0);
        mParams.put("sj_login_token", loginUser.getSj_login_token());
        mParams.put("page", mPageNum);
        mParams.put("num", mPageSize);
        mParams.put("goods_type", goodsType);
        mParams.put("is_seckill", AppConfig.SECKILL.SECKILL);
        new MyHttp().doPost(Api.getDefault().getSecKillGoodsList(mParams), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                if (mRefreshLayout == null) {//页面已销毁
                    return;
                }
                nullDataLayout.setVisibility(View.GONE);
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
                if (loginUser.getGrade_id() != AppConfig.StoreGrade.MARKET) {
                    //实体店数据赋值到菜市场 兼容
                    for (GoodsInfo goodsInfo : dataList) {
                        goodsInfo.setProduct_id(goodsInfo.getGoods_id());
                        goodsInfo.setProduct_name(goodsInfo.getTitle());
                        goodsInfo.setDesc(goodsInfo.getIntro());
                        goodsInfo.setCate_id(goodsInfo.getShopcate_id());
                    }
                }
                goodsInfoList.addAll(dataList);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(int code) {
                if (mRefreshLayout == null) {//页面已销毁
                    return;
                }
                if (code == AppConfig.ERROR_STATE.NULLDATA) {
                    //无数据情况下  如果是第一次就是没有数据 直接显示没数据的view
                    if (mPageNum == AppConfig.pageNum) {
                        mRefreshLayout.showView(ViewStatus.CONTENT_STATUS);
                        nullDataLayout.setVisibility(View.VISIBLE);
                    } else {
                        //说明下次已经没有数据了
                        canLoadMore = false;
                    }

                } else if (code == AppConfig.ERROR_STATE.NO_NETWORK || code == AppConfig.ERROR_STATE.SERVICE_ERROR) {
                    mRefreshLayout.showView(ViewStatus.ERROR_STATUS);
                    mRefreshLayout.buttonClickError(SeckillFragment.this, "getServiceData");
                } else {

                }
                mRefreshLayout.finishRefresh();
                mRefreshLayout.finishLoadMore();
            }
        });
    }

    @Override
    protected void initView() {
        mRefreshLayout.showView(ViewStatus.LOADING_STATUS);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new SeckillAdapter(goodsInfoList, getActivity());
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void fillData() {

    }

    @Override
    protected void bindEven() {
        mAdapter.setOnItemClickListener(R.id.share_im, new BaseAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(View view, int position) {
                ShareUtils.getInstance().share(getContext(), loginUser.getShop_name() + "_微百姓", "我在微百姓开了一家店，赶快来看看吧", loginUser.getFace(), loginUser.getGrade_id() == AppConfig.StoreGrade.MARKET ? "http://www.wbx365.com/wap/ele/shop/shop_id/" + loginUser.getShop_id() + ".html" : "http://www.wbx365.com/wap/shop/goods/shop_id/" + loginUser.getShop_id() + ".html");
            }
        });
        mRefreshLayout.setRefreshListener(this);
        mAdapter.setOnItemClickListener(R.id.delete_im, new BaseAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(View view, int position) {
                selectGoodsInfo = mAdapter.getItem(position);
                new AlertDialog(getActivity()).builder()
                        .setTitle("提示")
                        .setMsg("取消当前商品秒杀活动？")
                        .setNegativeButton("再想想", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        })
                        .setPositiveButton("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                cancelSecKill();
                            }
                        }).show();
            }
        });
    }


    /**
     * 取消秒杀
     */
    private void cancelSecKill() {
        new MyHttp().doPost(Api.getDefault().cancelSecKillGoods(loginUser.getSj_login_token(), selectGoodsInfo.getProduct_id()), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                goodsInfoList.remove(selectGoodsInfo);
                mAdapter.notifyDataSetChanged();
                showShortToast("取消成功！");
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    @Override
    public void refresh() {
        canLoadMore = true;
        mPageNum = AppConfig.pageNum;
        getServiceData();
    }

    @Override
    public void loadMore() {
        mPageNum++;
        if (!canLoadMore) {
            mRefreshLayout.finishLoadMore();
            showShortToast("没有更多数据了");
            return;
        }
        getServiceData();
    }
}
