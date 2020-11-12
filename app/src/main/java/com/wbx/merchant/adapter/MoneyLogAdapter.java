package com.wbx.merchant.adapter;

import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wbx.merchant.R;
import com.wbx.merchant.bean.MoneyLogBean;
import com.wbx.merchant.utils.FormatUtil;


public class MoneyLogAdapter extends BaseQuickAdapter<MoneyLogBean.DataBean, BaseViewHolder> {
    public MoneyLogAdapter() {
        super(R.layout.item_money_log);
    }

    @Override
    protected void convert(BaseViewHolder helper, MoneyLogBean.DataBean moneyLogBean) {
        helper.setText(R.id.tv_remark,moneyLogBean.getRemark()).setText(R.id.tv_create_time, FormatUtil.stampToDate(moneyLogBean.getCreate_time()));
        //0支出 1收入
        if (moneyLogBean.getChange_status()==0){
            helper.setText(R.id.tv_price,"-"+moneyLogBean.getMoney()/100.00);
            helper.setTextColor(R.id.tv_price, ContextCompat.getColor(mContext,R.color.red));
        }else {
            helper.setText(R.id.tv_price,"+"+moneyLogBean.getMoney()/100.00);
            helper.setTextColor(R.id.tv_price,ContextCompat.getColor(mContext,R.color.app_color));
        }
    }
}
