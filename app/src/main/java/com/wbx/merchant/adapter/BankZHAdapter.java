package com.wbx.merchant.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wbx.merchant.R;
import com.wbx.merchant.bean.BankListBean;


public class BankZHAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public BankZHAdapter() {
        super(R.layout.item_bank_list);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_name,item);
    }
}
