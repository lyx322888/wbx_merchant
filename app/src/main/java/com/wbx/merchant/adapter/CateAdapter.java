package com.wbx.merchant.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wbx.merchant.R;
import com.wbx.merchant.bean.CateBean;

import java.util.List;

/**
 * Created by wushenghui on 2017/6/23.
 */

public class CateAdapter extends RecyclerView.Adapter<CateAdapter.VH> {
    Context context;
    List<CateBean.DataBean.CatesBean> list;
    private static int getPosition;

    public CateAdapter(Context context, List<CateBean.DataBean.CatesBean> list) {
        this.context = context;
        this.list = list;
    }

    public static void setGetPosition(int getPosition) {
        CateAdapter.getPosition = getPosition;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cate, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final VH holder, int position) {
        if (getPosition == position) {
            holder.nameTV.setTextColor(context.getResources().getColor(R.color.fenlei_text));
        } else {
            holder.nameTV.setTextColor(context.getResources().getColor(R.color.fenlei_text2));
        }
        holder.nameTV.setText(list.get(position).getCate_name());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recyclerViewItemClick != null) {
                    recyclerViewItemClick.recyclerViewItemClick(holder.getAdapterPosition(), v);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class VH extends RecyclerView.ViewHolder {
        TextView nameTV;

        public VH(View itemView) {
            super(itemView);
            nameTV = itemView.findViewById(R.id.cate_name_tv);
        }
    }

    RecyclerViewItemClick recyclerViewItemClick;

    public interface RecyclerViewItemClick {
        void recyclerViewItemClick(int position, View view);
    }

    public void setRecyclerViewItemClick(RecyclerViewItemClick recyclerViewItemClick) {
        this.recyclerViewItemClick = recyclerViewItemClick;
    }
}
