package com.wbx.merchant.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wbx.merchant.R;
import com.wbx.merchant.base.BaseAdapter;
import com.wbx.merchant.base.BaseViewHolder;
import com.wbx.merchant.bean.DaDaOrderTrackBean;
import com.wbx.merchant.bean.OrderInfo;
import com.wbx.merchant.utils.DateUtil;
import com.wbx.merchant.utils.FormatUtil;

import java.util.List;

/**
 * Created by wushenghui on 2017/6/27.
 */

public class OrderAdapter extends BaseAdapter<OrderInfo, Context> {
    private boolean mIsMarket;

    public OrderAdapter(List<OrderInfo> dataList, Context context, boolean isMarket) {
        super(dataList, context);
        this.mIsMarket = isMarket;
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_order;
    }

    @Override
    public void convert(BaseViewHolder holder, final OrderInfo orderInfo, int position) {
        holder.setText(R.id.tv_order_id, "订单号：" + orderInfo.getOrder_id());
        TextView tvOrderState = holder.getView(R.id.tv_order_state);
        String orderState = "";
        String str = "立即配送";
        if (orderInfo.getDispatching_time() != 0) {
            str = FormatUtil.stampToDate(orderInfo.getDispatching_time() + "");
        }
        TextView tvDispatchTime = holder.getView(R.id.tv_dispatching_time);//配送时间
        tvDispatchTime.setText("配送时间: " + str);
        TextView tvRefuseOrder = holder.getView(R.id.tv_refuse_order);//拒单
        TextView tvSend = holder.getView(R.id.tv_send);//发货
        TextView tvSendByDaDa = holder.getView(R.id.tv_send_by_dada);//达达发货
        TextView tvCancelSendByDaDa = holder.getView(R.id.tv_cancel_send_by_dada);//取消达达发货
        TextView tvRefuseRefund = holder.getView(R.id.tv_refuse_refund);//拒绝退款
        TextView tvPrintOrder = holder.getView(R.id.tv_print_order);//打印订单
        TextView tvRefund = holder.getView(R.id.tv_refund);//退款
        TextView tvCopy = holder.getView(R.id.tv_copy_order);//一键复制

        TextView tvReceiverAddress = holder.getView(R.id.tv_receiver_address);//收货地址

        tvReceiverAddress.setText(orderInfo.getAddr().getAddr());
        tvRefuseOrder.setVisibility(View.GONE);
        tvSend.setVisibility(View.GONE);
        tvSendByDaDa.setVisibility(View.GONE);
        tvRefuseRefund.setVisibility(View.GONE);
        tvRefund.setVisibility(View.GONE);
        tvCancelSendByDaDa.setVisibility(View.GONE);
        tvPrintOrder.setVisibility(View.GONE);
        tvCopy.setVisibility(View.GONE);
        TextView tvSendType = holder.getView(R.id.tv_send_type);
        if (orderInfo.getStatus() == 1) {//待配送
            tvSendType.setVisibility(View.GONE);
        } else {
            tvSendType.setVisibility(View.VISIBLE);
            tvSendType.setText(orderInfo.getIs_dada() == 1 ? "[达达配送]" : (orderInfo.getIs_fengniao() == 1 ? "[蜂鸟配送]" : "[商家配送]"));
        }
        switch (orderInfo.getOrder_status()) {
            case 1:
                orderState = "待配送";
                tvRefuseOrder.setVisibility(View.VISIBLE);
                tvCopy.setVisibility(View.VISIBLE);
                //如果选了达达 就显示取消达达
                if (orderInfo.getIs_dada() == 1){
                        //显示取消达达配送按钮
                        tvCancelSendByDaDa.setVisibility(View.VISIBLE);
                }else {
                    //显示发货按钮
                    tvSend.setVisibility(View.VISIBLE);
                }
                break;
            case 2:
                orderState = "配送中";
                tvRefuseOrder.setVisibility(View.VISIBLE);
                if (orderInfo.getIs_dada() == 1) {
                    List<DaDaOrderTrackBean> dada = orderInfo.getDada();
                    if (dada == null || dada.size() == 0 || dada.get(dada.size() - 1).getDada_status() == 1 || dada.get(dada.size() - 1).getDada_status() == 2) {
                        tvCancelSendByDaDa.setVisibility(View.VISIBLE);
                    }
                } else {
                    tvSendByDaDa.setVisibility(View.VISIBLE);
                }
                tvCopy.setVisibility(View.VISIBLE);
                break;
            case 3:
                orderState = "待退款";
                tvRefuseRefund.setVisibility(View.VISIBLE);
                tvRefund.setVisibility(View.VISIBLE);
                break;
            case 4:
                if (mIsMarket) {
                    orderState = "已退款";
                } else {
                    orderState = "待退款";
                    tvRefuseRefund.setVisibility(View.VISIBLE);
                    tvRefund.setVisibility(View.VISIBLE);
                }
                if (orderInfo.getIs_afhalen() == 1) {
                    tvDispatchTime.setText("用户自提码: " + orderInfo.getOrder_id());
                    tvReceiverAddress.setVisibility(View.GONE);
                    tvSendType.setVisibility(View.GONE);
                }
                break;
            case 5:
                orderState = "已退款";
                if (orderInfo.getIs_afhalen() == 1) {
                    tvDispatchTime.setText("用户自提码: " + orderInfo.getOrder_id());
                    tvReceiverAddress.setVisibility(View.GONE);
                    tvSendType.setVisibility(View.GONE);
                }
                break;
            case 8:
                orderState = "已完成";
                tvPrintOrder.setVisibility(View.VISIBLE);
                break;
            case 77://待自提:
                tvDispatchTime.setText("用户自提码: " + orderInfo.getOrder_id());
                orderState = "待自提";
                tvSendType.setVisibility(View.GONE);
                tvReceiverAddress.setVisibility(View.GONE);
                tvRefuseOrder.setVisibility(View.VISIBLE);
                break;
        }
        tvOrderState.setText(orderState);
        holder.setText(R.id.tv_receiver_name, TextUtils.isEmpty(orderInfo.getAddr().getXm()) ? "" : orderInfo.getAddr().getXm())
                .setText(R.id.tv_create_order_time, "下单时间: " + FormatUtil.stampToDate(orderInfo.getCreate_time() + ""));
        holder.setText(R.id.tv_goods_num, "商品(" + orderInfo.getNum() + ")");
        if (orderInfo.getGoods().size() <= 3) {
            holder.getView(R.id.ll_toggle_expand_state).setVisibility(View.GONE);
        } else {
            holder.getView(R.id.ll_toggle_expand_state).setVisibility(View.VISIBLE);
        }
        RecyclerView goodsRecyclerView = holder.getView(R.id.item_order_goods_rv);
        final OrderGoodsAdapter adapter = new OrderGoodsAdapter(orderInfo.getGoods(), mContext);
        goodsRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        goodsRecyclerView.setAdapter(adapter);
        holder.setText(R.id.tv_goods_total_num, "共 " + orderInfo.getGoods().size() + " 件商品，实付款");
        holder.setText(R.id.tv_money, String.format("¥%.2f", orderInfo.getNeed_pay() / 100.00));
        final TextView tvExpandState = holder.getView(R.id.tv_expand_state);
        tvExpandState.setText("展开");
        tvExpandState.setTextColor(mContext.getResources().getColor(R.color.red));
        final ImageView ivExpandState = holder.getView(R.id.iv_expand_state);
        ivExpandState.setImageResource(R.drawable.icon_arrow_down);
        holder.getView(R.id.ll_toggle_expand_state).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("展开".equals(tvExpandState.getText().toString())) {
                    tvExpandState.setText("收起");
                    tvExpandState.setTextColor(mContext.getResources().getColor(R.color.app_color));
                    ivExpandState.setImageResource(R.drawable.icon_arrow_up);
                    adapter.showAll();
                } else {
                    tvExpandState.setText("展开");
                    tvExpandState.setTextColor(mContext.getResources().getColor(R.color.red));
                    ivExpandState.setImageResource(R.drawable.icon_arrow_down);
                    adapter.hideSome();
                }
            }
        });
        if (orderInfo.getIs_fengniao() != 0 || orderInfo.getIs_dada() != 0) {
            holder.getView(R.id.ll_order_track).setVisibility(View.VISIBLE);
            if (orderInfo.getDada() != null && orderInfo.getDada().size() > 0) {
                String carrier_driver_name = orderInfo.getDada().get(0).getDm_name();
                holder.setText(R.id.tv_driver_name, (orderInfo.getIs_fengniao() == 1 ? "蜂鸟骑手：" : "达达骑手") + (TextUtils.isEmpty(carrier_driver_name) ? "" : carrier_driver_name));
                View callPhone = holder.getView(R.id.iv_call_driver);
                if (!TextUtils.isEmpty(orderInfo.getDada().get(0).getDm_mobile())) {
                    callPhone.setVisibility(View.VISIBLE);
                } else {
                    callPhone.setVisibility(View.GONE);
                }
                final LinearLayout llContainer = holder.getView(R.id.ll_container);
                llContainer.setVisibility(View.GONE);
                llContainer.removeAllViews();
                for (DaDaOrderTrackBean daDaOrderTrackBean : orderInfo.getDada()) {
                    View item = LayoutInflater.from(mContext).inflate(R.layout.item_feng_niao_track, null);
                    TextView tvTrack = item.findViewById(R.id.tv_track);
                    String time = DateUtil.getHourAndMinute(Long.valueOf(daDaOrderTrackBean.getUpdate_time()));
                    time = time + "　　";
                    if (orderInfo.getIs_dada() == 1) {
                        switch (daDaOrderTrackBean.getDada_status()) {
                            case 1:
                                tvTrack.setText(time + "待接单");
                                break;
                            case 2:
                                tvTrack.setText(time + "待取货");
                                break;
                            case 3:
                                tvTrack.setText(time + "配送中");
                                break;
                            case 4:
                                tvTrack.setText(time + "已完成");
                                break;
                            case 5:
                                tvTrack.setText(time + "已取消");
                                break;
                            case 7:
                                tvTrack.setText(time + "已过期");
                                break;
                            case 8:
                                tvTrack.setText(time + "指派单");
                                break;
                            case 9:
                                tvTrack.setText(time + "妥投异常之物品返回中");
                                break;
                            case 10:
                                tvTrack.setText(time + "妥投异常之物品返回完成");
                                break;
                            case 100:
                                tvTrack.setText(time + "骑手到店");
                                break;
                            case 1000:
                                tvTrack.setText(time + "创建达达运单失败");
                                break;
                        }
                    } else {
                        switch (daDaOrderTrackBean.getDada_status()) {
                            case 1:
                                tvTrack.setText(time + "系统已接单");
                                break;
                            case 2:
                                tvTrack.setText(time + "配送中");
                                break;
                            case 3:
                                tvTrack.setText(time + "已送达");
                                break;
                            case 5:
                                tvTrack.setText(time + "系统拒单/配送异常");
                                break;
                            case 20:
                                tvTrack.setText(time + "已分配骑手");
                                break;
                            case 80:
                                tvTrack.setText(time + "骑手已到店");
                                break;
                        }
                    }
                    llContainer.addView(item);
                }
                final TextView tvShowTrack = holder.getView(R.id.tv_show_track);
                tvShowTrack.setText("展开");
                final ImageView ivShowTrack = holder.getView(R.id.iv_show_track);
                ivShowTrack.setImageResource(R.drawable.icon_arrow_down);
                holder.getView(R.id.ll_show_track).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (llContainer.getVisibility() == View.VISIBLE) {
                            llContainer.setVisibility(View.GONE);
                            tvShowTrack.setText("展开");
                            ivShowTrack.setImageResource(R.drawable.icon_arrow_down);
                        } else {
                            llContainer.setVisibility(View.VISIBLE);
                            tvShowTrack.setText("收起");
                            ivShowTrack.setImageResource(R.drawable.icon_arrow_up);
                        }
                    }
                });
            }
        } else {
            holder.getView(R.id.ll_order_track).setVisibility(View.GONE);
        }
    }
}