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
import com.wbx.merchant.bean.PrinterGoodsCateBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zero on 2017/1/17.
 */

public class PrinterGoodsClassifyAdapter extends RecyclerView.Adapter<PrinterGoodsClassifyAdapter.MyViewHolder> {
    private Context mContext;
    private List<PrinterGoodsCateBean> lstData = new ArrayList<>();

    public PrinterGoodsClassifyAdapter(Context context) {
        mContext = context;
    }

    public void update(List<PrinterGoodsCateBean> data) {
        lstData = data;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_printer_goods_classify, parent, false);
        MyViewHolder holder = new MyViewHolder(layout);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final PrinterGoodsCateBean data = lstData.get(position);
        holder.tvName.setText(data.getCate_name());
        holder.ivSelect.setSelected(data.getIs_print() == 1);
        holder.llRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.setIs_print(data.getIs_print() == 1 ? 0 : 1);
                holder.ivSelect.setSelected(data.getIs_print() == 1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lstData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.ll_root_view)
        LinearLayout llRootView;
        @Bind(R.id.iv_select)
        ImageView ivSelect;
        @Bind(R.id.tv_name)
        TextView tvName;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
