package com.wbx.merchant.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.wbx.merchant.R;
import com.wbx.merchant.adapter.OpenRadarAdapter;
import com.wbx.merchant.adapter.viewpageadapter.CustomerFragmentStateAdapter;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.bean.BindAllUserBean;
import com.wbx.merchant.bean.BindUserBean;
import com.wbx.merchant.bean.OpenRadarBean;
import com.wbx.merchant.common.LoginUtil;
import com.wbx.merchant.presenter.OpenRadarPresenterImp;
import com.wbx.merchant.utils.RetrofitUtils;
import com.wbx.merchant.utils.ToastUitl;
import com.wbx.merchant.view.OpenRadarView;
import com.wbx.merchant.widget.LoadingDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import cn.jpush.android.ui.PopWinActivity;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by wushenghui on 2017/7/25.
 * 客户管理
 */

public class CustomerManagerActivity extends BaseActivity implements OpenRadarView {
    @Bind(R.id.order_lab_layout)
    TabLayout mTabLayout;
    @Bind(R.id.order_view_pager)
    ViewPager mOrderViewPager;
    //    private String[] mTitles = new String[]{"最近消费", "次数最多", "金额最多", "购买过的客户", "关注过的客户"};
    private String[] mTitles = new String[]{"购买客户", "关注客户","雷达客户"};
    private CustomerFragmentStateAdapter mAdapter;
    private PopupWindow p;
    private RecyclerView mRecyclerView;
    private Button all_bind;
    private Dialog mLoadingDialog;
    private List<OpenRadarBean.DataBean> list = new ArrayList<>();
    private  ImageView radar_img;
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, CustomerManagerActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_customer_manager;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
    }

    @Override
    public void fillData() {
        mAdapter = new CustomerFragmentStateAdapter(getSupportFragmentManager(), mTitles);
        mOrderViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mOrderViewPager);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    @Override
    public void setListener() {

    }

    public void radar(View view) {
//        showLoadingDialog("正在扫描用户...");
        changeWindowAlfa(0.7f);
        view = View.inflate(this, R.layout.layout_radar, null);
        int width = getWindowManager().getDefaultDisplay().getWidth();
        int height = getWindowManager().getDefaultDisplay().getHeight();
        p = new PopupWindow(view, width * 8 / 10, height * 6 / 10);
        p.setTouchable(true);
        p.setFocusable(true);
        p.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.cc)));
        p.showAtLocation(view, Gravity.CENTER, 0, 0);
        p.showAsDropDown(view, 100, 100);
        p.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                changeWindowAlfa(1f);//pop消失，透明度恢复
            }
        });
        mRecyclerView = view.findViewById(R.id.recycler_radar);
        radar_img=view.findViewById(R.id.radar_img);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        OpenRadarPresenterImp openRadarPresenterImp = new OpenRadarPresenterImp(this);
        if (openRadarPresenterImp!=null){
            openRadarPresenterImp.getOpenRadar(LoginUtil.getLoginToken());
            mRecyclerView.setVisibility(View.VISIBLE);
            radar_img.setVisibility(View.GONE);
        }else{
            mRecyclerView.setVisibility(View.GONE);
            radar_img.setVisibility(View.VISIBLE);
        }

        p.setFocusable(true);
        p.setOutsideTouchable(true);


//        all_bind=view.findViewById(R.id.all_bind);

    }

    public void changeWindowAlfa(float alfa) {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = alfa;
        getWindow().setAttributes(params);
    }


    @Override
    public void getOpenRadar(final OpenRadarBean openRadarBean) {
        OpenRadarAdapter openRadarAdapter = new OpenRadarAdapter(mContext, openRadarBean.getData());
        mRecyclerView.setAdapter(openRadarAdapter);
        list.addAll(openRadarBean.getData());
        openRadarAdapter.notifyDataSetChanged();
    }

    public void all_bind(View view) {
        RetrofitUtils.getInstance().server().getBindAllUser(LoginUtil.getLoginToken())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<BindAllUserBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(BindAllUserBean bindAllUserBean) {
                        if (bindAllUserBean.getMsg().equals("成功")) {
                            OpenRadarAdapter openRadarAdapter = new OpenRadarAdapter(mContext, list);
                            mRecyclerView.setAdapter(openRadarAdapter);
                            openRadarAdapter.notifyDataSetChanged();
                            ToastUitl.showShort("全部绑定成功!");
                            p.dismiss();
                        }
                    }
                });
    }


}
