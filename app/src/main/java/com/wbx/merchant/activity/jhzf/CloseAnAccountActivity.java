package com.wbx.merchant.activity.jhzf;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.flyco.roundview.RoundTextView;
import com.wbx.merchant.R;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.bean.PayBankListBean;
import com.wbx.merchant.common.LoginUtil;
import com.wbx.merchant.dialog.ConfirmDialog;
import com.wbx.merchant.widget.LoadingDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

//结算信息
public class CloseAnAccountActivity extends BaseActivity {

    @Bind(R.id.title_name_tv)
    TextView titleNameTv;
    @Bind(R.id.tv_zhlx)
    TextView tvZhlx;
    @Bind(R.id.et_jsr)
    EditText etJsr;
    @Bind(R.id.et_jsr_sfz)
    EditText etJsrSfz;
    @Bind(R.id.tv_start_time)
    TextView tvStartTime;
    @Bind(R.id.tv_end_time)
    TextView tvEndTime;
    @Bind(R.id.tv_szyh)
    TextView tvSzyh;

    @Bind(R.id.tv_szzh)
    TextView tvSzzh;
    @Bind(R.id.ll_szzh)
    LinearLayout llSzzh;
    @Bind(R.id.tv_khsy)
    TextView tvKhsy;
    @Bind(R.id.ll_khsy)
    LinearLayout llKhsy;
    @Bind(R.id.et_phone)
    EditText etPhone;
    @Bind(R.id.tv_ktyw)
    TextView tvKtyw;
    @Bind(R.id.ll_kkyh)
    LinearLayout llKkyh;
    @Bind(R.id.tv_submit)
    RoundTextView tvSubmit;
    String bankCode = "";
    @Bind(R.id.ll_zhlx)
    LinearLayout llZhlx;
    @Bind(R.id.et_jskh)
    EditText etJskh;
    @Bind(R.id.ll_szyh)
    LinearLayout llSzyh;
    private OptionsPickerView pvOptions;
    String zhLxType = "";
    private PayBankListBean payBankListBean;
    private String province = "";
    private String city = "";

    @Override
    public int getLayoutId() {
        return R.layout.activity_close_an_account;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        initZhlx();
    }

    @Override
    public void fillData() {

    }

    @Override
    public void setListener() {

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && requestCode == BankListActivity.REQUESTCODE_YH) {
            //银行
            bankCode = data.getStringExtra("bankCode");
            tvSzyh.setText(data.getStringExtra("yhName"));
        } else if (data != null && requestCode == SubBranchActivity.REQUESTCODE_ZH) {
            //支行
            tvSzzh.setText(data.getStringExtra("zhName"));
        } else if (data != null && requestCode == CityListActivity.REQUESTCODE_CITY) {
            //选择城市
            province = data.getStringExtra("province");
            city = data.getStringExtra("city");
            tvKhsy.setText(province + "-" + city);
        } else if (data != null && requestCode == StartServiceActivity.REQUESTCODE_YW) {
            //开通业务
            payBankListBean = (PayBankListBean) data.getSerializableExtra("payBankListBean");
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < payBankListBean.getData().size(); i++) {
                if (payBankListBean.getData().get(i).isChoice()) {
                    stringBuilder.append(payBankListBean.getData().get(i).getName()).append(" ");
                }
            }
            tvKtyw.setText(stringBuilder.toString());
        }
    }

    @OnClick({R.id.ll_zhlx, R.id.tv_start_time, R.id.tv_end_time, R.id.ll_szyh, R.id.ll_szzh, R.id.ll_khsy, R.id.ll_kkyh, R.id.tv_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_zhlx:
                //账号类型
                if (pvOptions != null) {
                    pvOptions.show();
                }
                break;
            case R.id.tv_start_time:
            case R.id.tv_end_time:
                chooseTime((TextView) view);
                break;
            case R.id.ll_szyh:
                //所在银行
                BankListActivity.actionStart(mActivity, BankListActivity.REQUESTCODE_YH);
                break;
            case R.id.ll_szzh:
                //所在支行
                if (TextUtils.isEmpty(bankCode)) {
                    showShortToast("请先选择银行");
                } else {
                    SubBranchActivity.actionStart(mActivity, SubBranchActivity.REQUESTCODE_ZH, bankCode);
                }
                break;
            case R.id.ll_khsy:
                //开户省行
                CityListActivity.actionStart(mActivity);
                break;
            case R.id.ll_kkyh:
                //开通业务
                StartServiceActivity.actionStart(mActivity, payBankListBean);
                break;
            case R.id.tv_submit:
                submit();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        ConfirmDialog confirmDialog = ConfirmDialog.newInstance("返回后这个页面的信息将不保留，请确保您已经提交信息！");
        confirmDialog.setDialogListener(new ConfirmDialog.DialogListener() {
            @Override
            public void dialogClickListener() {
                CloseAnAccountActivity.super.onBackPressed();
            }
        });
        confirmDialog.show(getSupportFragmentManager(),"");
    }

    //提交
    private void submit() {
        if (TextUtils.isEmpty(zhLxType)) {
            showShortToast("请选择账号类型");
            return;
        }

        LoadingDialog.showDialogForLoading(mActivity);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sj_login_token", LoginUtil.getLoginToken());
        hashMap.put("accountType", zhLxType);
        hashMap.put("bankName", tvSzyh.getText().toString());
        hashMap.put("bankAccountNum", etJskh.getText().toString());
        hashMap.put("bankBranchName", tvSzzh.getText().toString());
        hashMap.put("bankAccountName", etJsr.getText().toString());
        hashMap.put("province", province);
        hashMap.put("city", city);
        hashMap.put("phone", etPhone.getText().toString());
        hashMap.put("settlerCertificateCode", etJsrSfz.getText().toString());
        hashMap.put("settlerCertificateStartDate",tvStartTime.getText().toString());
        hashMap.put("settlerCertificateEndDate",tvEndTime.getText().toString());
        if (payBankListBean!=null){
            hashMap.put("payBankList", JSONArray.toJSON(payBankListBean.getData()));
        }

        new MyHttp().doPost(Api.getDefault().add_customerinfo_declare(hashMap), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                showShortToast("设置成功");
                startActivity(new Intent(mContext, JdShopInfoActivity.class));
            }

            @Override
            public void onError(int code) {
            }
        });
    }

    //选择商户类型
    private void initZhlx() {
        final List<String> strings = new ArrayList<>();
        strings.add("法人");
        strings.add("非法人");
        pvOptions = new OptionsPickerView(new OptionsPickerView.Builder(mContext, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                tvZhlx.setText(strings.get(options1));
                switch (options1) {
                    case 0:
                        zhLxType = "PRIVATE";
                        break;
                    case 1:
                        zhLxType = "PRIVATE";
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

}