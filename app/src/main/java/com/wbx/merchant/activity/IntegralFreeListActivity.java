package com.wbx.merchant.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.wbx.merchant.R;
import com.wbx.merchant.adapter.IntegralFreeAdapter;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.bean.IntegralFreeGoodsBean;
import com.wbx.merchant.common.LoginUtil;
import com.wbx.merchant.utils.ToastUitl;
import com.wbx.merchant.widget.LoadingDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class IntegralFreeListActivity extends BaseActivity implements OnRefreshListener, OnLoadMoreListener {
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.srl)
    SmartRefreshLayout srl;
    List<IntegralFreeGoodsBean> lstData = new ArrayList<>();
    private MyHttp myHttp;
    private int currentPage = 1;
    private IntegralFreeAdapter adapter;
    private int editPosition = -1;

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, IntegralFreeListActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_integral_free_list;
    }

    @Override
    public void initPresenter() {
        myHttp = new MyHttp();
    }

    @Override
    public void initView() {
        srl.setOnRefreshListener(this);
        srl.setOnLoadMoreListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new IntegralFreeAdapter(lstData);
        adapter.setEmptyView(R.layout.empty_integral_free_layout, recyclerView);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void fillData() {
        LoadingDialog.showDialogForLoading(this);
        myHttp.doPost(Api.getDefault().getIntegralFreeGoodsList(LoginUtil.getLoginToken(), currentPage, 10), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                if (currentPage == 1) {
                    srl.finishRefresh();
                    lstData.clear();
                } else {
                    srl.finishLoadMore();
                }
                List<IntegralFreeGoodsBean> data = JSONArray.parseArray(result.getString("data"), IntegralFreeGoodsBean.class);
                if (data != null) {
                    if (data.size() < 10) {
                        srl.setEnableLoadMore(false);
                    }
                    lstData.addAll(data);
                    adapter.notifyDataSetChanged();
                } else {
                    srl.setEnableLoadMore(false);
                }
            }

            @Override
            public void onError(int code) {
                if (currentPage == 1) {
                    srl.finishRefresh();
                } else {
                    srl.finishLoadMore();
                }
            }
        });
    }

    @Override
    public void setListener() {
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.ll_edit:
                        AddIntegralFreeActivity.actionStart(IntegralFreeListActivity.this, lstData.get(position));
                        editPosition = position;
                        break;
                    case R.id.ll_delete:
                        toDelete(position);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    /**
     * 删除
     *
     * @param position
     */
    private void toDelete(final int position) {
        LoadingDialog.showDialogForLoading(this);
        myHttp.doPost(Api.getDefault().deleteFreeGoods(LoginUtil.getLoginToken(), lstData.get(position).getGoods_id(), "accumulate"), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                ToastUitl.showShort("删除成功");
                lstData.remove(position);
                adapter.notifyItemRemoved(position);
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    @OnClick(R.id.btn_add)
    public void onViewClicked() {
        SelectFreeActivityGoodsListActivity.actionStart(this, SelectFreeActivityGoodsListActivity.ACTIVITY_TYPE_ACCUMULATE);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        srl.setEnableLoadMore(true);
        currentPage = 1;
        fillData();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        currentPage++;
        fillData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case AddIntegralFreeActivity.REQUEST_EDIT:
                IntegralFreeGoodsBean integralFreeGoodsBean = (IntegralFreeGoodsBean) data.getSerializableExtra("data");
                boolean isDelete = data.getBooleanExtra("isDelete", false);
                if (isDelete) {
                    lstData.remove(editPosition);
                    adapter.notifyItemRemoved(editPosition);
                } else {
                    lstData.set(editPosition, integralFreeGoodsBean);
                    adapter.notifyDataSetChanged();
                }
                break;
            case SelectFreeActivityGoodsListActivity.REQUEST_ADD:
                IntegralFreeGoodsBean integralFreeGoodsBean2 = (IntegralFreeGoodsBean) data.getSerializableExtra("data");
                lstData.add(integralFreeGoodsBean2);
                adapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }
}