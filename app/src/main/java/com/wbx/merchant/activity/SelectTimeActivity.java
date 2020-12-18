package com.wbx.merchant.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wbx.merchant.R;
import com.wbx.merchant.adapter.BusinessTimeAdapter;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.bean.BusinessTimeBean;
import com.wbx.merchant.utils.ToastUitl;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

//到店套餐选择时间
public class SelectTimeActivity extends BaseActivity {

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.tv_add_time)
    TextView tvAddTime;
    @Bind(R.id.btn_save)
    Button btnSave;
    private BusinessTimeAdapter adapter;
    private List<BusinessTimeBean> lstData = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_select_time;
    }



    public static void actionStart(Activity activity, String businessTime) {
        Intent intent = new Intent(activity, BusinessTimeActivity.class);
        intent.putExtra("businessTime", businessTime);
        activity.startActivityForResult(intent, StoreManagerActivity.REQUEST_UPDATE_BUSINESS_TIME);
    }



    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BusinessTimeAdapter(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void fillData() {
        String businessTime = getIntent().getStringExtra("businessTime");
        if (TextUtils.isEmpty(businessTime)) {
            BusinessTimeBean businessTimeBean = new BusinessTimeBean();
            lstData.add(businessTimeBean);
            adapter.update(lstData);
        } else {
            if (businessTime.contains(",")) {
                String[] split = businessTime.split(",");
                for (String s : split) {
                    String[] time = s.split("-");
                    BusinessTimeBean businessTimeBean = new BusinessTimeBean();
                    businessTimeBean.setStart_time(time[0]);
                    businessTimeBean.setEnd_time(time[1]);
                    lstData.add(businessTimeBean);
                }
                adapter.update(lstData);
            } else {
                String[] split = businessTime.split("-");
                BusinessTimeBean businessTimeBean = new BusinessTimeBean();
                businessTimeBean.setStart_time(split[0]);
                businessTimeBean.setEnd_time(split[1]);
                lstData.add(businessTimeBean);
                adapter.update(lstData);
            }
        }
    }

    @Override
    public void setListener() {

    }

    @OnClick({R.id.tv_add_time, R.id.btn_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_add_time:
                addTime();
                break;
            case R.id.btn_save:
                save();
                break;
        }
    }

    private void addTime() {
        if (lstData.size() >= 3) {
            ToastUitl.showShort("最多只能设置三个时间段");
            return;
        }
        BusinessTimeBean businessTimeBean = new BusinessTimeBean();
        lstData.add(businessTimeBean);
        adapter.update(lstData);
    }

    private void save() {
        for (BusinessTimeBean lstDatum : lstData) {
            if (TextUtils.isEmpty(lstDatum.getStart_time()) || TextUtils.isEmpty(lstDatum.getEnd_time())) {
                ToastUitl.showShort("时间未设置");
                return;
            }
            String[] split = lstDatum.getStart_time().split(":");
            int startTime = Integer.valueOf(split[0]) * 60 + Integer.valueOf(split[1]);
            String[] split2 = lstDatum.getEnd_time().split(":");
            int endTime = Integer.valueOf(split2[0]) * 60 + Integer.valueOf(split2[1]);
            if (endTime <= startTime) {
                ToastUitl.showShort("结束时间需大于开始时间");
                return;
            }
        }
        final StringBuilder sb = new StringBuilder();
        for (BusinessTimeBean lstDatum : lstData) {
            sb.append(lstDatum.getStart_time());
            sb.append("-");
            sb.append(lstDatum.getEnd_time());
            sb.append(",");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }


        Intent intent = new Intent();
        intent.putExtra("businessTime", sb.toString());
        setResult(RESULT_OK, intent);
        finish();

    }
}