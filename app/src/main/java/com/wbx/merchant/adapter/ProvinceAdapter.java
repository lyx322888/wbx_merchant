package com.wbx.merchant.adapter;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wbx.merchant.R;
import com.wbx.merchant.bean.ProvinceBean;


public class ProvinceAdapter extends BaseQuickAdapter<ProvinceBean.DataBean, BaseViewHolder> {
    public ProvinceAdapter( ) {
        super(R.layout.item_bank_list);
    }

    @Override
    protected void convert(BaseViewHolder helper, ProvinceBean.DataBean item) {
        helper.setText(R.id.tv_name,item.getName());
    }
}
