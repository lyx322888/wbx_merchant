package com.wbx.merchant.activity;

import android.app.Dialog;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.wbx.merchant.R;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.baseapp.AppConfig;
import com.wbx.merchant.utils.MD5;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by wushenghui on 2017/6/20.
 * 商家提现
 */
public class CashActivity extends BaseActivity {
    public static final String TYPE_DA_DA = "dada_money";
    public static final String TYPE_SHOP = "shop";
    public static final String TYPE_REWARD = "shop_subsidy_money";
    public static final int CHOOSE_CASH_TYPE = 1000;
    private final int PRICE_LENGTH = 9;
    @Bind(R.id.scroll_view)
    ScrollView scrollView;
    @Bind(R.id.title_name_tv)
    TextView titleNameTv;
    @Bind(R.id.money_hint_tv)
    TextView moneyHintTv;
    @Bind(R.id.submit_btn)
    Button submitBtn;
    @Bind(R.id.show_user_money_tv)
    TextView showMoneyTv;
    @Bind(R.id.input_money_edit)
    EditText inputMoneyEdit;
    private JSONObject data;
    @Bind(R.id.pay_hint_tv)
    TextView payHintTv;
    @Bind(R.id.pay_mode_im)
    ImageView payModeIm;
    @Bind(R.id.pay_name_tv)
    TextView payNameTv;
    public static String cashType = "";
    private Dialog dialog;
    private HashMap<String, Object> mParams = new HashMap<>();
    private String type;
    private DecimalFormat format;

    @Override
    public int getLayoutId() {
        return R.layout.activity_cash;
    }

    @Override
    public void initPresenter() {
        inputMoneyEdit.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                //判断第一位输入是否是“.”
                if (s.toString().startsWith(".")) {
                    s = "0" + s;
                    inputMoneyEdit.setText(s);
                    if (s.toString().length() == 2) {
                        inputMoneyEdit.setSelection(s.length());
                    }
                    return;
                }
                //判断首位是否是“0”
                if (s.toString().startsWith("0") && s.toString().length() > 1) {
                    //判断第二位不是“.”
                    if (!s.toString().substring(1, 2).equals(".")) {

                        s = s.toString().substring(1);
                        inputMoneyEdit.setText(s);
                        inputMoneyEdit.setSelection(s.length());
                        return;
                    }
                }


                //判断小数点后两位
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0, s.toString().indexOf(".") + 3);
                        inputMoneyEdit.setText(s);
                        inputMoneyEdit.setSelection(s.length());
                        return;
                    }
                }

                //判断输入位数
                if (s.toString().length() > PRICE_LENGTH) {
                    if (s.toString().contains(".")) {
                        if (s.toString().indexOf(".") > PRICE_LENGTH) {
                            inputMoneyEdit.setText("");
                            inputMoneyEdit.setSelection(0);
                            return;
                        }
                    } else if (s.toString().length() == PRICE_LENGTH + 1) {
                        if (start + 1 == s.toString().length()) {
                            s = s.toString().subSequence(0, PRICE_LENGTH);
                            inputMoneyEdit.setText(s);
                            inputMoneyEdit.setSelection(s.length());
                            return;
                        } else {
                            inputMoneyEdit.setText("");
                            inputMoneyEdit.setSelection(0);
                            return;
                        }
                    } else if (s.toString().length() > PRICE_LENGTH + 1) {
                        inputMoneyEdit.setText("");
                        inputMoneyEdit.setSelection(0);
                        return;
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                showHint();
            }

        });
    }

    private void showHint() {
        String money = inputMoneyEdit.getText().toString();
        if (TextUtils.isEmpty(money)) {
            money = "0";
        }
        Date time = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date startDate = simpleDateFormat.parse("2018-05-01");
            Date endDate = simpleDateFormat.parse("2018-08-01");
            if (time.getTime() >= startDate.getTime() && time.getTime() <= endDate.getTime()) {
                //免手续费
                showMoneyTv.setText("实际提现" + format.format(Float.valueOf(money)) + "元（推广期间无需手续费）");
            } else {
                float cash_commission = getIntent().getFloatExtra("cash_commission", 3);//手续费
                double commission = Float.valueOf(money) * cash_commission * 0.01;
                showMoneyTv.setText("实际提现" + format.format(Float.valueOf(money) - commission) + "元（手续费¥" + format.format(commission) + "元/费率" + cash_commission + "%）");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initView() {
    }

    @Override
    public void fillData() {
        format = new DecimalFormat("#0.00");
        type = getIntent().getStringExtra("type");
        //获取绑定账户
        cashType = AppConfig.CashCode.alipay;
        getBindPayInfo();
        if (TYPE_REWARD.equals(type)) {
            inputMoneyEdit.setText(String.format("%.2f", (float) getIntent().getIntExtra("balance", 1) / 100.00));
        }
        showMoneyTv.setText(String.format("可提现余额%.2f元", getIntent().getIntExtra("balance", 1) / 100.00));
    }

    private void getBindPayInfo() {
        new MyHttp().doPost(Api.getDefault().getBindPayInfo(userInfo.getSj_login_token()), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                data = result.getJSONObject("data");
                if (data != null) {
                    JSONObject userAliPay = data.getJSONObject("user_alipay");
                    JSONObject userWeiXinPay = data.getJSONObject("user_weixinpay");
                    JSONObject userBank = data.getJSONObject("user_bank");
                    if (userAliPay == null && userWeiXinPay == null && userBank == null) {
                        showShortToast("请先绑定提现账户");
                        startActivity(new Intent(mContext, BindAccountActivity.class));
                        finish();
                    } else {
                        if (userAliPay != null && userAliPay.getIntValue("is_default") == 1) {
                            cashType = AppConfig.CashCode.alipay;
                            payModeIm.setImageResource(R.drawable.icon_pay_ali);
                            payNameTv.setText("支付宝");
                            payHintTv.setText(userAliPay.getString("depict"));
                        } else if (userWeiXinPay != null && userWeiXinPay.getIntValue("is_default") == 1) {
                            cashType = AppConfig.CashCode.wxpay;
                            payModeIm.setImageResource(R.drawable.icon_pay_weixin);
                            payNameTv.setText("微信");
                            payHintTv.setText(userWeiXinPay.getString("depict"));
                        } else if (userBank != null && userBank.getIntValue("is_default") == 1) {
                            cashType = AppConfig.CashCode.bank;
                            payModeIm.setImageResource(R.drawable.icon_unionpay);
                            payNameTv.setText(userBank.getString("bank_name"));
                            payHintTv.setText(userBank.getString("depict"));
                        }
                    }
                } else {
                    switch (cashType) {
                        case AppConfig.PayMode.alipay:
                            payModeIm.setImageResource(R.drawable.icon_pay_ali);
                            payNameTv.setText("支付宝");
                            payHintTv.setText("推荐已安装支付宝客户端的用户使用");
                            break;
                        case AppConfig.PayMode.wxpay:
                            payModeIm.setImageResource(R.drawable.icon_pay_weixin);
                            payNameTv.setText("微信");
                            payHintTv.setText("推荐已安装微信客户端的用户使用");
                            break;
                    }
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

    @OnClick({R.id.choose_third_layout, R.id.submit_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.choose_third_layout:
                if (data != null) {
                    Intent intent = new Intent(mContext, ChooseFinanceWayActivity.class);
                    intent.putExtra("data", data.toJSONString());
                    startActivityForResult(intent, CHOOSE_CASH_TYPE);
                }
                break;
            case R.id.submit_btn:
                if (TextUtils.isEmpty(inputMoneyEdit.getText().toString())) {
                    showShortToast("请输入提现金额");
                    return;
                }
                showInputPswDialog();
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
                    applyCash(md5PayPsw);
                }

            }
        });
    }

    //申请提现
    private void applyCash(String md5PayPsw) {
        mParams.put("sj_login_token", userInfo.getSj_login_token());
        mParams.put("money", inputMoneyEdit.getText().toString());
        mParams.put("pay_password", md5PayPsw);
        mParams.put("cash_type", type);
        mParams.put("pay_code", cashType);
        new MyHttp().doPost(Api.getDefault().applyCash(mParams), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                showShortToast(result.getString("msg"));
                finish();
            }

            @Override
            public void onError(int code) {
                if (code == AppConfig.ERROR_STATE.NULL_PAY_PSW) {
                    startActivity(new Intent(mContext, ResetPayPswActivity.class));
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == CHOOSE_CASH_TYPE) {
            updateCashType();
        }
    }

    private void updateCashType() {
        if (cashType == AppConfig.CashCode.alipay) {
            JSONObject userAliPay = data.getJSONObject("user_alipay");
            payModeIm.setImageResource(R.drawable.icon_pay_ali);
            payNameTv.setText("支付宝");
            payHintTv.setText(userAliPay.getString("depict"));
        } else if (cashType == AppConfig.CashCode.wxpay) {
            JSONObject userWeiXinPay = data.getJSONObject("user_weixinpay");
            payModeIm.setImageResource(R.drawable.icon_pay_weixin);
            payNameTv.setText("微信");
            payHintTv.setText(userWeiXinPay.getString("depict"));
        } else if (cashType == AppConfig.CashCode.bank) {
            JSONObject userBank = data.getJSONObject("user_bank");
            payModeIm.setImageResource(R.drawable.icon_unionpay);
            payNameTv.setText(userBank.getString("bank_name"));
            payHintTv.setText(userBank.getString("depict"));
        }
    }
}
