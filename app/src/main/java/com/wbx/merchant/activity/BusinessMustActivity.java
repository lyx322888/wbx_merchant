package com.wbx.merchant.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.wbx.merchant.R;
import com.wbx.merchant.adapter.ProprietaryGoodsAdapter;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.baseapp.AppConfig;
import com.wbx.merchant.bean.ProprietaryGoodsBean;
import com.wbx.merchant.common.LoginUtil;
import com.wbx.merchant.presenter.ProprietaryGoodsPresenterImp;
import com.wbx.merchant.view.ProprietaryGoodsView;

import butterknife.Bind;

public class BusinessMustActivity extends BaseActivity implements ProprietaryGoodsView {

    @Bind(R.id.goods_recycler)
    RecyclerView mRecycler;
    @Bind(R.id.button_buy)
    TextView mTextView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_business_must;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        ProprietaryGoodsPresenterImp presenterImp=new ProprietaryGoodsPresenterImp(this);
        presenterImp.getProprietaryGoods(LoginUtil.getLoginToken(),AppConfig.pageNum,AppConfig.pageSize);
        mRecycler.setLayoutManager(new LinearLayoutManager(mContext));


    }

    @Override
    public void fillData() {

    }

    @Override
    public void setListener() {

    }

    @Override
    public void getProprietaryGoods(ProprietaryGoodsBean goodsBean) {
        ProprietaryGoodsAdapter adapter = new ProprietaryGoodsAdapter(goodsBean.getData(), mContext);
        mRecycler.setAdapter(adapter);
    }

    public void buy(View view){

    }
}
