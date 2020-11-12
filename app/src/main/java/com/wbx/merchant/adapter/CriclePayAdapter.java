package com.wbx.merchant.adapter;


import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wbx.merchant.R;
import com.wbx.merchant.bean.CricePayBaen;

public class CriclePayAdapter extends BaseQuickAdapter<CricePayBaen, BaseViewHolder> {
    public CriclePayAdapter() {
        super(R.layout.item_pay_tv_select);
    }

    @Override
    protected void convert(BaseViewHolder helper, CricePayBaen item) {
        TextView textView = helper.getView(R.id.tv_price);
        textView.setText("¥"+item.price);
        if (item.isSelect){
            textView.setBackground(ContextCompat.getDrawable(mContext,R.drawable.pay_select));
            textView.setTextColor(ContextCompat.getColor(mContext,R.color.red));
        }else {
            textView.setBackground(ContextCompat.getDrawable(mContext,R.drawable.pay_unselect));
            textView.setTextColor(ContextCompat.getColor(mContext,R.color.gray));
        }
    }


    public void Select(int position){
        for (int i = 0; i < getData().size(); i++) {
            getData().get(i).isSelect = false;
        }
        getData().get(position).isSelect = true;
        notifyDataSetChanged();
    }
}
