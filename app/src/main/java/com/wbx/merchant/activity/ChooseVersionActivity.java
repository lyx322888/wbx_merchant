package com.wbx.merchant.activity;

import android.content.Intent;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wbx.merchant.R;
import com.wbx.merchant.adapter.ShopVersionAdapter;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.base.BaseAdapter;
import com.wbx.merchant.bean.ShopEnterParameter;
import com.wbx.merchant.bean.ShopVersionBean;
import com.wbx.merchant.widget.LoadingDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by wushenghui on 2017/12/4.
 * 选择店铺版本  旗舰版/通用版
 */
public class ChooseVersionActivity extends BaseActivity {
    @Bind(R.id.version_rv)
    RecyclerView mRecyclerView;
    ShopVersionAdapter shopVersionAdapter;
    private int gradeId;
    private List<ShopVersionBean.DataBean> lstShopVersion = new ArrayList<>();
    private ShopVersionBean.DataBean selectShopVersion;//选中项

    @Override
    public int getLayoutId() {
        return R.layout.activity_choose_version;
    }

    @Override
    public void initPresenter() {
    }

    @Override
    public void initView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        shopVersionAdapter = new ShopVersionAdapter(lstShopVersion, mContext);
        mRecyclerView.setAdapter(shopVersionAdapter);
    }

    @Override
    public void fillData() {
        gradeId = getIntent().getIntExtra("gradeId", -1);
        LoadingDialog.showDialogForLoading(mActivity, "获取数据中...", true);
        new MyHttp().doPost(Api.getDefault().getShopVersionInfo(gradeId), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                lstShopVersion.addAll(JSONArray.parseArray(result.getString("data"), ShopVersionBean.DataBean.class));
                shopVersionAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(int code) {
            }
        });
    }

    @Override
    public void setListener() {
        shopVersionAdapter.setOnItemClickListener(R.id.root_view, new BaseAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(View view, int position) {
                for (ShopVersionBean.DataBean dataBean : lstShopVersion) {
                    dataBean.setSelect(false);
                }
                selectShopVersion = lstShopVersion.get(position);
                lstShopVersion.get(position).setSelect(true);
                shopVersionAdapter.notifyDataSetChanged();
            }
        });
    }

    public void nextBtn(View view) {
        if (null == selectShopVersion) {
            showShortToast("请选择店铺版本");
            return;
        }
        LoadingDialog.showDialogForLoading(mActivity, "信息提交中...", true);
        new MyHttp().doPost(Api.getDefault().settingsShopGrade(userInfo.getSj_login_token(),selectShopVersion.getShop_grade()), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                ShopEnterParameter shopEnterParameter = new ShopEnterParameter();
                shopEnterParameter.setGradeName(selectShopVersion.getTitle());
                shopEnterParameter.setShopGradeId(selectShopVersion.getShop_grade());
                shopEnterParameter.setNeedPayPrice(selectShopVersion.getSpecial_price());
                shopEnterParameter.setGradeId(gradeId);
//                ElectronicContractActivity.actionStart(mContext, shopEnterParameter);
                Intent intent=new Intent(mContext,PayActivity.class);
                intent.putExtra("select_money",selectShopVersion.getSpecial_price()*100);
                intent.putExtra("gradeId",gradeId);//等级id
                intent.putExtra("gradeName",selectShopVersion.getTitle());//等级id
                intent.putExtra("shopGradeId",shopEnterParameter.getShopGradeId());
                startActivity(intent);
            }

            @Override
            public void onError(int code) {

            }
        });
    }
}
