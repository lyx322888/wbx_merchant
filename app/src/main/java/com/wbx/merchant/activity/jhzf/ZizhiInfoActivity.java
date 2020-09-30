package com.wbx.merchant.activity.jhzf;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.flyco.roundview.RoundTextView;
import com.wbx.merchant.R;
import com.wbx.merchant.base.BaseActivity;

import butterknife.Bind;
import butterknife.OnClick;

//资质信息填写
public class ZizhiInfoActivity extends BaseActivity {

    @Bind(R.id.title_name_tv)
    TextView titleNameTv;
    @Bind(R.id.et_frmc)
    EditText etFrmc;
    @Bind(R.id.et_fr_code)
    EditText etFrCode;
    @Bind(R.id.tv_start_time)
    TextView tvStartTime;
    @Bind(R.id.tv_end_time)
    TextView tvEndTime;
    @Bind(R.id.et_yyzzh)
    EditText etYyzzh;
    @Bind(R.id.et_yyzzzcdz)
    EditText etYyzzzcdz;
    @Bind(R.id.tv_start_time_yyzz)
    TextView tvStartTimeYyzz;
    @Bind(R.id.tv_end_time_yyzz)
    TextView tvEndTimeYyzz;
    @Bind(R.id.tv_submit)
    RoundTextView tvSubmit;

    @Override
    public int getLayoutId() {
        return R.layout.activity_zizhi_info;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {

    }

    @Override
    public void fillData() {

    }

    @Override
    public void setListener() {

    }


    @OnClick({R.id.tv_start_time, R.id.tv_end_time, R.id.tv_start_time_yyzz, R.id.tv_end_time_yyzz, R.id.tv_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_start_time:
                break;
            case R.id.tv_end_time:
                break;
            case R.id.tv_start_time_yyzz:
                break;
            case R.id.tv_end_time_yyzz:
                break;
            case R.id.tv_submit:
                startActivity(new Intent(mContext,CredentialsActivity.class));
                break;
        }
    }
}