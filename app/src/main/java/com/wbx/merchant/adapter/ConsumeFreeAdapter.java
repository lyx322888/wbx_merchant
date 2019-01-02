package com.wbx.merchant.adapter;

import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wbx.merchant.R;
import com.wbx.merchant.bean.ConsumeFreeGoodsBean;
import com.wbx.merchant.utils.GlideUtils;

import java.util.List;

/**
 * @author Zero
 * @date 2018/11/22
 */
public class ConsumeFreeAdapter extends BaseQuickAdapter<ConsumeFreeGoodsBean, BaseViewHolder> {

    public ConsumeFreeAdapter(@Nullable List<ConsumeFreeGoodsBean> data) {
        super(R.layout.item_consume_free, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ConsumeFreeGoodsBean item) {
        TextView tvOriginalPrice = helper.getView(R.id.tv_original_price);
        tvOriginalPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        GlideUtils.showMediumPic(mContext,(ImageView) helper.getView(R.id.iv_goods),item.getPhoto());
        helper.setText(R.id.tv_name, item.getTitle())
                .setText(R.id.tv_free_start_price, String.format("¥%.2f", item.getConsume_free_price() / 100.00))
                .setText(R.id.tv_original_price, String.format("¥%.2f", item.getPrice() / 100.00))
                .setText(R.id.tv_need_num, item.getConsume_free_amount() + "")
                .setText(R.id.tv_continue_time, String.format("活动持续时间：%s", item.getConsume_free_duration() == 999999999 ? "不限时" : (item.getConsume_free_duration() + "小时")))
                .setText(R.id.tv_stock, String.format("活动库存%d", item.getConsume_free_num()));
        helper.addOnClickListener(R.id.ll_edit);
    }
}
