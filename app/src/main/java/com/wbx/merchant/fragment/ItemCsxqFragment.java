package com.wbx.merchant.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.flyco.roundview.RoundLinearLayout;
import com.google.gson.Gson;
import com.wbx.merchant.R;
import com.wbx.merchant.adapter.SjtjAdapter;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseFragment;
import com.wbx.merchant.baseapp.AppConfig;
import com.wbx.merchant.bean.CountUserSetMealBean;
import com.wbx.merchant.bean.ListUserSetMealBean;
import com.wbx.merchant.common.LoginUtil;
import com.wbx.merchant.utils.AdapterUtilsNew;
import com.wbx.merchant.widget.refresh.BaseRefreshListener;
import com.wbx.merchant.widget.refresh.PullToRefreshLayout;

import java.util.HashMap;

import butterknife.Bind;

//到店套餐数据统计
public class ItemCsxqFragment extends BaseFragment {

    @Bind(R.id.tv_num)
    TextView tvNum;
    @Bind(R.id.tv_price)
    TextView tvPrice;
    @Bind(R.id.tv_tk_price)
    TextView tvTkPrice;
    @Bind(R.id.ll_cs)
    LinearLayout llCs;
    @Bind(R.id.ll_sy)
    LinearLayout llSy;
    @Bind(R.id.rv_sjtj)
    RecyclerView rvSjtj;
    @Bind(R.id.ll_cs_tj)
    RoundLinearLayout llCsTj;
    @Bind(R.id.tv_use_all_num)
    TextView tvUseAllNum;
    @Bind(R.id.tv_all_income_money)
    TextView tvAllIncomeMoney;
    @Bind(R.id.ll_sy_tj)
    RoundLinearLayout llSyTj;
    @Bind(R.id.refresh_layout)
    PullToRefreshLayout refreshLayout;
    private String shop_set_meal_id = "";
    private int is_use;
    private int mPageNum = AppConfig.pageNum;
    private int mPageSize = AppConfig.pageSize;
    private SjtjAdapter sjtjAdapter;
    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_item_csxq;
    }

    public static ItemCsxqFragment newInstance(int is_use, String shop_set_meal_id) {
        ItemCsxqFragment tabFragment = new ItemCsxqFragment();
        Bundle bundle = new Bundle();
        bundle.putString("shop_set_meal_id", shop_set_meal_id);
        bundle.putInt("is_use", is_use);
        tabFragment.setArguments(bundle);
        return tabFragment;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    protected void initView() {
        Bundle bundle = getArguments();
        is_use = bundle.getInt("is_use");
        shop_set_meal_id = bundle.getString("shop_set_meal_id");
        if (is_use == 0) {
            //出售
            llCs.setVisibility(View.VISIBLE);
            llSy.setVisibility(View.GONE);
            llCsTj.setVisibility(View.VISIBLE);
            llSyTj.setVisibility(View.GONE);
        } else {
            //使用
            llCs.setVisibility(View.GONE);
            llSy.setVisibility(View.VISIBLE);
            llCsTj.setVisibility(View.GONE);
            llSyTj.setVisibility(View.VISIBLE);
        }

        refreshLayout.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                mPageNum = 1;
                getList();
            }

            @Override
            public void loadMore() {
                mPageNum += 1;
                getList();
            }
        });

        sjtjAdapter = new SjtjAdapter(is_use);
        rvSjtj.setLayoutManager(new LinearLayoutManager(getContext()));
        rvSjtj.setAdapter(sjtjAdapter);
        sjtjAdapter.setEmptyView(R.layout.layout_empty,rvSjtj);
    }

    @Override
    protected void fillData() {
       getcount_user_set_meal();
       getList();
    }

    private void getcount_user_set_meal(){
        new MyHttp().doPost(Api.getDefault().count_user_set_meal(LoginUtil.getLoginToken(),shop_set_meal_id), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                CountUserSetMealBean countUserSetMealBean = new Gson().fromJson(result.toString(),CountUserSetMealBean.class);
                tvNum.setText(countUserSetMealBean.getData().getBuy_all_num());
                tvUseAllNum.setText(countUserSetMealBean.getData().getUse_all_num());
                tvPrice.setText(countUserSetMealBean.getData().getAll_pay_money()/100.00+"");
                tvTkPrice.setText(countUserSetMealBean.getData().getAll_refund_money()/100.00+"");
                tvAllIncomeMoney.setText(countUserSetMealBean.getData().getAll_income_money()/100.00+"");
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    private void getList(){
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("sj_login_token",LoginUtil.getLoginToken());
        hashMap.put("page",mPageNum);
        hashMap.put("num",mPageSize);
        hashMap.put("is_use",is_use);
        hashMap.put("shop_set_meal_id",shop_set_meal_id);
        new MyHttp().doPost(Api.getDefault().list_user_set_meal(hashMap), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                ListUserSetMealBean mealBean = new Gson().fromJson(result.toString(),ListUserSetMealBean.class);
                AdapterUtilsNew.setData(sjtjAdapter,mealBean.getData(),mPageNum,mPageSize);
                refreshLayout.finishRefresh();
                refreshLayout.finishLoadMore();
            }

            @Override
            public void onError(int code) {
                refreshLayout.finishRefresh();
                refreshLayout.finishLoadMore();
            }
        });
    }

    @Override
    protected void bindEven() {

    }
}