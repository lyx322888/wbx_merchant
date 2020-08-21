package com.wbx.merchant.fragment;


import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.wbx.merchant.R;
import com.wbx.merchant.activity.PublishBusinessCircleActivity;
import com.wbx.merchant.adapter.MyBusinessCircleAdapterNew;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseApplication;
import com.wbx.merchant.base.BaseFragment;
import com.wbx.merchant.baseapp.AppConfig;
import com.wbx.merchant.bean.BusinessCircleBean;
import com.wbx.merchant.common.LoginUtil;
import com.wbx.merchant.utils.AdapterUtilsNew;
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
public class MybusinessFragment extends BaseFragment implements BaseRefreshListener {
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.ptrl)
    PullToRefreshLayout ptrl;
    @Bind(R.id.rl_r)
    RelativeLayout rlR;
    private int mPageNum = AppConfig.pageNum;
    private int mPageSize = AppConfig.pageSize;
    private MyBusinessCircleAdapterNew mAdapter;
    private boolean canLoadMore = true;
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
        ptrl.showView(ViewStatus.LOADING_STATUS);
        ptrl.setCanLoadMore(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new MyBusinessCircleAdapterNew( loginUser);
        View emptyView = LayoutInflater.from(getContext()).inflate(R.layout.layout_empty_find, null);
        mAdapter.setEmptyView(emptyView);
        emptyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PublishBusinessCircleActivity.actionStart(getActivity(), String.valueOf(discover_num));
            }
        });
        recyclerView.setAdapter(mAdapter);
//        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
//            @Override
//            public void onLoadMoreRequested() {
//                mPageNum++;
//                fillData();
//            }
//        },recyclerView);
    }

    @Override
    protected void fillData() {
        new MyHttp().doPost(Api.getDefault().getDiscoveryList(LoginUtil.getLoginToken()), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                List<BusinessCircleBean> data = JSONArray.parseArray(result.getString("data"), BusinessCircleBean.class);
                if (data==null){
                    data=new ArrayList<>();
                }
                discover_num = data.size();
                ptrl.finishRefresh();
                mAdapter.setNewData(data);
                ptrl.showView(ViewStatus.CONTENT_STATUS);
            }

            @Override
            public void onError(int code) {
                if (code == AppConfig.ERROR_STATE.NULLDATA) {
                    //无数据情况下  如果是第一次就是没有数据 直接显示没数据的view
                    mAdapter.setNewData(new ArrayList<BusinessCircleBean>());
                    discover_num = 0;
                    ptrl.finishRefresh();
                    ptrl.showView(ViewStatus.CONTENT_STATUS);
                } else if (code == AppConfig.ERROR_STATE.NO_NETWORK || code == AppConfig.ERROR_STATE.SERVICE_ERROR) {
                    ptrl.showView(ViewStatus.ERROR_STATUS);
                    ptrl.buttonClickError(MybusinessFragment.this, "fillData");
                } else {

                }
                ptrl.finishRefresh();
            }
        });

    }

    @Override
    protected void bindEven() {
        ptrl.setRefreshListener(this);

    }


    @OnClick(R.id.rl_release)
    public void onViewClicked() {
        PublishBusinessCircleActivity.actionStart(getActivity(), String.valueOf(discover_num));
    }

    @Override
    public void refresh() {
        canLoadMore = true;
        mPageNum = AppConfig.pageNum;
        fillData();
    }

    @Override
    public void loadMore() {

    }
}
