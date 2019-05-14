package com.wbx.merchant.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.wbx.merchant.R;
import com.wbx.merchant.adapter.GradeAdapter;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.bean.GradeInfoBean;
import com.wbx.merchant.presenter.ShopGradePresenterImp;
import com.wbx.merchant.view.ShopGradeView;

import butterknife.Bind;

public class ChooseShopTypeActivity2  extends BaseActivity implements ShopGradeView {
    @Bind(R.id.cate_recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.grade_recycler_view)
    RecyclerView gRecyclerView;
    private ShopGradePresenterImp shopGradePresenterImp;
    private GradeAdapter gAdapter;
    @Override
    public int getLayoutId() {
        return R.layout.activity_choose_shop_type2;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        gRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        shopGradePresenterImp = new ShopGradePresenterImp(this);
        shopGradePresenterImp.getGrade();
    }

    @Override
    public void fillData() {

    }

    @Override
    public void setListener() {

    }

    @Override
    public void getGrade(GradeInfoBean gradeInfoBean) {
        gAdapter = new GradeAdapter(mContext,gradeInfoBean.getData());
        gRecyclerView.setAdapter(gAdapter);
        gAdapter.setRecyclerViewItemClieck(new GradeAdapter.RecyclerViewItemClieck() {
            @Override
            public void recyclerViewItemClieck(int position, View view, RecyclerView.ViewHolder viewHolder) {
                gAdapter.setGetPosition(position);
                gAdapter.notifyDataSetChanged();
            }
        });
    }
}
