package com.wbx.merchant.activity.jhzf;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.wbx.merchant.R;
import com.wbx.merchant.adapter.CityAdapter;
import com.wbx.merchant.adapter.DistrictAdapter;
import com.wbx.merchant.adapter.ProvinceAdapter;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.bean.CityBean;
import com.wbx.merchant.bean.DistrictBean;
import com.wbx.merchant.bean.ProvinceBean;
import com.wbx.merchant.common.LoginUtil;
import com.wbx.merchant.widget.LoadingDialog;

import butterknife.Bind;

public class DistrictActivity extends BaseActivity {


    @Bind(R.id.title_name_tv)
    TextView titleNameTv;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.recycler_city)
    RecyclerView recyclerCity;
    @Bind(R.id.recycler_district)
    RecyclerView recyclerDistrict;
    ProvinceAdapter provinceAdapter;
    CityAdapter cityAdapter;
    DistrictAdapter districtAdapter;
    String province = "";
    String city = "";
    String district = "";
    public static  final int REQUESTCODE_DISTRICT= 1526;


    public static void actionStart(Activity context){
        Intent intent = new Intent(context, DistrictActivity.class);
        context.startActivityForResult(intent,REQUESTCODE_DISTRICT);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_district;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        provinceAdapter = new ProvinceAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(provinceAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        provinceAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                province = provinceAdapter.getData().get(position).getName();
                getCityList(provinceAdapter.getData().get(position).getCode());
            }
        });

        cityAdapter = new CityAdapter();
        recyclerCity.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerCity.setAdapter(cityAdapter);
        recyclerCity.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        cityAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    city = cityAdapter.getData().get(position).getName();
                    getDistricy(cityAdapter.getData().get(position).getCode());
            }
        });

        districtAdapter = new DistrictAdapter();
        recyclerDistrict.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerDistrict.setAdapter(districtAdapter);
        recyclerDistrict.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        districtAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent();
                intent.putExtra("province",  province);
                intent.putExtra("city",  city);
                intent.putExtra("district",  districtAdapter.getItem(position).getName());
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

    }

    @Override
    public void fillData() {
        LoadingDialog.showDialogForLoading(mActivity);

        new MyHttp().doPost(Api.getDefault().list_province(LoginUtil.getLoginToken()), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                ProvinceBean bean =new Gson().fromJson(result.toString(),ProvinceBean.class);
                provinceAdapter.setNewData(bean.getData());
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    @Override
    public void setListener() {

    }

    private void getCityList(String code){
        LoadingDialog.showDialogForLoading(mActivity);

        new MyHttp().doPost(Api.getDefault().list_city(LoginUtil.getLoginToken(),code), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                CityBean bean =new Gson().fromJson(result.toString(),CityBean.class);
                cityAdapter.setNewData(bean.getData());
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    private void getDistricy(String code){
        LoadingDialog.showDialogForLoading(mActivity);

        new MyHttp().doPost(Api.getDefault().list_district(LoginUtil.getLoginToken(),code), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                DistrictBean bean =new Gson().fromJson(result.toString(),DistrictBean.class);
                districtAdapter.setNewData(bean.getData());
            }

            @Override
            public void onError(int code) {

            }
        });
    }
}