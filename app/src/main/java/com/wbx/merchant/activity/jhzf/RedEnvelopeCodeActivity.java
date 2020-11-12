package com.wbx.merchant.activity.jhzf;

import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;


import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.wbx.merchant.R;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.bean.RedPacketCodeBean;
import com.wbx.merchant.common.LoginUtil;
import com.wbx.merchant.widget.LoadingDialog;

import butterknife.Bind;
import butterknife.OnClick;

//红包码
public class RedEnvelopeCodeActivity extends BaseActivity {

    @Bind(R.id.title_name_tv)
    TextView titleNameTv;
    @Bind(R.id.tv_shop_name)
    TextView tvShopName;
    @Bind(R.id.et_hbm)
    EditText etHbm;
    @Bind(R.id.tv_submit)
    TextView tvSubmit;

    @Override
    public int getLayoutId() {
        return R.layout.activity_red_envelope_code;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {

    }

    @Override
    public void fillData() {
        new MyHttp().doPost(Api.getDefault().get_red_packet_code(LoginUtil.getLoginToken()), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                RedPacketCodeBean redPacketCodeBean = new Gson().fromJson(result.toString(),RedPacketCodeBean.class);
                tvShopName.setText(redPacketCodeBean.getData().getShop_name());
                if (TextUtils.isEmpty(redPacketCodeBean.getData().getRed_packet_code())){
                    tvSubmit.setEnabled(true);
                    tvSubmit.setText("提交绑定红包码");
                }else {
                    tvSubmit.setEnabled(false);
                    tvSubmit.setText("已绑定");
                    etHbm.setText(redPacketCodeBean.getData().getRed_packet_code());

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


    @OnClick(R.id.tv_submit)
    public void onViewClicked() {
        sumbit();
    }

    private void sumbit() {
        if (TextUtils.isEmpty(etHbm.getText())){
            showShortToast("请填写红包码");
            return;
        }
        LoadingDialog.showDialogForLoading(mActivity);
        new MyHttp().doPost(Api.getDefault().add_red_packet_code(LoginUtil.getLoginToken(), etHbm.getText().toString()), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                showShortToast("提交成功");
                fillData();
            }

            @Override
            public void onError(int code) {

            }
        });
    }
}