package com.wbx.merchant.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wbx.merchant.R;
import com.wbx.merchant.bean.ListIndustrySecondBean;

import java.util.List;

public class InduStrySecondAdapter extends BaseQuickAdapter<ListIndustrySecondBean.DataBean, BaseViewHolder> {
    public InduStrySecondAdapter() {
        super(R.layout.item_bank_list);
    }

    @Override
    protected void convert(BaseViewHolder helper, ListIndustrySecondBean.DataBean item) {
            helper.setText(R.id.tv_name,item.getIndustryName());
    }
}
