package com.wbx.merchant.adapter;

import android.content.Context;

import com.wbx.merchant.R;
import com.wbx.merchant.base.BaseAdapter;
import com.wbx.merchant.base.BaseViewHolder;
import com.wbx.merchant.bean.CouPonInfo;
import com.wbx.merchant.utils.FormatUtil;

import java.util.List;

/**
 * Created by wushenghui on 2017/10/13.
 */

public class CouponAdapter extends BaseAdapter<CouPonInfo, Context> {

    public CouponAdapter(List<CouPonInfo> dataList, Context context) {
        super(dataList, context);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_coupon;
    }

    @Override
    public void convert(BaseViewHolder holder, CouPonInfo couPonInfo, int position) {
        holder.setText(R.id.coupon_price_tv, String.format("¥%.2f", couPonInfo.getMoney() / 100.00)).setText(R.id.condition_money_tv, String.format("¥%d", couPonInfo.getCondition_money() / 100))
                .setText(R.id.coupon_time_tv, FormatUtil.stampToDate1(couPonInfo.getStart_time() + "") + "至" + FormatUtil.stampToDate1(couPonInfo.getEnd_time() + "")).setText(R.id.num_tv, "" + couPonInfo.getNum());
    }
}
