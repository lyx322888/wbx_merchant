package com.wbx.merchant.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.wbx.merchant.R;
import com.wbx.merchant.adapter.AddConsumeFreeGoodsListAdapter;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.baseapp.AppConfig;
import com.wbx.merchant.bean.CanFreeGoodsBean;
import com.wbx.merchant.common.LoginUtil;
import com.wbx.merchant.utils.KeyBordUtil;
import com.wbx.merchant.widget.LoadingDialog;
import com.wbx.merchant.widget.decoration.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class SelectFreeActivityGoodsListActivity extends BaseActivity implements OnLoadMoreListener, OnRefreshListener {
    public static final int REQUEST_ADD = 1000;
    public static final String ACTIVITY_TYPE_CONSUME = "consume";
    public static final String ACTIVITY_TYPE_SHARE = "share";
    public static final String ACTIVITY_TYPE_ACCUMULATE = "accumulate";
    @Bind(R.id.et_search)
    EditText etSearch;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.srl)
    SmartRefreshLayout srl;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_hint)
    TextView tvHint;
    private List<CanFreeGoodsBean> lstData = new ArrayList<>();
    private List<CanFreeGoodsBean> lstSearchData = new ArrayList<>();
    private int currentPage = 1;
    private MyHttp myHttp;
    private AddConsumeFreeGoodsListAdapter adapter;
    private String freeActivityType;

    public static void actionStart(Activity context, String activityType) {
        Intent intent = new Intent(context, SelectFreeActivityGoodsListActivity.class);
        intent.putExtra("freeActivityType", activityType);
        context.startActivityForResult(intent, REQUEST_ADD);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_select_free_activity_goods_list;
    }

    @Override
    public void initPresenter() {
        myHttp = new MyHttp();
        freeActivityType = getIntent().getStringExtra("freeActivityType");
    }

    @Override
    public void initView() {
        switch (freeActivityType) {
            case "consume":
                tvTitle.setText("消费免单");
                tvHint.setText("请选择参加消费免单的商品");
                break;
            case "share":
                tvTitle.setText("分享免单");
                tvHint.setText("请选择参加分享免单的商品");
                break;
            case "accumulate":
                tvTitle.setText("积分免单");
                tvHint.setText("请选择参加积分免单的商品");
                break;
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AddConsumeFreeGoodsListAdapter(lstData);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.root_view:
                        switch (freeActivityType) {
                            case "consume":
                                AddConsumeFreeActivity.actionStart(SelectFreeActivityGoodsListActivity.this, (CanFreeGoodsBean) adapter.getItem(position));
                                break;
                            case "share":
                                AddShareFreeActivity.actionStart(SelectFreeActivityGoodsListActivity.this, (CanFreeGoodsBean) adapter.getItem(position));
                                break;
                            case "accumulate":
                                AddIntegralFreeActivity.actionStart(SelectFreeActivityGoodsListActivity.this, (CanFreeGoodsBean) adapter.getItem(position));
                                break;
                        }
                        break;
                }
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        srl.setOnLoadMoreListener(this);
        srl.setOnRefreshListener(this);
    }

    @Override
    public void fillData() {
        LoadingDialog.showDialogForLoading(this);
        myHttp.doPost(Api.getDefault().getFreeActivityGoodsList(LoginUtil.getLoginToken(), currentPage, 10, freeActivityType), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                if (currentPage == 1) {
                    srl.finishRefresh();
                } else {
                    srl.finishLoadMore();
                }
                List<CanFreeGoodsBean> data = JSONArray.parseArray(result.getString("data"), CanFreeGoodsBean.class);
                if (data != null) {
                    if (data.size() < 10) {
                        srl.setEnableLoadMore(false);
                    }
                    if (currentPage == 1) {
                        lstData.clear();
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

    private void search() {
        srl.setEnableRefresh(false);
        srl.setEnableLoadMore(false);
        lstSearchData.clear();
        LoadingDialog.showDialogForLoading(this);
        myHttp.doPost(Api.getDefault().searchFreeActivityGoodsList(LoginUtil.getLoginToken(), etSearch.getText().toString(), freeActivityType), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                List<CanFreeGoodsBean> data = JSONArray.parseArray(result.getString("data"), CanFreeGoodsBean.class);
                if (data != null) {
                    lstSearchData.addAll(data);
                    adapter.setNewData(lstSearchData);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(int code) {
                if (code == AppConfig.ERROR_STATE.NULLDATA) {
                    adapter.setNewData(lstSearchData);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void setListener() {
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    KeyBordUtil.hideSoftKeyboard(etSearch);
                    search();
                }
                return false;
            }
        });
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    adapter.setNewData(lstData);
                    srl.setEnableRefresh(true);
                    srl.setEnableLoadMore(true);
                }
            }
        });
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        currentPage++;
        fillData();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        srl.setEnableLoadMore(true);
        currentPage = 1;
        fillData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REQUEST_ADD:
                setResult(RESULT_OK, data);
                finish();
                break;
        }
    }
}