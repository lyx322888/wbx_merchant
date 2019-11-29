package com.wbx.merchant.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.wbx.merchant.R;
import com.wbx.merchant.base.BaseAdapter;
import com.wbx.merchant.base.BaseViewHolder;
import com.wbx.merchant.bean.GoodsInfo;
import com.wbx.merchant.bean.SpecInfo;
import com.wbx.merchant.utils.GlideUtils;

import java.util.List;

/**
 * Created by wushenghui on 2017/9/28.
 */

public class InventoryAdapter extends BaseAdapter<GoodsInfo, Context> {

    public InventoryAdapter(List<GoodsInfo> dataList, Context context) {
        super(dataList, context);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_inventory;
    }

    @Override
    public void convert(BaseViewHolder holder, GoodsInfo goodsInfo, int position) {
        ImageView view = holder.getView(R.id.goods_photo_im);
        View view1 = holder.getView(R.id.is_spec_tv);
        GlideUtils.showMediumPic(mContext, view, goodsInfo.getPhoto());
        holder.setText(R.id.name_tv, goodsInfo.getProduct_name());
        if (goodsInfo.getIs_attr() == 1) {
            holder.setText(R.id.other_tv, String.format("已售%d    库存 : %d"  , goodsInfo.getSold_num(),goodsInfo.getNum()));
            view1.setVisibility(View.VISIBLE);
        } else {
            holder.setText(R.id.other_tv, String.format("已售 : %d    耗损 : %d    库存 : %d", goodsInfo.getSold_num(), goodsInfo.getLoss(), goodsInfo.getNum()));
            view1.setVisibility(View.GONE);
        }


        if (goodsInfo.getIs_attr() == 1) {
            boolean isEnough = false;
            for (SpecInfo specInfo : goodsInfo.getGoods_attr()) {
                if (specInfo.getNum() >= 10) {
                    isEnough = true;
                    break;
                }
            }
            holder.getView(R.id.iv_inventory_no_enough).setVisibility(isEnough ? View.GONE : View.VISIBLE);
        } else {
            holder.getView(R.id.iv_inventory_no_enough).setVisibility(goodsInfo.getNum() < 10 ? View.VISIBLE : View.GONE);
        }
    }
}
