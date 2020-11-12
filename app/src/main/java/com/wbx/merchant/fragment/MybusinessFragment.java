package com.wbx.merchant.fragment;


import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.flyco.tablayout.SlidingTabLayout;
import com.google.gson.Gson;
import com.wbx.merchant.R;
import com.wbx.merchant.activity.CriclePutlnPayActivity;
import com.wbx.merchant.activity.NewCirclePutInActivity;
import com.wbx.merchant.activity.PublishBusinessCircleActivity;
import com.wbx.merchant.adapter.MyBusinessCircleAdapterNew;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseFragment;
import com.wbx.merchant.baseapp.AppConfig;
import com.wbx.merchant.bean.BusinessCircleBean;
import com.wbx.merchant.bean.CircleFansMoneyBean;
import com.wbx.merchant.common.LoginUtil;
import com.wbx.merchant.dialog.SelectDialog;
import com.wbx.merchant.widget.DragImageView;
import com.wbx.merchant.widget.refresh.BaseRefreshListener;
import com.wbx.merchant.widget.refresh.PullToRefreshLayout;
import com.wbx.merchant.widget.refresh.ViewStatus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 有acvitity迁移过来
 * A simple {@link Fragment} subclass.
 */
public class MybusinessFragment extends BaseFragment  {

    @Bind(R.id.rl_r)
    RelativeLayout rlR;
    @Bind(R.id.rl_release)
    RelativeLayout rlRelease;
    @Bind(R.id.tv_price)
    TextView tvPrice;
    @Bind(R.id.tv_qfxq)
    TextView tvQfxq;
    @Bind(R.id.tv_qfcz)
    TextView tvQfcz;
    @Bind(R.id.lab_layout)
    SlidingTabLayout labLayout;
    @Bind(R.id.order_view_pager)
    ViewPager orderViewPager;
    @Bind(R.id.div)
    DragImageView dragImageView;

    private int discover_num;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_my_business_circle;
    }

    @Override
    public void initPresenter() {

    }

    //隐藏返回键
    public void setVisibilityHead(int visibility) {
        rlR.setVisibility(visibility);
    }

    @Override
    protected void initView() {
//        ptrl.showView(ViewStatus.LOADING_STATUS);
//        ptrl.setCanLoadMore(false);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        mAdapter = new MyBusinessCircleAdapterNew(loginUser);
//        View emptyView = LayoutInflater.from(getContext()).inflate(R.layout.layout_empty_find, null);
//        mAdapter.setEmptyView(emptyView);
//        emptyView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                PublishBusinessCircleActivity.actionStart(getActivity(), String.valueOf(discover_num));
//            }
//        });
//        recyclerView.setAdapter(mAdapter);


//        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
//            @Override
//            public void onLoadMoreRequested() {
//                mPageNum++;
//                fillData();
//            }
//        },recyclerView);



         String[] mTitles = new String[]{"全部资讯", "已投圈粉", "未投圈粉"};
        ArrayList<Fragment> arrayList = new ArrayList<>();
        arrayList.add(ItemBusinessDistrictFragment.newInstance());
        arrayList.add(ItemBusinessDistrictFragment.newInstance());
        arrayList.add(ItemBusinessDistrictFragment.newInstance());
        labLayout.setViewPager(orderViewPager,mTitles,getActivity(),arrayList);

    }

    @Override
    protected void fillData() {
//        new MyHttp().doPost(Api.getDefault().getDiscoveryList(LoginUtil.getLoginToken()), new HttpListener() {
//            @Override
//            public void onSuccess(JSONObject result) {
//                List<BusinessCircleBean> data = JSONArray.parseArray(result.getString("data"), BusinessCircleBean.class);
//                if (data == null) {
//                    data = new ArrayList<>();
//                }
//                discover_num = data.size();
//                ptrl.finishRefresh();
//                mAdapter.setNewData(data);
//                ptrl.showView(ViewStatus.CONTENT_STATUS);
//            }
//
//            @Override
//            public void onError(int code) {
//                if (code == AppConfig.ERROR_STATE.NULLDATA) {
//                    //无数据情况下  如果是第一次就是没有数据 直接显示没数据的view
//                    mAdapter.setNewData(new ArrayList<BusinessCircleBean>());
//                    discover_num = 0;
//                    ptrl.finishRefresh();
//                    ptrl.showView(ViewStatus.CONTENT_STATUS);
//                } else if (code == AppConfig.ERROR_STATE.NO_NETWORK || code == AppConfig.ERROR_STATE.SERVICE_ERROR) {
//                    ptrl.showView(ViewStatus.ERROR_STATUS);
//                    ptrl.buttonClickError(MybusinessFragment.this, "fillData");
//                } else {
//
//                }
//                ptrl.finishRefresh();
//            }
//        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getQfprice();
    }

    private void getQfprice(){
        new MyHttp().doPost(Api.getDefault().get_draw_fans_money(LoginUtil.getLoginToken()), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                CircleFansMoneyBean circleFansMoneyBean = new Gson().fromJson(result.toString(), CircleFansMoneyBean.class);
                tvPrice.setText(circleFansMoneyBean.getData().getDraw_fans_money()/100.00+"");
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    @Override
    protected void bindEven() {

    }

    @OnClick({R.id.tv_qfcz,R.id.tv_qfxq,R.id.div})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.div:
                    SelectDialog dialog = new SelectDialog(getContext());
                    dialog.show();
//                PublishBusinessCircleActivity.actionStart(getActivity(), String.valueOf(discover_num));
                break;
            case R.id.tv_qfcz:
                //圈粉充值
                startActivity(new Intent(getContext(), CriclePutlnPayActivity.class));
                break;
            case R.id.tv_qfxq:
                //圈粉详情
                startActivity(new Intent(getContext(), NewCirclePutInActivity.class));
                break;
        }
    }



}
