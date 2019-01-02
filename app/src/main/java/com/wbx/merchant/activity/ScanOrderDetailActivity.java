package com.wbx.merchant.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.wbx.merchant.R;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.base.BaseApplication;
import com.wbx.merchant.bean.ModifyFoodNumBean;
import com.wbx.merchant.bean.ScanOrderDetailBean;
import com.wbx.merchant.utils.GlideUtils;
import com.wbx.merchant.utils.ToastUitl;
import com.wbx.merchant.widget.LoadingDialog;
import com.wbx.merchant.widget.iosdialog.AlertDialog;

import butterknife.Bind;
import butterknife.OnClick;

public class ScanOrderDetailActivity extends BaseActivity {

    @Bind(R.id.tv_table_num)
    TextView tvTableNum;
    @Bind(R.id.tv_order_num)
    TextView tvOrderNum;
    @Bind(R.id.ll_container)
    LinearLayout llContainer;
    @Bind(R.id.iv_arrow)
    ImageView ivArrow;
    @Bind(R.id.ll_check_more)
    LinearLayout llCheckMore;
    @Bind(R.id.tv_money_coupon)
    TextView tvMoneyCoupon;
    @Bind(R.id.tv_money_full_discount)
    TextView tvMoneyFullDiscount;
    @Bind(R.id.tv_total_fee)
    TextView tvTotalFee;
    @Bind(R.id.tv_already_cash_pay)
    TextView tvAlreadyCashPay;
    @Bind(R.id.tv_print)
    TextView tvPrint;
    @Bind(R.id.tv_check_more)
    TextView tvCheckMore;
    @Bind(R.id.rl_right)
    RelativeLayout rlRight;
    private ScanOrderDetailBean data;
    private boolean isShowAllGoods = false;
    private MyHttp myHttp;
    private String out_trade_no;
    private ModifyFoodNumBean modifyFoodNumBean = new ModifyFoodNumBean();
    private String sj_login_token;
    private boolean isComplete = false;//是否已完成

    public static void actionStart(Context context, String out_trade_no) {
        Intent intent = new Intent(context, ScanOrderDetailActivity.class);
        intent.putExtra("out_trade_no", out_trade_no);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_scan_order_detail;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
    }

    private void updateUI() {
        tvTableNum.setText(data.getSeat());
        tvOrderNum.setText("订单编号：" + data.getOut_trade_no());
        tvMoneyCoupon.setText("-¥" + data.getCoupon_money());
        tvMoneyFullDiscount.setText("-¥" + data.getFull_money_reduce());
        tvTotalFee.setText("合计费用：" + data.getNeed_price() + "元");
        isComplete = data.getStatus() != 0;
        tvAlreadyCashPay.setVisibility(isComplete ? View.GONE : View.VISIBLE);
        tvPrint.setVisibility(isComplete ? View.GONE : View.VISIBLE);
        rlRight.setVisibility(isComplete ? View.GONE : View.VISIBLE);
        llContainer.removeAllViews();
        for (int i = 0; i < data.getGoods().size(); i++) {
            final ScanOrderDetailBean.GoodsBean goodsBean = data.getGoods().get(i);
            View layout = LayoutInflater.from(this).inflate(R.layout.item_scan_order_food, null);
            ImageView ivGoods = layout.findViewById(R.id.iv_goods);
            GlideUtils.showMediumPic(this, ivGoods, goodsBean.getPhoto());
            ((TextView) layout.findViewById(R.id.tv_name)).setText(goodsBean.getTitle());
            ((TextView) layout.findViewById(R.id.tv_price)).setText("¥" + goodsBean.getPrice());
            TextView tvSpec = layout.findViewById(R.id.tv_spec);
            StringBuilder sb = new StringBuilder();
            if (!TextUtils.isEmpty(goodsBean.getAttr_name()) || !TextUtils.isEmpty(goodsBean.getNature_name())) {
                sb.append("(");
                if (!TextUtils.isEmpty(goodsBean.getAttr_name())) {
                    sb.append(goodsBean.getAttr_name());
                }
                if (!TextUtils.isEmpty(goodsBean.getAttr_name()) && !TextUtils.isEmpty(goodsBean.getNature_name())) {
                    sb.append("+");
                }
                if (!TextUtils.isEmpty(goodsBean.getNature_name())) {
                    sb.append(goodsBean.getNature_name());
                }
                sb.append(")");
            }
            if (!TextUtils.isEmpty(sb.toString())) {
                tvSpec.setText(sb.toString());
            } else {
                tvSpec.setVisibility(View.GONE);
            }
            layout.findViewById(R.id.iv_add_food).setVisibility(goodsBean.getIs_add() == 1 ? View.VISIBLE : View.GONE);
            final TextView tvNum = layout.findViewById(R.id.tv_num);
            tvNum.setText(String.valueOf(goodsBean.getNum()));
            View ivReduce = layout.findViewById(R.id.iv_reduce);
            ivReduce.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    reduceFood(goodsBean);
                }
            });
            View ivAdd = layout.findViewById(R.id.iv_add);
            ivAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addFood(goodsBean);
                }
            });
            ivAdd.setEnabled(data.getStatus() == 0 ? true : false);
            ivReduce.setEnabled(data.getStatus() == 0 ? true : false);
            llContainer.addView(layout);
        }
        if (data.getGoods().size() > 3) {
            ((View) llCheckMore.getParent()).setVisibility(View.VISIBLE);
        } else {
            ((View) llCheckMore.getParent()).setVisibility(View.GONE);
        }
        showGoodsList();
    }

    private void reduceFood(ScanOrderDetailBean.GoodsBean goodsBean) {
        if (isComplete) {
            return;
        }
        modifyFoodNumBean.setGoods_id(goodsBean.getGoods_id());
        modifyFoodNumBean.setAttr_id(goodsBean.getAttr_id());
        modifyFoodNumBean.setNum(1);
        modifyFoodNumBean.setId(goodsBean.getId());
        String jsonString = JSONObject.toJSONString(modifyFoodNumBean);
        LoadingDialog.showDialogForLoading(this);
        myHttp.doPost(Api.getDefault().reduceFoodNum(sj_login_token, out_trade_no, jsonString), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                loadData();
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    private void addFood(ScanOrderDetailBean.GoodsBean goodsBean) {
        if (isComplete) {
            return;
        }
        modifyFoodNumBean.setGoods_id(goodsBean.getGoods_id());
        modifyFoodNumBean.setAttr_id(goodsBean.getAttr_id());
        modifyFoodNumBean.setNum(1);
        modifyFoodNumBean.setId(goodsBean.getId());
        String jsonString = JSONObject.toJSONString(modifyFoodNumBean);
        LoadingDialog.showDialogForLoading(this);
        myHttp.doPost(Api.getDefault().addFoodNum(sj_login_token, out_trade_no, jsonString), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                loadData();
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    @Override
    public void fillData() {
        out_trade_no = getIntent().getStringExtra("out_trade_no");
        sj_login_token = BaseApplication.getInstance().readLoginUser().getSj_login_token();
        myHttp = new MyHttp();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        LoadingDialog.showDialogForLoading(this);
        myHttp.doPost(Api.getDefault().getScanOrderDetail(sj_login_token, out_trade_no), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                data = JSONObject.parseObject(result.getString("data"), ScanOrderDetailBean.class);
                updateUI();
            }

            @Override
            public void onError(int code) {
            }
        });
    }

    @Override
    public void setListener() {

    }

    @OnClick({R.id.ll_check_more, R.id.tv_delete_order, R.id.tv_already_cash_pay, R.id.tv_print, R.id.rl_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_right:
                if (!isComplete) {
                    AddFoodActivity.actionStart(this, out_trade_no);
                }
                break;
            case R.id.ll_check_more:
                isShowAllGoods = !isShowAllGoods;
                showGoodsList();
                break;
            case R.id.tv_delete_order:
                deleteOrder();
                break;
            case R.id.tv_already_cash_pay:
                payByCash();
                break;
            case R.id.tv_print:
                printOrder();
                break;
        }
    }

    private void printOrder() {
        new AlertDialog(this).builder().setTitle("提示").setMsg("确定打印该订单吗？")
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).setPositiveButton("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myHttp.doPost(Api.getDefault().printScanOrder(BaseApplication.getInstance().readLoginUser().getSj_login_token()
                        , out_trade_no), new HttpListener() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        ToastUitl.showShort(result.getString("msg"));
                    }

                    @Override
                    public void onError(int code) {

                    }
                });
            }
        }).show();
    }

    private void payByCash() {
        new AlertDialog(this).builder().setTitle("提示").setMsg("确定已收到现金吗？")
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).setPositiveButton("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myHttp.doPost(Api.getDefault().cashPay(BaseApplication.getInstance().readLoginUser().getSj_login_token()
                        , out_trade_no), new HttpListener() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        ToastUitl.showShort(result.getString("msg"));
                        tvAlreadyCashPay.setVisibility(View.GONE);
                        isComplete = true;
                    }

                    @Override
                    public void onError(int code) {

                    }
                });
            }
        }).show();
    }

    private void deleteOrder() {
        new AlertDialog(this).builder().setTitle("提示").setMsg("确定删除该订单吗？")
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).setPositiveButton("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myHttp.doPost(Api.getDefault().delScanOrder(BaseApplication.getInstance().readLoginUser().getSj_login_token()
                        , out_trade_no), new HttpListener() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        ToastUitl.showShort(result.getString("msg"));
                        finish();
                    }

                    @Override
                    public void onError(int code) {

                    }
                });
            }
        }).show();
    }

    private void showGoodsList() {
        if (isShowAllGoods) {
            //展开
            tvCheckMore.setText("点击折叠");
            ivArrow.setImageResource(R.drawable.icon_arrow_up_gray);
            for (int i = 0; i < llContainer.getChildCount(); i++) {
                if (i >= 3) {
                    llContainer.getChildAt(i).setVisibility(View.VISIBLE);
                }
            }
        } else {
            //隐藏
            tvCheckMore.setText("点击展开");
            ivArrow.setImageResource(R.drawable.icon_arrow_down_gray);
            for (int i = 0; i < llContainer.getChildCount(); i++) {
                if (i >= 3) {
                    llContainer.getChildAt(i).setVisibility(View.GONE);
                }
            }
        }
    }
}
