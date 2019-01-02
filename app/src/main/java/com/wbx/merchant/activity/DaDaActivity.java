package com.wbx.merchant.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wbx.merchant.R;
import com.wbx.merchant.adapter.ThirdOrderAdapter;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.base.BaseAdapter;
import com.wbx.merchant.bean.ThirdOrderInfo;
import com.wbx.merchant.utils.GlideUtils;
import com.wbx.merchant.widget.LoadingDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import rx.Observable;

/**
 * Created by Administrator on 2017/10/24 0024.
 */

public class DaDaActivity extends BaseActivity {
    @Bind(R.id.img_name)
    ImageView img_name;
    @Bind(R.id.tv_name)
    TextView tv_name;

    @Bind(R.id.withdraw_recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.third_price_tv)
    TextView third_price_tv;
    private List<ThirdOrderInfo> mInfo = new ArrayList<>();
    private ThirdOrderAdapter mInfoAdapter;

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, DaDaActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_da_da;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mInfoAdapter = new ThirdOrderAdapter(mInfo, mContext);
        mRecyclerView.setAdapter(mInfoAdapter);
    }

    @Override
    public void fillData() {
        LoadingDialog.showDialogForLoading(mActivity, "加载中...", true);
        Observable<JSONObject> thirdOrder = Api.getDefault().getThirdOrderList(userInfo.getSj_login_token());
        new MyHttp().doPost(thirdOrder, new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                int intValue = result.getJSONObject("data").getIntValue("fengniao_money");
                List<ThirdOrderInfo> order_list = JSONArray.parseArray(result.getJSONObject("data").getString("order_list"), ThirdOrderInfo.class);
                mInfo.addAll(order_list);
                mInfoAdapter.notifyDataSetChanged();
                third_price_tv.setText(String.format("%.2f", intValue / 100.00));
                tv_name.setText(userInfo.getShop_name());
                GlideUtils.showSmallPic(mContext, img_name, userInfo.getShopPhoto());
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    @Override
    public void setListener() {
        mInfoAdapter.setOnItemClickListener(R.id.root_view, new BaseAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(View view, int position) {
                Intent intent = new Intent(mContext, ThridOrderDetailActivity.class);
                intent.putExtra("orderInfo", mInfoAdapter.getItem(position));
                startActivity(intent);
            }
        });
    }

    @OnClick({R.id.bt_recharge, R.id.bt_bring, R.id.tv_charge_standard})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_recharge:
                Intent intent = new Intent(mContext, PayActivity.class);
                intent.putExtra("isDaDa", true);
                startActivity(intent);
                break;
            case R.id.bt_bring:
                Intent intentCash = new Intent(mContext, CashActivity.class);
                intentCash.putExtra("type", CashActivity.TYPE_DA_DA);
                startActivity(intentCash);
                break;
            case R.id.tv_charge_standard:
                Intent intent2 = new Intent(this, WebActivity.class);
                intent2.putExtra("url", "http://www.wbx365.com/Wbxwaphome/help/dada.html");
                startActivity(intent2);
                break;
        }
    }

}
