package com.wbx.merchant.activity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.viewpager.widget.ViewPager;

import com.alibaba.fastjson.JSONObject;
import com.flyco.tablayout.SlidingTabLayout;
import com.google.gson.Gson;
import com.wbx.merchant.R;
import com.wbx.merchant.adapter.viewpageadapter.OrderFragmentStateAdapter;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.bean.CountBean;
import com.wbx.merchant.common.LoginUtil;

import java.util.HashMap;

import butterknife.Bind;
import rx.functions.Action1;

/**
 * Created by wushenghui on 2017/6/20.
 */

public class OrderActivity extends BaseActivity {
    public static final int POSITION_WAIT_SEND = 0;//待配送
    public static final int POSITION_SENDING = 1;//配送中
    public static final int POSITION_SELF= 2;//待自提
    public static final int POSITION_WAIT_REFUND = 3;//待退款
    public static final int POSITION_REFUNDED = 4;//已退款
    public static final int POSITION_COMPLETED = 5;//已完成
    @Bind(R.id.order_lab_layout)
    SlidingTabLayout mTabLayout;
    @Bind(R.id.order_view_pager)
    ViewPager mOrderViewPager;
    private String[] mTitles = new String[]{"待配送", "配送中", "待自提","待退款", "已退款", "已完成"};
    private OrderFragmentStateAdapter mPagerAdapter;

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, OrderActivity.class);
        context.startActivity(intent);
    }

    public static void actionStart(Context context, int position) {
        Intent intent = new Intent(context, OrderActivity.class);
        intent.putExtra("position", position);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_order;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        mPagerAdapter = new OrderFragmentStateAdapter(getSupportFragmentManager(), mTitles);
        mOrderViewPager.setAdapter(mPagerAdapter);
        mTabLayout.setViewPager(mOrderViewPager,mTitles);
        mTabLayout.setCurrentTab(getIntent().getIntExtra("position", 0));
        //刷新
        mRxManager.on("OrderActivity", new Action1<Object>() {
            @Override
            public void call(Object o) {
                upData();
            }
        });

    }


    @Override
    public void fillData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        upData();
    }
    //更新角标
    private void upData(){
        HashMap<String, Object> mParams = new HashMap<>();
        mParams.put("sj_login_token", LoginUtil.getLoginToken());
        new MyHttp().doPost(Api.getDefault().getCountOrder(mParams), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                CountBean bean = new Gson().fromJson(result.toString(),CountBean.class);
                if (bean.getData().getCount_no_shipped_order()!=0){
                    mTabLayout.showMsg(0,bean.getData().getCount_no_shipped_order());
                    mTabLayout.setMsgMargin(0,-10,13);
                }else {
                    mTabLayout.hideMsg(0);
                }
                if (bean.getData().getCount_shipped_order()!=0){
                    mTabLayout.showMsg(1,bean.getData().getCount_shipped_order());
                    mTabLayout.setMsgMargin(1,-10,13);
                }else {
                    mTabLayout.hideMsg(1);
                }
                if (bean.getData().getCount_afhalen_order()!=0){
                    mTabLayout.showMsg(2,bean.getData().getCount_shipped_order());
                    mTabLayout.setMsgMargin(2,-10,13);
                }else {
                    mTabLayout.hideMsg(2);
                }
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    @Override
    public void setListener() {

    }
}
