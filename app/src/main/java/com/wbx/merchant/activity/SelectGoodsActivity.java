package com.wbx.merchant.activity;

import android.app.Activity;
import android.content.Intent;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wbx.merchant.R;
import com.wbx.merchant.adapter.ScreenCateAdapter;
import com.wbx.merchant.adapter.SelectGoodsAdapter;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class SelectGoodsActivity extends BaseActivity implements BaseRefreshListener {
    public static final int REQUEST_SELECT_GOODS = 1001;
    @Bind(R.id.choose_type_tv)
    TextView typeTv;
    @Bind(R.id.type_layout)
    LinearLayout typeLayout;
    @Bind(R.id.search_edit_text)
    EditText searchEditText;
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.refresh_layout)
    PullToRefreshLayout mRefreshLayout;
    @Bind(R.id.tv_select_goods_num)
    TextView tvSelectGoodsNum;
    private SelectGoodsAdapter mAdapter;
    private List<GoodsInfo> lstAllGoods = new ArrayList<>();
    private List<GoodsInfo> lstSearchGoods = new ArrayList<>();
    private List<CateInfo> cateList;
    private PopupWindow popWnd;

    public static void actionStart(Activity context) {
        Intent intent = new Intent(context, SelectGoodsActivity.class);
        context.startActivityForResult(intent, REQUEST_SELECT_GOODS);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_select_goods;
    }

    @Override
    public void initPresenter() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                lstSearchGoods.clear();
                if (!TextUtils.isEmpty(charSequence)) {
                    for (GoodsInfo lstAllGood : lstAllGoods) {
                        if (!TextUtils.isEmpty(lstAllGood.getProduct_name()) && lstAllGood.getProduct_name().contains(charSequence.toString())) {
                            lstSearchGoods.add(lstAllGood);
                        }
                    }
                } else {
                    lstSearchGoods.addAll(lstAllGoods);
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        typeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCateData();
            }
        });
    }

    @Override
    public void initView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new SelectGoodsAdapter(lstSearchGoods, mContext);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void fillData() {
        mRefreshLayout.showView(ViewStatus.LOADING_STATUS);
        new MyHttp().doPost(Api.getDefault().getMemberGoodsList(userInfo.getSj_login_token()), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                List<GoodsInfo> dataList = JSONArray.parseArray(result.getString("data"), GoodsInfo.class);
                mRefreshLayout.finishRefresh();
                if (dataList == null || dataList.size() == 0) {
                    mRefreshLayout.showView(ViewStatus.EMPTY_STATUS);
                    mRefreshLayout.buttonClickNullData(SelectGoodsActivity.this, "fillData");
                    return;
                }
                mRefreshLayout.showView(ViewStatus.CONTENT_STATUS);
                if (userInfo.getGrade_id() == AppConfig.StoreGrade.MARKET) {
                    //菜市场数据赋值到实体店 兼容
                    for (GoodsInfo goodsInfo : dataList) {
                        goodsInfo.setGoods_id(goodsInfo.getProduct_id());
                        goodsInfo.setTitle(goodsInfo.getProduct_name());
                        goodsInfo.setIntro(goodsInfo.getDesc());
                        goodsInfo.setShopcate_id(goodsInfo.getCate_id());
                    }
                }
                lstAllGoods.clear();
                lstAllGoods.addAll(dataList);
                lstSearchGoods.clear();
                lstSearchGoods.addAll(lstAllGoods);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(int code) {
                if (code == AppConfig.ERROR_STATE.NULLDATA) {
                    //无数据情况下  如果是第一次就是没有数据 直接显示没数据的view
                    mRefreshLayout.showView(ViewStatus.EMPTY_STATUS);
                    mRefreshLayout.buttonClickNullData(SelectGoodsActivity.this, "fillData");
                } else if (code == AppConfig.ERROR_STATE.NO_NETWORK || code == AppConfig.ERROR_STATE.SERVICE_ERROR) {
                    mRefreshLayout.showView(ViewStatus.ERROR_STATUS);
                    mRefreshLayout.buttonClickError(SelectGoodsActivity.this, "fillData");
                } else {

                }
                mRefreshLayout.finishRefresh();
                mRefreshLayout.finishLoadMore();
            }
        });
    }

    //获取分类数据
    private void getCateData() {
        if (cateList == null) {
            LoadingDialog.showDialogForLoading(this, "加载中...", true);
            new MyHttp().doPost(Api.getDefault().getCateList(userInfo.getSj_login_token()), new HttpListener() {
                @Override
                public void onSuccess(JSONObject result) {
                    cateList = JSONArray.parseArray(result.getString("data"), CateInfo.class);
                    CateInfo cateInfo = new CateInfo();
                    cateInfo.setCate_name("全部");
                    cateInfo.setCate_id(0);
                    cateList.add(0, cateInfo);
                    showTypePop();
                }

                @Override
                public void onError(int code) {

                }
            });
        } else {
            showTypePop();
        }
    }

    private void showTypePop() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.popwin_cate_layout, null);
        popWnd = new PopupWindow(contentView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popWnd.setWidth(typeLayout.getWidth());
        RecyclerView cateRecyclerView = contentView.findViewById(R.id.cate_recycler_view);
        cateRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        ScreenCateAdapter screenCateAdapter = new ScreenCateAdapter(cateList, this);
        cateRecyclerView.setAdapter(screenCateAdapter);
        popWnd.showAsDropDown(typeLayout);
        screenCateAdapter.setOnItemClickListener(R.id.root_view, new BaseAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(View view, int position) {
                CateInfo cateInfo = cateList.get(position);
                typeTv.setText(cateInfo.getCate_name());
                popWnd.dismiss();
                lstSearchGoods.clear();
                if (cateInfo.getCate_id() == 0) {
                    lstSearchGoods.addAll(lstAllGoods);
                } else {
                    for (GoodsInfo lstAllGood : lstAllGoods) {
                        if (lstAllGood.getShopcate_id() == cateInfo.getCate_id()) {
                            lstSearchGoods.add(lstAllGood);
                        }
                    }
                }
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void setListener() {
        mRefreshLayout.setRefreshListener(this);
        mRefreshLayout.setCanLoadMore(false);
        mAdapter.setOnItemClickListener(R.id.root_view, new BaseAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(View view, int position) {
                GoodsInfo item = mAdapter.getItem(position);
                item.setSelect(!item.isSelect());
                int num = Integer.parseInt(tvSelectGoodsNum.getText().toString());
                if (item.isSelect()) {
                    num++;
                } else {
                    num--;
                }
                tvSelectGoodsNum.setText(String.valueOf(num));
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void refresh() {
        fillData();
    }

    @Override
    public void loadMore() {
    }

    @OnClick(R.id.btn_ensure)
    public void onViewClicked() {
        if (Integer.parseInt(tvSelectGoodsNum.getText().toString()) == 0) {
            showShortToast("请选择商品");
            return;
        }
        List<GoodsInfo> lstSelectGoods = new ArrayList<>();
        for (GoodsInfo lstSearchGood : lstSearchGoods) {
            if (lstSearchGood.isSelect()) {
                lstSelectGoods.add(lstSearchGood);
            }
        }
        Intent intent = new Intent();
        intent.putExtra("selectGoodsList", (Serializable) lstSelectGoods);
        setResult(RESULT_OK, intent);
        finish();
    }
}
