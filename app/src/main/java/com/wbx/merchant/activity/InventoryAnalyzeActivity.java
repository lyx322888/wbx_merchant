package com.wbx.merchant.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wbx.merchant.R;
import com.wbx.merchant.adapter.InventoryAdapter;
import com.wbx.merchant.adapter.ScreenCateAdapter;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.base.BaseAdapter;
import com.wbx.merchant.baseapp.AppConfig;
import com.wbx.merchant.bean.CateInfo;
import com.wbx.merchant.bean.GoodsInfo;
import com.wbx.merchant.widget.LoadingDialog;
import com.wbx.merchant.widget.refresh.BaseRefreshListener;
import com.wbx.merchant.widget.refresh.PullToRefreshLayout;
import com.wbx.merchant.widget.refresh.ViewStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;

/**
 * Created by wushenghui on 2017/9/28.
 * 库存分析
 */

public class InventoryAnalyzeActivity extends BaseActivity implements BaseRefreshListener {
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.choose_type_tv)
    TextView chooseTypeTv;
    private HashMap<String, Object> mParams = new HashMap<>();
    private int pageNum = AppConfig.pageNum;
    private int pageSize = AppConfig.pageSize;
    private List<GoodsInfo> goodsInfoList = new ArrayList<>();
    private InventoryAdapter mAdapter;
    @Bind(R.id.refresh_layout)
    PullToRefreshLayout mRefreshLayout;
    private boolean canLoadMore = true;
    private List<CateInfo> cateList = new ArrayList<>();
    private PopupWindow popWnd;
    @Bind(R.id.type_layout)
    LinearLayout typeLayout;
    @Bind(R.id.search_edit_text)
    EditText searchEditText;

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, InventoryAnalyzeActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_inventory_analyze;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        mParams.put("cate_id", 0);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new InventoryAdapter(goodsInfoList, mContext);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getServiceData();
    }

    private void getServiceData() {
        mParams.put("sj_login_token", userInfo.getSj_login_token());
        mParams.put("page", pageNum);
        mParams.put("num", pageSize);
        new MyHttp().doPost(Api.getDefault().inventoryAnalyze(mParams), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                mRefreshLayout.finishRefresh();
                mRefreshLayout.finishLoadMore();
                List<GoodsInfo> dataList = JSONArray.parseArray(result.getString("data"), GoodsInfo.class);
                if (null == dataList && pageNum == AppConfig.pageNum) {
                    mRefreshLayout.showView(ViewStatus.EMPTY_STATUS);
                    mRefreshLayout.buttonClickNullData(InventoryAnalyzeActivity.this, "getServiceData");
                    return;
                }
                if (dataList.size() < pageNum) {
                    //说明下次已经没有数据了
                    canLoadMore = false;
                }
                if (pageNum == AppConfig.pageNum) {
                    goodsInfoList.clear();
                }
                mRefreshLayout.showView(ViewStatus.CONTENT_STATUS);
                if (userInfo.getGrade_id() != AppConfig.StoreGrade.MARKET) {
                    //实体店数据赋值到菜市场 兼容
                    for (GoodsInfo goodsInfo : dataList) {
                        goodsInfo.setProduct_id(goodsInfo.getGoods_id());
                        goodsInfo.setProduct_name(goodsInfo.getTitle());
                        goodsInfo.setDesc(goodsInfo.getIntro());
                        goodsInfo.setCate_id(goodsInfo.getShopcate_id());
                    }
                }
                goodsInfoList.addAll(dataList);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(int code) {
                if (code == AppConfig.ERROR_STATE.NULLDATA) {
                    //无数据情况下  如果是第一次就是没有数据 直接显示没数据的view
                    if (pageNum == AppConfig.pageNum) {
                        mRefreshLayout.showView(ViewStatus.EMPTY_STATUS);
                        mRefreshLayout.buttonClickNullData(InventoryAnalyzeActivity.this, "getServiceData");
                    } else {
                        //说明下次已经没有数据了
                        canLoadMore = false;
                    }
                } else if (code == AppConfig.ERROR_STATE.NO_NETWORK || code == AppConfig.ERROR_STATE.SERVICE_ERROR) {
                    mRefreshLayout.showView(ViewStatus.ERROR_STATUS);
                    mRefreshLayout.buttonClickError(InventoryAnalyzeActivity.this, "getServiceData");
                }
                mRefreshLayout.finishRefresh();
                mRefreshLayout.finishLoadMore();
            }
        });
    }

    @Override
    public void fillData() {

    }

    @Override
    public void setListener() {

        mAdapter.setOnItemClickListener(R.id.root_view, new BaseAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(View view, int position) {
                GoodsInfo item = mAdapter.getItem(position);
                Intent intent = new Intent(mContext, UpDateNumActivity.class);
                intent.putExtra("goods", item);
                startActivity(intent);
            }
        });
        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    canLoadMore = true;
                    mParams.put("keyword", textView.getText().toString());
                    mParams.put("page", AppConfig.pageNum);
                    fillData();
                }
                return false;
            }
        });
        mRefreshLayout.setRefreshListener(this);
        chooseTypeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCateData();
            }
        });
    }

    //获取分类数据
    private void getCateData() {
        LoadingDialog.showDialogForLoading(mActivity, "加载中...", true);
        new MyHttp().doPost(Api.getDefault().getCateList(userInfo.getSj_login_token()), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                cateList = JSONArray.parseArray(result.getString("data"), CateInfo.class);
                CateInfo cateInfo = new CateInfo();
                cateInfo.setCate_id(0);
                cateInfo.setCate_name("全部");
                cateList.add(0,cateInfo);
                showTypePop();
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    private void showTypePop() {
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.popwin_cate_layout, null);
        popWnd = new PopupWindow(contentView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popWnd.setWidth(typeLayout.getWidth());
        RecyclerView cateRecyclerView = contentView.findViewById(R.id.cate_recycler_view);
        cateRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 4));
        ScreenCateAdapter screenCateAdapter = new ScreenCateAdapter(cateList, mContext);
        cateRecyclerView.setAdapter(screenCateAdapter);
        popWnd.showAsDropDown(typeLayout);
        screenCateAdapter.setOnItemClickListener(R.id.root_view, new BaseAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(View view, int position) {
                canLoadMore = true;
                chooseTypeTv.setText(cateList.get(position).getCate_name());
                mParams.put("cate_id", cateList.get(position).getCate_id());
                mParams.put("page", pageNum);
                popWnd.dismiss();
                fillData();
            }
        });
    }

    @Override
    public void refresh() {
        canLoadMore = true;
        pageNum = AppConfig.pageNum;
        getServiceData();
    }

    @Override
    public void loadMore() {
        pageNum++;
        if (!canLoadMore) {
            mRefreshLayout.finishLoadMore();
            showShortToast("没有更多数据了");
            return;
        }
        getServiceData();
    }
}
