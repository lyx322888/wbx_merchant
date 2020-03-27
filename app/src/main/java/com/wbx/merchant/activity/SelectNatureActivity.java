package com.wbx.merchant.activity;

import android.app.Activity;
import android.content.Intent;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.wbx.merchant.R;
import com.wbx.merchant.adapter.SelectNatureAdapter;
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

public class SelectNatureActivity extends BaseActivity {
    public static final int REQUEST_SELECT_NATURE = 1000;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    private List<GoodsInfo.Nature> lstData = new ArrayList<>();
    private ArrayList<GoodsInfo.Nature> lstDelete = new ArrayList<>();
    private ArrayList<GoodsInfo.Nature> lstModify = new ArrayList<>();
    private SelectNatureAdapter adapter;
    private int currentPage = 1;
    private MyHttp myHttp;
    private int selectPosition = -1;

    public static void actionStart(Activity activity) {
        Intent intent = new Intent(activity, SelectNatureActivity.class);
        activity.startActivityForResult(intent, REQUEST_SELECT_NATURE);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_select_nature;
    }

    @Override
    public void initPresenter() {
        myHttp = new MyHttp();
    }

    @Override
    public void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new SelectNatureAdapter(R.layout.item_select_sub_spec_project, lstData);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.ll_project:
                        selectPosition = position;
                        onBackPressed();
                        break;
                    case R.id.iv_delete:
                        deleteNature(position);
                        break;
                    case R.id.iv_edit:
                        EditNatureActivity.actionStart(SelectNatureActivity.this, lstData.get(position));
                        break;
                }
            }
        });
        View emptyView = LayoutInflater.from(this).inflate(R.layout.empty_sub_spec_attr, null);
        ((TextView) emptyView.findViewById(R.id.tv_hint)).setText("尚未添加任何规格项目");
        adapter.setEmptyView(emptyView);
        adapter.setEnableLoadMore(true);
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadMore();
            }
        }, recyclerView);
    }

    @Override
    public void back(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        if (selectPosition != -1) {
            intent.putExtra("selectNature", lstData.get(selectPosition));
        }
        intent.putExtra("lstDelete", lstDelete);
        intent.putExtra("lstModify", lstModify);
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }

    private void deleteNature(final int position) {
        new AlertDialog(this).builder().setTitle("提示")
                .setMsg("将会取消所有商品对该规格的关联，同时会删除该规格项目的所有属性，是否确定删除？")
                .setNegativeButton("再想想", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).setPositiveButton("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadingDialog.showDialogForLoading(SelectNatureActivity.this);
                myHttp.doPost(Api.getDefault().deleteNature(userInfo.getSj_login_token(), lstData.get(position).getItem_id()), new HttpListener() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT).show();
                        lstDelete.add(lstData.get(position));
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

    @Override
    public void fillData() {
        LoadingDialog.showDialogForLoading(this);
        myHttp.doPost(Api.getDefault().getNatureList(userInfo.getSj_login_token(), 1, null, currentPage, 10), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                List<GoodsInfo.Nature_attr> data = JSONArray.parseArray(result.getString("data"), GoodsInfo.Nature_attr.class);
                if (currentPage == 1) {
                    lstData.clear();
                } else {
                    adapter.loadMoreComplete();
                }
                if (data != null) {
                    for (GoodsInfo.Nature_attr datum : data) {
                        GoodsInfo.Nature nature = new GoodsInfo.Nature();
                        nature.setIs_use(datum.getIs_use());
                        nature.setItem_id(datum.getNature_id());
                        nature.setItem_name(datum.getNature_name());
                        lstData.add(nature);
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

    @OnClick(R.id.rl_right)
    public void onViewClicked() {
        EditNatureActivity.actionStart(this);
    }

    public void refresh() {
        currentPage = 1;
        fillData();
    }

    public void loadMore() {
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
            case EditNatureActivity.REQUEST_ADD:
                refresh();
                break;
            case EditNatureActivity.REQUEST_EDIT:
                GoodsInfo.Nature spec = (GoodsInfo.Nature) data.getSerializableExtra("spec");
                for (GoodsInfo.Nature lstDatum : lstData) {
                    if (!TextUtils.isEmpty(lstDatum.getItem_id()) && lstDatum.getItem_id().equals(spec.getItem_id())) {
                        lstDatum.setItem_name(spec.getItem_name());
                        lstModify.add(lstDatum);
                        break;
                    }
                }
                adapter.notifyDataSetChanged();
                break;
        }
    }


}
