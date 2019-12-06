package com.wbx.merchant.adapter;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wbx.merchant.R;
import com.wbx.merchant.bean.GiftBagInfo;

/*新人礼包 红包*/
public class GiftbagHBAdapter extends BaseQuickAdapter<GiftBagInfo.DataBean.RedPacketBean, BaseViewHolder> {
    public GiftbagHBAdapter() {
        super(R.layout.item_giftbaghb);
    }

    @Override
    protected void convert(BaseViewHolder helper, GiftBagInfo.DataBean.RedPacketBean item) {
        helper.setText(R.id.tv_price,item.getMoney()+"元");
        helper.addOnClickListener(R.id.tv_delete);
    }
}
