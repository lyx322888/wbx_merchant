package com.wbx.merchant.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;

import com.example.popwindowutils.CustomPopWindow;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.wbx.merchant.R;
import com.wbx.merchant.adapter.OpenRadarAdapter;
import com.wbx.merchant.adapter.viewpageadapter.CustomerFragmentStateAdapter;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.baseapp.AppConfig;
import com.wbx.merchant.bean.BindAllUserBean;
import com.wbx.merchant.bean.OpenRadarBean;
import com.wbx.merchant.common.LoginUtil;
import com.wbx.merchant.presenter.OpenRadarPresenterImp;
import com.wbx.merchant.utils.RetrofitUtils;
import com.wbx.merchant.utils.SPUtils;
import com.wbx.merchant.utils.SpannableStringUtils;
import com.wbx.merchant.utils.ToastUitl;
import com.wbx.merchant.view.OpenRadarView;
import com.wbx.merchant.widget.refresh.BaseRefreshListener;
import com.wbx.merchant.widget.refresh.PullToRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by wushenghui on 2017/7/25.
 * 客户管理
 */

public class CustomerManagerActivity extends BaseActivity {
    @Bind(R.id.order_lab_layout)
    TabLayout mTabLayout;
    @Bind(R.id.order_view_pager)
    ViewPager mOrderViewPager;
    //    private String[] mTitles = new String[]{"最近消费", "次数最多", "金额最多", "购买过的客户", "关注过的客户"};
    private String[] mTitles = new String[]{"购买客户", "关注客户", "雷达客户"};
    private CustomerFragmentStateAdapter mAdapter;
    private PopupWindow p;
    private RecyclerView mRecyclerView;
    private Button all_bind;
    private Dialog mLoadingDialog;
    private List<OpenRadarBean.DataBean> list = new ArrayList<>();
    private OpenRadarAdapter openRadarAdapter;
    private int page = 1;//雷达分页

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
            //试用版提示框
            if (SPUtils.getSharedIntData(mContext, AppConfig.TRY_SHOP) == 1) {
                mTabLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        final View inflate = LayoutInflater.from(mContext).inflate(R.layout.pop_restrict_goods, null);
                        ImageView ivDsj = inflate.findViewById(R.id.iv_dsj_l);
                        ImageView ivGb = inflate.findViewById(R.id.iv_gb);
                        TextView tvType = inflate.findViewById(R.id.tv_type);
                        TextView tv_ljkt = inflate.findViewById(R.id.tv_ljkt);
                        TextView tvCs = inflate.findViewById(R.id.tv_cs);
                        ivDsj.setVisibility(View.VISIBLE);
                        tvType.setText("客户管理");
                        tvCs.setText(SpannableStringUtils.getBuilder("可管理客户 10").create());
                        final CustomPopWindow customPopWindow = new CustomPopWindow.PopupWindowBuilder(mContext)
                                .setView(inflate)
                                .size(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                                .create()
                                .showAsDropDown(mTabLayout,0,0, Gravity.BOTTOM);
                        ivGb.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                customPopWindow.dissmiss();
                            }
                        });
                        tv_ljkt.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                startActivity(new Intent(mContext, ChooseShopVersionsPrwActivity.class));
                            }
                        });

                    }
                });

            }
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
        //雷达分页数

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
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        final PullToRefreshLayout refreshLayout = view.findViewById(R.id.refresh_layout);

        final OpenRadarPresenterImp openRadarPresenterImp = new OpenRadarPresenterImp(new OpenRadarView() {
            @Override
            public void getOpenRadar(OpenRadarBean openRadarBean) {
                list.addAll(openRadarBean.getData());
                openRadarAdapter.notifyDataSetChanged();
                refreshLayout.finishLoadMore();
                refreshLayout.finishRefresh();
            }
        });
        openRadarPresenterImp.getOpenRadar(LoginUtil.getLoginToken(), page,10);

        p.setFocusable(true);
        p.setOutsideTouchable(true);

        openRadarAdapter = new OpenRadarAdapter(mContext, list);
        mRecyclerView.setAdapter(openRadarAdapter);
        //刷新
        refreshLayout.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                page = 1;
                list.clear();
                openRadarPresenterImp.getOpenRadar(LoginUtil.getLoginToken(), page,10);
            }

            @Override
            public void loadMore() {
                page += 1;
                openRadarPresenterImp.getOpenRadar(LoginUtil.getLoginToken(), page,10);
            }
        });

//        all_bind=view.findViewById(R.id.all_bind);

    }

    public void changeWindowAlfa(float alfa) {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = alfa;
        getWindow().setAttributes(params);
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
