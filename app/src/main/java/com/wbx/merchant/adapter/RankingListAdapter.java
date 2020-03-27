package com.wbx.merchant.adapter;

import androidx.core.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wbx.merchant.R;
import com.wbx.merchant.bean.RanKingListBean;
import com.wbx.merchant.utils.GlideUtils;

public class RankingListAdapter extends BaseQuickAdapter<RanKingListBean.DataBean.AllUserBean, BaseViewHolder> {
    public RankingListAdapter() {
        super(R.layout.item_phb);
    }

    @Override
    protected void convert(BaseViewHolder helper, RanKingListBean.DataBean.AllUserBean item) {
        ImageView ivph =  helper.getView(R.id.iv_ph);
        TextView tvph =  helper.getView(R.id.tv_ph);
        tvph.setText(""+helper.getLayoutPosition());
        switch (helper.getLayoutPosition()){
            case 0:
                ivph.setVisibility(View.VISIBLE);
                ivph.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.ic_first));
                tvph.setVisibility(View.GONE);
                break;
            case 1:
                ivph.setVisibility(View.VISIBLE);
                ivph.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.ic_tow));
                tvph.setVisibility(View.GONE);
                break;
            case 2:
                ivph.setVisibility(View.VISIBLE);
                ivph.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.ic_thirdly));
                tvph.setVisibility(View.GONE);
                break;
            default:
                ivph.setVisibility(View.GONE);
                tvph.setVisibility(View.VISIBLE);
                break;
        }
        GlideUtils.showMediumPic(mContext, (ImageView) helper.getView(R.id.iv_shop),item.getFace());
        helper.setText(R.id.tv_shop_name,item.getNickname())
                .setText(R.id.tv_sr,""+item.getAll_share_bounty()/100)
                .setText(R.id.tv_kds,item.getMerchant_num());
    }
}
