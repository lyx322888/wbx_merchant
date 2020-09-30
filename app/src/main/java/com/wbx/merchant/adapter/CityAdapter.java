package com.wbx.merchant.adapter;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wbx.merchant.R;
import com.wbx.merchant.bean.CityBean;
import com.wbx.merchant.bean.ProvinceBean;


public class CityAdapter extends BaseQuickAdapter<CityBean.DataBean, BaseViewHolder> {
    public CityAdapter( ) {
        super(R.layout.item_bank_list);
    }

    @Override
    protected void convert(BaseViewHolder helper, CityBean.DataBean item) {
        helper.setText(R.id.tv_name,item.getName());
    }
}
