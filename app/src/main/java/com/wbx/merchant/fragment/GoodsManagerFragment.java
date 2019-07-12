package com.wbx.merchant.fragment;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
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
import com.wbx.merchant.activity.GoodsManagerActivity;
import com.wbx.merchant.activity.ReleaseActivity;
import com.wbx.merchant.adapter.GoodsAdapter;
import com.wbx.merchant.adapter.ScreenCateAdapter;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseAdapter;
import com.wbx.merchant.base.BaseFragment;
import com.wbx.merchant.baseapp.AppConfig;
import com.wbx.merchant.bean.CateInfo;
import com.wbx.merchant.bean.GoodsInfo;
import com.wbx.merchant.utils.SPUtils;
import com.wbx.merchant.utils.ToastUitl;
import com.wbx.merchant.widget.LoadingDialog;
import com.wbx.merchant.widget.iosdialog.AlertDialog;
import com.wbx.merchant.widget.refresh.BaseRefreshListener;
import com.wbx.merchant.widget.refresh.PullToRefreshLayout;
import com.wbx.merchant.widget.refresh.ViewStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;

/**
 * Created by wushenghui on 2017/6/21.
 * 商品管理
 */

public class GoodsManagerFragment extends BaseFragment implements BaseRefreshListener {
    public static final String POSITION = "position";
    @Bind(R.id.refresh_layout)
    PullToRefreshLayout mRefreshLayout;
    @Bind(R.id.order_recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.choose_type_tv)
    TextView typeTv;
    @Bind(R.id.type_layout)
    LinearLayout typeLayout;
    @Bind(R.id.search_edit_text)
    EditText searchEditText;
    private List<GoodsInfo> goodsInfoList = new ArrayList<>();
    private GoodsAdapter mAdapter;
    private int mPageNum = AppConfig.pageNum;
    private int mPageSize = AppConfig.pageSize;
    private boolean canLoadMore = true;
    private boolean isBatchEdit;
    private int anInt;
    private List<GoodsInfo> selectGoodsInfoList = new ArrayList<>();//选中的商品
    private GoodsManagerActivity goodsManagerActivity;
    private android.app.AlertDialog dialog;
    private int selectPosition;
    private HashMap<String, Object> mParams = new HashMap<>();
    private List<CateInfo> cateList = new ArrayList<>();
    private PopupWindow popWnd;
    private boolean isDataChange = false;//是否由于上下架导致数据变动需要刷新界面

    public static GoodsManagerFragment newInstance(int position) {
        GoodsManagerFragment tabFragment = new GoodsManagerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(POSITION, position);
        tabFragment.setArguments(bundle);
        return tabFragment;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.sign_xscroll_view_layout;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    protected void initView() {
        mRefreshLayout.showView(ViewStatus.LOADING_STATUS);
        goodsManagerActivity = (GoodsManagerActivity) getActivity();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new GoodsAdapter(goodsInfoList, getActivity());
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    protected void fillData() {
        anInt = getArguments().getInt(POSITION, 0);
        getServiceData();
    }

    private void getServiceData() {
        mParams.put("sj_login_token", loginUser.getSj_login_token());
        mParams.put("page", mPageNum);
        mParams.put("num", mPageSize);
        mParams.put("closed", anInt);
        new MyHttp().doPost(Api.getDefault().goodsList(mParams), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                if (mRefreshLayout == null) {
                    //该界面已销毁
                    return;
                }
                List<GoodsInfo> dataList = JSONArray.parseArray(result.getString("data"), GoodsInfo.class);
                mRefreshLayout.finishRefresh();
                mRefreshLayout.finishLoadMore();
                if (null == dataList && mPageNum == AppConfig.pageNum) {
                    mRefreshLayout.showView(ViewStatus.EMPTY_STATUS);
                    mRefreshLayout.buttonClickNullData(GoodsManagerFragment.this, "getServiceData");
                    return;
                }
                if (dataList.size() < mPageSize) {
                    //说明下次已经没有数据了
                    canLoadMore = false;
                }
                if (mPageNum == AppConfig.pageNum) {
                    goodsInfoList.clear();
                }
                mRefreshLayout.showView(ViewStatus.CONTENT_STATUS);
                if (loginUser.getGrade_id() != AppConfig.StoreGrade.MARKET) {
                    //实体店数据赋值到菜市场 兼容
                    for (GoodsInfo goodsInfo : dataList) {
                        goodsInfo.setProduct_id(goodsInfo.getGoods_id());
                        goodsInfo.setProduct_name(goodsInfo.getTitle());
                        goodsInfo.setDesc(goodsInfo.getIntro());
                        goodsInfo.setCate_id(goodsInfo.getShopcate_id());
                    }
                }
//                for(GoodsInfo mGoods : goodsInfoList){
//                    for(GoodsInfo goodsInfo: dataList){
//                        if(mGoods.getProduct_id()==mGoods.getProduct_id()){
//                            goodsInfoList.remove(mGoods);
//                            goodsInfoList.add(goodsInfo);
//                        }
//                    }
//                }
                goodsInfoList.addAll(dataList);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(int code) {
                if (mRefreshLayout == null) {
                    //该界面已销毁
                    return;
                }
                if (code == AppConfig.ERROR_STATE.NULLDATA) {
                    //无数据情况下  如果是第一次就是没有数据 直接显示没数据的view
                    if (mPageNum == AppConfig.pageNum) {
                        mRefreshLayout.showView(ViewStatus.EMPTY_STATUS);
                        mRefreshLayout.buttonClickNullData(GoodsManagerFragment.this, "getServiceData");
                    } else {
                        //说明下次已经没有数据了
                        canLoadMore = false;
                    }

                } else if (code == AppConfig.ERROR_STATE.NO_NETWORK || code == AppConfig.ERROR_STATE.SERVICE_ERROR) {
                    mRefreshLayout.showView(ViewStatus.ERROR_STATUS);
                    mRefreshLayout.buttonClickError(GoodsManagerFragment.this, "getServiceData");
                } else {

                }
                mRefreshLayout.finishRefresh();
                mRefreshLayout.finishLoadMore();
            }
        });
    }

    @Override
    protected void bindEven() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (TextUtils.isEmpty(charSequence)) {
                    mParams.put("keyword", "");
                    mParams.put("page", AppConfig.pageNum);
                    getServiceData();
                    canLoadMore = true;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    canLoadMore = true;
                    mParams.put("keyword", textView.getText().toString());
                    mParams.put("page", AppConfig.pageNum);
                    getServiceData();
                }
                return false;
            }
        });

        typeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCateData();
            }
        });
        mRefreshLayout.setRefreshListener(this);
        //促销
        mAdapter.setOnItemClickListener(R.id.promotion_btn, new BaseAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(View view, int position) {
                selectPosition = position;
                if (goodsInfoList.get(position).getSales_promotion_is() == 0) {
                    View inflate = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_sales_promotion, null);
                    if (null == dialog) {
                        dialog = new Builder(getActivity()).setView(inflate).create();
                        dialog.show();
                    } else {
                        dialog.show();
                    }

                    TextView shopNameTv = (TextView) inflate.findViewById(R.id.shop_name_tv);
                    final EditText salesPriceEdit = (EditText) inflate.findViewById(R.id.sales_price_edit);
                    shopNameTv.setText(String.format("促销商品：%s", goodsInfoList.get(position).getProduct_name()));
                    inflate.findViewById(R.id.sure_btn).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String price = salesPriceEdit.getText().toString();
                            if (TextUtils.isEmpty(price)) {
                                showShortToast("请输入促销价格");
                                return;
                            }
                            doSalesPro(price);

                        }
                    });
                    inflate.findViewById(R.id.cancel_btn).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                } else {
                    new AlertDialog(getActivity()).builder()
                            .setTitle("提示")
                            .setMsg("确认取消促销？")
                            .setNegativeButton("再想想", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            })
                            .setPositiveButton("确定", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    doSalesPro("0");
                                }
                            }).show();
                }


            }
        });
        //编辑
        mAdapter.setOnItemClickListener(R.id.edit_btn, new BaseAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(View view, int position) {
                goodsIndex = position;
                Intent intent = new Intent(getActivity(), ReleaseActivity.class);
                intent.putExtra("goods", goodsInfoList.get(position));
                getActivity().startActivityForResult(intent, GoodsManagerActivity.REQUEST_UPDATE_GOODS);
            }
        });
        //上下架
        mAdapter.setOnItemClickListener(R.id.sold_out_in_btn, new BaseAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(View view, int position) {
                selectPosition = position;
                new AlertDialog(getActivity()).builder()
                        .setTitle("提示")
                        .setMsg(anInt == 0 ? "确定下架？" : "确定上架？")
                        .setNegativeButton("再想想", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        })
                        .setPositiveButton("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                selectGoodsInfoList.add(goodsInfoList.get(selectPosition));
                                if (anInt == 0) {
                                    soldOut();
                                } else {
                                    soldIn();
                                }
                            }
                        }).show();

            }
        });
        //删除
        mAdapter.setOnItemClickListener(R.id.delete_btn, new BaseAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(View view, int position) {
                selectPosition = position;
                new AlertDialog(getActivity()).builder()
                        .setTitle("提示")
                        .setMsg("确定删除？")
                        .setNegativeButton("再想想", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        })
                        .setPositiveButton("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                selectGoodsInfoList.add(goodsInfoList.get(selectPosition));
                                deleteGoods();
                            }
                        }).show();

            }
        });

        mAdapter.setOnItemClickListener(R.id.ll_goods_info, new BaseAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(View view, int position) {
                GoodsInfo goodsInfo = goodsInfoList.get(position);
                goodsInfo.setSelect(!goodsInfo.isSelect());
                if (goodsInfo.isSelect()) {
                    selectGoodsInfoList.add(goodsInfo);
                } else {
                    selectGoodsInfoList.remove(goodsInfo);
                }
                mAdapter.notifyDataSetChanged();
                if (selectGoodsInfoList.size() == goodsInfoList.size()) {
                    goodsManagerActivity.setSelectAll(true);
                } else {
                    goodsManagerActivity.setSelectAll(false);
                }
            }
        });
    }

    private void showTypePop() {
        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.popwin_cate_layout, null);
        popWnd = new PopupWindow(contentView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popWnd.setWidth(typeLayout.getWidth());
        RecyclerView cateRecyclerView = (RecyclerView) contentView.findViewById(R.id.cate_recycler_view);
        cateRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        ScreenCateAdapter screenCateAdapter = new ScreenCateAdapter(cateList, getActivity());
        cateRecyclerView.setAdapter(screenCateAdapter);
        popWnd.showAsDropDown(typeLayout);
        screenCateAdapter.setOnItemClickListener(R.id.root_view, new BaseAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(View view, int position) {
                canLoadMore = true;
                typeTv.setText(cateList.get(position).getCate_name());
                mParams.put("cate_id", cateList.get(position).getCate_id());
                mParams.put("page", mPageNum);
                popWnd.dismiss();
                getServiceData();
            }
        });
    }

    private void doSalesPro(final String price) {
        //设置促销
        HashMap<String, Object> params = new HashMap<>();
        params.put("sj_login_token", loginUser.getSj_login_token());
        params.put("goods_id", goodsInfoList.get(selectPosition).getGoods_id());
        params.put("sales_promotion_price", price);
        params.put("sales_promotion_is", goodsInfoList.get(selectPosition).getSales_promotion_is() == 0 ? 1 : 0);
        LoadingDialog.showDialogForLoading(getActivity(), "提交中...", true);
        new MyHttp().doPost(Api.getDefault().setPromotion(params), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                if (goodsInfoList.get(selectPosition).getSales_promotion_is() == 0) {
                    goodsInfoList.get(selectPosition).setSales_promotion_is(1);
                    goodsInfoList.get(selectPosition).setSales_promotion_price(Integer.parseInt(price) * 100);
                    mAdapter.notifyDataSetChanged();
                } else {
                    goodsInfoList.get(selectPosition).setSales_promotion_is(0);
                    mAdapter.notifyDataSetChanged();
                }
                dialog.dismiss();

            }

            @Override
            public void onError(int code) {

            }
        });
    }

    //删除商品
    public void deleteGoods() {
        if (0 == selectGoodsInfoList.size()) {
            showShortToast("请选中需要操作的商品");
            return;
        }
        LoadingDialog.showDialogForLoading(getActivity(), "删除中...", true);
        HashMap<String, Object> params = new HashMap<>();
        List<Integer> idList = new ArrayList<>();
        for (GoodsInfo goodsInfo : selectGoodsInfoList) {
            idList.add(goodsInfo.getProduct_id());
        }
        String ids = TextUtils.join(",", idList);
        params.put("sj_login_token", loginUser.getSj_login_token());
        params.put(loginUser.getGrade_id() == AppConfig.StoreGrade.MARKET ? "product_id" : "goods_id", ids);
        new MyHttp().doPost(Api.getDefault().goodsDelete(params), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                goodsInfoList.removeAll(selectGoodsInfoList);
                mAdapter.notifyDataSetChanged();
                selectGoodsInfoList.clear();
                isNullData();
            }

            @Override
            public void onError(int code) {
                selectGoodsInfoList.clear();
            }
        });
    }

    //批量管理
    public void batchManager(boolean isBatchEdit) {
        mAdapter.setIsBath(isBatchEdit);
    }

    //全选
    public void setSelectAll() {
        for (GoodsInfo goodsInfo : goodsInfoList) {
            goodsInfo.setSelect(true);
        }
        selectGoodsInfoList.clear();
        selectGoodsInfoList.addAll(goodsInfoList);
        mAdapter.notifyDataSetChanged();
    }

    //取消全选
    public void cancelSelectAll() {
        for (GoodsInfo goodsInfo : goodsInfoList) {
            goodsInfo.setSelect(false);
        }
        selectGoodsInfoList.removeAll(goodsInfoList);
        mAdapter.notifyDataSetChanged();
    }

    //商品下架
    public void soldOut() {
        if (0 == selectGoodsInfoList.size()) {
            showShortToast("请选中需要操作的商品");
            return;
        }
        List<Integer> idList = new ArrayList<>();
        for (GoodsInfo goodsInfo : selectGoodsInfoList) {
            idList.add(goodsInfo.getProduct_id());
        }
        String ids = TextUtils.join(",", idList);
        LoadingDialog.showDialogForLoading(getActivity(), "执行中...", true);
        HashMap<String, Object> params = new HashMap<>();
        params.put("sj_login_token", loginUser.getSj_login_token());
        params.put(loginUser.getGrade_id() == AppConfig.StoreGrade.MARKET ? "product_id" : "goods_id", ids);
        new MyHttp().doPost(Api.getDefault().soldOut(params), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                goodsInfoList.removeAll(selectGoodsInfoList);
                mAdapter.notifyDataSetChanged();
                selectGoodsInfoList.clear();
                isNullData();
                goodsManagerActivity.setOtherItemDataChange();
            }

            @Override
            public void onError(int code) {
                selectGoodsInfoList.clear();
            }
        });
    }


    //上架
    public void soldIn() {
        if (0 == selectGoodsInfoList.size()) {
            showShortToast("请选中需要操作的商品");
            return;
        }
        List<Integer> idList = new ArrayList<>();
        for (GoodsInfo goodsInfo : selectGoodsInfoList) {
            idList.add(goodsInfo.getProduct_id());
        }
        String ids = TextUtils.join(",", idList);
        LoadingDialog.showDialogForLoading(getActivity(), "执行中...", true);
        HashMap<String, Object> params = new HashMap<>();
        params.put("sj_login_token", loginUser.getSj_login_token());
        params.put(loginUser.getGrade_id() == AppConfig.StoreGrade.MARKET ? "product_id" : "goods_id", ids);
        new MyHttp().doPost(Api.getDefault().soldUp(params), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                goodsInfoList.removeAll(selectGoodsInfoList);
                mAdapter.notifyDataSetChanged();
                selectGoodsInfoList.clear();
                isNullData();
                goodsManagerActivity.setOtherItemDataChange();
            }

            @Override
            public void onError(int code) {
                selectGoodsInfoList.clear();
            }
        });

    }

    //批量修改分类
    public void batchClassify(int cate_id) {
        if (0 == selectGoodsInfoList.size()) {
            showShortToast("请选中需要操作的商品");
            return;
        }
        List<Integer> idList = new ArrayList<>();
        for (GoodsInfo goodsInfo : selectGoodsInfoList) {
            idList.add(goodsInfo.getProduct_id());
        }
        String ids = TextUtils.join(",", idList);
        LoadingDialog.showDialogForLoading(getActivity(), "修改中...", true);
        new MyHttp().doPost(Api.getDefault().goodsBatchClassify(loginUser.getSj_login_token(), ids, cate_id), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                ToastUitl.showShort(result.getString("msg"));
                goodsManagerActivity.rlRight.performClick();
            }

            @Override
            public void onError(int code) {

            }
        });

    }

    @Override
    public void refresh() {
        canLoadMore = true;
        mPageNum = AppConfig.pageNum;
        getServiceData();
    }

    @Override
    public void loadMore() {
        mPageNum++;
        if (!canLoadMore) {
            mRefreshLayout.finishLoadMore();
            showShortToast("没有更多数据了");
            return;
        }
        getServiceData();
    }

    //判断操作完了以后数据是否为空
    private void isNullData() {
        if (goodsInfoList.size() == 0) {
            mRefreshLayout.showView(ViewStatus.EMPTY_STATUS);
            mRefreshLayout.buttonClickNullData(GoodsManagerFragment.this, "getServiceData");
        }
    }

    //获取分类数据
    private void getCateData() {
        LoadingDialog.showDialogForLoading(getActivity(), "加载中...", true);
        new MyHttp().doPost(Api.getDefault().getCateList(loginUser.getSj_login_token()), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                cateList = JSONArray.parseArray(result.getString("data"), CateInfo.class);
                showTypePop();
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    private int goodsIndex;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case GoodsManagerActivity.REQUEST_UPDATE_GOODS:
                GoodsInfo goodsInfo = (GoodsInfo) data.getSerializableExtra(ReleaseActivity.RESULT_GOODS);
                goodsInfoList.set(goodsIndex,goodsInfo);
                mAdapter.notifyItemChanged(goodsIndex);
                refresh();
                break;

            case GoodsManagerActivity.REQUEST_ADD_GOODS:
                refresh();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!getUserVisibleHint()) {
            onPause();
            return;
        }
        if (isDataChange) {
            refresh();
            isDataChange = false;
        }
    }

    public void setDataChange() {
        isDataChange = true;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isResumed()) {
            if (isVisibleToUser) {
                onResume();
            } else {
                onPause();
            }
        }
    }
}
