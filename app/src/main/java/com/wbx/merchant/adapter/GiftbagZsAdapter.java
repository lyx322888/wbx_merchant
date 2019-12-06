package com.wbx.merchant.adapter;


import android.widget.ImageView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wbx.merchant.R;
import com.wbx.merchant.bean.GiftBagInfo;
import com.wbx.merchant.utils.GlideUtils;

//新人礼包赠送商品
public class GiftbagZsAdapter extends BaseQuickAdapter<GiftBagInfo.DataBean.GiveGoodsBean, BaseViewHolder> {
    public GiftbagZsAdapter() {
        super(R.layout.item_gift_bag_zs);
    }

    @Override
    protected void convert(BaseViewHolder helper, GiftBagInfo.DataBean.GiveGoodsBean item) {
        GlideUtils.showMediumPic(mContext, (ImageView) helper.getView(R.id.iv_goods),item.getPhoto());
        helper.setText(R.id.tv_goods_name,item.getTitle());
        helper.addOnClickListener(R.id.fl_del);
    }
}
