package com.wbx.merchant.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wbx.merchant.R;
import com.wbx.merchant.adapter.UpDateNumAdapter;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.baseapp.AppConfig;
import com.wbx.merchant.bean.GoodsInfo;
import com.wbx.merchant.bean.SpecInfo;
import com.wbx.merchant.bean.UpdateNumInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;

/**
 * Created by wushenghui on 2017/9/30.
 */

public class UpDateNumActivity extends BaseActivity {
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private List<SpecInfo> specInfoList = new ArrayList<>();
    private UpDateNumAdapter mAdapter;
    private HashMap<String, Object> mParams = new HashMap<>();
    private GoodsInfo goods;

    @Override
    public int getLayoutId() {
        return R.layout.activity_updatenum;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));

    }

    @Override
    public void fillData() {
        goods = (GoodsInfo) getIntent().getSerializableExtra("goods");
        mParams.put("sj_login_token", userInfo.getSj_login_token());
        mParams.put(userInfo.getGrade_id() == AppConfig.StoreGrade.MARKET ? "product_id" : "goods_id", goods.getProduct_id());
        mParams.put("is_attr", goods.getIs_attr());
        if (goods.getIs_attr() == 1) {
            specInfoList = goods.getGoods_attr();
            mParams.put("num", 0);
            mParams.put("loss", 0);
        } else {
            SpecInfo specInfo = new SpecInfo();
            specInfo.setAttr_name(goods.getProduct_name());
            specInfo.setNum((int)goods.getNum());
            specInfo.setLoss(goods.getLoss());
            specInfoList.add(specInfo);
            mParams.put("num", goods.getNum());
            mParams.put("loss", goods.getLoss());
        }
        mAdapter = new UpDateNumAdapter(specInfoList, mContext);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void setListener() {

        findViewById(R.id.complete_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
            }
        });
    }

    private void submit() {
        if (goods.getIs_attr() == 1) {
            List<SpecInfo> specData = mAdapter.getSpecData();
            List<UpdateNumInfo> updateNumInfos = new ArrayList<>();
            updateNumInfos.clear();
            for (SpecInfo specInfo : specData) {
                UpdateNumInfo updateNumInfo = new UpdateNumInfo();
                updateNumInfo.setAttr_id(specInfo.getAttr_id());
                updateNumInfo.setLoss(specInfo.getLoss());
                updateNumInfo.setNum(specInfo.getNum());
                updateNumInfos.add(updateNumInfo);
            }
            mParams.put("goods_attr", JSONArray.toJSON(updateNumInfos));
        } else {
            mParams.put("num", mAdapter.getSpecData().get(0).getNum());
            mParams.put("loss", mAdapter.getSpecData().get(0).getLoss());
        }
        new MyHttp().doPost(Api.getDefault().updateGoodsNum(mParams), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                showShortToast("修改成功！");
                finish();
            }

            @Override
            public void onError(int code) {

            }
        });
    }
}
