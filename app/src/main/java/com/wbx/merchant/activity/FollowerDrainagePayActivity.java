package com.wbx.merchant.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.flyco.roundview.RoundTextView;
import com.google.gson.Gson;
import com.wbx.merchant.R;
import com.wbx.merchant.adapter.CriclePayAdapter;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.baseapp.AppConfig;
import com.wbx.merchant.bean.CircleFansMoneyBean;
import com.wbx.merchant.bean.CricePayBaen;
import com.wbx.merchant.bean.WxPayInfo;
import com.wbx.merchant.common.LoginUtil;
import com.wbx.merchant.utils.MD5;
import com.wbx.merchant.utils.PayUtils;
import com.wbx.merchant.widget.LoadingDialog;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

//圈粉引流支付
public class FollowerDrainagePayActivity extends BaseActivity {

    @Bind(R.id.title_name_tv)
    TextView titleNameTv;

    @Bind(R.id.ali_pay_im)
    ImageView aliPayIm;
    @Bind(R.id.ali_pay_layout)
    LinearLayout aliPayLayout;
    @Bind(R.id.wx_pay_im)
    ImageView wxPayIm;
    @Bind(R.id.wx_pay_layout)
    LinearLayout wxPayLayout;
    @Bind(R.id.tv_zf)
    RoundTextView tvZf;
    @Bind(R.id.rv_price)
    RecyclerView rvPrice;
    @Bind(R.id.tv_sm)
    TextView tvSm;
    @Bind(R.id.tv_balance)
    TextView tvBalance;
    @Bind(R.id.balance_pay_im)
    ImageView balancePayIm;
    @Bind(R.id.balance_pay_layout)
    LinearLayout balancePayLayout;
    private String payPrice = "100";
    private String payMode;
    CriclePayAdapter criclePayAdapter;
    private PayUtils payUtils;
    String discover_id;
    private AlertDialog dialog;

    public static void actionStart(Context context, String discover_id) {
        Intent intent = new Intent(context, FollowerDrainagePayActivity.class);
        intent.putExtra("discover_id", discover_id);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_follower_drainage_pay;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        discover_id = getIntent().getStringExtra("discover_id");

        rvPrice.setLayoutManager(new GridLayoutManager(mContext, 4));
        criclePayAdapter = new CriclePayAdapter();
        rvPrice.setAdapter(criclePayAdapter);
        criclePayAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                criclePayAdapter.Select(position);
                payPrice = criclePayAdapter.getData().get(position).price;
                tvZf.setText("确认支付（" + payPrice + "元）");
                String dzSum  = "";
                String renSum  = "";
                switch (payPrice){
                    case "100":
                        renSum = "300";
                        dzSum = "10-50";
                        break;
                    case "200":
                        renSum = "600";
                        dzSum = "50-100";
                        break;
                    case "300":
                        renSum = "1000";
                        dzSum = "100-150";
                        break;
                    case "500":
                        renSum = "1500";
                        dzSum = "200-250";
                        break;
                }
                tvSm.setText("3、优先推荐给用户并促进"+dzSum+"单商品交易；\n4、预估可精准推送"+renSum+"人；");
            }
        });

        ArrayList<CricePayBaen> arrayList = new ArrayList<>();
        arrayList.add(new CricePayBaen("100", true));
        arrayList.add(new CricePayBaen("200", false));
        arrayList.add(new CricePayBaen("300", false));
        arrayList.add(new CricePayBaen("500", false));
        criclePayAdapter.setNewData(arrayList);

        payUtils = new PayUtils(mActivity, new PayUtils.OnAlipayListener() {
            @Override
            public void onSuccess() {
                finish();
                showShortToast("发布成功！");
            }
        });


    }

    @Override
    public void fillData() {
        getQfprice();
    }

    @Override
    public void setListener() {

    }

    private void getQfprice(){
        new MyHttp().doPost(Api.getDefault().get_draw_fans_money(LoginUtil.getLoginToken()), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                CircleFansMoneyBean circleFansMoneyBean = new Gson().fromJson(result.toString(), CircleFansMoneyBean.class);
                tvBalance.setText("剩余"+circleFansMoneyBean.getData().getDraw_fans_money()/100.00 +"元");
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    @OnClick({R.id.ali_pay_layout, R.id.wx_pay_layout,R.id.balance_pay_layout, R.id.tv_zf})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ali_pay_layout:
                payMode = AppConfig.PayMode.alipay;
                aliPayIm.setImageResource(R.drawable.ic_ok);
                wxPayIm.setImageResource(R.drawable.ic_round);
                balancePayIm.setImageResource(R.drawable.ic_round);
                break;
            case R.id.wx_pay_layout:
                payMode = AppConfig.PayMode.wxpay;
                aliPayIm.setImageResource(R.drawable.ic_round);
                wxPayIm.setImageResource(R.drawable.ic_ok);
                balancePayIm.setImageResource(R.drawable.ic_round);
                break;
            case R.id.balance_pay_layout:
                payMode = AppConfig.PayMode.draw_fans_money;
                aliPayIm.setImageResource(R.drawable.ic_round);
                wxPayIm.setImageResource(R.drawable.ic_round);
                balancePayIm.setImageResource(R.drawable.ic_ok);
                break;
            case R.id.tv_zf:
                if (TextUtils.isEmpty(payMode)) {
                    showShortToast("请选择支付方式");
                    return;
                }

                if (payMode.equals(AppConfig.PayMode.draw_fans_money)){
                    showInputPswDialog();
                }else {
                    pay("");
                }
                break;
        }
    }

    //弹出输入密码的dialog
    private void showInputPswDialog() {
        View inflate = getLayoutInflater().inflate(R.layout.balance_pay_view, null);
        if (null == dialog) {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mContext);
            builder.setView(inflate);
            dialog = builder.create();
        }
        dialog.show();
        final EditText payEdit = inflate.findViewById(R.id.pay_balance_edit);
        inflate.findViewById(R.id.cancel_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        inflate.findViewById(R.id.sure_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String payPsw = payEdit.getText().toString();
                if ("".equals(payPsw)) {
                    showShortToast("请填写支付密码");
                } else {
                    dialog.dismiss();
                    String md5PayPsw = "";
                    try {
                        md5PayPsw = MD5.EncoderByMd5(MD5.EncoderByMd5(payPsw));
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    pay(md5PayPsw);
                }

            }
        });
    }

    private void pay(String pay_password) {
        LoadingDialog.showDialogForLoading(mActivity);
        new MyHttp().doPost(Api.getDefault().pay(LoginUtil.getLoginToken(), payMode, payPrice,discover_id,pay_password), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                if (payMode.equals(AppConfig.PayMode.alipay)) {
                    payUtils.startAliPay(result.getString("data"));
                } else if (payMode.equals(AppConfig.PayMode.wxpay)){
                    WxPayInfo wxPayInfo = result.getObject("data", WxPayInfo.class);
                    payUtils.startWxPay(wxPayInfo);
                }else {
                    showShortToast("发布成功");
                    finish();
                }
            }

            @Override
            public void onError(int code) {
                if (code == AppConfig.ERROR_STATE.NULL_PAY_PSW) {
                    startActivity(new Intent(mContext, ResetPayPswActivity.class));
                }
            }
        });
    }

}