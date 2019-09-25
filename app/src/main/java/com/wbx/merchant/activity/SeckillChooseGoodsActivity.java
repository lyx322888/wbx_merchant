package com.wbx.merchant.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wbx.merchant.R;
import com.wbx.merchant.adapter.SecKillChooseAdapter;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.base.BaseAdapter;
import com.wbx.merchant.baseapp.AppConfig;
import com.wbx.merchant.bean.GoodsInfo;
import com.wbx.merchant.widget.refresh.BaseRefreshListener;
import com.wbx.merchant.widget.refresh.PullToRefreshLayout;
import com.wbx.merchant.widget.refresh.ViewStatus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;

/**
 * Created by wushenghui on 2017/12/12.
 */
//选择要设置的秒杀商品
public class SeckillChooseGoodsActivity extends BaseActivity implements BaseRefreshListener {
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private List<GoodsInfo> goodsInfoList = new ArrayList<>();
    private SecKillChooseAdapter mAdapter;
    private HashMap<String, Object> mParams = new HashMap<>();
    @Bind(R.id.refresh_layout)
    PullToRefreshLayout mRefreshLayout;
    private int mPageNum = AppConfig.pageNum;
    private int mPageSize = AppConfig.pageSize;
    private boolean canLoadMore = true;
    @Bind(R.id.select_goods_num_tv)
    TextView selectGoodsNumTv;
    private List<GoodsInfo> selectGoodsList = new ArrayList<>();
    private String seckill_start_time, seckill_end_time, limitations_num;

    @Override
    public int getLayoutId() {
        return R.layout.activity_seckill_choose_goods;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        seckill_start_time = getIntent().getStringExtra("seckill_start_time");
        seckill_end_time = getIntent().getStringExtra("seckill_end_time");
        limitations_num = getIntent().getStringExtra("limitations_num");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new SecKillChooseAdapter(goodsInfoList, mContext);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void fillData() {
        mParams.put("sj_login_token", userInfo.getSj_login_token());
        mParams.put("page", mPageNum);
        mParams.put("num", mPageSize);
        mParams.put("goods_type", 0);
        mParams.put("is_seckill", AppConfig.SECKILL.UNSECKILL);
        new MyHttp().doPost(Api.getDefault().getSecKillGoodsList(mParams), new HttpListener() {
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
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(int code) {
                if (code == AppConfig.ERROR_STATE.NULLDATA) {
                    //无数据情况下  如果是第一次就是没有数据 直接显示没数据的view
                    if (mPageNum == AppConfig.pageNum) {
                    } else {
                        //说明下次已经没有数据了
                        canLoadMore = false;
                    }

                } else if (code == AppConfig.ERROR_STATE.NO_NETWORK || code == AppConfig.ERROR_STATE.SERVICE_ERROR) {
                    mRefreshLayout.showView(ViewStatus.ERROR_STATUS);
                    mRefreshLayout.buttonClickError(SeckillChooseGoodsActivity.this, "fillData");
                } else {

                }
                mRefreshLayout.finishRefresh();
                mRefreshLayout.finishLoadMore();
            }
        });
    }

    @Override
    public void setListener() {
        mRefreshLayout.setRefreshListener(this);
        mAdapter.setOnItemClickListener(R.id.root_view, new BaseAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(View view, int position) {
                GoodsInfo item = mAdapter.getItem(position);
                item.setSelect(!item.isSelect());
                if (item.isSelect()) {
                    //设置成秒杀
                    item.setIs_seckill(1);
                    selectGoodsList.add(item);
                } else {
                    selectGoodsList.remove(item);
                }
                mAdapter.notifyDataSetChanged();
                selectGoodsNumTv.setText(Html.fromHtml("已选择<font color=#ff0000>" + selectGoodsList.size() + "</font>件"));
            }
        });
    }

    public void submit(View view) {
        if (selectGoodsList.size() == 0) {
            showShortToast("请选择需要参与活动的商品");
            return;
        }
        Intent intent = new Intent(mContext, ReleaseSecKillActivity.class);
        intent.putExtra("selectGoodsList", (Serializable) selectGoodsList);
        intent.putExtra("seckill_start_time", seckill_start_time);
        intent.putExtra("seckill_end_time", seckill_end_time);
        intent.putExtra("limitations_num", limitations_num);
        startActivityForResult(intent, 1000);
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
            mRefreshLayout.finishLoadMore();
            showShortToast("没有更多数据了");
            return;
        }
        fillData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
    }
}
