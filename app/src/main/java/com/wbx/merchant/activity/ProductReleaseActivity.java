package com.wbx.merchant.activity;


import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wbx.merchant.R;
import com.wbx.merchant.adapter.ProductReleaseAdapter;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.base.BaseAdapter;
import com.wbx.merchant.bean.MaterialInfoBean;
import com.wbx.merchant.bean.ProductJson;
import com.wbx.merchant.widget.iosdialog.AlertDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import rx.Observable;

/**
 * Created by Administrator on 2017/11/6 0006.
 */

public class ProductReleaseActivity extends BaseActivity {
    private List<MaterialInfoBean.ProductBean> dataList = new ArrayList<>();
    private List<ProductJson> productJsonList = new ArrayList<>();
    private ProductReleaseAdapter mReleaseAdapter;
    @Bind(R.id.produt_view_release)
    RecyclerView mReleaseview;
    private int selectPosition;
    private int cateId;

    @Override
    public int getLayoutId() {
        return R.layout.product_release;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        mReleaseview.setLayoutManager(new GridLayoutManager(mContext, 2));
        mReleaseAdapter = new ProductReleaseAdapter(dataList, mContext);
        mReleaseview.setAdapter(mReleaseAdapter);
    }

    @Override
    public void fillData() {
        List<MaterialInfoBean.ProductBean> checkedList = (List<MaterialInfoBean.ProductBean>) getIntent().getSerializableExtra("checkedList");
        cateId = getIntent().getIntExtra("cateId", 0);
        dataList.addAll(checkedList);
        for (MaterialInfoBean.ProductBean productInfo : dataList) {
            productInfo.setPrice((Double.valueOf(productInfo.getPrice()) / 100.00) + "");
        }
        mReleaseAdapter.notifyDataSetChanged();
    }

    @Override
    public void setListener() {
        mReleaseAdapter.setOnItemClickListener(R.id.item_recycler_delete, new BaseAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(View view, int position) {
                selectPosition = position;
                new AlertDialog(mContext).builder()
                        .setTitle("提示")
                        .setMsg("是否删除此项？")
                        .setNegativeButton("再想想", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        })
                        .setPositiveButton("删除", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mReleaseAdapter.isDel = true;
                                dataList.remove(selectPosition);
                                mReleaseAdapter.notifyDataSetChanged();
                            }
                        }).show();
            }
        });
        findViewById(R.id.release_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productJsonList.clear();
                for (MaterialInfoBean.ProductBean productInfo : dataList) {
                    ProductJson productJson = new ProductJson();
                    productJson.setPrice(productInfo.getPrice());
                    productJson.setName(productInfo.getName());
                    productJson.setCate_id(cateId);
                    productJson.setPhoto(productInfo.getPhoto());
                    productJsonList.add(productJson);
                }
                String jsonString = JSON.toJSONString(productJsonList);
                Observable<JSONObject> j = Api.getDefault().getproductrelease(userInfo.getSj_login_token(),
                        jsonString);
                new MyHttp().doPost(j, new HttpListener() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        showHintDialog();
                    }

                    @Override
                    public void onError(int code) {
                    }
                });
            }
        });
    }

    private void showHintDialog() {
        new AlertDialog(mActivity).builder()
                .setTitle("提示")
                .setMsg("产品已经发布成功，正在等待审核，是否立即查看？")
                .setNegativeButton("继续发布", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                })
                .setPositiveButton("立即查看", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        GoodsManagerActivity.actionStart(mContext);
                        finish();
                    }
                }).show();
    }
}
