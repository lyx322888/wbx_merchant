package com.wbx.merchant.activity;

import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.wbx.merchant.R;
import com.wbx.merchant.adapter.ProprietaryGoodsAdapter;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.base.BaseAdapter;
import com.wbx.merchant.baseapp.AppConfig;
import com.wbx.merchant.bean.ProprietaryGoodsBean;
import com.wbx.merchant.common.LoginUtil;
import com.wbx.merchant.presenter.ProprietaryGoodsPresenterImp;
import com.wbx.merchant.utils.ToastUitl;
import com.wbx.merchant.view.ProprietaryGoodsView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class BusinessMustActivity extends BaseActivity implements ProprietaryGoodsView {

    @Bind(R.id.goods_recycler)
    RecyclerView mRecycler;
    @Bind(R.id.button_buy)
    TextView mTextView;
    private List<ProprietaryGoodsBean.DataBean> dataList=new ArrayList<>();
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
        dataList.addAll(goodsBean.getData());
        ProprietaryGoodsAdapter adapter = new ProprietaryGoodsAdapter(goodsBean.getData(), mContext);
        mRecycler.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.setOnItemClickListener(R.id.root_view, new BaseAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(View view, int position) {
                new MyHttp().doPost(Api.getDefault().getGoodsDetails(LoginUtil.getLoginToken(), goodsBean.getData().get(position).getGoods_id()), new HttpListener() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        if (result.getString("data")!=null){

                        }else {
                            return;
                        }
                    }

                    @Override
                    public void onError(int code) {

                    }
                });
            }
        });
    }

    public void buy(View view) {
        if (dataList.size()==0){
            ToastUitl.showShort("请选择商品");
            return;
        }


    }
}
