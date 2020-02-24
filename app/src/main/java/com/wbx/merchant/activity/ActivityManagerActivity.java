package com.wbx.merchant.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wbx.merchant.R;
import com.wbx.merchant.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wushenghui on 2017/10/13.
 * 活动管理
 */
public class ActivityManagerActivity extends BaseActivity {

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, ActivityManagerActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_activity_manager;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {

    }

    @Override
    public void fillData() {

    }

    @Override
    public void setListener() {

    }
    @OnClick({R.id.iv_xrlb, R.id.iv_full_reduce, R.id.iv_seckill, R.id.iv_send_red_packet, R.id.iv_share_free, R.id.iv_integral_free, R.id.iv_no_delivery_fee, R.id.iv_coupon, R.id.iv_sjtj})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_sjtj:
                //商家推荐
                startActivity(new Intent(mContext, MerchantRecommendActivity.class));
                break;
            case R.id.tv_right:
                startActivity(new Intent(mContext, ActivitySettingsActivity.class));
                break;
            case R.id.iv_coupon:
                startActivity(new Intent(mContext, CouponManagerActivity.class));
                break;
            case R.id.iv_full_reduce:
                startActivity(new Intent(mContext, FullActivity.class));
                break;
            case R.id.iv_seckill:
                startActivity(new Intent(mContext, SeckillActivity.class));
                break;
            case R.id.iv_send_red_packet:
                ShopRedPacketActivity.actionStart(ActivityManagerActivity.this);
                break;
//            case R.id.iv_consume_free:
//                ConsumeFreeListActivity.actionStart(this);
//                break;
            case R.id.iv_share_free:
                ShareFreeListActivity.actionStart(this);
                break;
            case R.id.iv_integral_free:
                IntegralFreeListActivity.actionStart(this);
                break;
            case R.id.iv_no_delivery_fee:
                startActivity(new Intent(mContext, NoDeliveryFeeActivity.class));
                break;
            case R.id.iv_xrlb:
                //新人礼包
                startActivity(new Intent(mContext, GiftBagActivity.class));
                break;
        }
    }

}
