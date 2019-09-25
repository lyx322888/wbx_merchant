package com.wbx.merchant.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wbx.merchant.R;
import com.wbx.merchant.base.BaseAdapter;
import com.wbx.merchant.base.BaseViewHolder;
import com.wbx.merchant.bean.GoodsInfo;
import com.wbx.merchant.utils.FormatUtil;
import com.wbx.merchant.utils.GlideUtils;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by wushenghui on 2017/12/12.
 */

public class SeckillAdapter extends BaseAdapter<GoodsInfo, Context> {

    public SeckillAdapter(List<GoodsInfo> dataList, Context context) {
        super(dataList, context);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_seckill;
    }

    @Override
    public void convert(BaseViewHolder holder, GoodsInfo goodsInfo, int position) {
        if (goodsInfo.getIs_attr() == 1) {
            holder.getView(R.id.is_attr_im).setVisibility(View.VISIBLE);
        } else {
            holder.getView(R.id.is_attr_im).setVisibility(View.GONE);
        }

        int startTime = Integer.parseInt(goodsInfo.getSeckill_start_time());
        int endTime = Integer.parseInt(goodsInfo.getSeckill_end_time());
        long nowTime = Calendar.getInstance().getTimeInMillis() / 1000L;
        if (nowTime > endTime) {
            //当前时间大于结束时间  活动已结束
            holder.setText(R.id.state_tv, "已结束");
        } else if (nowTime < startTime) {
            //当前时间小于开始时间
            holder.setText(R.id.state_tv, "未开始");
        } else {
            holder.setText(R.id.state_tv, "进行中");
        }
        holder.setText(R.id.product_name_tv, goodsInfo.getProduct_name()).setText(R.id.seckill_price_tv, Html.fromHtml("秒杀价:<font color = #ff0000 size=16> " + String.format("¥%.2f", goodsInfo.getSeckill_price()) + "</font>"));
        holder.setText(R.id.price_tv, String.format("原价:¥%.2f", goodsInfo.getPrice() / 100.00)).setText(R.id.limitations_tv, String.format("单人限购:%s", goodsInfo.getLimitations_num())).setText(R.id.seckill_num_tv, String.format("活动库存:%s", goodsInfo.getSeckill_num()));
        TextView priceTv = holder.getView(R.id.price_tv);
        priceTv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        holder.setText(R.id.time_tv, FormatUtil.myStampToDate1(goodsInfo.getSeckill_start_time(), "yyyy-MM-dd HH:mm") + "~" + FormatUtil.myStampToDate1(goodsInfo.getSeckill_end_time(), "yyyy-MM-dd HH:mm"));
        ImageView productPicIm = holder.getView(R.id.product_pic_im);
        GlideUtils.showRoundMediumPic(mContext, productPicIm, goodsInfo.getPhoto());
    }
}
