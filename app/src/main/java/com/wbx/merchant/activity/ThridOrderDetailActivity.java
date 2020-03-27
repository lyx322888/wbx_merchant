package com.wbx.merchant.activity;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.wbx.merchant.R;
import com.wbx.merchant.adapter.OrderDetailAdapter;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.bean.GoodsInfo;
import com.wbx.merchant.bean.OrderInfo;
import com.wbx.merchant.bean.ThirdOrderInfo;
import com.wbx.merchant.utils.FormatUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by wushenghui on 2017/11/1.
 */

public class ThridOrderDetailActivity extends BaseActivity {

    @Bind(R.id.tv_send_type)
    TextView tvSendType;
    private ThirdOrderInfo thirdOrderInfo;
    private OrderInfo orderInfo;
    @Bind({R.id.contacts_tv, R.id.receiving_address_tv, R.id.delivery_name_tv, R.id.delivery_phone_tv, R.id.logistics_tv, R.id.total_price_tv, R.id.message_tv, R.id.order_num_tv, R.id.create_time_tv})
    //联系人 配送地址  配送员姓名 配送员电话  运费 合计 买家留言 订单编号 下单时间
            List<TextView> textViewList;
    @Bind(R.id.order_detail_goods_rv)
    RecyclerView mRecyclerView;
    private OrderDetailAdapter mAdapter;
    private List<GoodsInfo> goodsInfoList = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_thrid_order_detail;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new OrderDetailAdapter(goodsInfoList, mContext);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void fillData() {
        thirdOrderInfo = (ThirdOrderInfo) getIntent().getSerializableExtra("orderInfo");
        tvSendType.setText(thirdOrderInfo.getDispatching_type());
        new MyHttp().doPost(Api.getDefault().getThirdOrderDetail(userInfo.getSj_login_token(), thirdOrderInfo.getOrder_id()), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                orderInfo = result.getObject("data", OrderInfo.class);
                pullData();
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    private void pullData() {
        textViewList.get(0).setText(orderInfo.getAddr().getXm());
        textViewList.get(1).setText(orderInfo.getAddr().getAddr());
        textViewList.get(2).setText(thirdOrderInfo.getDm_name());
        textViewList.get(3).setText(thirdOrderInfo.getDm_mobile());
        textViewList.get(4).setText(String.format("%.2f", orderInfo.getLogistics() / 100.00));
        textViewList.get(5).setText(String.format("%.2f", orderInfo.getNeed_pay() / 100.00));
        textViewList.get(6).setText(orderInfo.getMessage());
        textViewList.get(7).setText(orderInfo.getOrder_id() + "");
        textViewList.get(8).setText(FormatUtil.stampToDate1(orderInfo.getCreate_time() + ""));
        goodsInfoList.addAll(orderInfo.getGoods());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void setListener() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
