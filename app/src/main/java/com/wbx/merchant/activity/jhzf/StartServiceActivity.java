package com.wbx.merchant.activity.jhzf;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.wbx.merchant.R;
import com.wbx.merchant.adapter.StartServiceAdapter;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.bean.PayBankListBean;
import com.wbx.merchant.common.LoginUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

//开通业务
public class StartServiceActivity extends BaseActivity {

    @Bind(R.id.title_name_tv)
    TextView titleNameTv;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    StartServiceAdapter startServiceAdapter;
    public static final int REQUESTCODE_YW = 1625;
    private PayBankListBean payBankListBean;

    @Override
    public int getLayoutId() {
        return R.layout.activity_start_service;
    }

    public static void actionStart(Activity context, PayBankListBean payBankListBean) {
        Intent intent = new Intent(context, StartServiceActivity.class);
        intent.putExtra("payBankListBean", payBankListBean);
        context.startActivityForResult(intent, REQUESTCODE_YW);
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        startServiceAdapter = new StartServiceAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(startServiceAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

    }

    @Override
    public void fillData() {
        payBankListBean = (PayBankListBean) getIntent().getSerializableExtra("payBankListBean");

        if (payBankListBean != null) {
            startServiceAdapter.setNewData(payBankListBean.getData());
        } else {
            new MyHttp().doPost(Api.getDefault().list_bankinfo(LoginUtil.getLoginToken()), new HttpListener() {
                @Override
                public void onSuccess(JSONObject result) {
                    payBankListBean = new Gson().fromJson(result.toString(), PayBankListBean.class);
                    startServiceAdapter.setNewData(payBankListBean.getData());
                }

                @Override
                public void onError(int code) {

                }
            });
        }

    }

    @Override
    public void setListener() {

    }



    @OnClick(R.id.tv_bc)
    public void onViewClicked() {
        if (payBankListBean!=null){
            payBankListBean.setData(startServiceAdapter.getData());
            Intent intent = new Intent();
            intent.putExtra("payBankListBean", payBankListBean);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }

    }
}