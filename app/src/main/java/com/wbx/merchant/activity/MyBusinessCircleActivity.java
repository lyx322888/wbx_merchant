package com.wbx.merchant.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wbx.merchant.R;
import com.wbx.merchant.adapter.MyBusinessCircleAdapter;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.base.BaseApplication;
import com.wbx.merchant.baseapp.AppConfig;
import com.wbx.merchant.bean.BusinessCircleBean;
import com.wbx.merchant.fragment.MybusinessFragment;
import com.wbx.merchant.widget.refresh.BaseRefreshListener;
import com.wbx.merchant.widget.refresh.PullToRefreshLayout;
import com.wbx.merchant.widget.refresh.ViewStatus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class MyBusinessCircleActivity extends BaseActivity  {

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, MyBusinessCircleActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_business_circle;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        MybusinessFragment mineFragment = (MybusinessFragment) getSupportFragmentManager().getFragments().get(0);
        mineFragment.setVisibilityHead(View.VISIBLE);
    }

    @Override
    public void fillData() {

    }

    @Override
    public void setListener() {

    }


}
