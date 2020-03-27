package com.wbx.merchant.adapter;

import androidx.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wbx.merchant.R;
import com.wbx.merchant.bean.IntegralFreeGoodsBean;
import com.wbx.merchant.utils.GlideUtils;

import java.util.List;

/**
 * @author Zero
 * @date 2018/11/22
 */
public class IntegralFreeAdapter extends BaseQuickAdapter<IntegralFreeGoodsBean, BaseViewHolder> {

    public IntegralFreeAdapter(@Nullable List<IntegralFreeGoodsBean> data) {
        super(R.layout.item_integral_free, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, IntegralFreeGoodsBean item) {
        GlideUtils.showMediumPic(mContext, (ImageView) helper.getView(R.id.iv_goods), item.getPhoto());
        helper.setText(R.id.tv_name, item.getTitle())
                .setText(R.id.tv_price, String.format("¥%.2f", item.getPrice() / 100.00))
                .setText(R.id.tv_need_num, String.valueOf(item.getAccumulate_free_need_num()))
                .setText(R.id.tv_free_num, "免" + item.getAccumulate_free_get_num());
        helper.addOnClickListener(R.id.ll_edit)
                .addOnClickListener(R.id.ll_delete);
    }
}
