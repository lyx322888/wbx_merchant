package com.wbx.merchant.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.wbx.merchant.R;
import com.wbx.merchant.base.BaseAdapter;
import com.wbx.merchant.base.BaseViewHolder;
import com.wbx.merchant.bean.OrderBean;
import com.wbx.merchant.utils.GlideUtils;

import java.util.List;

public class ShopCartAdapter extends BaseAdapter<OrderBean.DataBean.OrderGoodsBean, Context> {

    public ShopCartAdapter(List<OrderBean.DataBean.OrderGoodsBean> dataList, Context context) {
        super(dataList, context);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_order_goods_info;
    }

    @Override
    public void convert(BaseViewHolder holder, final OrderBean.DataBean.OrderGoodsBean orderBean, final int position) {
        holder.setText(R.id.tv_name, orderBean.getTitle());
        holder.setText(R.id.tv_price, "¥" + (float) orderBean.getPrice() / 100);
        holder.setText(R.id.tv_num, orderBean.getOrder_num() + "件");
        ImageView tvPhoto = holder.getView(R.id.iv_photo);
        GlideUtils.showRoundSmallPic(mContext, tvPhoto, orderBean.getPhoto());
    }
}
