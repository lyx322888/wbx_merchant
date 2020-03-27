package com.wbx.merchant.adapter;

import androidx.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wbx.merchant.R;
import com.wbx.merchant.bean.CanFreeGoodsBean;
import com.wbx.merchant.utils.GlideUtils;

import java.util.List;

/**
 * @author Zero
 * @date 2018/11/22
 */
public class AddConsumeFreeGoodsListAdapter extends BaseQuickAdapter<CanFreeGoodsBean, BaseViewHolder> {

    public AddConsumeFreeGoodsListAdapter(@Nullable List<CanFreeGoodsBean> data) {
        super(R.layout.item_add_consume_free_goods_list, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CanFreeGoodsBean item) {
        helper.addOnClickListener(R.id.root_view);
        GlideUtils.showMediumPic(mContext, (ImageView) helper.getView(R.id.iv_goods), item.getPhoto());
        helper.setText(R.id.tv_name, item.getTitle())
                .setText(R.id.tv_price, String.format("¥%.2f", item.getPrice() / 100.00))
                .setText(R.id.tv_sold_num, String.format("销量%d", item.getSold_num()))
                .setText(R.id.tv_stock, String.format("库存%d", item.getNum()));
    }
}
