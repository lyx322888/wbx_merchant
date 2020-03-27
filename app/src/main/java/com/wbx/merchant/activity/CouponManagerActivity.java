package com.wbx.merchant.activity;

import android.content.Intent;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wbx.merchant.R;
import com.wbx.merchant.adapter.CouponAdapter;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.base.BaseAdapter;
import com.wbx.merchant.bean.CouPonInfo;
import com.wbx.merchant.widget.iosdialog.AlertDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;

/**
 * Created by wushenghui on 2017/10/13.
 */

public class CouponManagerActivity extends BaseActivity {
    @Bind(R.id.coupon_recycler_view)
    RecyclerView mRecyclerView;
    private HashMap<String, Object> mParams = new HashMap<>();
    private List<CouPonInfo> couPonInfoList = new ArrayList<>();
    private CouponAdapter couponAdapter;
    private CouPonInfo selectCoupon;

    @Override
    public int getLayoutId() {
        return R.layout.activity_coupon_manager;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        couPonInfoList.clear();
        getServiceData();
    }

    private void getServiceData() {
        new MyHttp().doPost(Api.getDefault().getCouponList(mParams), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                couPonInfoList.addAll(JSONArray.parseArray(result.getString("data"), CouPonInfo.class));
                couponAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    @Override
    public void initView() {
        mParams.put("sj_login_token", userInfo.getSj_login_token());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        couponAdapter = new CouponAdapter(couPonInfoList, mContext);
        mRecyclerView.setAdapter(couponAdapter);
    }

    @Override
    public void fillData() {
//       getServiceData();
    }

    @Override
    public void setListener() {
        couponAdapter.setOnItemClickListener(R.id.delete_coupon_im, new BaseAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(View view, int position) {
                selectCoupon = couponAdapter.getItem(position);
                new AlertDialog(mContext).builder()
                        .setTitle("提示")
                        .setMsg("删除优惠券？")
                        .setNegativeButton("再想想", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        })
                        .setPositiveButton("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                deleteCoupon();

                            }
                        }).show();
            }
        });
        findViewById(R.id.add_coupon_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, AddCouponActivity.class));
            }
        });
    }

    private void deleteCoupon() {
        new MyHttp().doPost(Api.getDefault().deleteCoupon(userInfo.getSj_login_token(), selectCoupon.getCoupon_id()), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                couPonInfoList.remove(selectCoupon);
                couponAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(int code) {

            }
        });
    }
}
