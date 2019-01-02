package com.wbx.merchant.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.wbx.merchant.R;
import com.wbx.merchant.adapter.SelectSubSpecAttrAdapter;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.baseapp.AppConfig;
import com.wbx.merchant.bean.GoodsInfo;
import com.wbx.merchant.widget.LoadingDialog;
import com.wbx.merchant.widget.iosdialog.AlertDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class SelectNatureAttrActivity extends BaseActivity {
    public static final int REQUEST_SELECT_NATURE_ATTR = 1001;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    private List<GoodsInfo.Nature_attr> lstData = new ArrayList<>();
    private SelectSubSpecAttrAdapter adapter;
    private int currentPage = 1;
    private MyHttp myHttp;
    private GoodsInfo.Nature selectSubSpecBean;

    public static void actionStart(Activity activity, GoodsInfo.Nature nature) {
        Intent intent = new Intent(activity, SelectNatureAttrActivity.class);
        intent.putExtra("nature", nature);
        activity.startActivityForResult(intent, REQUEST_SELECT_NATURE_ATTR);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_select_nature_attr;
    }

    @Override
    public void initPresenter() {
        myHttp = new MyHttp();
        selectSubSpecBean = (GoodsInfo.Nature) getIntent().getSerializableExtra("nature");
    }

    @Override
    public void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new SelectSubSpecAttrAdapter(R.layout.item_select_sub_spec_attr, lstData);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.ll_attr:
                        lstData.get(position).setSelect(!lstData.get(position).isSelect());
                        adapter.notifyDataSetChanged();
                        break;
                    case R.id.iv_delete:
                        deleteNature(position);
                        break;
                }
            }
        });
        adapter.setEmptyView(R.layout.empty_sub_spec_attr, recyclerView);
        adapter.setEnableLoadMore(true);
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadMore();
            }
        }, recyclerView);
    }

    @Override
    public void fillData() {
        LoadingDialog.showDialogForLoading(this);
        myHttp.doPost(Api.getDefault().getNatureList(userInfo.getSj_login_token(), 2, selectSubSpecBean.getItem_id(), currentPage, 10), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                List<GoodsInfo.Nature_attr> data = JSONArray.parseArray(result.getString("data"), GoodsInfo.Nature_attr.class);
                if (currentPage == 1) {
                    lstData.clear();
                } else {
                    adapter.loadMoreComplete();
                }
                lstData.addAll(data);
                if (selectSubSpecBean.getNature_arr() != null) {
                    for (GoodsInfo.Nature_attr natureBean : selectSubSpecBean.getNature_arr()) {
                        for (GoodsInfo.Nature_attr lstDatum : lstData) {
                            if (natureBean.getNature_id().equals(lstDatum.getNature_id())) {
                                lstDatum.setSelect(true);
                                break;
                            }
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(int code) {
                if (code == AppConfig.ERROR_STATE.NULLDATA && currentPage != 1) {
                    adapter.loadMoreEnd();
                }
            }
        });
    }

    @Override
    public void setListener() {

    }

    private void deleteNature(final int position) {
        new AlertDialog(this).builder().setTitle("提示")
                .setMsg("将会删除所有商品对该属性的关联，是否确定删除？")
                .setNegativeButton("再想想", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).setPositiveButton("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadingDialog.showDialogForLoading(SelectNatureAttrActivity.this);
                myHttp.doPost(Api.getDefault().deleteNature(userInfo.getSj_login_token(), lstData.get(position).getNature_id()), new HttpListener() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT).show();
                        lstData.remove(position);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(int code) {

                    }
                });
            }
        }).show();
    }

    public void refresh() {
        currentPage = 1;
        fillData();
    }

    public void loadMore() {
        currentPage++;
        fillData();
    }

    @OnClick({R.id.rl_right, R.id.tv_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_right:
                AddNatureAttrActivity.actionStart(SelectNatureAttrActivity.this, selectSubSpecBean.getItem_id());
                break;
            case R.id.tv_save:
                save();
                break;
        }
    }

    private void save() {
        ArrayList<GoodsInfo.Nature_attr> lstSelect = new ArrayList<>();
        for (GoodsInfo.Nature_attr lstDatum : lstData) {
            if (lstDatum.isSelect()) {
                lstSelect.add(lstDatum);
            }
        }
        if (lstSelect.size() == 0) {
            Toast.makeText(mContext, "请选择属性", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent();
        intent.putExtra("selectAttr", lstSelect);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case AddNatureAttrActivity.REQUEST_ADD:
                refresh();
                break;
        }
    }
}
