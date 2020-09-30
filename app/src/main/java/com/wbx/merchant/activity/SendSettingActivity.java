package com.wbx.merchant.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.wbx.merchant.R;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.bean.SenDSettingBean;
import com.wbx.merchant.bean.ShopDetailInfo;
import com.wbx.merchant.common.LoginUtil;
import com.wbx.merchant.utils.ToastUitl;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SendSettingActivity extends BaseActivity {

    @Bind(R.id.rl_right)
    RelativeLayout rlRight;
    @Bind(R.id.et_delivery_scope)
    EditText etDeliveryScope;
    @Bind(R.id.et_since_money)
    EditText etSinceMoney;
    @Bind(R.id.et_logistics)
    EditText etLogistics;
    @Bind(R.id.view_bg)
    View viewBg;
    @Bind(R.id.textView2)
    TextView textView2;
    @Bind(R.id.et_full_minus_shipping_fee)
    EditText etFullMinusShippingFee;
    @Bind(R.id.textView3)
    TextView textView3;
    @Bind(R.id.textView4)
    TextView textView4;
    @Bind(R.id.tv_open)
    TextView tvOpen;
    @Bind(R.id.tv_close)
    TextView tvClose;
    @Bind(R.id.delivery_btn)
    Button deliveryBtn;
    private int flag; //判断是否开启功能

    public static void actionStart(Activity context, ShopDetailInfo shopDetailInfo) {
        Intent intent = new Intent(context, SendSettingActivity.class);
        intent.putExtra("shopDetailInfo", shopDetailInfo);
        context.startActivityForResult(intent, StoreManagerActivity.REQUEST_UPDATE_SEND_SETTING);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_send_setting;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {

    }

    private void setDrawable(TextView view, int id) {
        Drawable dra = getResources().getDrawable(id);
        dra.setBounds(0, 0, dra.getMinimumWidth(), dra.getMinimumHeight());
        view.setCompoundDrawables(dra, null, null, null);
    }

    @Override
    public void fillData() {
        new MyHttp().doPost(Api.getDefault().get_delivery_manage(LoginUtil.getLoginToken()), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                SenDSettingBean dSettingBean = new Gson().fromJson(result.toString(), SenDSettingBean.class);
                setData(dSettingBean);
            }

            @Override
            public void onError(int code) {

            }
        });

    }

    private void setData(SenDSettingBean dSettingBean) {
        if (dSettingBean.getData().getDelivery_scope()>0){
            etDeliveryScope.setText(String.valueOf(dSettingBean.getData().getDelivery_scope()));
        }
        etLogistics.setText(String.valueOf(dSettingBean.getData().getLogistics()/100.00));
        etFullMinusShippingFee.setText(String.valueOf(dSettingBean.getData().getFull_minus_shipping_fee()/100.00));
        etSinceMoney.setText(String.valueOf(dSettingBean.getData().getSince_money()/100.00));

        if (dSettingBean.getData().getIs_full_minus_shipping_fee()==1){
            flag = 1;
            etFullMinusShippingFee.setEnabled(true);
            setDrawable(tvOpen, R.drawable.ic_ok);
            setDrawable(tvClose, R.drawable.ic_round);
        }else {
            flag = 0;
            etFullMinusShippingFee.setEnabled(false);
            setDrawable(tvOpen, R.drawable.ic_round);
            setDrawable(tvClose, R.drawable.ic_ok);
        }
    }

    @Override
    public void setListener() {

    }


    @OnClick({R.id.tv_open, R.id.tv_close, R.id.delivery_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_open:
                flag = 1;
                etFullMinusShippingFee.setEnabled(true);
                setDrawable(tvOpen, R.drawable.ic_ok);
                setDrawable(tvClose, R.drawable.ic_round);
                break;
            case R.id.tv_close:
                flag = 0;
                etFullMinusShippingFee.setEnabled(false);
                setDrawable(tvOpen, R.drawable.ic_round);
                setDrawable(tvClose, R.drawable.ic_ok);
                break;
            case R.id.delivery_btn:
                if (flag == 1 && TextUtils.isEmpty(etFullMinusShippingFee.getText().toString())) {
                    ToastUitl.showShort("请输入金额");
                    return;
                }
                 sumbit();
                break;
        }
    }

    private void sumbit() {
        HashMap<String,Object> hashMap =new HashMap<>();
        hashMap.put("sj_login_token",LoginUtil.getLoginToken());
        hashMap.put("since_money", etSinceMoney.getText().toString());
        hashMap.put("logistics", etLogistics.getText().toString());
        hashMap.put("delivery_scope", etDeliveryScope.getText().toString());
        hashMap.put("full_minus_shipping_fee", etFullMinusShippingFee.getText().toString());
        hashMap.put("is_full_minus_shipping_fee", flag);
        new MyHttp().doPost(Api.getDefault().update_delivery_manage(hashMap), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                showShortToast("设置成功");
            }

            @Override
            public void onError(int code) {

            }
        });
    }

}
