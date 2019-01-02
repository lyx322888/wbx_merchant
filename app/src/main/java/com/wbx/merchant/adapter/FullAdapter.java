package com.wbx.merchant.adapter;

import android.content.Context;

import com.wbx.merchant.R;
import com.wbx.merchant.base.BaseAdapter;
import com.wbx.merchant.base.BaseViewHolder;
import com.wbx.merchant.bean.FullDataInfo;

import java.util.List;

/**
 * Created by Administrator on 2017/10/27 0027.
 */

public class FullAdapter extends BaseAdapter<FullDataInfo,Context>{
    public FullAdapter(List<FullDataInfo> dataList, Context context) {
        super(dataList, context);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_full;
    }

    @Override
    public void convert(BaseViewHolder holder, FullDataInfo fullDataInfo, int position) {

        holder.setText(R.id.full_money,String.format("%.2f",fullDataInfo.getFull_money()/100.00));
             holder.setText(R.id.reduce_money,String.format("%.2f",fullDataInfo.getReduce_money()/100.00));
    }
}
