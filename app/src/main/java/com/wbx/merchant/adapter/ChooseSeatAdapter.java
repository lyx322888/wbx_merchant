package com.wbx.merchant.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.wbx.merchant.R;
import com.wbx.merchant.base.BaseAdapter;
import com.wbx.merchant.base.BaseViewHolder;
import com.wbx.merchant.bean.SeatInfo;

import java.util.List;

/**
 * Created by wushenghui on 2017/12/27.
 */

public class ChooseSeatAdapter extends BaseAdapter<SeatInfo,Context> {
    public ChooseSeatAdapter(List<SeatInfo> dataList, Context context) {
        super(dataList, context);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_recycler_product;
    }

    @Override
    public void convert(BaseViewHolder holder, SeatInfo seatInfo, int position) {
        ImageView imageView = holder.getView(R.id.item_recycler_img);
        TextView title = holder.getView(R.id.item_recycler_tv);
        title.setText(seatInfo.getTable_number());
        boolean select = seatInfo.isSelect();
        if (select){
            imageView.setImageResource(R.drawable.ic_ok);
        }else {
            imageView.setImageResource(R.drawable.ic_round);
        }
    }
}
