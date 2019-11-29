package com.wbx.merchant.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.View;
import android.widget.TextView;

import com.wbx.merchant.R;
import com.wbx.merchant.base.BaseAdapter;
import com.wbx.merchant.base.BaseViewHolder;
import com.wbx.merchant.bean.ShopVersionBean;

import java.util.List;

/**
 * Created by wushenghui on 2017/6/23.
 */

public class ShopVersionAdapter extends BaseAdapter<ShopVersionBean, Context> {

    public ShopVersionAdapter(List<ShopVersionBean> dataList, Context context) {
        super(dataList, context);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_shop_version;
    }

    @Override
    public void convert(BaseViewHolder holder, ShopVersionBean shopVersionBean, int position) {
        if (shopVersionBean.isSelect()) {
            holder.setImageResource(R.id.sel_im, R.drawable.ic_ok);
        } else {
            holder.setImageResource(R.id.sel_im, R.drawable.ic_round);
        }
        TextView tvVersionName = holder.getView(R.id.tv_version_name);
        tvVersionName.setText(shopVersionBean.getName());
        if (shopVersionBean.getShop_grade() == 3) {
            //终身版
            tvVersionName.setTextColor(mContext.getResources().getColor(R.color.red));
            holder.getView(R.id.iv_limit_bug).setVisibility(View.VISIBLE);
            ((View) holder.getView(R.id.tv_remain_num).getParent()).setVisibility(View.VISIBLE);
            holder.setText(R.id.tv_need_money, String.format("¥%.2f", shopVersionBean.getMoney() / 100.00));
            TextView tvOriginalPrice = holder.getView(R.id.tv_original_price);
            tvOriginalPrice.setVisibility(View.VISIBLE);
            tvOriginalPrice.setText(String.format("原价¥%.2f", shopVersionBean.getOriginal_price() / 100.00));
            tvOriginalPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            holder.setText(R.id.tv_remain_num, String.valueOf(shopVersionBean.getSurplus_num()));
        } else {
            tvVersionName.setTextColor(mContext.getResources().getColor(R.color.app_color));
            holder.setText(R.id.tv_need_money, String.format("¥%.2f", shopVersionBean.getMoney() / 100.00));
            holder.getView(R.id.iv_limit_bug).setVisibility(View.GONE);
            ((View) holder.getView(R.id.tv_remain_num).getParent()).setVisibility(View.GONE);
            holder.getView(R.id.tv_original_price).setVisibility(View.GONE);
        }
    }
}
