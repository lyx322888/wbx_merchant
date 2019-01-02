package com.wbx.merchant.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.wbx.merchant.R;
import com.wbx.merchant.bean.BusinessTimeBean;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zero on 2017/1/17.
 */

public class BusinessTimeAdapter extends RecyclerView.Adapter<BusinessTimeAdapter.MyViewHolder> {
    private Context mContext;
    private List<BusinessTimeBean> lstData;

    public BusinessTimeAdapter(Context context) {
        mContext = context;
    }

    public void update(List<BusinessTimeBean> data) {
        this.lstData = data;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_business_time, parent, false);
        MyViewHolder holder = new MyViewHolder(layout);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final BusinessTimeBean data = lstData.get(position);
        holder.tvHint.setText("时间段" + (position + 1));
        holder.tvStartTime.setText(TextUtils.isEmpty(data.getStart_time()) ? "未设置" : data.getStart_time());
        holder.tvEndTime.setText(TextUtils.isEmpty(data.getEnd_time()) ? "未设置" : data.getEnd_time());
        holder.tvStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTime(holder.tvStartTime, true, data);
            }
        });
        holder.tvEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTime(holder.tvEndTime, false, data);
            }
        });
        holder.ivDelete.setVisibility(View.VISIBLE);
        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lstData.size() > position) {
                    lstData.remove(position);
                    notifyDataSetChanged();
                }
            }
        });
    }

    private void getTime(final TextView textView, final boolean isStartTime, final BusinessTimeBean data) {
        TimePickerView pvTime = new TimePickerView.Builder(mContext, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                String time = new SimpleDateFormat("HH:mm").format(date);
                textView.setText(time);
                if (isStartTime) {
                    data.setStart_time(time);
                } else {
                    data.setEnd_time(time);
                }
            }
        }).setType(new boolean[]{false, false, false, true, true, false})//年月日时分秒 的显示与否，不设置则默认全部显示
                .setLabel("", "", "", "", "", "")
                .isCenterLabel(false)
                .setDividerColor(Color.GRAY)
                .setContentSize(17)
                .setDate(Calendar.getInstance())
                .setBackgroundId(0x00FFFFFF) //设置外部遮罩颜色
                .setDecorView(null)
                .build();
        pvTime.show();
    }

    @Override
    public int getItemCount() {
        return lstData == null ? 0 : lstData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_hint)
        TextView tvHint;
        @Bind(R.id.tv_start_time)
        TextView tvStartTime;
        @Bind(R.id.tv_end_time)
        TextView tvEndTime;
        @Bind(R.id.iv_delete)
        ImageView ivDelete;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
