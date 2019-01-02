package com.wbx.merchant.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.wbx.merchant.R;
import com.wbx.merchant.adapter.OrderDetailAdapter;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.baseapp.AppConfig;
import com.wbx.merchant.bean.OrderInfo;
import com.wbx.merchant.utils.FormatUtil;
import com.wbx.merchant.widget.LoadingDialog;
import com.wbx.merchant.widget.iosdialog.AlertDialog;

import butterknife.Bind;
import butterknife.OnClick;
import rx.Observable;

/**
 * Created by wushenghui on 2017/6/27.
 * 订单详情
 */

public class OrderDetailActivity extends BaseActivity {
    @Bind(R.id.root_view)
    LinearLayout rootView;
    @Bind(R.id.is_default_address_tv)
    TextView isDefaultAddressTv;
    @Bind(R.id.support_address_ll)
    LinearLayout supportAddressLl;
    @Bind(R.id.tv_packing_fee)
    TextView tvPackingFee;
    @Bind(R.id.tv_red_packet)
    TextView tvRedPacket;
    @Bind(R.id.tv_shop_red_packet)
    TextView tvShopRedPacket;
    @Bind(R.id.tv_money_coupon)
    TextView tvMoneyCoupon;
    @Bind(R.id.tv_money_full_discount)
    TextView tvMoneyFullDiscount;
    @Bind(R.id.order_detail_state_tv)
    TextView orderStateTv;
    @Bind(R.id.order_detail_total_price_tv)
    TextView totalPriceTv;
    @Bind(R.id.order_detail_logistics_price_tv)
    TextView logisticsPriceTv;
    @Bind(R.id.remark_tv)
    TextView messageTv;
    @Bind(R.id.oder_num_tv)
    TextView orderNumTv;
    @Bind(R.id.order_create_time_tv)
    TextView createTimeTv;
    @Bind(R.id.receiver_name_tv)
    TextView receiverTv;
    @Bind(R.id.receiver_phone_tv)
    TextView receiverPhoneTv;
    @Bind(R.id.receiver_address_tv)
    TextView receiverAddressTv;
    @Bind(R.id.order_detail_goods_rv)
    RecyclerView mRecyclerView;
    @Bind(R.id.order_detail_btn1)
    Button orderDetailBtn1;
    @Bind(R.id.order_detail_btn2)
    Button orderDetailBtn2;
    private OrderInfo orderDetail;
    private OrderDetailAdapter mAdapter;
    private MyHttp myHttp;

    @Override
    public int getLayoutId() {
        return R.layout.activity_order_detail;
    }

    @Override
    public void initPresenter() {
        myHttp = new MyHttp();
    }

    @Override
    public void initView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
    }

    @Override
    public void fillData() {
        orderDetail = (OrderInfo) getIntent().getSerializableExtra("orderDetail");
        if (null != orderDetail) {
            orderStateTv.setText(AppConfig.orderStateStr(orderDetail.getStatus()));
            totalPriceTv.setText(String.format("¥%.2f", orderDetail.getNeed_pay() / 100.00));
            logisticsPriceTv.setText(String.format("¥%.2f", orderDetail.getLogistics() / 100.00));
            tvPackingFee.setText(String.format("¥%.2f", orderDetail.getCasing_price() / 100.00));
            tvMoneyCoupon.setText(String.format("-¥%.2f", orderDetail.getCoupon_money() / 100.00));
            tvMoneyFullDiscount.setText(String.format("-¥%.2f", orderDetail.getFull_money_reduce() / 100.00));
            tvRedPacket.setText(String.format("-¥%.2f", orderDetail.getUser_subsidy_money() / 100.00));
            tvShopRedPacket.setText(String.format("-¥%.2f", orderDetail.getRed_packet_money() / 100.00));
            if (null != orderDetail.getMessage()) {
                messageTv.setText("买家备注：" + orderDetail.getMessage());
            }
            orderNumTv.setText(String.format("订单编号:%d", orderDetail.getOrder_id()));
            createTimeTv.setText(String.format("下单时间:%s", FormatUtil.stampToDate(orderDetail.getCreate_time() + "")));
            receiverTv.setText(orderDetail.getAddr().getXm());
            receiverPhoneTv.setText(orderDetail.getAddr().getTel());
            receiverAddressTv.setText(orderDetail.getAddr().getAddr());
            mAdapter = new OrderDetailAdapter(orderDetail.getGoods(), mContext);
            mRecyclerView.setAdapter(mAdapter);
        }

        switch (orderDetail.getStatus()) {
            case 1:
                orderDetailBtn1.setVisibility(View.VISIBLE);
                orderDetailBtn2.setVisibility(View.VISIBLE);
                orderDetailBtn1.setText("拒单");
                orderDetailBtn2.setText("配送");
                break;
            case 2:
                orderDetailBtn1.setVisibility(View.VISIBLE);
                orderDetailBtn1.setText("拒单");
                break;
            case 3:
                orderDetailBtn1.setVisibility(View.VISIBLE);
                orderDetailBtn1.setText("退款");
                break;
            case 8:
                orderDetailBtn1.setVisibility(View.VISIBLE);
                orderDetailBtn1.setText("打印订单");
                break;
        }

    }

    @Override
    public void setListener() {

    }

    @OnClick({R.id.order_detail_btn1, R.id.order_detail_btn2})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.order_detail_btn1:
                switch (orderDetail.getStatus()) {
                    case 1:
                    case 2:
                        //拒单
                        refuse();
                        break;
                    case 3:
                    case 4:
                        //退款
                        refund();
                        break;
                    case 8:
                        printOrder();
                        break;
                }
                break;
            case R.id.order_detail_btn2:
                if (orderDetail.getStatus() == 1) {
                    //配送
                    showSendPop();
                } else {
                    //退款
                    refund();
                }

                break;
        }
    }

    private void printOrder() {
        new AlertDialog(this).builder()
                .setTitle("提示")
                .setMsg("是否确定打印该订单？")
                .setNegativeButton("不了", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                })
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myHttp.doPost(Api.getDefault().printOrder(userInfo.getSj_login_token(), String.valueOf(orderDetail.getOrder_id())), new HttpListener() {
                            @Override
                            public void onSuccess(JSONObject result) {
                                Toast.makeText(mContext, result.getString("msg"), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(int code) {

                            }
                        });
                    }
                }).show();
    }

    //拒单
    private void refuse() {
        new AlertDialog(mContext).builder()
                .setTitle("提示")
                .setMsg("拒绝接单？")
                .setNegativeButton("不了", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                })
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myHttp.doPost(Api.getDefault().refuseOrder(userInfo.getSj_login_token(), orderDetail.getOrder_id()), new HttpListener() {
                            @Override
                            public void onSuccess(JSONObject result) {
                                showShortToast(result.getString("msg"));
                                finish();
                            }

                            @Override
                            public void onError(int code) {

                            }
                        });

                    }
                }).show();
    }

    //退款
    private void refund() {
        new AlertDialog(mContext).builder()
                .setTitle("提示")
                .setMsg("同意退款？")
                .setNegativeButton("不了", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                })
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myHttp.doPost(Api.getDefault().agreeRefund(userInfo.getSj_login_token(), orderDetail.getOrder_id()), new HttpListener() {
                            @Override
                            public void onSuccess(JSONObject result) {
                                showShortToast(result.getString("msg"));
                                finish();
                            }

                            @Override
                            public void onError(int code) {

                            }
                        });

                    }
                }).show();
    }

    private void showSendPop() {
        final View popView = getLayoutInflater().inflate(R.layout.pop_goods_send_layout, null);
        final PopupWindow mPopUpWindow = new PopupWindow();
        mPopUpWindow.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        mPopUpWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        mPopUpWindow.setAnimationStyle(R.style.ActionSheetDialogAnimation);
        mPopUpWindow.setFocusable(true);
        mPopUpWindow.setOutsideTouchable(true);
        mPopUpWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopUpWindow.setContentView(popView);
        final ImageView ivSendByDaDa = popView.findViewById(R.id.iv_send_by_dada);
        final ImageView ivSendByMerchant = popView.findViewById(R.id.iv_send_by_merchant);
        final TextView tvEstimatePrice = popView.findViewById(R.id.tv_estimate_price);
        ivSendByDaDa.setSelected(true);
        popView.findViewById(R.id.ll_send_by_dada).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivSendByDaDa.setSelected(true);
                ivSendByMerchant.setSelected(false);
                tvEstimatePrice.setVisibility(View.VISIBLE);
            }
        });
        popView.findViewById(R.id.ll_send_by_merchant).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivSendByDaDa.setSelected(false);
                ivSendByMerchant.setSelected(true);
                tvEstimatePrice.setVisibility(View.INVISIBLE);
            }
        });
        popView.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopUpWindow.dismiss();
            }
        });
        popView.findViewById(R.id.tv_confirm_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ivSendByDaDa.isSelected()) {
                    sendByDaDa(mPopUpWindow);
                } else {
                    //商家配送
                    orderOperation(Api.getDefault().sendGoods(userInfo.getSj_login_token(), orderDetail.getOrder_id()));
                    mPopUpWindow.dismiss();
                }
            }
        });
        mPopUpWindow.showAtLocation(rootView.findViewById(R.id.root_view), Gravity.CENTER, 0, 0);
        backgroundAlpha(0.6f);
        mPopUpWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
        calculateDaDaPrice(tvEstimatePrice);
    }

    private void calculateDaDaPrice(final TextView tvEstimatePrice) {
        LoadingDialog.showDialogForLoading(this);
        myHttp.doPost(Api.getDefault().getDaDaEstimatePrice(userInfo.getSj_login_token(), String.valueOf(orderDetail.getOrder_id())), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                if (tvEstimatePrice != null) {
                    tvEstimatePrice.setText(result.getJSONObject("data").getString("dada_estimate_price"));
                }
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    private void orderOperation(Observable<JSONObject> objectObservable) {
        myHttp.doPost(objectObservable, new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                showShortToast(result.getString("msg"));
                finish();
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    private void sendByDaDa(final PopupWindow popupWindow) {
        LoadingDialog.showDialogForLoading(this);
        myHttp.doPost(Api.getDefault().sendByDaDa(userInfo.getSj_login_token(), String.valueOf(orderDetail.getOrder_id())), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                showShortToast("已提交至达达");
                if (popupWindow != null) {
                    popupWindow.dismiss();
                }
                finish();
            }

            @Override
            public void onError(int code) {
                if (popupWindow != null) {
                    popupWindow.dismiss();
                }
                if (code == AppConfig.ERROR_STATE.SEND_FEE_NO_ENOUGH) {
                    new AlertDialog(mActivity).builder().setTitle("提示")
                            .setMsg("余额不足请充值").setPositiveButton("充值", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mActivity, PayActivity.class);
                            intent.putExtra("isDaDa", true);
                            startActivity(intent);
                        }
                    }).setNegativeButton("取消", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    }).show();
                }
            }
        });
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }
}
