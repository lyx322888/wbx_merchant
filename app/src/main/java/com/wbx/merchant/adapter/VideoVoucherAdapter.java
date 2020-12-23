package com.wbx.merchant.adapter;

import android.media.MediaMetadataRetriever;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wbx.merchant.R;
import com.wbx.merchant.bean.VideoVVoucherListBean;
import com.wbx.merchant.utils.GlideUtils;
import com.wbx.merchant.utils.SpannableStringUtils;

import java.util.HashMap;

import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;

public class VideoVoucherAdapter extends BaseQuickAdapter<VideoVVoucherListBean.DataBean, BaseViewHolder> {
    public VideoVoucherAdapter() {
        super(R.layout.item_video);
    }

    @Override
    protected void convert(BaseViewHolder helper, VideoVVoucherListBean.DataBean item) {
        TextView tvState = helper.getView(R.id.tv_state);
        TextView tvSbYy = helper.getView(R.id.tv_sb_yy);
        JZVideoPlayerStandard jzvd = helper.getView(R.id.jz_video);
        jzvd.setUp(item.getVideo(), JZVideoPlayer.SCREEN_WINDOW_NORMAL, "");
        GlideUtils.showBigPic(mContext,jzvd.thumbImageView,item.getShop_set_meal().getPhoto());
        helper.setText(R.id.tv_title,item.getShop_set_meal().getSet_meal_name())
                .setText(R.id.tv_price, SpannableStringUtils.getBuilder("¥"+item.getShop_set_meal().getOne_price()+"  ")
                        .append("¥"+item.getShop_set_meal().getOriginal_price()).setStrikethrough().setProportion((float) 0.8)
                        .setForegroundColor(ContextCompat.getColor(mContext,R.color.gray89)).create())
                .setText(R.id.tv_num,SpannableStringUtils.getBuilder(String.format("已售%s张",item.getShop_set_meal().getAlready_sell_num()))
                        .append(String.format("     已用%s张",item.getShop_set_meal().getAlready_use_num())).setForegroundColor(ContextCompat.getColor(mContext,R.color.gray89)).create());

        switch (item.getAudit_status()){
            case 0:
                //审核中
                tvState.setTextColor(ContextCompat.getColor(mContext,R.color.red));
                tvState.setText("审核中");
                tvSbYy.setVisibility(View.GONE);
                helper.setGone(R.id.tv_bj,false);
                break;
            case 1:
                //审核成功
                tvState.setTextColor(ContextCompat.getColor(mContext,R.color.app_color));
                tvState.setText("审核成功");
                tvSbYy.setVisibility(View.GONE);
                helper.setGone(R.id.tv_bj,false);
                break;
            case 2:
                //拒绝
                tvState.setTextColor(ContextCompat.getColor(mContext,R.color.red));
                tvState.setText("审核失败");
                tvSbYy.setVisibility(View.VISIBLE);
                tvSbYy.setText("审核失败原因："+item.getAudit_message());
                helper.setGone(R.id.tv_bj,true);
                break;
        }
        helper.addOnClickListener(R.id.tv_bj);
        helper.addOnClickListener(R.id.tv_sjtj);
        helper.addOnClickListener(R.id.tv_sc);
    }
}
