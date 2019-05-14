package com.wbx.merchant.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wbx.merchant.R;
import com.wbx.merchant.base.BaseAdapter;
import com.wbx.merchant.base.BaseViewHolder;
import com.wbx.merchant.bean.CateBean;
import com.wbx.merchant.bean.CateInfo;

import java.util.List;

/**
 * Created by wushenghui on 2017/6/23.
 */

public class CateAdapter extends RecyclerView.Adapter<CateAdapter.VH> {
    Context context;
    List<CateBean.DataBean.CatesBean> list;
    public static int getPosition;

    public CateAdapter(Context context, List<CateBean.DataBean.CatesBean> list) {
        this.context = context;
        this.list = list;
    }

    public static int getGetPosition() {
        return getPosition;
    }

    public static void setGetPosition(int getPosition) {
        CateAdapter.getPosition = getPosition;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(context).inflate(R.layout.item_cate, parent, false);
        final VH vh = new VH(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerViewItemClieck.recyclerViewItemClieck(vh.getAdapterPosition(), view, vh);
            }
        });
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        if (getPosition == position) {
            holder.nameTV.setTextColor(context.getResources().getColor(R.color.fenlei_text));
        } else {
            holder.nameTV.setTextColor(context.getResources().getColor(R.color.fenlei_text2));
        }
        holder.nameTV.setText(list.get(position).getCate_name());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
//    public CateAdapter(List<CateInfo> dataList, Context context) {
//        super(dataList, context);
//    }

//    @Override
//    public int getLayoutId(int viewType) {
//        return R.layout.item_cate;
//    }

//    @Override
//    public void convert(BaseViewHolder holder, CateInfo cateInfo, int position) {
////        if (cateInfo.isSelect()) {
////            holder.setImageResource(R.id.sel_im, R.drawable.ic_ok);
////        } else {
////            holder.setImageResource(R.id.sel_im, R.drawable.ic_round);
////        }
//        holder.setText(R.id.cate_name_tv, cateInfo.getCate_name());
//    }

    class VH extends RecyclerView.ViewHolder {
        TextView nameTV;

        public VH(View itemView) {
            super(itemView);
            nameTV = itemView.findViewById(R.id.cate_name_tv);
        }
    }

    RecyclerViewItemClieck recyclerViewItemClieck;

    public interface RecyclerViewItemClieck {
        void recyclerViewItemClieck(int position, View view, RecyclerView.ViewHolder viewHolder);
    }

    public void setRecyclerViewItemClieck(RecyclerViewItemClieck recyclerViewItemClieck) {
        this.recyclerViewItemClieck = recyclerViewItemClieck;
    }
}
