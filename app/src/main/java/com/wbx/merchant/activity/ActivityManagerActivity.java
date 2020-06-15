package com.wbx.merchant.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.wbx.merchant.R;
import com.wbx.merchant.adapter.ActivityManagerAdpapter;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.bean.ActivityManagerBean;
import com.wbx.merchant.common.LoginUtil;
import com.wbx.merchant.utils.FormatUtil;
import com.wbx.merchant.widget.decoration.DividerItemDecoration;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wushenghui on 2017/10/13.
 * 活动管理
 */
public class ActivityManagerActivity extends BaseActivity {
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_right)
    TextView tvRight;
    @Bind(R.id.rv_activity)
    RecyclerView rvActivity;
    private ActivityManagerAdpapter activityManagerAdpapter;

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, ActivityManagerActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        FormatUtil.setStatubarColor(mActivity, R.color.app_color);
        return R.layout.activity_manager;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        rvActivity.setLayoutManager(new LinearLayoutManager(mContext));
        activityManagerAdpapter = new ActivityManagerAdpapter();
        rvActivity.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        rvActivity.setAdapter(activityManagerAdpapter);
        activityManagerAdpapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId()==R.id.btn_cj){
                    switch (activityManagerAdpapter.getData().get(position).getType()){
                        case 1:
                            //新人专享礼包
                            startActivity(new Intent(mContext, GiftBagActivity.class));
                            break;
                        case 2:
                            //商品推荐
                            startActivity(new Intent(mContext, MerchantRecommendActivity.class));
                            break;
                        case 3:
                            //发优惠券
                            startActivity(new Intent(mContext, CouponManagerActivity.class));
                            break;
                        case 4:
                            //发红包
                            ShopRedPacketActivity.actionStart(ActivityManagerActivity.this);
                            break;
                        case 5:
                            //特价
                            startActivity(new Intent(mContext, SeckillActivity.class));
                            break;
                        case 6:
                            //积免单劵
                            IntegralFreeListActivity.actionStart(mContext);
                            break;
                        case 7:
                            //满金额免配送费
                            startActivity(new Intent(mContext, NoDeliveryFeeActivity.class));
                            break;
                        case 8:
                            //满减活动
                            startActivity(new Intent(mContext, FullActivity.class));
                            break;
                    }
                }
            }
        });
    }

    @Override
    public void fillData() {
        new MyHttp().doPost(Api.getDefault().getShopActivity(LoginUtil.getLoginToken()), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                ActivityManagerBean activityManagerBean = new Gson().fromJson(result.toString(),ActivityManagerBean.class);
                activityManagerAdpapter.setNewData(activityManagerBean.getData());
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    @Override
    public void setListener() {
    }



    @OnClick(R.id.tv_right)
    public void onViewClicked() {
        //设置
        startActivity(new Intent(mContext, ActivitySettingsActivity.class));
    }

}
