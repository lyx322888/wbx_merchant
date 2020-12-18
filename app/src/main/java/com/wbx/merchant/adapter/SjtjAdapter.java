package com.wbx.merchant.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wbx.merchant.R;
import com.wbx.merchant.bean.ListUserSetMealBean;
import com.wbx.merchant.utils.FormatUtil;

import java.util.List;

public class SjtjAdapter extends BaseQuickAdapter<ListUserSetMealBean.DataBean, BaseViewHolder> {
    private int is_use;
    public SjtjAdapter(int isUser) {
        super(R.layout.item_sjtj);
        this.is_use = isUser;
    }

    @Override
    protected void convert(BaseViewHolder helper, ListUserSetMealBean.DataBean item) {
        if (is_use==1){
            helper.setGone(R.id.ll_sy,true);
            helper.setGone(R.id.ll_cs,false);
        }else {
            helper.setGone(R.id.ll_sy,false);
            helper.setGone(R.id.ll_cs,true);
        }
        helper.setText(R.id.tv_phone,item.getAccount())
                .setText(R.id.tv_time, FormatUtil.stampToDate1(item.getConfirm_time()))
                .setText(R.id.tv_num,item.getConfirm_num())
                .setText(R.id.tv_sr_price,"¥"+item.getPay_money()/100.00)
                .setText(R.id.tv_fwf,"扣除服务费¥"+item.getSet_meal_skill_fee()/100.00)
                .setText(R.id.tv_gm_price,"¥"+item.getPay_money()/100.00);

        if (item.getIs_refund()==1){
            //已退款
            helper.setGone(R.id.tv_is_tk,true);
        }else {
            helper.setGone(R.id.tv_is_tk,false);

        }
    }
}
