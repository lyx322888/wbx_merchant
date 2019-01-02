package com.wbx.merchant.adapter;

import android.content.Context;
import android.text.TextUtils;

import com.wbx.merchant.R;
import com.wbx.merchant.base.BaseAdapter;
import com.wbx.merchant.base.BaseViewHolder;
import com.wbx.merchant.bean.GoodsInfo;

import java.util.List;

/**
 * Created by wushenghui on 2017/6/27.
 */

public class OrderGoodsAdapter extends BaseAdapter<GoodsInfo, Context> {
    private List<GoodsInfo> mList;
    private boolean isExpand = false;

    public OrderGoodsAdapter(List<GoodsInfo> dataList, Context context) {
        super(dataList, context);
        this.mList = dataList;
    }

    public void showAll() {
        isExpand = true;
        notifyDataSetChanged();
    }

    public void hideSome() {
        isExpand = false;
        notifyDataSetChanged();
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_order_goods;
    }

    @Override
    public void convert(BaseViewHolder holder, GoodsInfo goodsInfo, int position) {
        StringBuilder sbNature = new StringBuilder();
        if (!TextUtils.isEmpty(goodsInfo.getAttr_name()) || !TextUtils.isEmpty(goodsInfo.getNature_name())) {
            sbNature.append("(");
            if (!TextUtils.isEmpty(goodsInfo.getAttr_name())) {
                sbNature.append(goodsInfo.getAttr_name());
            }
            if (!TextUtils.isEmpty(goodsInfo.getAttr_name()) && !TextUtils.isEmpty(goodsInfo.getNature_name())) {
                sbNature.append("+");
            }
            if (!TextUtils.isEmpty(goodsInfo.getNature_name())) {
                sbNature.append(goodsInfo.getNature_name());
            }
            sbNature.append(")");
        }
        holder.setText(R.id.order_goods_name_tv, goodsInfo.getProduct_name() + sbNature.toString()).setText(R.id.order_goods_num_tv, "X" + goodsInfo.getNum()).setText(R.id.order_goods_price_tv, String.format("%.2f", goodsInfo.getPrice() / 100.00));
    }

    @Override
    public int getItemCount() {
        return isExpand ? mList.size() : (mList.size() > 3 ? 3 : mList.size());
    }
}
