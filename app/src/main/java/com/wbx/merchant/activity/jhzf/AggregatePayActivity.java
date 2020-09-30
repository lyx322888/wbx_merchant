package com.wbx.merchant.activity.jhzf;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.alibaba.fastjson.JSONObject;
import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.flyco.roundview.RoundTextView;
import com.google.gson.Gson;
import com.wbx.merchant.R;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.bean.ListIndustryBean;
import com.wbx.merchant.bean.ShopDetailInfo;
import com.wbx.merchant.common.LoginUtil;
import com.wbx.merchant.dialog.ConfirmDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

//聚合支付
public class AggregatePayActivity extends BaseActivity {

    @Bind(R.id.title_name_tv)
    TextView titleNameTv;
    @Bind(R.id.tv_shmc)
    EditText tvShmc;
    @Bind(R.id.tv_shjc)
    EditText tvShJc;
    @Bind(R.id.et_shdz)
    EditText etShdz;
    @Bind(R.id.et_shyx)
    EditText etShyx;
    @Bind(R.id.et_shlxr)
    EditText etShlxr;
    @Bind(R.id.et_phone)
    EditText etPhone;
    @Bind(R.id.et_lxr_sfh)
    EditText etLxrSfh;
    @Bind(R.id.tv_hylx)
    TextView tvHylx;
    @Bind(R.id.tv_shlx)
    TextView tvShlx;


    @Bind(R.id.tv_submit)
    RoundTextView tvSubmit;
    @Bind(R.id.ll_hylx)
    LinearLayout llHylx;
    @Bind(R.id.ll_shlx)
    LinearLayout llShlx;
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
    @Bind(R.id.tv_xzdq)
    TextView tvXzdq;
    private OptionsPickerView pvOptions;
    private OptionsPickerView pvOptionsHyxl;
    private String customerType = "";//商户类型
    private String industry = "";//行业类型
    String province = "";
    String city = "";
    String district = "";
    @Override
    public int getLayoutId() {
        return R.layout.activity_aggregate_pay;
    }

    @Override
    public void initPresenter() {

    }


    @Override
    public void initView() {
        ShopDetailInfo shopInfo = (ShopDetailInfo) getIntent().getSerializableExtra("shopInfo");
        if (shopInfo != null) {
            pullData(shopInfo);
        }
        initShlx();
    }

    //填充信息
    private void pullData(ShopDetailInfo shopInfo) {
        etShdz.setText(shopInfo.getAddr());
    }

    @Override
    public void fillData() {
        getHylx();
    }

    private void getHylx() {
        new MyHttp().doPost(Api.getDefault().list_industry(LoginUtil.getLoginToken()), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                ListIndustryBean listIndustryBean = new Gson().fromJson(result.toString(), ListIndustryBean.class);
                if (listIndustryBean != null && listIndustryBean.getData().size() != 0) {
                    initHylx(listIndustryBean.getData());
                }
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    @Override
    public void setListener() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data!=null&&requestCode==DistrictActivity.REQUESTCODE_DISTRICT){
            province = data.getStringExtra("province");
            city = data.getStringExtra("city");
            district = data.getStringExtra("district");
            tvXzdq.setText(province+city+district);
        }
    }

    @Override
    public void onBackPressed() {
        ConfirmDialog confirmDialog = ConfirmDialog.newInstance("返回后这个页面的信息将不保留，请确保您已经提交信息！");
        confirmDialog.setDialogListener(new ConfirmDialog.DialogListener() {
            @Override
            public void dialogClickListener() {
                finish();
            }
        });
        confirmDialog.show(getSupportFragmentManager(), "");
    }

    @OnClick({R.id.ll_hylx,R.id.ll_xzdq, R.id.ll_shlx, R.id.tv_submit, R.id.tv_start_time, R.id.tv_end_time, R.id.tv_start_time_yyzz, R.id.tv_end_time_yyzz})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_hylx:
                //行业类型
                if (pvOptionsHyxl != null) {
                    pvOptionsHyxl.show();
                } else {
                    showShortToast("加载中，稍后试试");
                    getHylx();
                }
                break;
            case R.id.ll_shlx:
                //商户类型
                if (pvOptions != null) {
                    pvOptions.show();
                }
                break;
            case R.id.ll_xzdq:
                //选择地区
                DistrictActivity.actionStart(mActivity);
                break;
            case R.id.tv_start_time:
                //开始时间
                chooseTime((TextView) view);
                break;
            case R.id.tv_end_time:
                //结束时间
                chooseTime((TextView) view);
                break;
            case R.id.tv_start_time_yyzz:
                //开始时间
                chooseTime((TextView) view);
                break;
            case R.id.tv_end_time_yyzz:
                //开始时间
                chooseTime((TextView) view);
                break;
            case R.id.tv_submit:
                //提交
                submit();
                break;
        }
    }

    //提交
    private void submit() {
        if (TextUtils.isEmpty(tvHylx.getText())) {
            showShortToast("请选择行业类型");
            return;
        }
        if (TextUtils.isEmpty(tvShlx.getText())) {
            showShortToast("请选择商户类型");
            return;
        }


        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sj_login_token", LoginUtil.getLoginToken());
        hashMap.put("fullName", tvShmc.getText().toString());
        hashMap.put("shortName", tvShJc.getText().toString());
        hashMap.put("linkMan", etShlxr.getText().toString() + "");
        hashMap.put("linkPhone", etPhone.getText().toString() + "");
        hashMap.put("linkManId", etLxrSfh.getText().toString() + "");
        hashMap.put("customerType", customerType);
        hashMap.put("industry", tvHylx.getText().toString() + "");
        //地址
        hashMap.put("province", province);
        hashMap.put("city", city);
        hashMap.put("district", district);
        //法人
        hashMap.put("certificateName", etFrmc.getText().toString() + "");
        hashMap.put("certificateCode", etFrCode.getText().toString() + "");
        hashMap.put("certificateStartDate", tvStartTime.getText().toString() + "");
        hashMap.put("ertificateEndDate", tvEndTime.getText().toString() + "");
        //营业执照
        hashMap.put("organizationCode", etYyzzh.getText().toString() + "");
        hashMap.put("postalAddress", etYyzzzcdz.getText().toString() + "");


        new MyHttp().doPost(Api.getDefault().add_customerinfo(hashMap), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                showShortToast("提交成功");
                startActivity(new Intent(mContext, CloseAnAccountActivity.class));
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    //选择时间
    private void chooseTime(final TextView textView) {
        TimePickerView pvTime = new TimePickerView.Builder(mContext, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                String time = new SimpleDateFormat("yyyy-MM-dd").format(date);
                textView.setText(time);
            }
        }).setType(new boolean[]{true, true, true, false, false, false})//年月日时分秒 的显示与否，不设置则默认全部显示
                .setLabel("", "", "", "", "", "")
                .isCenterLabel(false)
                .setTitleColor(ContextCompat.getColor(mContext, R.color.black))//标题文字颜色
                .setSubmitColor(ContextCompat.getColor(mContext, R.color.app_color))//确定按钮文字颜色
                .setCancelColor(ContextCompat.getColor(mContext, R.color.black))//取消按钮文字颜色
                .setDate(Calendar.getInstance())
                .setDecorView(null)
                .build();
        pvTime.show();
    }

    //选择商户类
    private void initShlx() {
        //数据源
        final List<String> strings = new ArrayList<>();
        strings.add("个人");
        strings.add("个体工商");
        pvOptions = new OptionsPickerView(new OptionsPickerView.Builder(mContext, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                tvShlx.setText(strings.get(options1));
                switch (options1) {
                    case 0:
                        tvShmc.setHint("如：商户_张三");
                        customerType = "PERSON";
                        break;
                    case 1:
                        tvShmc.setHint("请输入营业执照上名称");
                        customerType = "INDIVIDUALBISS";
                        break;
                }
            }
        }).setSubmitText("确定")//确定按钮文字
                .setCancelText("取消")//取消按钮文字
                .setSubCalSize(18)//确定和取消文字大小
                .setTitleSize(20)//标题文字大小
                .setTitleColor(ContextCompat.getColor(mContext, R.color.black))//标题文字颜色
                .setSubmitColor(ContextCompat.getColor(mContext, R.color.app_color))//确定按钮文字颜色
                .setCancelColor(ContextCompat.getColor(mContext, R.color.black))//取消按钮文字颜色
//                .setTitleBgColor(0xFF333333)//标题背景颜色 Night mode
//                .setBgColor(0xFF000000)//滚轮背景颜色 Night mode
                .setContentTextSize(18)//滚轮文字大小
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setCyclic(false, false, false)//循环与否
                .setSelectOptions(0)  //设置默认选中项
                .setOutSideCancelable(true)//点击外部dismiss default true
        );
        pvOptions.setPicker(strings);//添加数据源


    }

    //行业类型
    private void initHylx(final List<ListIndustryBean.DataBean> data) {
        final List<String> strings = new ArrayList<>();

        //数据源
        for (int i = 0; i < data.size(); i++) {
            strings.add(data.get(i).getIndustryName());
        }
        pvOptionsHyxl = new OptionsPickerView(new OptionsPickerView.Builder(mContext, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                tvHylx.setText(strings.get(options1));
                industry = data.get(options1).getIndustryNum();
            }
        }).setSubmitText("确定")//确定按钮文字
                .setCancelText("取消")//取消按钮文字
                .setSubCalSize(18)//确定和取消文字大小
                .setTitleSize(20)//标题文字大小
                .setTitleColor(ContextCompat.getColor(mContext, R.color.black))//标题文字颜色
                .setSubmitColor(ContextCompat.getColor(mContext, R.color.app_color))//确定按钮文字颜色
                .setCancelColor(ContextCompat.getColor(mContext, R.color.black))//取消按钮文字颜色
//                .setTitleBgColor(0xFF333333)//标题背景颜色 Night mode
//                .setBgColor(0xFF000000)//滚轮背景颜色 Night mode
                .setContentTextSize(18)//滚轮文字大小
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setCyclic(false, false, false)//循环与否
                .setSelectOptions(0)  //设置默认选中项
                .setOutSideCancelable(true)//点击外部dismiss default true
        );
        pvOptionsHyxl.setPicker(strings);//添加数据源


    }


}