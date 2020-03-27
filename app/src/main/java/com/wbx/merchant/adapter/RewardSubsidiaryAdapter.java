package com.wbx.merchant.adapter;

import androidx.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wbx.merchant.R;
import com.wbx.merchant.bean.RewardSubsidiaryBean;
import com.wbx.merchant.utils.FormatUtil;
import com.wbx.merchant.utils.GlideUtils;

import java.util.List;

public class RewardSubsidiaryAdapter extends BaseQuickAdapter<RewardSubsidiaryBean.DataBean.MerchantInvitationLogBean, BaseViewHolder> {
    public RewardSubsidiaryAdapter( @Nullable List<RewardSubsidiaryBean.DataBean.MerchantInvitationLogBean> data) {
        super(R.layout.item_reward, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, RewardSubsidiaryBean.DataBean.MerchantInvitationLogBean item) {
        GlideUtils.showMediumPic(mContext, (ImageView) helper.getView(R.id.civ_head),item.getFace());
        helper.setText(R.id.tv_name,item.getNickname())
                .setText(R.id.tv_tc,String.format("我提成%.2f元",(float)item.getBounty()/100.00))
                .setText(R.id.tv_time, FormatUtil.stampToDate(item.getCreate_time()));
        helper.addOnClickListener(R.id.tv_ckhd);

    }
}
