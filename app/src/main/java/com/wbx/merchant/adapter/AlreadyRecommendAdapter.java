package com.wbx.merchant.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wbx.merchant.R;
import com.wbx.merchant.bean.GoodsInfo;
import com.wbx.merchant.utils.GlideUtils;

import java.util.List;

public class AlreadyRecommendAdapter extends BaseQuickAdapter<GoodsInfo, BaseViewHolder> {
    public AlreadyRecommendAdapter(int layoutResId, @Nullable List<GoodsInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodsInfo goodsInfo) {
        ImageView picIm = helper.getView(R.id.goods_pic_im);
        GlideUtils.showRoundMediumPic(mContext, picIm, goodsInfo.getPhoto());
        helper.setText(R.id.goods_name_tv, goodsInfo.getProduct_name())
                .setText(R.id.selling_price_tv, goodsInfo.getMall_price() == 0 ? String.format("¥%.2f", goodsInfo.getPrice() / 100.00) : String.format("¥%.2f", goodsInfo.getMall_price() / 100.00))
                .setText(R.id.goods_info_tv, String.format("总销量:%d        库存:%d", goodsInfo.getSold_num(), goodsInfo.getNum()));
        helper.addOnClickListener(R.id.iv_delete);
    }
}
