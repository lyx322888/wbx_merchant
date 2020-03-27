package com.wbx.merchant.activity;

import android.app.Activity;
import android.content.Intent;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wbx.merchant.R;
import com.wbx.merchant.adapter.viewpageadapter.AddFoodFragmentStateAdapter;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.base.BaseApplication;
import com.wbx.merchant.bean.ModifyFoodNumBean;
import com.wbx.merchant.bean.ScanOrderGoodsBean;
import com.wbx.merchant.widget.LoadingDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class AddFoodActivity extends BaseActivity {
    @Bind(R.id.tab_layout)
    TabLayout tabLayout;
    @Bind(R.id.view_pager)
    ViewPager viewPager;
    private AddFoodFragmentStateAdapter mPagerAdapter;
    private String outTradeNo;
    private List<ScanOrderGoodsBean> lstAllGoods;

    public static void actionStart(Activity context, String outTradeNo) {
        Intent intent = new Intent(context, AddFoodActivity.class);
        intent.putExtra("outTradeNo", outTradeNo);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_food;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {

    }

    @Override
    public void fillData() {
        outTradeNo = getIntent().getStringExtra("outTradeNo");
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        LoadingDialog.showDialogForLoading(this);
        new MyHttp().doPost(Api.getDefault().getScanOrderGoodsList(BaseApplication.getInstance().readLoginUser().getSj_login_token()), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                lstAllGoods = JSONObject.parseArray(result.getString("data"), ScanOrderGoodsBean.class);
                updateUI();
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    public List<ScanOrderGoodsBean> getAllGoods() {
        return lstAllGoods;
    }

    private void updateUI() {
        if (lstAllGoods == null || lstAllGoods.size() == 0) {
            return;
        }
        List<String> lstTitle = new ArrayList<>();
        for (ScanOrderGoodsBean lstAllGood : lstAllGoods) {
            lstTitle.add(lstAllGood.getCate_name());
        }
        mPagerAdapter = new AddFoodFragmentStateAdapter(getSupportFragmentManager(), lstTitle);
        viewPager.setAdapter(mPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void setListener() {

    }

    @OnClick(R.id.rl_right)
    public void onViewClicked() {
        List<ModifyFoodNumBean> lstSelectGoods = new ArrayList<>();
        for (ScanOrderGoodsBean lstAllGood : lstAllGoods) {
            for (ScanOrderGoodsBean.GoodsBean goodsBean : lstAllGood.getGoods()) {
                if (goodsBean.getNum() > 0) {
                    ModifyFoodNumBean modifyFoodNumBean = new ModifyFoodNumBean();
                    modifyFoodNumBean.setNum(goodsBean.getNum());
                    modifyFoodNumBean.setGoods_id(goodsBean.getGoods_id());
                    modifyFoodNumBean.setAttr_id(goodsBean.getAttr_id());
                    lstSelectGoods.add(modifyFoodNumBean);
                }
            }
        }
        if (lstSelectGoods.size() > 0) {
            String jsonString = JSONArray.toJSONString(lstSelectGoods);
            new MyHttp().doPost(Api.getDefault().addFood(BaseApplication.getInstance().readLoginUser().getSj_login_token(),
                    outTradeNo, jsonString), new HttpListener() {
                @Override
                public void onSuccess(JSONObject result) {
                    finish();
                }

                @Override
                public void onError(int code) {

                }
            });
        } else {
            finish();
        }
    }
}
