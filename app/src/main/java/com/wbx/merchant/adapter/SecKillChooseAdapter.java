package com.wbx.merchant.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.wbx.merchant.R;
import com.wbx.merchant.base.BaseAdapter;
import com.wbx.merchant.base.BaseViewHolder;
import com.wbx.merchant.bean.GoodsInfo;
import com.wbx.merchant.utils.GlideUtils;

import java.util.List;

/**
 * Created by wushenghui on 2017/12/12.
 * 秒杀选择商品
 */

public class SecKillChooseAdapter extends BaseAdapter<GoodsInfo, Context> {

    public SecKillChooseAdapter(List<GoodsInfo> dataList, Context context) {
        super(dataList, context);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_seckill_choose;
    }

    @Override
    public void convert(BaseViewHolder holder, GoodsInfo goodsInfo, int position) {
        ImageView selectIm = holder.getView(R.id.edit_check_im);
        if (goodsInfo.getIs_attr() == 1) {
            holder.getView(R.id.is_attr_im).setVisibility(View.VISIBLE);
        } else {
            holder.getView(R.id.is_attr_im).setVisibility(View.GONE);
        }
        if (goodsInfo.isSelect()) {
            selectIm.setImageResource(R.drawable.ic_ok);
        } else {
            selectIm.setImageResource(R.drawable.ic_round);
        }
        ImageView picIm = holder.getView(R.id.goods_pic_im);
        GlideUtils.showRoundMediumPic(mContext, picIm, goodsInfo.getPhoto());
        holder.setText(R.id.goods_name_tv, goodsInfo.getProduct_name())
                .setText(R.id.selling_price_tv, goodsInfo.getMall_price() == 0 ? String.format("¥%.2f", goodsInfo.getPrice() / 100.00) : String.format("¥%.2f", goodsInfo.getMall_price() / 100.00))
                .setText(R.id.goods_info_tv, String.format("总销量:%d        库存:%d", goodsInfo.getSold_num(), goodsInfo.getNum()));
    }
}
