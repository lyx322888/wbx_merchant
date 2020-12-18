package com.wbx.merchant.adapter;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wbx.merchant.R;
import com.wbx.merchant.bean.SelectGoodsInfo;
import com.wbx.merchant.utils.GlideUtils;

//到店套餐商品选择
public class SelectDdtcGoodsAdapter extends BaseQuickAdapter<SelectGoodsInfo.DataBean, BaseViewHolder> {
    public SelectDdtcGoodsAdapter( ) {
        super(R.layout.item_select_goods_ddtc);
    }

    @Override
    protected void convert(BaseViewHolder helper, SelectGoodsInfo.DataBean item) {
        helper.setText(R.id.tv_title,item.getTitle())
                .setText(R.id.tv_price,item.getMall_price()+"");
        GlideUtils.showBigPic(mContext,helper.getView(R.id.iv_shop),item.getPhoto());
        helper.setVisible(R.id.iv_yssp,item.getIs_secret()==1);
    }
}
