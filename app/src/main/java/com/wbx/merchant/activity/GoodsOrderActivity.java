package com.wbx.merchant.activity;

import android.content.Context;
import android.graphics.Paint;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.wbx.merchant.R;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.base.BaseAdapter;
import com.wbx.merchant.base.BaseViewHolder;
import com.wbx.merchant.baseapp.AppConfig;
import com.wbx.merchant.bean.OrderGoodsBean;
import com.wbx.merchant.common.LoginUtil;
import com.wbx.merchant.utils.FormatUtil;
import com.wbx.merchant.utils.GlideUtils;
import com.wbx.merchant.utils.ToastUitl;
import com.wbx.merchant.widget.CopyButtonView;
import com.wbx.merchant.widget.LoadingDialog;
import com.wbx.merchant.widget.refresh.BaseRefreshListener;
import com.wbx.merchant.widget.refresh.PullToRefreshLayout;
import com.wbx.merchant.widget.refresh.ViewStatus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 微百姓自营商品--- 我的订单
 */
public class GoodsOrderActivity extends BaseActivity implements BaseRefreshListener {
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.refresh_layout)
    PullToRefreshLayout refreshLayout;
    private List<OrderGoodsBean> lstData = new ArrayList<>();
    private GoodsOrderAdapter mAdapter;
    private boolean canLoadMore = true;//控制下次是否加载更多
    private int pageNum = AppConfig.pageNum;
    private int pageSize = AppConfig.pageSize;

    @Override
    public int getLayoutId() {
        return R.layout.activity_goods_order;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        refreshLayout.setRefreshListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        mAdapter = new GoodsOrderAdapter(lstData, mContext);
        recyclerView.setAdapter(mAdapter);
    }

    private void loadData() {
        LoadingDialog.showDialogForLoading(this, "加载中...", true);
        new MyHttp().doPost(Api.getDefault().getListOrder(LoginUtil.getLoginToken(), pageNum, pageSize), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                refreshLayout.finishRefresh();
                refreshLayout.finishLoadMore();
                List<OrderGoodsBean> lstarray = JSONObject.parseArray(result.getString("data"), OrderGoodsBean.class);
                if (pageNum == 1) {
                    lstData.clear();
                }
                if (lstarray.size() < pageSize) {
                    //说明下次已经没有数据了
                    canLoadMore = false;
                }
                lstData.addAll(lstarray);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(int code) {
                if (refreshLayout == null) {
                    return;
                }
                refreshLayout.finishLoadMore();
                refreshLayout.finishRefresh();
                refreshLayout.showView(ViewStatus.EMPTY_STATUS);
                refreshLayout.buttonClickNullData(GoodsOrderActivity.this, "loadData");
            }
        });
    }

    @Override
    public void fillData() {
        loadData();
    }

    @Override
    public void setListener() {
    }

    @Override
    public void refresh() {
        canLoadMore = true;
        pageNum = 1;
        loadData();
    }

    @Override
    public void loadMore() {
        if (!canLoadMore) {
            ToastUitl.showShort("没有更多数据");
            refreshLayout.finishLoadMore();
            return;
        }
        pageNum++;
        loadData();
    }

    public class GoodsOrderAdapter extends BaseAdapter<OrderGoodsBean, Context> {

        GoodsOrderAdapter(List<OrderGoodsBean> dataList, Context context) {
            super(dataList, context);
        }

        @Override
        public int getLayoutId(int viewType) {
            return R.layout.item_goods_order;
        }

        @Override
        public void convert(BaseViewHolder holder, OrderGoodsBean orderGoodsBean, int position) {
            holder.setText(R.id.order_id_tv, "单号：" + orderGoodsBean.getOrder_id());
            holder.setText(R.id.create_time_tv, FormatUtil.myStampToDate1(orderGoodsBean.getCreate_time() + "", "yyyy-MM-dd HH:mm"));
            holder.setText(R.id.all_order_num_tv, "共" + orderGoodsBean.getAll_order_num() + "件商品");
            holder.setText(R.id.need_pay_tv, String.format("¥%.2f", orderGoodsBean.getNeed_pay() / 100));
            final TextView expressNumberTv = holder.getView(R.id.express_number_tv);
            TextView copyTv = holder.getView(R.id.copy_tv);
            TextView shipmentsTv = holder.getView(R.id.tv_shipments);
            expressNumberTv.setText(orderGoodsBean.getExpress_number());
            copyTv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //中划线
            copyTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CopyButtonView copyButtonView = new CopyButtonView(mContext, expressNumberTv);
                    copyButtonView.init();
                }
            });
            if (orderGoodsBean.getIs_shipments() == 0) {
                shipmentsTv.setText("未发货");
            } else {
                shipmentsTv.setText("已发货");
            }
            RecyclerView recyclerView = holder.getView(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(new GoodItemAdapter(orderGoodsBean.getGoods(), mContext));
        }
    }


    public class GoodItemAdapter extends BaseAdapter<OrderGoodsBean.GoodsBean, Context> {
        GoodItemAdapter(List<OrderGoodsBean.GoodsBean> dataList, Context context) {
            super(dataList, context);
        }

        @Override
        public int getLayoutId(int viewType) {
            return R.layout.item_business_must;
        }

        @Override
        public void convert(BaseViewHolder holder, OrderGoodsBean.GoodsBean goodsBean, int position) {
            ImageView photoIv = holder.getView(R.id.iv_photo);
            GlideUtils.showSmallPic(mContext, photoIv, goodsBean.getPhoto());
            holder.setText(R.id.tv_title, goodsBean.getTitle());
            holder.setText(R.id.order_num_tv, "x" + goodsBean.getOrder_num());
            holder.setText(R.id.tv_price, String.format("¥ %.2f", goodsBean.getPrice() / 100));
            TextView originalTv = holder.getView(R.id.tv_original_price);
            originalTv.setText(String.format("¥ %.2f", goodsBean.getOriginal_price() / 100));
            originalTv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
            originalTv.getPaint().setAntiAlias(true);//抗锯齿
        }
    }
}
