package com.wbx.merchant.adapter;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wbx.merchant.R;
import com.wbx.merchant.bean.ChooseShopVersionsBean;
import com.wbx.merchant.utils.GlideUtils;
import com.wbx.merchant.utils.SpannableStringUtils;


public class ChooseShopVersionsAdapter extends BaseQuickAdapter<ChooseShopVersionsBean.DataBean, BaseViewHolder> {
    public ChooseShopVersionsAdapter() {
        super(R.layout.item_choose_version);
    }

    @Override
    protected void convert(BaseViewHolder helper, ChooseShopVersionsBean.DataBean item) {
        GlideUtils.showMediumPic(mContext, (ImageView) helper.getView(R.id.iv_photo),item.getOrder_img());
        helper.setText(R.id.tv_description,item.getPresent())
                .setText(R.id.tv_mfty,item.getPresent())
                .setText(R.id.tv_title,SpannableStringUtils.getBuilder(item.getTitle()).append(item.getSubhead()).setProportion(0.5f).create())
                .setText(R.id.tv_price, SpannableStringUtils.getBuilder("¥"+item.getMoney()/100)
                .append(String.format("%s  ",item.getTime())).setProportion((float) 0.5)
                .append(String.format("%s",item.getOriginal_price())).setProportion((float) 0.5).setForegroundColor(ContextCompat.getColor(mContext,R.color.gray)).setStrikethrough()
                        .create());
        if (item.is_choose){
            helper.getView(R.id.iv_choose).setVisibility(View.VISIBLE);
            helper.getView(R.id.ll_choose).setBackground(ContextCompat.getDrawable(mContext,R.drawable.line_rounde_appcolour));
        }else {
            helper.getView(R.id.iv_choose).setVisibility(View.GONE);
            helper.getView(R.id.ll_choose).setBackground(ContextCompat.getDrawable(mContext,R.drawable.line_rounde_hui));
        }
        if (item.getIs_recommend()==1){
            helper.getView(R.id.iv_tj).setVisibility(View.VISIBLE);
        }else {
            helper.getView(R.id.iv_tj).setVisibility(View.GONE);
        }
        switch (item.getGrade_type()){
            case 1:
                //试用版
                helper.getView(R.id.ll_qjb).setVisibility(View.GONE);
                helper.getView(R.id.tv_mfty).setVisibility(View.VISIBLE);
                break;
            case 2:
                //旗舰版 12个月
                helper.getView(R.id.ll_qjb).setVisibility(View.VISIBLE);
                helper.getView(R.id.tv_mfty).setVisibility(View.GONE);
                break;
            default :
                //旗舰版 24个月
                helper.getView(R.id.ll_qjb).setVisibility(View.VISIBLE);
                helper.getView(R.id.tv_mfty).setVisibility(View.GONE);
                break;
        }
    }

    //重置
    public void chooseVersion(int position){
        for (int i = 0; i < mData.size(); i++) {
            mData.get(i).is_choose = false;
        }
        mData.get(position).is_choose = true;
        notifyDataSetChanged();
    }
}
