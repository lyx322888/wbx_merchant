package com.wbx.merchant.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.wbx.merchant.R;
import com.wbx.merchant.base.BaseAdapter;
import com.wbx.merchant.base.BaseViewHolder;
import com.wbx.merchant.bean.MerchantSubsidyBean;
import com.wbx.merchant.utils.GlideUtils;

import java.util.List;

/**
 * Created by wushenghui on 2017/6/26.
 */

public class MerchantSubsidyAdapter extends BaseAdapter<MerchantSubsidyBean.ListBean, Context> {
    public MerchantSubsidyAdapter(List<MerchantSubsidyBean.ListBean> dataList, Context context) {
        super(dataList, context);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_merchant_subsidy;
    }

    @Override
    public void convert(BaseViewHolder holder, final MerchantSubsidyBean.ListBean data, int position) {
        holder.setText(R.id.tv_name, data.getNickname());
        holder.setText(R.id.tv_date, data.getOrder_time());
        holder.setText(R.id.tv_type, data.getSubsidy_type());
        holder.setText(R.id.tv_money, "+" + String.format("%.2f", (float) data.getMoney() / 100));
        if (TextUtils.isEmpty(data.getFace())) {
            ((ImageView) holder.getView(R.id.iv_user)).setImageResource(R.drawable.logo);
        } else {
            GlideUtils.showSmallPic(mContext, (ImageView) holder.getView(R.id.iv_user), data.getFace());
        }
    }
}
