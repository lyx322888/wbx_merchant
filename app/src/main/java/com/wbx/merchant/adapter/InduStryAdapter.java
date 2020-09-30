package com.wbx.merchant.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wbx.merchant.R;
import com.wbx.merchant.bean.ListIndustryBean;
import com.wbx.merchant.bean.ListIndustrySecondBean;

public class InduStryAdapter extends BaseQuickAdapter<ListIndustryBean.DataBean, BaseViewHolder> {
    public InduStryAdapter() {
        super(R.layout.item_bank_list);
    }

    @Override
    protected void convert(BaseViewHolder helper, ListIndustryBean.DataBean item) {
            helper.setText(R.id.tv_name,item.getIndustryName());
    }
}
