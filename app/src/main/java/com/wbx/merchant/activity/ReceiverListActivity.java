package com.wbx.merchant.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.tencent.bugly.crashreport.CrashReport;
import com.wbx.merchant.R;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.bean.OrderReceiverBean;
import com.wbx.merchant.bean.OrderReceiverListBean;

import butterknife.Bind;
import butterknife.OnClick;

public class ReceiverListActivity extends BaseActivity {
    @Bind(R.id.iv_receiver_main)
    ImageView ivMainReceiver;
    @Bind(R.id.iv_add_assistant_receiver)
    ImageView ivAddAssistantReceiver;
    @Bind(R.id.ll_container)
    LinearLayout llContainer;
    private OrderReceiverListBean data;

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, ReceiverListActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_receiver_list;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        new MyHttp().doPost(Api.getDefault().getPrinter(userInfo.getSj_login_token()), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                data = result.getObject("data", OrderReceiverListBean.class);
                updateUI(data);
            }

            @Override
            public void onError(int code) {

            }
        });
    }


    @Override
    public void fillData() {
    }

    private void updateUI(final OrderReceiverListBean data) {
        if (!TextUtils.isEmpty(data.getMachine_code()) && !TextUtils.isEmpty(data.getMKey())) {
            ivMainReceiver.setImageResource(R.drawable.receiver_1);
        } else {
            ivMainReceiver.setImageResource(R.drawable.receiver_empty_1);
        }
        llContainer.removeAllViews();
        if (data.getAssistant_print() != null && data.getAssistant_print().size() > 0) {
            for (final OrderReceiverBean orderReceiverBean : data.getAssistant_print()) {
                View inflate = LayoutInflater.from(this).inflate(R.layout.item_assistant_receiver, null);
                if (!TextUtils.isEmpty(orderReceiverBean.getPrint_name())) {
                    ((TextView) inflate.findViewById(R.id.tv_name)).setText("(" + orderReceiverBean.getPrint_name() + ")");
                }
                inflate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SettingsPrinterActivity.actionStart(ReceiverListActivity.this, orderReceiverBean, false);
                    }
                });
                llContainer.addView(inflate);
            }
        }
        if (data.getAssistant_print() != null && data.getAssistant_print().size() >= 5) {
            ivAddAssistantReceiver.setVisibility(View.GONE);
        } else {
            ivAddAssistantReceiver.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setListener() {

    }

    @OnClick({R.id.iv_receiver_main, R.id.iv_add_assistant_receiver})
    public void onViewClicked(View view) {
        if (data == null) {
            return;
        }
        switch (view.getId()) {
            case R.id.iv_receiver_main:
                OrderReceiverBean orderReceiverBean = new OrderReceiverBean();
                orderReceiverBean.setApiKey(data.getApiKey());
                orderReceiverBean.setMachine_code(data.getMachine_code());
                orderReceiverBean.setMKey(data.getMKey());
                orderReceiverBean.setPartner(data.getPartner());
                orderReceiverBean.setPrint_id("");
                orderReceiverBean.setPrint_num(data.getPrint_num());
                SettingsPrinterActivity.actionStart(this, orderReceiverBean, true);
                break;
            case R.id.iv_add_assistant_receiver:
                OrderReceiverBean orderReceiverBean2 = new OrderReceiverBean();
                orderReceiverBean2.setApiKey(data.getApiKey());
                orderReceiverBean2.setMachine_code("");
                orderReceiverBean2.setMKey("");
                orderReceiverBean2.setPartner(data.getPartner());
                orderReceiverBean2.setPrint_id("");
                orderReceiverBean2.setPrint_num(2);
                orderReceiverBean2.setPrint_name("");
                SettingsPrinterActivity.actionStart(this, orderReceiverBean2, false);
                break;
            default:
                break;
        }
    }
}
