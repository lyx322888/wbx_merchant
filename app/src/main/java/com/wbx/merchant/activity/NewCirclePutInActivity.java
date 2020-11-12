package com.wbx.merchant.activity;

import android.content.Intent;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.fastjson.JSONObject;
import com.flyco.roundview.RoundTextView;
import com.flyco.tablayout.SlidingTabLayout;
import com.google.gson.Gson;
import com.wbx.merchant.R;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.bean.CircleFansMoneyBean;
import com.wbx.merchant.common.LoginUtil;
import com.wbx.merchant.fragment.ItemNewCirclePutlnFragment;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;

public class NewCirclePutInActivity extends BaseActivity {

    @Bind(R.id.lab_layout)
    SlidingTabLayout labLayout;
    @Bind(R.id.order_view_pager)
    ViewPager orderViewPager;
    @Bind(R.id.tv_cz)
    RoundTextView tvCz;
    @Bind(R.id.title_name_tv)
    TextView titleNameTv;
    @Bind(R.id.tv_price)
    TextView tvPrice;

    @Override
    public int getLayoutId() {
        return R.layout.activity_new_circle_put_in;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        String[] mTitles = new String[]{"全部", "收入", "支出"};
        ArrayList<Fragment> arrayList = new ArrayList<>();
        arrayList.add(ItemNewCirclePutlnFragment.newInstance("8"));
        arrayList.add(ItemNewCirclePutlnFragment.newInstance("1"));
        arrayList.add(ItemNewCirclePutlnFragment.newInstance("0"));
        labLayout.setViewPager(orderViewPager, mTitles, this, arrayList);
    }

    @Override
    public void fillData() {
        new MyHttp().doPost(Api.getDefault().get_draw_fans_money(LoginUtil.getLoginToken()), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                CircleFansMoneyBean circleFansMoneyBean = new Gson().fromJson(result.toString(), CircleFansMoneyBean.class);
                tvPrice.setText(circleFansMoneyBean.getData().getDraw_fans_money()/100.00+"");
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        fillData();
    }

    @Override
    public void setListener() {

    }

    @OnClick(R.id.tv_cz)
    public void onViewClicked() {
        startActivity(new Intent(mContext, CriclePutlnPayActivity.class));
    }

}