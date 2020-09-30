package com.wbx.merchant.adapter;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wbx.merchant.R;
import com.wbx.merchant.bean.JDDetailBean;


public class JdDetailAdapter extends BaseQuickAdapter<JDDetailBean, BaseViewHolder> {
    public JdDetailAdapter() {
        super(R.layout.item_jd_detail);
    }

    @Override
    protected void convert(BaseViewHolder helper, JDDetailBean item) {
        helper.setText(R.id.tv_name,"订单号："+item.getOrderNum())
                .setText(R.id.tv_time,item.getCompleteTime())
                .setText(R.id.tv_price,"¥ "+item.getOrderAmount());
    }
}
