package com.wbx.merchant.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.wbx.merchant.R;
import com.wbx.merchant.base.BaseAdapter;
import com.wbx.merchant.base.BaseViewHolder;
import com.wbx.merchant.bean.ThirdOrderInfo;

import java.util.List;

/**
 * Created by Administrator on 2017/10/24 0024.
 */

public class ThirdOrderAdapter extends BaseAdapter<ThirdOrderInfo, Context> {

    public ThirdOrderAdapter(List<ThirdOrderInfo> dataList, Context context) {
        super(dataList, context);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_third_order;
    }

    @Override
    public void convert(BaseViewHolder holder, ThirdOrderInfo orderInfo, int position) {
        holder.setText(R.id.tv_order_id, orderInfo.getOrder_id());
        holder.setText(R.id.tv_send_platform, orderInfo.getDispatching_type());
        holder.setText(R.id.tv_send_fee, String.format("(配送费%.2f元)", orderInfo.getMoney() / 100.00));
        holder.setText(R.id.carrier_driver_name, orderInfo.getDm_name() + " " + orderInfo.getDm_mobile());
        holder.setText(R.id.state_tv, orderInfo.getOrder_status());
        TextView tvDescription = holder.getView(R.id.tv_description);
        if (TextUtils.isEmpty(orderInfo.getCancel_reason())) {
            tvDescription.setVisibility(View.GONE);
        } else {
            tvDescription.setVisibility(View.VISIBLE);
            tvDescription.setText("(" + orderInfo.getCancel_reason() + ")");
        }
    }
}
