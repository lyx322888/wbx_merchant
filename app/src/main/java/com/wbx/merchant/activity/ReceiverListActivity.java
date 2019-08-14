package com.wbx.merchant.activity;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.wbx.merchant.R;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.bean.OrderReceiverBean;
import com.wbx.merchant.bean.OrderReceiverListBean;
import com.wbx.merchant.bean.PrinterBrandBean;
import com.wbx.merchant.dialog.PrinterDialog;
import com.wbx.merchant.utils.GlideUtils;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 接单器设置(打印机)
 */
public class ReceiverListActivity extends BaseActivity {
    @Bind(R.id.iv_receiver_main)
    ImageView ivMainReceiver;
    @Bind(R.id.iv_add_assistant_receiver)
    ImageView ivAddAssistantReceiver;
    @Bind(R.id.ll_container)
    LinearLayout llContainer;
    private OrderReceiverListBean data;
    private PrinterDialog dialog;
    private int mIsZ;//是否有主打印机

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
            GlideUtils.showRoundMediumPic(mContext, ivMainReceiver, data.getPrint_z_logo());
            mIsZ = 1;
        } else if (!TextUtils.isEmpty(data.getFe_sn()) && !TextUtils.isEmpty(data.getFe_key())) {
            GlideUtils.showRoundMediumPic(mContext, ivMainReceiver, data.getPrint_z_logo());
            mIsZ = 1;
        } else {
            ivMainReceiver.setImageResource(R.drawable.receiver_empty_1);
            mIsZ = 0;
        }
        llContainer.removeAllViews();
        if (data.getAssistant_print() != null && data.getAssistant_print().size() > 0) {
            for (final OrderReceiverBean orderReceiverBean : data.getAssistant_print()) {
                View inflate = LayoutInflater.from(this).inflate(R.layout.item_assistant_receiver, null);
                if (!TextUtils.isEmpty(orderReceiverBean.getPrint_name())){
                    ((TextView) inflate.findViewById(R.id.tv_name)).setText("(" + orderReceiverBean.getPrint_name() + ")");
                }
                ImageView logo = inflate.findViewById(R.id.iv_receiver);
                GlideUtils.showRoundMediumPic(mContext, logo, orderReceiverBean.getPrint_f_logo());
                inflate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        orderReceiverBean.setIs_edit(1);
                        orderReceiverBean.setLogo(orderReceiverBean.getPrint_brand_logo());
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
        switch (view.getId()) {
            case R.id.iv_receiver_main:
                if (mIsZ == 0) {
                    if (dialog == null) {
                        dialog = new PrinterDialog(this);
                    }
                    dialog.setDialogListener(new PrinterDialog.DialogListener() {
                        @Override
                        public void dialogClickListener(PrinterBrandBean.PrinterBean bean) {
                            OrderReceiverBean orderReceiverBean = new OrderReceiverBean();
                            orderReceiverBean.setPrint_brand(bean.getShop_brand());
                            orderReceiverBean.setIs_edit(0);
                            orderReceiverBean.setApiKey(data.getApiKey());
                            orderReceiverBean.setPartner(data.getPartner());
                            orderReceiverBean.setLogo(bean.getShop_brand_logo());
                            orderReceiverBean.setPrint_num(data.getPrint_num());
                            SettingsPrinterActivity.actionStart(ReceiverListActivity.this, orderReceiverBean, true);
                        }
                    });
                    dialog.show();
                } else {
                    OrderReceiverBean orderReceiverBean = new OrderReceiverBean();
                    orderReceiverBean.setPrint_brand(data.getPrint_brand());
                    orderReceiverBean.setIs_edit(1);
                    orderReceiverBean.setApiKey(data.getApiKey());
                    orderReceiverBean.setFe_sn(data.getFe_sn());
                    orderReceiverBean.setFe_key(data.getFe_key());
                    orderReceiverBean.setMKey(data.getMKey());
                    orderReceiverBean.setMachine_code(data.getMachine_code());
                    orderReceiverBean.setPartner(data.getPartner());
                    orderReceiverBean.setLogo(data.getPrint_brand_logo());
                    orderReceiverBean.setPrint_num(data.getPrint_num());
                    SettingsPrinterActivity.actionStart(ReceiverListActivity.this, orderReceiverBean, true);
                }
                break;
            case R.id.iv_add_assistant_receiver:
                if (dialog == null) {
                    dialog = new PrinterDialog(this);
                }
                dialog.setDialogListener(new PrinterDialog.DialogListener() {
                    @Override
                    public void dialogClickListener(PrinterBrandBean.PrinterBean bean) {
                        OrderReceiverBean orderReceiverBean = new OrderReceiverBean();
                        orderReceiverBean.setPrint_brand(bean.getShop_brand());
                        orderReceiverBean.setIs_edit(0);
                        orderReceiverBean.setApiKey(data.getApiKey());
                        orderReceiverBean.setPartner(data.getPartner());
                        orderReceiverBean.setLogo(bean.getShop_brand_logo());
                        orderReceiverBean.setPrint_num(data.getPrint_num());
                        SettingsPrinterActivity.actionStart(ReceiverListActivity.this, orderReceiverBean, false);
                    }
                });
                dialog.show();
                break;
            default:
                break;
        }
    }
}
