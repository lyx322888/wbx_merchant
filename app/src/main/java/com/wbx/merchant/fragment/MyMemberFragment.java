package com.wbx.merchant.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wbx.merchant.R;
import com.wbx.merchant.activity.MemberDetailActivity;
import com.wbx.merchant.adapter.MyMemberAdapter;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseFragment;
import com.wbx.merchant.baseapp.AppConfig;
import com.wbx.merchant.bean.MemberBean;
import com.wbx.merchant.widget.refresh.BaseRefreshListener;
import com.wbx.merchant.widget.refresh.PullToRefreshLayout;
import com.wbx.merchant.widget.refresh.ViewStatus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by wushenghui on 2017/7/25.
 */

public class MyMemberFragment extends BaseFragment implements BaseRefreshListener {
    public static final String POSITION = "position";
    private MyMemberAdapter mAdapter;
    private List<MemberBean> lstData = new ArrayList<>();
    private int position;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.ptrl)
    PullToRefreshLayout ptrl;
    private int currentPage = 1;
    private MyHttp myHttp;

    public static MyMemberFragment newInstance(int position) {
        MyMemberFragment tabFragment = new MyMemberFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(POSITION, position);
        tabFragment.setArguments(bundle);
        return tabFragment;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_my_member;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    protected void initView() {
        myHttp = new MyHttp();
        position = getArguments().getInt(POSITION, 1);
        ptrl.showView(ViewStatus.LOADING_STATUS);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new MyMemberAdapter(this, position);
        recyclerView.setAdapter(mAdapter);
        ptrl.setRefreshListener(this);
    }

    @Override
    protected void fillData() {
        ptrl.showView(ViewStatus.LOADING_STATUS);
        loadData(false);
    }

    private void loadData(final boolean isLoadMore) {
        if (isLoadMore) {
            currentPage++;
        } else {
            ptrl.setCanLoadMore(true);
            currentPage = 1;
        }
        myHttp.doPost(Api.getDefault().getMyMemberList(loginUser.getSj_login_token(), position, currentPage, 10), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                if (ptrl == null) {
                    //页面已销毁
                    return;
                }
                if (isLoadMore) {
                    ptrl.finishLoadMore();
                } else {
                    ptrl.finishRefresh();
                }
                List<MemberBean> data = JSONArray.parseArray(result.getString("data"), MemberBean.class);
                if (!isLoadMore && (data == null || data.size() == 0)) {
                    ptrl.showView(ViewStatus.EMPTY_STATUS);
                    ptrl.buttonClickNullData(MyMemberFragment.this, "refresh");
                    return;
                }
                ptrl.showView(ViewStatus.CONTENT_STATUS);
                if (!isLoadMore) {
                    lstData.clear();
                }
                lstData.addAll(data);
                mAdapter.update(lstData);
            }

            @Override
            public void onError(int code) {
                if (ptrl == null) {
                    //页面已销毁
                    return;
                }
                if (isLoadMore) {
                    ptrl.finishLoadMore();
                } else {
                    ptrl.finishRefresh();
                }
                if (code == AppConfig.ERROR_STATE.NULLDATA && ptrl != null) {
                    if (isLoadMore) {
                        ptrl.setCanLoadMore(false);
                    } else {
                        ptrl.showView(ViewStatus.EMPTY_STATUS);
                        ptrl.buttonClickNullData(MyMemberFragment.this, "refresh");
                    }
                } else {
                    ptrl.showView(ViewStatus.ERROR_STATUS);
                    ptrl.buttonClickError(MyMemberFragment.this, "refresh");
                }
            }
        });
    }

    @Override
    protected void bindEven() {
    }

    @Override
    public void refresh() {
        loadData(false);
    }

    @Override
    public void loadMore() {
        loadData(true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MemberDetailActivity.MODIFY_IS_MEMBER && data != null) {
            MemberBean member = (MemberBean) data.getSerializableExtra("member");
            for (int i = 0; i < lstData.size(); i++) {
                if (lstData.get(i).getUser_id().equals(member.getUser_id())) {
                    lstData.set(i, member);
                    break;
                }
            }
            mAdapter.update(lstData);
        }
    }
}
