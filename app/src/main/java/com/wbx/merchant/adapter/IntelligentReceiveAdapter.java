package com.wbx.merchant.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wbx.merchant.R;
import com.wbx.merchant.bean.IntelligentReceiveBean;
import com.wbx.merchant.utils.DateUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zero on 2017/1/17.
 */

public class IntelligentReceiveAdapter extends RecyclerView.Adapter<IntelligentReceiveAdapter.MyViewHolder> {
    private Context mContext;

    private List<IntelligentReceiveBean> lstData = new ArrayList<>();

    public IntelligentReceiveAdapter(Context context) {
        mContext = context;
    }

    public void update(List<IntelligentReceiveBean> data) {
        lstData.clear();
        lstData.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_intelligent_receive, parent, false);
        MyViewHolder holder = new MyViewHolder(layout);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        IntelligentReceiveBean data = lstData.get(position);
        holder.tvOrderNum.setText(data.getOrder_id());
        holder.tvName.setText(data.getNickname());
        holder.tvPayType.setText(data.getPay_type());
        holder.tvTime.setText(DateUtil.formatDateAndTime(data.getCreate_time()));
        holder.tvMoney.setText(String.format("¥%.2f", data.getMoney() / 100.00));
    }

    @Override
    public int getItemCount() {
        return lstData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_order_num)
        TextView tvOrderNum;
        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(R.id.tv_pay_type)
        TextView tvPayType;
        @Bind(R.id.tv_time)
        TextView tvTime;
        @Bind(R.id.tv_money)
        TextView tvMoney;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
