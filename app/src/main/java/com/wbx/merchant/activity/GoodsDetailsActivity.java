package com.wbx.merchant.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wbx.merchant.R;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.bean.GoodsDetailsInfo;
import com.wbx.merchant.common.LoginUtil;
import com.wbx.merchant.presenter.GoodsDetailsPresenterImp;
import com.wbx.merchant.utils.GlideUtils;
import com.wbx.merchant.view.GoodsDetailsView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class GoodsDetailsActivity extends BaseActivity implements GoodsDetailsView {
    @Bind(R.id.goods_photo)
    ImageView ivGoods;
    @Bind(R.id.goods_name)
    TextView tvName;
    @Bind(R.id.goods_ps)
    TextView tvPs;
    @Bind(R.id.goods_price)
    TextView tvPrice;
    @Bind(R.id.goods_original)
    TextView tvOriginal;
    @Bind(R.id.goods_volume)
    TextView tvVolume;
    @Bind(R.id.goods_shop_num)
    TextView tvShopNum;
    private int goods_id;

    @Override
    public int getLayoutId() {
        return R.layout.activity_goods_details;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        goods_id = getIntent().getIntExtra("details_goods_id", 1);
        GoodsDetailsPresenterImp presenterImp=new GoodsDetailsPresenterImp(this);
        presenterImp.getGoodsDetails(LoginUtil.getLoginToken(),goods_id);
    }

    @Override
    public void fillData() {

    }

    @Override
    public void setListener() {

    }

    @Override
    public void getGoodsDetails(GoodsDetailsInfo goodInfo) {
        GlideUtils.showRoundBigPic(mContext,ivGoods,goodInfo.getData().getPhoto());
        tvName.setText(goodInfo.getData().getTitle());
        tvPs.setText(goodInfo.getData().getSubhead());
        tvPrice.setText("¥"+goodInfo.getData().getPrice()/100+"");
        tvOriginal.setText("¥"+goodInfo.getData().getOriginal_price()/100+"");
        tvOriginal.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG|Paint.ANTI_ALIAS_FLAG);
        tvVolume.setText("销量 "+goodInfo.getData().getSales_volume()+"");
        tvShopNum.setText(goodInfo.getData().getBuy_shop_num()+"");

    }
}
