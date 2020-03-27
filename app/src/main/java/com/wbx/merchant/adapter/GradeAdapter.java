package com.wbx.merchant.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wbx.merchant.R;
import com.wbx.merchant.bean.GradeInfoBean;

import java.util.List;

public class GradeAdapter extends RecyclerView.Adapter<GradeAdapter.VH> {
    Context context;
    List<GradeInfoBean.DataBean> list;
    public static int getPosition;

    public GradeAdapter(Context context, List<GradeInfoBean.DataBean> list) {
        this.context = context;
        this.list = list;
    }

    public static int getGetPosition() {
        return getPosition;
    }

    public static void setGetPosition(int getPosition) {
        GradeAdapter.getPosition = getPosition;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(context).inflate(R.layout.item_grade, parent, false);
        final VH vh = new VH(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerViewItemClick.recyclerViewItemClick(vh.getAdapterPosition(),view,vh);
            }
        });
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        holder.nameTv.setText(list.get(position).getGrade_name());
        if (getPosition == position) {
            holder.itemView.setBackgroundResource(R.color.white);
            holder.nameTv.setTextColor(context.getResources().getColor(R.color.fenlei_text));
        } else {
            holder.itemView.setBackgroundResource(R.color.back_ground_color);
            holder.nameTv.setTextColor(context.getResources().getColor(R.color.fenlei_text2));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class VH extends RecyclerView.ViewHolder {
        TextView nameTv;

        public VH(View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.grade_name_tv);
        }
    }


    RecyclerViewItemClick recyclerViewItemClick;

    public interface RecyclerViewItemClick {
        void recyclerViewItemClick(int position, View view, RecyclerView.ViewHolder viewHolder);
    }

    public void setRecyclerViewItemClick(RecyclerViewItemClick recyclerViewItemClick) {
        this.recyclerViewItemClick = recyclerViewItemClick;
    }
}
