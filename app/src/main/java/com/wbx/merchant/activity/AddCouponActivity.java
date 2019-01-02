package com.wbx.merchant.activity;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bigkoo.pickerview.TimePickerView;
import com.wbx.merchant.R;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.widget.PriceEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by wushenghui on 2017/10/13.
 */

public class AddCouponActivity extends BaseActivity {
    @Bind(R.id.start_time_tv)
    TextView startTimeTv;
    @Bind(R.id.end_time_tv)
    TextView endTimeTv;
    @Bind(R.id.money_edit)
    PriceEditText moneyEdit;
    @Bind(R.id.use_condition_edit)
    EditText useConditionEdit;
    @Bind(R.id.total_count_edit)
    EditText totalCountEdit;
    private HashMap<String, Object> mParams = new HashMap<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_coupon;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {

    }

    @Override
    public void fillData() {
        mParams.put("sj_login_token", userInfo.getSj_login_token());

    }

    @Override
    public void setListener() {

    }

    @OnClick({R.id.start_time_layout, R.id.end_time_layout, R.id.complete_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_time_layout:
            case R.id.start_time_tv:
                TimePickerView pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {//选中事件回调
                        startTimeTv.setText(new SimpleDateFormat("yyyy-MM-dd").format(date));
                    }
                })
                        //年月日时分秒 的显示与否，不设置则默认全部显示
                        .setType(new boolean[]{true, true, true, false, false, false})
                        .setLabel("年", "月", "日", "", "", "")
                        .isCenterLabel(false)
                        .setDividerColor(Color.GRAY)
                        .setContentSize(17)
                        .setDate(Calendar.getInstance())
                        .setBackgroundId(0x00FFFFFF) //设置外部遮罩颜色
                        .setDecorView(null)
                        .build();
                pvTime.show();
                break;
            case R.id.end_time_layout:
            case R.id.end_time_tv:
                TimePickerView pvTime2 = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {//选中事件回调
                        endTimeTv.setText(new SimpleDateFormat("yyyy-MM-dd").format(date));
                    }
                })
                        //年月日时分秒 的显示与否，不设置则默认全部显示
                        .setType(new boolean[]{true, true, true, false, false, false})
                        .setLabel("年", "月", "日", "", "", "")
                        .isCenterLabel(false)
                        .setDividerColor(Color.GRAY)
                        .setContentSize(17)
                        .setDate(Calendar.getInstance())
                        .setBackgroundId(0x00FFFFFF) //设置外部遮罩颜色
                        .setDecorView(null)
                        .build();
                pvTime2.show();
                break;
            case R.id.complete_btn:
                complete();
                break;
        }
    }

    private void complete() {
        if (!canSubmit()) {
            return;
        }
        mParams.put("money", moneyEdit.getText().toString());
        mParams.put("condition_money", useConditionEdit.getText().toString());
        mParams.put("num", totalCountEdit.getText().toString());
        mParams.put("start_time", startTimeTv.getText().toString());
        mParams.put("end_time", endTimeTv.getText().toString());
        new MyHttp().doPost(Api.getDefault().addCoupon(mParams), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                showShortToast("添加成功!");
                finish();
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    private boolean canSubmit() {
        if (TextUtils.isEmpty(moneyEdit.getText().toString())) {
            showShortToast("请输入优惠金额");
            return false;
        }

        if (TextUtils.isEmpty(useConditionEdit.getText().toString())) {
            showShortToast("请输入优惠金额");
            return false;
        }
        if (TextUtils.isEmpty(totalCountEdit.getText().toString())) {
            showShortToast("请输入优惠券总数量");
            return false;
        }
        if (TextUtils.isEmpty(startTimeTv.getText().toString())) {
            showShortToast("请选择开始时间");
            return false;
        }
        if (TextUtils.isEmpty(endTimeTv.getText().toString())) {
            showShortToast("请选择结束时间");
            return false;
        }
        return true;
    }
}
