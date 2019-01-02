package com.wbx.merchant.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wbx.merchant.R;
import com.wbx.merchant.activity.ScanOrderActivity;
import com.wbx.merchant.activity.ScanOrderDetailActivity;
import com.wbx.merchant.bean.ScanOrderBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zero on 2017/1/17.
 */

public class ScanOrderAdapter extends RecyclerView.Adapter<ScanOrderAdapter.MyViewHolder> {
    private Context mContext;
    private int status;
    private List<ScanOrderBean> lstData = new ArrayList<>();

    public ScanOrderAdapter(Context context) {
        mContext = context;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void update(List<ScanOrderBean> data) {
        lstData.clear();
        lstData.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_scan_order, parent, false);
        MyViewHolder holder = new MyViewHolder(layout);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        if (status == ScanOrderActivity.POSITION_WAIT_PAY) {
            holder.tvState.setText("待付款");
        } else if (status == ScanOrderActivity.POSITION_COMPLETE) {
            holder.tvState.setText("已完成");
        }
        final ScanOrderBean data = lstData.get(position);
        holder.tvTableNum.setText(data.getSeat());
        holder.tvOrderNum.setText("订单编号：" + data.getOut_trade_no());
        holder.tvTime.setText("就餐时间：" + data.getCreate_time());
        holder.tvPeopleNum.setText("就餐人数：" + data.getPeople_num());
        holder.tvTotalFee.setText("合计费用：" + data.getNeed_price() + "元");
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScanOrderDetailActivity.actionStart(mContext, data.getOut_trade_no());
            }
        });
    }

    @Override
    public int getItemCount() {
        return lstData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_table_num)
        TextView tvTableNum;
        @Bind(R.id.tv_state)
        TextView tvState;
        @Bind(R.id.tv_order_num)
        TextView tvOrderNum;
        @Bind(R.id.tv_time)
        TextView tvTime;
        @Bind(R.id.tv_people_num)
        TextView tvPeopleNum;
        @Bind(R.id.tv_total_fee)
        TextView tvTotalFee;
        @Bind(R.id.root_view)
        LinearLayout rootView;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
