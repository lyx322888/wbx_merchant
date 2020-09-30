package com.wbx.merchant.activity.jhzf;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.wbx.merchant.R;
import com.wbx.merchant.adapter.InduStryAdapter;
import com.wbx.merchant.adapter.InduStrySecondAdapter;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.bean.CityBean;
import com.wbx.merchant.bean.ListIndustryBean;
import com.wbx.merchant.bean.ListIndustrySecondBean;
import com.wbx.merchant.bean.ProvinceBean;
import com.wbx.merchant.common.LoginUtil;
import com.wbx.merchant.widget.LoadingDialog;

import butterknife.Bind;

//选择经营类型
public class IndustrySecondActivity extends BaseActivity {

    @Bind(R.id.title_name_tv)
    TextView titleNameTv;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.recycler_city)
    RecyclerView recyclerCity;
    InduStryAdapter induStryAdapter;
    InduStrySecondAdapter induStrySecondAdapter;
    String oneIndustry = "";
    public static  final int REQUESTCODE_JYLX= 1136;

    public static void actionStart(Activity context){
        Intent intent = new Intent(context, IndustrySecondActivity.class);
        context.startActivityForResult(intent,REQUESTCODE_JYLX);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_city_list;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        titleNameTv.setText("经营品类");

        induStryAdapter = new InduStryAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(induStryAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        induStryAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                oneIndustry = induStryAdapter.getData().get(position).getIndustryName();
                getList(induStryAdapter.getData().get(position).getIndustryNum());
            }
        });

        induStrySecondAdapter = new InduStrySecondAdapter();
        recyclerCity.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerCity.setAdapter(induStrySecondAdapter);
        recyclerCity.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        induStrySecondAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent();
                intent.putExtra("oneIndustry", oneIndustry);
                intent.putExtra("twoIndustry ",  induStrySecondAdapter.getItem(position).getIndustryName());
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }



    @Override
    public void fillData() {
        LoadingDialog.showDialogForLoading(mActivity);

        new MyHttp().doPost(Api.getDefault().list_industry(LoginUtil.getLoginToken()), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                ListIndustryBean bean =new Gson().fromJson(result.toString(),ListIndustryBean.class);
                induStryAdapter.setNewData(bean.getData());
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    private void getList(String industryNum){
        LoadingDialog.showDialogForLoading(mActivity);

        new MyHttp().doPost(Api.getDefault().list_industry_second(LoginUtil.getLoginToken(),industryNum), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                ListIndustrySecondBean bean =new Gson().fromJson(result.toString(),ListIndustrySecondBean.class);
                induStrySecondAdapter.setNewData(bean.getData());
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