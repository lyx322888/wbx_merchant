package com.wbx.merchant.activity;

import android.content.Intent;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wbx.merchant.R;
import com.wbx.merchant.adapter.RecommendChooseAdapter;
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
import butterknife.OnClick;

/*商家推荐*/
public class MerchantRecommendActivity extends BaseActivity {

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.refresh_layout)
    PullToRefreshLayout mRefreshLayout;
    @Bind(R.id.select_goods_num_tv)
    TextView selectGoodsNumTv;
    @Bind(R.id.choose_type_tv)
    TextView chooseTypeTv;
    @Bind(R.id.search_edit_text)
    EditText searchEditText;
    @Bind(R.id.type_layout)
    LinearLayout typeLayout;
    @Bind(R.id.submmit)
    Button submmit;
    private int mPageNum = AppConfig.pageNum;
    private int mPageSize = AppConfig.pageSize;
    private int cate_id = 0;//分类id
    private List<GoodsInfo> goodsInfoList = new ArrayList<>();
    private boolean canLoadMore = true;
    private RecommendChooseAdapter mAdapter;
    private List<GoodsInfo> selectGoodsList = new ArrayList<>();
    private List<CateInfo> cateList = new ArrayList<>();
    private PopupWindow popWnd;
    @Override
    public int getLayoutId() {
        return R.layout.activity_merchant_recommend;
    }

    @Override
    public void initPresenter() {
        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    canLoadMore = true;
                    mPageSize = AppConfig.pageSize;

                    getData();
                }
                return false;
            }
        });
    }


    @Override
    public void initView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new RecommendChooseAdapter(goodsInfoList, mContext);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        canLoadMore = true;
        mPageNum = AppConfig.pageNum;
        getData();
    }

    @Override
    public void fillData() {

    }

    private void getData(){
        HashMap<String, Object> mParams = new HashMap<>();
        mParams.put("sj_login_token", userInfo.getSj_login_token());
        mParams.put("page", mPageNum);
        mParams.put("cate_id", cate_id);
        mParams.put("num", mPageSize);
        mParams.put("is_recommend", 0);
        mParams.put("keyword", searchEditText.getText().toString());
        new MyHttp().doPost(Api.getDefault().getRecommend_goods(mParams), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                List<GoodsInfo> dataList = JSONArray.parseArray(result.getString("data"), GoodsInfo.class);
                mRefreshLayout.finishRefresh();
                mRefreshLayout.finishLoadMore();
                if (dataList.size() < mPageSize) {
                    //说明下次已经没有数据了
                    canLoadMore = false;
                }
                if (mPageNum == AppConfig.pageNum) {
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
                } else {
                    for (GoodsInfo goodsInfo : dataList) {
                        goodsInfo.setGoods_id(goodsInfo.getProduct_id());
                    }
                }
                //重新标记选择过的商品
                for (int i = 0; i < selectGoodsList.size(); i++) {
                    for (int j = 0; j < dataList.size(); j++) {
                        if (selectGoodsList.get(i).getGoods_id()==dataList.get(j).getGoods_id()){
                            dataList.get(j).setIs_recommend(1);
                        }
                    }
                }
                goodsInfoList.addAll(dataList);
                mAdapter.notifyDataSetChanged();

                selectGoodsNumTv.setText(Html.fromHtml("已选择<font color=#ff0000>" + selectGoodsList.size() + "</font>件"));
            }

            @Override
            public void onError(int code) {
                if (code == AppConfig.ERROR_STATE.NULLDATA) {
                    //无数据情况下  如果是第一次就是没有数据 直接显示没数据的view
                    if (mPageNum == AppConfig.pageNum) {
                    } else {
                        //说明下次已经没有数据了
                        canLoadMore = false;
                    }

                } else if (code == AppConfig.ERROR_STATE.NO_NETWORK || code == AppConfig.ERROR_STATE.SERVICE_ERROR) {
                    mRefreshLayout.showView(ViewStatus.ERROR_STATUS);
                    mRefreshLayout.buttonClickError(MerchantRecommendActivity.this, "getData");
                } else {

                }
                mRefreshLayout.finishRefresh();
                mRefreshLayout.finishLoadMore();
            }
        });
    }

    //推荐到首页
    private void postRecommend(String goodsIds) {
        LoadingDialog.showDialogForLoading(this);
        HashMap<String, Object> mParams = new HashMap<>();
        mParams.put("goods_ids", goodsIds);
        mParams.put("sj_login_token", userInfo.getSj_login_token());
        new MyHttp().doPost(Api.getDefault().getUpdateRecommned(mParams), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                showShortToast("商品已推荐成功");
                canLoadMore = true;
                mPageNum = AppConfig.pageNum;
                selectGoodsList.clear();
                getData();
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    @Override
    public void setListener() {
        mRefreshLayout.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                canLoadMore = true;
                mPageNum = AppConfig.pageNum;
                getData();
            }

            @Override
            public void loadMore() {
                mPageNum++;
                if (!canLoadMore) {
                    mRefreshLayout.finishLoadMore();
                    showShortToast("没有更多数据了");
                    return;
                }
                getData();
            }
        });
        mAdapter.setOnItemClickListener(R.id.root_view, new BaseAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(View view, int position) {
                //商品最多推荐12件
                GoodsInfo item = mAdapter.getItem(position);
                    item.setIs_recommend(item.getIs_recommend() == 1 ? 0 : 1);
                    if (item.getIs_recommend() == 1) {
                        //设置成秒杀
                        item.setIs_recommend(1);
                        selectGoodsList.add(item);

                    } else {
                        for (int i = 0; i < selectGoodsList.size(); i++) {
                            if (selectGoodsList.get(i).getGoods_id()==item.getGoods_id()){
                                selectGoodsList.remove(i);
                            }
                        }

                    }
                    mAdapter.notifyDataSetChanged();
                    selectGoodsNumTv.setText(Html.fromHtml("已选择<font color=#ff0000>" + selectGoodsList.size() + "</font>件"));
            }
        });
    }

    //获取分类数据
    private void getCateData() {
        LoadingDialog.showDialogForLoading(this, "加载中...", true);
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
                canLoadMore = true;
                mPageNum = AppConfig.pageNum;
                chooseTypeTv.setText(cateList.get(position).getCate_name());
                cate_id = cateList.get(position).getCate_id();
                getData();
                popWnd.dismiss();
            }
        });
    }

    @OnClick({R.id.submmit, R.id.choose_type_tv,R.id.rl_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.submmit:
                //推荐提交
                String goodsIds = "";
                for (int i = 0; i < selectGoodsList.size(); i++) {
                    if (selectGoodsList.get(i).getIs_recommend() == 1) {
                        goodsIds += selectGoodsList.get(i).getGoods_id() + ",";
                    }
                }
                if (!TextUtils.isEmpty(goodsIds)) {
                    goodsIds = goodsIds.substring(0, goodsIds.length() - 1);
                }
                postRecommend(goodsIds);
                break;
            case R.id.choose_type_tv:
                //分类
                getCateData();
                break;
                case R.id.rl_right:
                //已推荐
                    startActivity(new Intent(this,AlreadyRecommendActivity.class));
                break;

        }

    }

}
