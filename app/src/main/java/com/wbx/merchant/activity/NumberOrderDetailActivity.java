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
import com.wbx.merchant.R;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.base.BaseApplication;
import com.wbx.merchant.bean.NumberOrderBean;
import com.wbx.merchant.utils.GlideUtils;
import com.wbx.merchant.utils.ToastUitl;
import com.wbx.merchant.widget.iosdialog.AlertDialog;

import butterknife.Bind;
import butterknife.OnClick;

public class NumberOrderDetailActivity extends BaseActivity {
    @Bind(R.id.tv_pick_food_num)
    TextView tvPickFoodNum;
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
    @Bind(R.id.tv_check_more)
    TextView tvCheckMore;
    private NumberOrderBean data;
    private boolean isShowAllGoods = false;

    public static void actionStart(Context context, NumberOrderBean numberOrderBean) {
        Intent intent = new Intent(context, NumberOrderDetailActivity.class);
        intent.putExtra("data", numberOrderBean);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_number_order_detail;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {

    }

    @Override
    public void fillData() {
        data = (NumberOrderBean) getIntent().getSerializableExtra("data");
        tvPickFoodNum.setText(data.getOrder_take_number());
        tvOrderNum.setText("订单编号：" + data.getOrder_id());
        tvMoneyCoupon.setText("-¥" + data.getCoupon_money() / 100);
        tvMoneyFullDiscount.setText("-¥" + data.getFull_money_reduce() / 100);
        tvTotalFee.setText(String.format("合计费用：%.2f元", data.getTotal_price() / 100.00));
        llContainer.removeAllViews();
        for (int i = 0; i < data.getGoods().size(); i++) {
            final NumberOrderBean.GoodsBean goodsBean = data.getGoods().get(i);
            View layout = LayoutInflater.from(this).inflate(R.layout.item_number_order_food, null);
            ImageView ivGoods = layout.findViewById(R.id.iv_goods);
            GlideUtils.showMediumPic(this, ivGoods, goodsBean.getPhoto());
            ((TextView) layout.findViewById(R.id.tv_name)).setText(goodsBean.getTitle());
            ((TextView) layout.findViewById(R.id.tv_price)).setText(String.format("¥%.2f", goodsBean.getPrice() / 100.00));
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
            final TextView tvNum = layout.findViewById(R.id.tv_num);
            tvNum.setText("共" + goodsBean.getNum() + "件");
            llContainer.addView(layout);
        }
        if (data.getGoods().size() > 3) {
            ((View) llCheckMore.getParent()).setVisibility(View.VISIBLE);
        } else {
            ((View) llCheckMore.getParent()).setVisibility(View.GONE);
        }
        showGoodsList();
    }

    @Override
    public void setListener() {

    }

    @OnClick({R.id.ll_check_more, R.id.tv_delete_order,})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_check_more:
                isShowAllGoods = !isShowAllGoods;
                showGoodsList();
                break;
            case R.id.tv_delete_order:
                deleteOrder();
                break;
        }
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
                new MyHttp().doPost(Api.getDefault().deleteNumberOrder(BaseApplication.getInstance().readLoginUser().getSj_login_token()
                        , data.getOrder_id()), new HttpListener() {
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
