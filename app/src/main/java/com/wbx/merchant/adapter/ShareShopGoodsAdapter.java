package com.wbx.merchant.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wbx.merchant.R;
import com.wbx.merchant.bean.ShopFxinfo;
import com.wbx.merchant.utils.GlideUtils;

import java.util.List;

public class ShareShopGoodsAdapter extends BaseQuickAdapter<ShopFxinfo.DataBean.GoodsBean, BaseViewHolder> {
    public ShareShopGoodsAdapter() {
        super(R.layout.item_shop_goods);
    }

    @Override
    protected void convert(BaseViewHolder helper, ShopFxinfo.DataBean.GoodsBean item) {
        GlideUtils.showMediumPic(mContext, (ImageView) helper.getView(R.id.iv_goods),item.getPhoto());
        helper.setText(R.id.tv_money,String.format("¥%s",item.getPrice()/100.00));
        helper.setText(R.id.tv_goods_name,item.getTitle());
    }

}
