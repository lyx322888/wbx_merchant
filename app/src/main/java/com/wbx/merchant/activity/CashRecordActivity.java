package com.wbx.merchant.activity;

import android.graphics.Color;
import android.os.Handler;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bigkoo.pickerview.TimePickerView;
import com.wbx.merchant.R;
import com.wbx.merchant.adapter.RecordAdapter;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.bean.RecordInfo;
import com.wbx.merchant.widget.LoadingDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by wushenghui on 2017/6/30.
 * 提现记录
 */

public class CashRecordActivity extends BaseActivity {
    @Bind(R.id.show_date_tv)
    TextView showDateTv;
    @Bind(R.id.record_recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.null_data_view)
    LinearLayout nullDataView;
    @Bind(R.id.cash_total_price_tv)
    TextView cashTotalPriceTv;
    private List<RecordInfo> recordInfoList = new ArrayList<>();
    private RecordAdapter mAdapter;
    private int cashTotalPrice;

    @Override
    public int getLayoutId() {
        return R.layout.activity_cash_record;
    }

    @Override
    public void initPresenter() {
    }

    @Override
    public void initView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new RecordAdapter(recordInfoList, mContext);
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public void fillData() {
        cashTotalPrice = getIntent().getIntExtra("cashTotalPrice", 0);
        showDateTv.setText(new SimpleDateFormat("yyyy年MM月").format(new Date()));
        getServiceData(new SimpleDateFormat("yyyy-MM").format(new Date()));
    }

    private void getServiceData(String queryTime) {
        LoadingDialog.showDialogForLoading(mActivity, "加载中...", true);
        HashMap<String, Object> params = new HashMap<>();
        params.put("sj_login_token", userInfo.getSj_login_token());
        params.put("addtime", queryTime);
        params.put("page", 1);
        params.put("num", 100000);
        new MyHttp().doPost(Api.getDefault().getCashRecord(params), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                recordInfoList.clear();
                cashTotalPriceTv.setText(String.format("提现总额¥%.2f", cashTotalPrice / 100.00));
                List<RecordInfo> data = JSONArray.parseArray(result.getString("data"), RecordInfo.class);
                recordInfoList.addAll(data);
                mAdapter.notifyDataSetChanged();
                nullDataView.setVisibility(View.GONE);
            }

            @Override
            public void onError(int code) {

                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        recordInfoList.clear();
                        mAdapter.notifyDataSetChanged();
                        nullDataView.setVisibility(View.VISIBLE);
                    }
                });

            }
        });
    }

    @Override
    public void setListener() {

    }

    @OnClick({R.id.choose_date_im})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.choose_date_im:
                Calendar startDate = Calendar.getInstance();
                startDate.set(2007, 1, 1);//开始时间2015
                Calendar endDate = Calendar.getInstance();
                endDate.setTime(Calendar.getInstance().getTime());
                TimePickerView pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {//选中事件回调
                        getServiceData(new SimpleDateFormat("yyyy-MM").format(date));
                        showDateTv.setText(new SimpleDateFormat("yyyy年MM月").format(date));
                    }
                })
                        //年月日时分秒 的显示与否，不设置则默认全部显示
                        .setType(new boolean[]{true, true, false, false, false, false})
                        .setLabel("", "", "", "", "", "")
                        .isCenterLabel(false)
                        .setDividerColor(Color.GRAY)
                        .setContentSize(17)
                        .setSubmitColor(getResources().getColor(R.color.app_color))
                        .setRangDate(startDate, endDate)//设置开始时间和结束时间
                        .setDate(endDate)//默认选中当前时间
                        .setBackgroundId(0x00FFFFFF) //设置外部遮罩颜色
                        .setDecorView(null)
                        .build();
                pvTime.show();
                break;
        }
    }
}
