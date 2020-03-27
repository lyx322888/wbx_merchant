package com.wbx.merchant.activity;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.fastjson.JSONObject;
import com.wbx.merchant.R;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.base.BaseAdapter;
import com.wbx.merchant.base.BaseViewHolder;
import com.wbx.merchant.baseapp.AppConfig;
import com.wbx.merchant.bean.OrderBean;
import com.wbx.merchant.bean.ProprietaryGoodsBean;
import com.wbx.merchant.common.LoginUtil;
import com.wbx.merchant.utils.GlideUtils;
import com.wbx.merchant.utils.RetrofitUtils;
import com.wbx.merchant.utils.ToastUitl;
import com.wbx.merchant.widget.CicleAddAndSubView;
import com.wbx.merchant.widget.decoration.DividerItemDecoration;
import com.wbx.merchant.widget.refresh.BaseRefreshListener;
import com.wbx.merchant.widget.refresh.PullToRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class BusinessMustActivity extends BaseActivity implements BaseRefreshListener {

    @Bind(R.id.goods_recycler)
    RecyclerView mRecycler;
    @Bind(R.id.refresh_layout)
    PullToRefreshLayout refreshLayout;
    private List<ProprietaryGoodsBean.DataBean> dataList = new ArrayList<>();
    private ProprietaryGoodsAdapter mGoodsAdapter;
    private int mPageNum = AppConfig.pageNum;
    private int mPageSize = AppConfig.pageSize;
    private boolean canLoadMore = true;


    @Override

    public int getLayoutId() {
        return R.layout.activity_business_must;
    }

    @Override
    public void initPresenter() {
    }

    @Override
    public void initView() {
        mRecycler.setLayoutManager(new LinearLayoutManager(mContext));
        mRecycler.setHasFixedSize(true);
        mRecycler.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mGoodsAdapter = new ProprietaryGoodsAdapter(dataList, mContext);
        mRecycler.setAdapter(mGoodsAdapter);
        mGoodsAdapter.setOnItemClickListener(R.id.root_view, new BaseAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(View view, int position) {
                Intent intent = new Intent(mContext, GoodsDetailsActivity.class);
                intent.putExtra("details_goods_id", dataList.get(position).getGoods_id());
                startActivity(intent);
            }
        });
        refreshLayout.setRefreshListener(this);
    }

    @Override
    public void fillData() {
        RetrofitUtils.getInstance().server().getProprietaryGoods(LoginUtil.getLoginToken(), mPageNum, mPageSize)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<ProprietaryGoodsBean>() {
                    @Override
                    public void onCompleted() {
                        refreshLayout.finishRefresh();
                        refreshLayout.finishLoadMore();
                    }

                    @Override
                    public void onError(Throwable e) {
                        refreshLayout.finishRefresh();
                        refreshLayout.finishLoadMore();
                    }

                    @Override
                    public void onNext(ProprietaryGoodsBean goodsBean) {
                        refreshLayout.finishRefresh();
                        refreshLayout.finishLoadMore();
                        if (goodsBean.getData().size() < mPageSize) {
                            //说明下次已经没有数据了
                            canLoadMore = false;
                        }
                        if (mPageNum == AppConfig.pageNum) {
                            dataList.clear();
                        }
                        dataList.addAll(goodsBean.getData());
                        mGoodsAdapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    public void setListener() {
    }

    @OnClick({R.id.button_buy, R.id.tv_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_buy:
                if (isNext()) {
                    order();
                } else {
                    ToastUitl.showShort("请选择商品");
                }
                break;
            case R.id.tv_right:
                Intent intent = new Intent(BusinessMustActivity.this, GoodsOrderActivity.class);
                startActivity(intent);
                break;
        }
    }

    private boolean isNext() {
        int mCount = 0;
        if (dataList != null && dataList.size() != 0) {
            for (ProprietaryGoodsBean.DataBean bean : dataList) {
                mCount += bean.getOrder_num();
            }
        }
        return mCount != 0;
    }

    private void order() {
        RetrofitUtils.getInstance().server().getOeder(LoginUtil.getLoginToken())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<OrderBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(OrderBean orderBean) {
                        Intent intent = new Intent(BusinessMustActivity.this, ShopCartActivity.class);
                        intent.putExtra(ShopCartActivity.order, orderBean);
                        startActivity(intent);
                        finish();
                    }
                });
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
            refreshLayout.finishLoadMore();
            showShortToast("没有更多数据了");
            return;
        }
        fillData();
    }

    /**
     * 自营商品适配器
     */
    public class ProprietaryGoodsAdapter extends BaseAdapter<ProprietaryGoodsBean.DataBean, Context> {

        private ProprietaryGoodsAdapter(List<ProprietaryGoodsBean.DataBean> dataList, Context context) {
            super(dataList, context);
        }

        @Override
        public int getLayoutId(int viewType) {
            return R.layout.item_proproetary_goods;
        }

        @Override
        public void convert(BaseViewHolder holder, final ProprietaryGoodsBean.DataBean dataBean, int position) {
            holder.setText(R.id.tv_price, "¥" + (float) dataBean.getPrice() / 100);
            holder.setText(R.id.tv_name, dataBean.getTitle());
            holder.setText(R.id.tv_ps, dataBean.getDescribe());
            ImageView tvPhoto = holder.getView(R.id.iv_photo);
            CicleAddAndSubView asView = holder.getView(R.id.ASView);
            asView.setNum(dataBean.getOrder_num());
            GlideUtils.showRoundSmallPic(mContext, tvPhoto, dataBean.getPhoto());
            asView.setOnNumChangeListener(new CicleAddAndSubView.OnNumChangeListener() {
                @Override
                public void onNumChange(View view, String type, final int num) {//type: plus加  minus减
                    new MyHttp().doPost(Api.getDefault().getUpdate(LoginUtil.getLoginToken(), dataBean.getGoods_id(), type), new HttpListener() {
                        @Override
                        public void onSuccess(JSONObject result) {
                            dataBean.setOrder_num(num);
                        }

                        @Override
                        public void onError(int code) {

                        }
                    });
                }
            });
        }
    }
}
