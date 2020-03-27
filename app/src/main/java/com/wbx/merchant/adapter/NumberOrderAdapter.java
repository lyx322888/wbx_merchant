package com.wbx.merchant.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wbx.merchant.R;
import com.wbx.merchant.activity.NumberOrderDetailActivity;
import com.wbx.merchant.bean.NumberOrderBean;
import com.wbx.merchant.utils.DateUtil;
import com.wbx.merchant.utils.GlideUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zero on 2017/1/17.
 */

public class NumberOrderAdapter extends RecyclerView.Adapter<NumberOrderAdapter.MyViewHolder> {
    private Context mContext;
    private List<NumberOrderBean> lstData = new ArrayList<>();

    public NumberOrderAdapter(Context context) {
        mContext = context;
    }

    public void update(List<NumberOrderBean> data) {
        lstData.clear();
        lstData.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_number_order, parent, false);
        MyViewHolder holder = new MyViewHolder(layout);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final NumberOrderBean data = lstData.get(position);
        holder.tvPickFoodNum.setText(data.getOrder_take_number());
        holder.tvOrderNum.setText("订单编号：" + data.getOrder_id());
        holder.tvTime.setText(DateUtil.formatDateAndTime2(data.getCreate_time()));
        holder.llContainer.removeAllViews();
        for (NumberOrderBean.GoodsBean goodsBean : data.getGoods()) {
            View layout = LayoutInflater.from(mContext).inflate(R.layout.item_number_order_goods, null);
            ImageView ivGoods = layout.findViewById(R.id.iv_goods);
            GlideUtils.showMediumPic(mContext, ivGoods, goodsBean.getPhoto());
            TextView tvPrice = layout.findViewById(R.id.tv_price);
            tvPrice.setText(String.format("¥%.2f", goodsBean.getPrice() / 100.00));
            holder.llContainer.addView(layout);
        }
        holder.tvTotalFee.setText(String.format("合计费用:%.2f元", data.getTotal_price() / 100.00));
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NumberOrderDetailActivity.actionStart(mContext, data);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lstData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_pick_food_num)
        TextView tvPickFoodNum;
        @Bind(R.id.tv_order_num)
        TextView tvOrderNum;
        @Bind(R.id.tv_time)
        TextView tvTime;
        @Bind(R.id.tv_total_fee)
        TextView tvTotalFee;
        @Bind(R.id.root_view)
        LinearLayout rootView;
        @Bind(R.id.ll_container)
        LinearLayout llContainer;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
