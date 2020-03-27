package com.wbx.merchant.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wbx.merchant.R;
import com.wbx.merchant.bean.ScanOrderGoodsBean;
import com.wbx.merchant.utils.GlideUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zero on 2017/1/17.
 */

public class AddFoodAdapter extends RecyclerView.Adapter<AddFoodAdapter.MyViewHolder> {
    private Context mContext;

    private List<ScanOrderGoodsBean.GoodsBean> lstData = new ArrayList<>();

    public AddFoodAdapter(Context context) {
        mContext = context;
    }

    public void update(List<ScanOrderGoodsBean.GoodsBean> data) {
        lstData.clear();
        lstData.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_scan_order_food, parent, false);
        MyViewHolder holder = new MyViewHolder(layout);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final ScanOrderGoodsBean.GoodsBean data = lstData.get(position);
        GlideUtils.showMediumPic(mContext, holder.ivGoods, data.getPhoto());
        holder.tvName.setText(data.getTitle());
        holder.tvPrice.setText(String.format("¥%.2f", data.getPrice() / 100.00));
        holder.tvSpec.setText(data.getAttr_name());
        holder.tvSpec.setVisibility(data.getIs_attr() > 0 ? View.VISIBLE : View.GONE);
        holder.tvNum.setText(String.valueOf(data.getNum()));
        holder.ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.setNum(data.getNum() + 1);
                holder.tvNum.setText(String.valueOf(data.getNum()));
            }
        });
        holder.ivReduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data.getNum() > 0) {
                    data.setNum(data.getNum() - 1);
                    holder.tvNum.setText(String.valueOf(data.getNum()));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return lstData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.iv_goods)
        ImageView ivGoods;
        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(R.id.tv_price)
        TextView tvPrice;
        @Bind(R.id.tv_spec)
        TextView tvSpec;
        @Bind(R.id.iv_reduce)
        ImageView ivReduce;
        @Bind(R.id.tv_num)
        TextView tvNum;
        @Bind(R.id.iv_add)
        ImageView ivAdd;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
