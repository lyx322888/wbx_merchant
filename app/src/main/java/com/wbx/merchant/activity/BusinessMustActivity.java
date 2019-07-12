package com.wbx.merchant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.wbx.merchant.R;
import com.wbx.merchant.adapter.ProprietaryGoodsAdapter;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.base.BaseAdapter;
import com.wbx.merchant.baseapp.AppConfig;
import com.wbx.merchant.bean.OrderBean;
import com.wbx.merchant.bean.ProprietaryGoodsBean;
import com.wbx.merchant.common.LoginUtil;
import com.wbx.merchant.presenter.ProprietaryGoodsPresenterImp;
import com.wbx.merchant.utils.RetrofitUtils;
import com.wbx.merchant.utils.ToastUitl;
import com.wbx.merchant.view.ProprietaryGoodsView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class BusinessMustActivity extends BaseActivity implements ProprietaryGoodsView {

    @Bind(R.id.goods_recycler)
    RecyclerView mRecycler;
    @Bind(R.id.button_buy)
    TextView mTextView;
    private List<ProprietaryGoodsBean.DataBean> dataList = new ArrayList<>();
    private ProprietaryGoodsAdapter adapter;


    @Override

    public int getLayoutId() {
        return R.layout.activity_business_must;
    }

    @Override
    public void initPresenter() {
    }

    @Override
    public void initView() {
        ProprietaryGoodsPresenterImp presenterImp = new ProprietaryGoodsPresenterImp(this);
        presenterImp.getProprietaryGoods(LoginUtil.getLoginToken(), AppConfig.pageNum, AppConfig.pageSize);
        mRecycler.setLayoutManager(new LinearLayoutManager(mContext));

    }

    @Override
    public void fillData() {

    }

    @Override
    public void setListener() {

    }

    @Override
    public void getProprietaryGoods(final ProprietaryGoodsBean goodsBean) {

        adapter = new ProprietaryGoodsAdapter(goodsBean.getData(), mContext);
        mRecycler.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        dataList.addAll(goodsBean.getData());
        adapter.setOnItemClickListener(R.id.root_view, new BaseAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(View view, final int position) {
                shopIndex = position;
                Intent intent = new Intent(mContext, GoodsDetailsActivity.class);
                intent.putExtra("details_goods_id", goodsBean.getData().get(position).getGoods_id());
                startActivity(intent);
            }
        });
    }

    private static int shopIndex;

    @OnClick({R.id.button_buy})
    public void onClick(View view) {
//        if (isNext()) {
//            order();
//        } else {
//            ToastUitl.showShort("请选择商品");
//        }
        order();
    }

    private int mCount;

    private boolean isNext() {
        mCount = 0;
        for (ProprietaryGoodsBean.DataBean dataBean : dataList) {
            mCount = mCount + dataBean.getOrder_num();
        }

        return mCount != 0;
    }

    private void order() {
        RetrofitUtils.getInstance().server().getOeder(LoginUtil.getLoginToken())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<OrderBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(OrderBean orderBean) {
                        Intent intent = new Intent(BusinessMustActivity.this, ShopCartActivity.class);
                        intent.putExtra(ShopCartActivity.order, orderBean);
                        startActivity(intent);
                        finish();
                    }
                });
    }
}
