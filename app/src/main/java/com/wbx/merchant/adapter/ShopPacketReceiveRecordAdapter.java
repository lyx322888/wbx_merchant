package com.wbx.merchant.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wbx.merchant.R;
import com.wbx.merchant.bean.RedPacketListBean;
import com.wbx.merchant.utils.GlideUtils;
import com.wbx.merchant.widget.CircleImageView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zero on 2017/1/17.
 */

public class ShopPacketReceiveRecordAdapter extends RecyclerView.Adapter<ShopPacketReceiveRecordAdapter.MyViewHolder> {
    private Context mContext;
    private List<RedPacketListBean> lstData = new ArrayList<>();
    private DecimalFormat format = new DecimalFormat("0.00");

    public ShopPacketReceiveRecordAdapter(Context context) {
        mContext = context;
    }

    public void update(List<RedPacketListBean> data) {
        lstData = data;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shop_red_packet_receive_record, parent, false);
        MyViewHolder holder = new MyViewHolder(layout);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        RedPacketListBean data = lstData.get(position);
        if (TextUtils.isEmpty(data.getFace())) {
            holder.ivUser.setImageResource(R.drawable.logo);
        } else {
            GlideUtils.showSmallPic(mContext, holder.ivUser, data.getFace());
        }
        holder.tvName.setText(data.getNickname());
        holder.tvDate.setText(data.getCreate_time());
        holder.tvMoney.setText(String.valueOf(format.format(data.getMoney())));
    }

    @Override
    public int getItemCount() {
        return lstData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.iv_user)
        CircleImageView ivUser;
        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(R.id.tv_date)
        TextView tvDate;
        @Bind(R.id.tv_money)
        TextView tvMoney;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
