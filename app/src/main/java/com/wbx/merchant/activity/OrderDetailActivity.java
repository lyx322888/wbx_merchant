package com.wbx.merchant.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.constraint.ConstraintLayout;
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

import com.alibaba.fastjson.JSONObject;
import com.wbx.merchant.R;
import com.wbx.merchant.adapter.OrderDetailAdapter;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.baseapp.AppConfig;
import com.wbx.merchant.bean.GoodsInfo;
import com.wbx.merchant.bean.OrderAddressInfo;
import com.wbx.merchant.bean.OrderInfo;
import com.wbx.merchant.utils.FormatUtil;
import com.wbx.merchant.widget.LoadingDialog;
import com.wbx.merchant.widget.iosdialog.AlertDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import rx.Observable;

/**
 * Created by wushenghui on 2017/6/27.
 * 订单详情
 */

public class OrderDetailActivity extends BaseActivity {
    @Bind(R.id.root_view)
    ConstraintLayout rootView;
    @Bind(R.id.tv_order_state)
    TextView tvOrderState;
    @Bind(R.id.tv_receiver_name)
    TextView tvReceiverName;
    @Bind(R.id.tv_receiver_address)
    TextView tvReceiverAddress;
    @Bind(R.id.tv_receiver_time)
    TextView tvReceiverTime;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.tv_logistics_price)
    TextView tvLogisticsPrice;
    @Bind(R.id.tv_money_coupon)
    TextView tvMoneyCoupon;
    @Bind(R.id.tv_full_discount)
    TextView tvFullDiscount;
    @Bind(R.id.tv_subsidy_money)
    TextView tvSubsidyMoney;
    @Bind(R.id.tv_casing_price)
    TextView tvCasingPrice;
    @Bind(R.id.tv_shop_red_packet)
    TextView tvShopRedPacket;
    @Bind(R.id.tv_sum)
    TextView tvSum;
    @Bind(R.id.tv_remark)
    TextView tvRemark;
    @Bind(R.id.tv_order_no)
    TextView tvOrderNo;
    @Bind(R.id.tv_order_time)
    TextView tvOrderTime;
    @Bind(R.id.btn_submit)
    Button btnSubmit;

    private OrderInfo orderDetail;
    private OrderDetailAdapter mAdapter;
    private MyHttp myHttp;
    private List<GoodsInfo> list = new ArrayList<>();


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
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setHasFixedSize(true);
        mAdapter = new OrderDetailAdapter(list, this);
        recyclerView.setAdapter(mAdapter);
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public void fillData() {
        orderDetail = (OrderInfo) getIntent().getSerializableExtra("orderDetail");
        if (null != orderDetail) {
            switch (orderDetail.getStatus()) {
                case 1:
                    btnSubmit.setVisibility(View.VISIBLE);
                    btnSubmit.setText("发货");
                    break;
                case 2:
                    btnSubmit.setVisibility(View.VISIBLE);
                    btnSubmit.setText("拒单");
                    break;
                case 3:
                    btnSubmit.setVisibility(View.VISIBLE);
                    btnSubmit.setText("退款");
                    break;
            }
            OrderAddressInfo addr = orderDetail.getAddr();
            tvReceiverName.setText("联系人：" + addr.getXm() + "    " + addr.getTel());
            tvOrderState.setText("订单详情：" + AppConfig.orderStateStr(orderDetail.getStatus()));
            tvReceiverAddress.setText("配送地址：" + addr.getAddr());
            tvReceiverAddress.setVisibility(View.VISIBLE);
            String str = "立即配送";
            if (orderDetail.getDispatching_time() != 0) {
                str = FormatUtil.stampToDate(orderDetail.getDispatching_time() + "");
            }
            tvReceiverTime.setText("配送时间：" + str);
            btnSubmit.setVisibility(View.VISIBLE);

            if (orderDetail.getOrder_status()==4){
                if (orderDetail.getIs_afhalen() == 1) {
                    tvReceiverAddress.setVisibility(View.GONE);
                    tvReceiverTime.setText("自提码：" + orderDetail.getOrder_id());
                    tvOrderState.setText("订单详情：" + "已退款");
                    btnSubmit.setVisibility(View.GONE);
                }
            } else if (orderDetail.getOrder_status() == 77) {
                tvReceiverAddress.setVisibility(View.GONE);
                tvReceiverTime.setText("自提码：" + orderDetail.getOrder_id());
                tvOrderState.setText("订单详情：" + "待自提");
                btnSubmit.setVisibility(View.GONE);
            }

            tvLogisticsPrice.setText("运费：             " + orderDetail.getLogistics() / 100.00 + "元");
            tvMoneyCoupon.setText("优惠券：         " + orderDetail.getCoupon_money() / 100.00 + "元");
            tvFullDiscount.setText("满减：             " + orderDetail.getFull_money_reduce() / 100.00 + "元");
            tvSubsidyMoney.setText("奖励金：         " + orderDetail.getUser_subsidy_money() / 100.00 + "元");
            tvCasingPrice.setText("包装费：         " + orderDetail.getCasing_price() / 100.00 + "元");
            tvShopRedPacket.setText("店铺红包：     " + orderDetail.getRed_packet_money() / 100.00 + "元");
            tvSum.setText("合计：            " + orderDetail.getNeed_pay() / 100.00 + "元");
            tvRemark.setText("买家留言：     " + orderDetail.getMessage());
            tvOrderNo.setText("订单编号：     " + orderDetail.getOrder_id());
            tvOrderTime.setText("下单时间：     " + FormatUtil.stampToDate(orderDetail.getCreate_time() + ""));
            list.addAll(orderDetail.getGoods());
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void setListener() {
    }

    /**
     * 拒单
     */
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

    /**
     * 退款
     */

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

    /**
     * 发货
     */
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


    @OnClick(R.id.btn_submit)
    public void onViewClicked() {
        if (orderDetail != null) {
            switch (orderDetail.getStatus()) {
                case 1://发货
                    showSendPop();
                    break;
                case 2://拒单
                    refuse();
                    break;
                case 3://退款
                    refund();
                    break;
            }
        }
    }
}
