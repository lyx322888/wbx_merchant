package com.wbx.merchant.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.alipay.sdk.app.PayTask;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wbx.merchant.R;
import com.wbx.merchant.adapter.ShopCartAdapter;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.baseapp.AppConfig;
import com.wbx.merchant.bean.OrderBean;
import com.wbx.merchant.bean.PayResult;
import com.wbx.merchant.bean.WxPayInfo;
import com.wbx.merchant.common.LoginUtil;
import com.wbx.merchant.presenter.OrderPresenterImp;
import com.wbx.merchant.view.OrderView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

public class ShopCartActivity extends BaseActivity implements OrderView {

    @Bind(R.id.et_name)
    EditText etName;
    @Bind(R.id.et_phone)
    EditText etPhone;
    @Bind(R.id.et_address)
    EditText etAddress;
    @Bind(R.id.updata_user)
    LinearLayout updataUser;
    @Bind(R.id.shop_name_edit)
    EditText shopNameEdit;
    @Bind(R.id.et_time)
    EditText etTime;
    @Bind(R.id.shop_phone_edit)
    EditText shopPhoneEdit;
    @Bind(R.id.shop_address_edit)
    EditText shopAddressEdit;
    @Bind(R.id.shop_scope_edit)
    EditText shopScopeEdit;
    @Bind(R.id.has_printing)
    LinearLayout hasPrinting;
    @Bind(R.id.order_recycler_view)
    RecyclerView orderRecyclerView;
    @Bind(R.id.order_monet_tv)
    TextView orderMonetTv;
    @Bind(R.id.order_btn)
    Button orderBtn;
    private IWXAPI msgApi;
    private PayReq request;
    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_CHECK_FLAG = 2;
    private OrderBean orderBean;
    private HashMap<String, Object> mParams = new HashMap<>();
    private List<OrderBean> dataBeans = new ArrayList<>();
    private String payCode = AppConfig.PayMode.wxpay;
    public static String order = "interiorshop_order";
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
//                        paySuccess();
                        startActivity(new Intent(ShopCartActivity.this, GoodsOrderActivity.class));
                        finish();
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            showShortToast("支付结果确认中");

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            showShortToast("支付失败");
                        }
                    }
                    break;


                }
                case SDK_CHECK_FLAG: {
                    showShortToast("检查结果为：" + msg.obj);
                    break;
                }
                default:
                    break;
            }
        }
    };

    @Override

    public int getLayoutId() {
        return R.layout.activity_shop_cart;
    }

    @Override
    public void initPresenter() {
    }

    @Override
    public void initView() {
        orderBean = (OrderBean) getIntent().getSerializableExtra(order);
        OrderPresenterImp orderPresenterImp = new OrderPresenterImp(this);
        orderPresenterImp.getOrder(LoginUtil.getLoginToken());
        orderRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        orderRecyclerView.setHasFixedSize(true);
    }

    @Override
    public void fillData() {
        msgApi = WXAPIFactory.createWXAPI(mActivity, AppConfig.WX_APP_ID);
        request = new PayReq();
        if (orderBean.getData().getShop_info().getHas_printing() == 0) {
            hasPrinting.setVisibility(View.GONE);
        } else {
            hasPrinting.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setListener() {
    }

    private boolean canNext() {
        if (TextUtils.isEmpty(etName.getText().toString())) {
            showShortToast("请输入姓名");
            return false;
        }
        if (TextUtils.isEmpty(etPhone.getText().toString())) {
            showShortToast("请输入联系人电话");
            return false;
        }
        if (TextUtils.isEmpty(etAddress.getText().toString())) {
            showShortToast("请输入收货地址");
            return false;
        }
        if (orderBean.getData().getShop_info().getHas_printing() == 1) {
            if (TextUtils.isEmpty(shopNameEdit.getText().toString())) {
                showShortToast("请输入店铺名称");
                return false;
            }
            if (TextUtils.isEmpty(etTime.getText().toString())) {
                showShortToast("请选择营业时间");
                return false;
            }
            if (TextUtils.isEmpty(shopPhoneEdit.getText().toString())) {
                showShortToast("请输入名片联系电话");
                return false;
            }
            if (TextUtils.isEmpty(shopAddressEdit.getText().toString())) {
                showShortToast("请输入名片详细地址");
                return false;
            }
            if (TextUtils.isEmpty(shopScopeEdit.getText().toString())) {
                showShortToast("请输入店铺的经营范围");
                return false;
            }
        }
        return true;
    }

    @Override
    public void getOrder(OrderBean orderBean) {
        dataBeans.add(orderBean);
        ShopCartAdapter adapter = new ShopCartAdapter(orderBean.getData().getOrder_goods(), mContext);
        orderRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        orderMonetTv.setText("¥:" + (float) orderBean.getData().getAll_money() / 100);
    }

    @OnClick({R.id.order_btn})
    public void onClick(View view) {
        if (canNext()) {
            orderPay();
        }
    }

    private void orderPay() {
        mParams.put("sj_login_token", LoginUtil.getLoginToken());
        mParams.put("order_id", orderBean.getData().getOrder_id());
        mParams.put("receive_address", etAddress.getText().toString());
        mParams.put("receive_name", etName.getText().toString());
        mParams.put("receive_phone", etPhone.getText().toString());
        if (orderBean.getData().getShop_info().getHas_printing() == 0) {
            hasPrinting.setVisibility(View.GONE);
        } else {
            hasPrinting.setVisibility(View.VISIBLE);
            mParams.put("shop_name", shopNameEdit.getText().toString());
            mParams.put("business_hours", etTime.getText().toString());
            mParams.put("shop_phone", shopPhoneEdit.getText().toString());
            mParams.put("shop_address", shopAddressEdit.getText().toString());
            mParams.put("business_scope", shopScopeEdit.getText().toString());
        }
        new MyHttp().doPost(Api.getDefault().getOrderInfo(mParams), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                showBottomDialog();
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    //获取支付的弹窗
    private void showBottomDialog() {
        //1、使用Dialog、设置style
        final Dialog dialog = new Dialog(this, R.style.DialogTheme);
        //2、设置布局
        View view = View.inflate(this, R.layout.dialog_custom_layout, null);
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        //设置弹出位置
        window.setGravity(Gravity.BOTTOM);
        //设置弹出动画
        window.setWindowAnimations(R.style.main_menu_animStyle);
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
        TextView tv_price = dialog.findViewById(R.id.tv_dialog_price);
        Button btn_price = dialog.findViewById(R.id.btn_dialog_price);
        tv_price.setText((float) dataBeans.get(0).getData().getAll_money() / 100 + "");
        btn_price.setText("确认支付" + (float) dataBeans.get(0).getData().getAll_money() / 100 + "元");
        LinearLayout llWxPay = dialog.findViewById(R.id.ll_wxpay);
        LinearLayout llZfbPay = dialog.findViewById(R.id.ll_zfbpay);
        final ImageView radioWX = dialog.findViewById(R.id.rb_wx_pay);
        final ImageView radioZFB = dialog.findViewById(R.id.rb_zfb_pay);
        //点击切换支付方式
        llWxPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payCode = AppConfig.PayMode.wxpay;
                radioWX.setImageResource(R.drawable.ic_ok);
                radioZFB.setImageResource(R.drawable.ic_round);
            }
        });
        llZfbPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payCode = AppConfig.PayMode.alipay;
                radioWX.setImageResource(R.drawable.ic_round);
                radioZFB.setImageResource(R.drawable.ic_ok);
            }
        });

        dialog.findViewById(R.id.btn_dialog_price).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MyHttp().doPost(Api.getDefault().getOrderPay(LoginUtil.getLoginToken(), orderBean.getData().getOrder_id(), payCode), new HttpListener() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        if (payCode.equals(AppConfig.PayMode.alipay)) {
                            final String data = result.getString("data");
                            //支付宝支付
                            Runnable payRunnable = new Runnable() {
                                @Override
                                public void run() {
                                    PayTask alipay = new PayTask(mActivity);
                                    Map<String, String> payResult = alipay.payV2(data, true);
                                    Message msg = new Message();
                                    msg.what = SDK_PAY_FLAG;
                                    msg.obj = payResult;
                                    mHandler.sendMessage(msg);
                                }
                            };
                            Thread payThread = new Thread(payRunnable);
                            payThread.start();
                        } else if (payCode.equals(AppConfig.PayMode.wxpay)) {
                            //微信支付
                            AppConfig.RESULT_PAY_TYPE = false;
                            AppConfig.isBuy = true;
                            WxPayInfo data = result.getObject("data", WxPayInfo.class);
                            startWxPay(data);
                            finish();
                        }
                    }

                    @Override
                    public void onError(int code) {

                    }
                });
            }
        });
        dialog.findViewById(R.id.icon_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    //调取微信
    private void startWxPay(WxPayInfo data) {
        genPayReq(data);
        //注册到微信
        msgApi.registerApp(AppConfig.WX_APP_ID);
        msgApi.sendReq(request);
    }

    //获取微信的各个号
    private void genPayReq(WxPayInfo data) {
        request.appId = AppConfig.WX_APP_ID;
        request.partnerId = data.getMch_id();
        request.prepayId = data.getPrepay_id();
        request.packageValue = "Sign=WXPay";
        request.nonceStr = data.getNonce_str();
        request.timeStamp = data.getTime() + "";
        request.sign = data.getTwo_sign();
    }
}
