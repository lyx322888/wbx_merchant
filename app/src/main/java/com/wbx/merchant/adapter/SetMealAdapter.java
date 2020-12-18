package com.wbx.merchant.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wbx.merchant.R;
import com.wbx.merchant.bean.StoreSetMealBean;
import com.wbx.merchant.utils.GlideUtils;
import com.wbx.merchant.utils.SpannableStringUtils;

public class SetMealAdapter extends BaseQuickAdapter<StoreSetMealBean.DataBean, BaseViewHolder>{
    public SetMealAdapter() {
        super(R.layout.item_set_meal);
    }

    @Override
    protected void convert(BaseViewHolder helper, StoreSetMealBean.DataBean item) {
        TextView tvState = helper.getView(R.id.tv_state);
        TextView tvSbYy = helper.getView(R.id.tv_sb_yy);
        ImageView ivPause = helper.getView(R.id.iv_pause);
        ImageView ivEnd = helper.getView(R.id.iv_end);
        GlideUtils.showBigPic(mContext,helper.getView(R.id.imageView),item.getPhoto());
        helper.setText(R.id.tv_title,item.getSet_meal_name())
                .setText(R.id.tv_time,item.getBusiness_week_days_str()+"  "+item.getCan_hours())
                .setText(R.id.tv_price, SpannableStringUtils.getBuilder(String.format("¥%s (拼团价¥%s)  ",item.getOne_price()/100.00,item.getTwo_price()/100.00))
                        .append("¥"+item.getOriginal_price()/100.00).setForegroundColor(ContextCompat.getColor(mContext,R.color.gray89)).setStrikethrough().create())
                .setText(R.id.tv_ys,String.format("已售%s张    已用%s张    VIP金额：%s",item.getBuy_count(),item.getUse_count(),item.getVip_price()/100.00));

        switch (item.getAudit_status()){
            case 0:
                //审核中
                tvState.setTextColor(ContextCompat.getColor(mContext,R.color.red));
                tvState.setText("审核中");
                tvSbYy.setVisibility(View.GONE);
                helper.setGone(R.id.tv_bj,false);
                helper.setGone(R.id.tv_kq,true);
                break;
            case 1:
                //审核成功
                tvState.setTextColor(ContextCompat.getColor(mContext,R.color.app_color));
                tvState.setText("审核成功");
                tvSbYy.setVisibility(View.GONE);
                helper.setGone(R.id.tv_bj,false);
                helper.setGone(R.id.tv_kq,true);
                break;
            case 2:
                //拒绝
                tvState.setTextColor(ContextCompat.getColor(mContext,R.color.red));
                tvState.setText("审核失败");
                tvSbYy.setVisibility(View.VISIBLE);
                tvSbYy.setText("审核失败原因："+item.getAudit_message());
                helper.setGone(R.id.tv_bj,true);
                helper.setGone(R.id.tv_kq,false);
                break;
        }


        //是否停售
        if (item.getIs_end()==1){
            ivPause.setVisibility(View.GONE);
            ivEnd.setVisibility(View.VISIBLE);
            helper.setText(R.id.tv_kq,"再次发布");
        }else {
            ivEnd.setVisibility(View.GONE);
            //是否暂停
            if (TextUtils.equals(item.getIs_pause(),"0")){
                //显示暂停
                ivPause.setVisibility(View.GONE);
                helper.setText(R.id.tv_kq,"暂停");

            }else {
                ivPause.setVisibility(View.VISIBLE);
                helper.setText(R.id.tv_kq,"开启");
            }
        }
        helper.addOnClickListener(R.id.tv_bj);
        helper.addOnClickListener(R.id.tv_sjtj);
        helper.addOnClickListener(R.id.tv_sc);
        helper.addOnClickListener(R.id.tv_kq);

    }
}
