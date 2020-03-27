package com.wbx.merchant.activity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wbx.merchant.R;
import com.wbx.merchant.adapter.ProductAdapter;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.base.BaseAdapter;
import com.wbx.merchant.bean.CateInfo;
import com.wbx.merchant.widget.LoadingDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import rx.Observable;

/**
 * Created by Administrator on 2017/11/6 0006.
 */

public class ProductActivity extends BaseActivity {
    @Bind(R.id.recycler_product)
    RecyclerView recyclerProduct;
    private ProductAdapter productAdapter;
    private List<CateInfo> dataList = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_product;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        recyclerProduct.setLayoutManager(new LinearLayoutManager(this));
        recyclerProduct.setHasFixedSize(true);
        productAdapter = new ProductAdapter(dataList, mContext);
        recyclerProduct.setAdapter(productAdapter);
    }

    @Override
    public void fillData() {
        LoadingDialog.showDialogForLoading(mActivity, "加载中...", true);
        Observable<JSONObject> getproduct = Api.getDefault().getproduct(userInfo.getSj_login_token());
        new MyHttp().doPost(getproduct, new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                List<CateInfo> cateInfos = JSONArray.parseArray(result.getString("data"), CateInfo.class);
                dataList.addAll(cateInfos);
                productAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    @Override
    public void setListener() {
        productAdapter.setOnItemClickListener(R.id.root_view, new BaseAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(View view, int position) {
                MaterialCenterActivity.actionStart(ProductActivity.this, dataList.get(position).getCate_id());
            }
        });
    }
}