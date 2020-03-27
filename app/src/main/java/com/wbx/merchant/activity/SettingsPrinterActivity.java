package com.wbx.merchant.activity;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wbx.merchant.R;
import com.wbx.merchant.adapter.PrinterGoodsClassifyAdapter;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.bean.OrderReceiverBean;
import com.wbx.merchant.bean.PrinterGoodsCateBean;
import com.wbx.merchant.utils.GlideUtils;
import com.wbx.merchant.widget.LoadingDialog;

import java.util.HashMap;
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
    @Bind(R.id.tv_code_title)
    TextView tvCodeTitle;
    @Bind(R.id.tv_m_title)
    TextView tvMTitle;
    @Bind(R.id.title_name_tv)
    TextView titleNameTv;
    @Bind(R.id.et_printer_name)
    EditText etPrinterName;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.ll_select_cate)
    LinearLayout llSelectCate;
    @Bind(R.id.iv_logo)
    ImageView ivLogo;
    private OrderReceiverBean data;
    private boolean isMainReceiver;
    private boolean isEdit;
    private PrinterGoodsClassifyAdapter adapter;
    private List<PrinterGoodsCateBean> lstCate;
    private String mPrintBrand;//打印机品牌 1微百姓 2飞鹅 3易联云

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
        data = (OrderReceiverBean) getIntent().getSerializableExtra("data");
        isMainReceiver = getIntent().getBooleanExtra("isMainReceiver", true);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new PrinterGoodsClassifyAdapter(this);
        recyclerView.setAdapter(adapter);
        mPrintBrand = data.getPrint_brand();
        GlideUtils.showRoundMediumPic(mContext, ivLogo, data.getLogo());
        if (mPrintBrand.equals("2")) {
            tvCodeTitle.setText("打印机编号（SN）");
            tvMTitle.setText("打印机密钥（KEY）");
            etMachineCode.setHint("请输入打印机编号（SN）");
            etMKey.setHint("请输入打印机密钥（KEY）");
        }
    }

    @Override
    public void fillData() {
        if (isMainReceiver) {
            titleNameTv.setText("接单器设置");
            ((View) etPrinterName.getParent()).setVisibility(View.GONE);
            llSelectCate.setVisibility(View.GONE);
        } else {
            titleNameTv.setText("副接单器设置");
            etPrinterName.setText(data.getPrint_name());
            llSelectCate.setVisibility(View.VISIBLE);
        }
        isEdit = data.getIs_edit() == 1;
        if (isEdit) {
            tvDelete.setVisibility(View.VISIBLE);
        } else {
            tvDelete.setVisibility(View.INVISIBLE);
        }
        etMachineCode.setText(mPrintBrand.equals("2") ? data.getFe_sn() : data.getMachine_code());
        etMKey.setText(mPrintBrand.equals("2") ? data.getFe_key() : data.getMKey());
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

    private void OnNext() {
        String mCode = etMachineCode.getText().toString().trim();//打印机编号
        String mKey = etMKey.getText().toString().trim();//打印机key
        String printNum = etNum.getText().toString().trim();//打印联数
        String url = isMainReceiver ? "/sjapi/user/add_print" : "/sjapi/user/add_assistant_print";
        HashMap<String, Object> mParams = new HashMap<>();
        if (!isMainReceiver) {//副打印机
            mParams.put("cate", JSONArray.toJSON(lstCate));
            mParams.put("print_id", TextUtils.isEmpty(data.getPrint_id()) ? "0" : data.getPrint_id());
            mParams.put("print_name", etPrinterName.getText().toString().trim());
        }
        if (mPrintBrand.equals("2")) {//飞蛾打印机
            mParams.put("fe_sn", mCode);
            mParams.put("fe_key", mKey);
        } else {//微百姓/易连云
            mParams.put("apiKey", data.getApiKey());
            mParams.put("mKey", mKey);
            mParams.put("partner", data.getPartner());
            mParams.put("machine_code", mCode);
        }
        mParams.put("is_edit", isEdit ? "1" : "0");
        mParams.put("sj_login_token", userInfo.getSj_login_token());
        mParams.put("print_num", printNum);
        mParams.put("print_brand", mPrintBrand);
        new MyHttp().doPost(Api.getDefault().addPrinter(url, mParams), new HttpListener() {
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

    private void complete() {
        if (TextUtils.isEmpty(etMachineCode.getText().toString().trim())) {
            showShortToast(mPrintBrand.equals("2") ? "请输入打印机编号" : "请输入打印机终端号");
            return;
        }
        if (TextUtils.isEmpty(etMKey.getText().toString().trim())) {
            showShortToast(mPrintBrand.equals("2") ? "请输入打印机密钥" : "请输入密钥");
            return;
        }
        if (TextUtils.isEmpty(etNum.getText().toString().trim())) {
            showShortToast("请输入打印联数");
            return;
        }
        if (Integer.parseInt(etNum.getText().toString().trim()) == 0) {
            showShortToast("打印联数不能为0");
            return;
        }
        LoadingDialog.showDialogForLoading(this, "保存中...", true);
        OnNext();
    }

    private void deletePrinter() {
        if (isMainReceiver) {
            new MyHttp().doPost(Api.getDefault().deletePrinter(userInfo.getSj_login_token(), data.getPrint_brand()), new HttpListener() {
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
}