package com.wbx.merchant.activity;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.wbx.merchant.R;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.common.LoginUtil;
import com.wbx.merchant.utils.ToastUitl;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 满金额配送
 */
public class NoDeliveryFeeActivity extends BaseActivity {
    @Bind(R.id.money_edit)
    EditText moneyEdit;
    @Bind(R.id.tv_open)
    TextView tvOpen;
    @Bind(R.id.tv_close)
    TextView tvClose;
    @Bind(R.id.delivery_btn)
    Button deliveryBtn;
    private int flag; //判断是否开启功能

    @Override
    public int getLayoutId() {
        return R.layout.activity_no_delivery_fee;
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

    @OnClick({R.id.tv_close, R.id.tv_open, R.id.delivery_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_open:
                flag = 0;
                moneyEdit.setEnabled(true);
                setDrawable(tvOpen, R.drawable.ic_ok);
                setDrawable(tvClose, R.drawable.ic_round);
                break;
            case R.id.tv_close:
                flag = 1;
                moneyEdit.setEnabled(false);
                setDrawable(tvOpen, R.drawable.ic_round);
                setDrawable(tvClose, R.drawable.ic_ok);
                break;
            case R.id.delivery_btn:
                if (flag == 0 && TextUtils.isEmpty(moneyEdit.getText().toString())) {
                    ToastUitl.showShort("请输入金额");
                    return;
                }
                getNoDeliveryFee();
                break;
        }
    }

    @Override
    public void fillData() {
    }

    @Override
    public void setListener() {
    }

    /**
     * 1.40. 设置满多少免配送费
     */
    private void getNoDeliveryFee() {
        new MyHttp().doPost(Api.getDefault().getNoDeliveryFee(LoginUtil.getLoginToken(), flag, moneyEdit.getText().toString()), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                ToastUitl.showShort(result.getString("msg"));
            }

            @Override
            public void onError(int code) {
            }
        });
    }
}
