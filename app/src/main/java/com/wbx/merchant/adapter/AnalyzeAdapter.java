package com.wbx.merchant.adapter;

import android.content.Context;

import com.wbx.merchant.R;
import com.wbx.merchant.base.BaseAdapter;
import com.wbx.merchant.base.BaseViewHolder;
import com.wbx.merchant.bean.OrderInfo;
import com.wbx.merchant.utils.FormatUtil;

import java.util.List;

/**
 * Created by wushenghui on 2017/9/29.
 */

public class AnalyzeAdapter extends BaseAdapter<OrderInfo, Context> {
    private boolean isRefund;

    public AnalyzeAdapter(List<OrderInfo> dataList, Context context) {
        super(dataList, context);
    }

    public void setIsRefund(boolean isRefund) {
        this.isRefund = isRefund;
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_analyze;
    }

    @Override
    public void convert(BaseViewHolder holder, OrderInfo orderInfo, int position) {
        holder.setText(R.id.order_number_tv, String.format("订单编号：%d", orderInfo.getOrder_id())).setText(R.id.order_create_time_tv, FormatUtil.stampToDate(orderInfo.getCreate_time() + "")).setText(R.id.need_pay_tv, (isRefund ? "-" : "+") + String.format("%.2f", orderInfo.getNeed_pay() / 100.00));
    }
}
