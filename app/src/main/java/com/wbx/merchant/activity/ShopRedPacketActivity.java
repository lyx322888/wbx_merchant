package com.wbx.merchant.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.example.popwindowutils.CustomPopWindow;
import com.wbx.merchant.R;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.baseapp.AppConfig;
import com.wbx.merchant.bean.LastRedPacketBean;
import com.wbx.merchant.utils.ToastUitl;
import com.wbx.merchant.widget.PriceEditText;

import butterknife.Bind;
import butterknife.OnClick;

public class ShopRedPacketActivity extends BaseActivity {
    @Bind(R.id.et_total_money)
    PriceEditText etTotalMoney;
    @Bind(R.id.et_start_money)
    PriceEditText etStartMoney;
    @Bind(R.id.et_end_money)
    PriceEditText etEndMoney;
    @Bind(R.id.tv_already_receive_money)
    TextView tvAlreadyReceiveMoney;
    @Bind(R.id.tv_surplus_money)
    TextView tvSurplusMoney;
    @Bind(R.id.tv_ensure)
    TextView tvEnsure;
    private MyHttp myHttp;
    private LastRedPacketBean data;

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, ShopRedPacketActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_shop_red_packet;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {

    }

    @Override
    public void fillData() {
        myHttp = new MyHttp();
        myHttp.doPost(Api.getDefault().getLastRedPacket(userInfo.getSj_login_token()), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                data = result.getObject("data", LastRedPacketBean.class);
                etTotalMoney.setText(data.getMoney());
                etStartMoney.setText(data.getMin_money());
                etEndMoney.setText(data.getMax_money());
                tvAlreadyReceiveMoney.setText(data.getUser_packet_money() + "元");
                tvSurplusMoney.setText(data.getSurplus_red_packet() + "元");
            }

            @Override
            public void onError(int code) {
                if (code == AppConfig.ERROR_STATE.NULLDATA) {
                    //未设置过红包
                    ((View) tvAlreadyReceiveMoney.getParent()).setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void setListener() {

    }

    @OnClick({R.id.rl_right, R.id.tv_ensure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_right:
                ShopRedPacketReceiveRecordActivity.actionStart(this);
                break;
            case R.id.tv_ensure:
                ensure();
                break;
        }
    }

    private void ensure() {
        if (TextUtils.isEmpty(etTotalMoney.getText().toString())) {
            ToastUitl.showShort("请输入红包总额");
            return;
        }
        if (TextUtils.isEmpty(etStartMoney.getText().toString()) || TextUtils.isEmpty(etEndMoney.getText().toString())) {
            ToastUitl.showShort("请输入红包范围");
            return;
        }
        if (Float.valueOf(etStartMoney.getText().toString()) == 0) {
            ToastUitl.showShort("红包需大于0");
            return;
        }
        if (Float.valueOf(etStartMoney.getText().toString()) >= Float.valueOf(etEndMoney.getText().toString())) {
            ToastUitl.showShort("请输入正确的红包范围");
            return;
        }
        if (Float.valueOf(etEndMoney.getText().toString()) > Float.valueOf(etTotalMoney.getText().toString())) {
            ToastUitl.showShort("单个红包不能大于总金额");
            return;
        }
        if (data != null && Float.valueOf(data.getSurplus_red_packet()) != 0) {
            myHttp.doPost(Api.getDefault().updateRedPacket(userInfo.getSj_login_token(), etTotalMoney.getText().toString(), etStartMoney.getText().toString()
                    , etEndMoney.getText().toString(), data.getRed_packet_id()), new HttpListener() {
                @Override
                public void onSuccess(JSONObject result) {
                    ToastUitl.showShort(result.getString("msg"));
                    finish();
                }

                @Override
                public void onError(int code) {
                    if (code==AppConfig.ERROR_STATE.JURISDICTION){
                        final View inflate = LayoutInflater.from(mContext).inflate(R.layout.pop_ljkd, null);
                        TextView tvContent = inflate.findViewById(R.id.tv_content);
                        TextView tvLjkd = inflate.findViewById(R.id.tv_ljkt);
                        tvContent.setText("开通特权享发红包");
                        CustomPopWindow customPopWindow = new CustomPopWindow.PopupWindowBuilder(mContext)
                                .enableBackgroundDark(true)
                                .setView(inflate)
                                .create()
                                .showAtLocation(inflate, Gravity.CENTER,0,0);
                        tvLjkd.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                startActivity(new Intent(mContext, ChooseShopVersionsPrwActivity.class));
                            }
                        });
                    }
                }
            });
        } else {
            myHttp.doPost(Api.getDefault().addRedPacket(userInfo.getSj_login_token(), etTotalMoney.getText().toString(), etStartMoney.getText().toString()
                    , etEndMoney.getText().toString()), new HttpListener() {
                @Override
                public void onSuccess(JSONObject result) {
                    ToastUitl.showShort(result.getString("msg"));
                    finish();
                }

                @Override
                public void onError(int code) {
                    if (code==AppConfig.ERROR_STATE.JURISDICTION){
                        final View inflate = LayoutInflater.from(mContext).inflate(R.layout.pop_ljkd, null);
                        TextView tvContent = inflate.findViewById(R.id.tv_content);
                        TextView tvLjkd = inflate.findViewById(R.id.tv_ljkt);
                        tvContent.setText("开通特权享发红包");
                        CustomPopWindow customPopWindow = new CustomPopWindow.PopupWindowBuilder(mContext)
                                .enableBackgroundDark(true)
                                .setView(inflate)
                                .create()
                                .showAtLocation(inflate, Gravity.CENTER,0,0);
                        tvLjkd.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                startActivity(new Intent(mContext, ChooseShopVersionsPrwActivity.class));
                            }
                        });
                    }

                }
            });
        }
    }
}
