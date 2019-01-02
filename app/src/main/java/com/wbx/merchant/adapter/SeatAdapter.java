package com.wbx.merchant.adapter;

import android.content.Context;
import android.view.View;

import com.wbx.merchant.R;
import com.wbx.merchant.base.BaseAdapter;
import com.wbx.merchant.base.BaseViewHolder;
import com.wbx.merchant.bean.SeatInfo;

import java.util.List;

/**
 * Created by wushenghui on 2017/12/6.
 */

public class SeatAdapter extends BaseAdapter<SeatInfo, Context> {

    private boolean isEdit = false;

    public SeatAdapter(List<SeatInfo> dataList, Context context) {
        super(dataList, context);
    }

    public void setEdit(boolean isEdit) {
        this.isEdit = isEdit;
        notifyDataSetChanged();
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_seat;
    }

    @Override
    public void convert(BaseViewHolder holder, SeatInfo seatInfo, int position) {
        holder.setText(R.id.seat_name_tv, seatInfo.getTable_number()).setText(R.id.seat_num_tv, seatInfo.getDinner_people_min() + "-" + seatInfo.getDinner_people_max() + "人");
        holder.getView(R.id.iv_close).setVisibility(isEdit ? View.VISIBLE : View.GONE);
    }
}
