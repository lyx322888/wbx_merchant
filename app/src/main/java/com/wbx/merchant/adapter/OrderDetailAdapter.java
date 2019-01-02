package com.wbx.merchant.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.wbx.merchant.R;
import com.wbx.merchant.base.BaseAdapter;
import com.wbx.merchant.base.BaseViewHolder;
import com.wbx.merchant.bean.GoodsInfo;
import com.wbx.merchant.utils.GlideUtils;

import java.util.List;

/**
 * Created by wushenghui on 2017/6/28.
 */

public class OrderDetailAdapter extends BaseAdapter<GoodsInfo, Context> {

    public OrderDetailAdapter(List<GoodsInfo> dataList, Context context) {
        super(dataList, context);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_order_detail;
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
        holder.setText(R.id.goods_name_tv, (TextUtils.isEmpty(goodsInfo.getProduct_name()) ? goodsInfo.getTitle() : goodsInfo.getProduct_name()) + sbNature.toString())
                .setText(R.id.goods_price_tv, String.format("¥%.2f", goodsInfo.getPrice() / 100.00))
                .setText(R.id.buy_num_tv, String.format("共%d件", goodsInfo.getNum()));
        ImageView picIm = holder.getView(R.id.goods_photo);
        GlideUtils.showRoundSmallPic(mContext, picIm, goodsInfo.getPhoto());
    }
}
