package com.wbx.merchant.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.wbx.merchant.R;
import com.wbx.merchant.adapter.PrinterGoodsClassifyAdapter;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.bean.OrderReceiverBean;
import com.wbx.merchant.bean.PrinterGoodsCateBean;
import com.wbx.merchant.widget.LoadingDialog;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by wushenghui on 2017/11/21.
 * 设置打印机
 */

public class SettingsPrinterActivity extends BaseActivity {
    @Bind(R.id.tv_delete)
    TextView tvDelete;
    @Bind(R.id.et_machine_code)
    EditText etMachineCode;
    @Bind(R.id.et_m_key)
    EditText etMKey;
    @Bind(R.id.et_num)
    EditText etNum;
    @Bind(R.id.title_name_tv)
    TextView titleNameTv;
    @Bind(R.id.et_printer_name)
    EditText etPrinterName;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.ll_select_cate)
    LinearLayout llSelectCate;
    private OrderReceiverBean data;
    private boolean isMainReceiver;
    private boolean isEdit;
    private PrinterGoodsClassifyAdapter adapter;
    private List<PrinterGoodsCateBean> lstCate;

    public static void actionStart(Context context, OrderReceiverBean orderRewardBean, boolean isMainReceiver) {
        Intent intent = new Intent(context, SettingsPrinterActivity.class);
        intent.putExtra("data", orderRewardBean);
        intent.putExtra("isMainReceiver", isMainReceiver);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_settings_printer;
    }

    @Override
    public void initPresenter() {
    }

    @Override
    public void initView() {
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new PrinterGoodsClassifyAdapter(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void fillData() {
        data = (OrderReceiverBean) getIntent().getSerializableExtra("data");
        isMainReceiver = getIntent().getBooleanExtra("isMainReceiver", true);
        if (isMainReceiver) {
            titleNameTv.setText("接单器设置");
            ((View) etPrinterName.getParent()).setVisibility(View.GONE);
            llSelectCate.setVisibility(View.GONE);
        } else {
            titleNameTv.setText("副接单器设置");
            etPrinterName.setText(data.getPrint_name());
            llSelectCate.setVisibility(View.VISIBLE);
        }
        isEdit = !TextUtils.isEmpty(data.getMachine_code()) && !TextUtils.isEmpty(data.getMKey());
        if (isEdit) {
            tvDelete.setVisibility(View.VISIBLE);
        } else {
            tvDelete.setVisibility(View.INVISIBLE);
        }
        etMachineCode.setText(data.getMachine_code());
        etMKey.setText(data.getMKey());
        etNum.setText(String.valueOf(data.getPrint_num()));
        if (!isMainReceiver) {
            getClassify();
        }
    }

    private void getClassify() {
        LoadingDialog.showDialogForLoading(mActivity, "加载中...", true);
        new MyHttp().doPost(Api.getDefault().getPrintGoodsCate(userInfo.getSj_login_token(), data.getPrint_id()), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                lstCate = JSONArray.parseArray(result.getString("data"), PrinterGoodsCateBean.class);
                adapter.update(lstCate);
            }

            @Override
            public void onError(int code) {
            }
        });
    }

    @Override
    public void setListener() {

    }

    @OnClick({R.id.rl_delete, R.id.btn_complete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_delete:
                deletePrinter();
                break;
            case R.id.btn_complete:
                complete();
                break;
        }
    }

    private void deletePrinter() {
        if (isMainReceiver) {
            new MyHttp().doPost(Api.getDefault().deletePrinter(userInfo.getSj_login_token()), new HttpListener() {
                @Override
                public void onSuccess(JSONObject result) {
                    showShortToast("删除成功");
                    finish();
                }

                @Override
                public void onError(int code) {

                }
            });
        } else {
            new MyHttp().doPost(Api.getDefault().deleteAssistantPrinter(userInfo.getSj_login_token(), data.getPrint_id()), new HttpListener() {
                @Override
                public void onSuccess(JSONObject result) {
                    showShortToast("删除成功");
                    finish();
                }

                @Override
                public void onError(int code) {

                }
            });
        }
    }

    private void complete() {
        if (TextUtils.isEmpty(etMachineCode.getText().toString())) {
            showShortToast("请输入打印机终端号");
            return;
        }
        if (TextUtils.isEmpty(etMKey.getText().toString())) {
            showShortToast("请输入密钥");
            return;
        }
        if (TextUtils.isEmpty(etNum.getText().toString())) {
            showShortToast("请输入打印联数");
            return;
        }
        if (Integer.parseInt(etNum.getText().toString()) == 0) {
            showShortToast("打印联数不能为0");
            return;
        }
        LoadingDialog.showDialogForLoading(this, "保存中...", true);
        new MyHttp().doPost(Api.getDefault().addPrinter(isMainReceiver ? "/sjapi/user/add_print" : "/sjapi/user/add_assistant_print", isEdit ? data.getPrint_id() : "", userInfo.getSj_login_token(), data.getApiKey(), etMKey.getText().toString(),
                data.getPartner(), etMachineCode.getText().toString(), etNum.getText().toString(), etPrinterName.getText().toString(), isEdit ? 1 : 0, lstCate == null ? "" : new Gson().toJson(lstCate)), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                if (isEdit) {
                    showShortToast("修改成功");
                } else {
                    showShortToast("添加成功");
                }
                finish();
            }

            @Override
            public void onError(int code) {

            }
        });
    }
}