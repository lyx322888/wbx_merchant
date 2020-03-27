package com.wbx.merchant.activity;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wbx.merchant.R;
import com.wbx.merchant.adapter.MemberGoodsAdapter;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.base.BaseApplication;
import com.wbx.merchant.baseapp.AppConfig;
import com.wbx.merchant.bean.GoodsInfo;
import com.wbx.merchant.bean.MemberGoodsListBean;
import com.wbx.merchant.bean.SpecInfo;
import com.wbx.merchant.bean.UploadMemberGoodsBean;
import com.wbx.merchant.utils.ToastUitl;
import com.wbx.merchant.widget.iosdialog.AlertDialog;
import com.wbx.merchant.widget.refresh.BaseRefreshListener;
import com.wbx.merchant.widget.refresh.PullToRefreshLayout;
import com.wbx.merchant.widget.refresh.ViewStatus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class MemberGoodsActivity extends BaseActivity implements BaseRefreshListener {

    @Bind(R.id.rl_cancel)
    RelativeLayout rlCancel;
    @Bind(R.id.tv_goods_num)
    TextView tvGoodsNum;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.ptrl)
    PullToRefreshLayout ptrl;
    @Bind(R.id.tv_ensure)
    TextView tvEnsure;
    private MemberGoodsAdapter mAdapter;
    private int currentPage = 1;
    private List<GoodsInfo> lstData = new ArrayList<>();
    private MyHttp myHttp;
    private int totalGoodsNum;

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, MemberGoodsActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_member_goods;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        myHttp = new MyHttp();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MemberGoodsAdapter(this);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void fillData() {
        ptrl.showView(ViewStatus.LOADING_STATUS);
        loadData(false);
    }

    @Override
    public void setListener() {
        ptrl.setRefreshListener(this);
    }

    private void loadData(final boolean isLoadMore) {
        if (isLoadMore) {
            currentPage++;
        } else {
            currentPage = 1;
        }
        myHttp.doPost(Api.getDefault().getMemberGoods(userInfo.getSj_login_token(), currentPage, 10), new HttpListener() {
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
                MemberGoodsListBean data = JSONObject.parseObject(result.getString("data"), MemberGoodsListBean.class);
                totalGoodsNum = data.getGoods_num();
                if (!isLoadMore && (data.getGoods() == null || data.getGoods().size() == 0)) {
                    ptrl.showView(ViewStatus.EMPTY_STATUS);
                    ptrl.buttonClickNullData(MemberGoodsActivity.this, "refresh");
                    updateGoodsNum(0);
                    return;
                }
                if (data.getGoods().size() < AppConfig.pageSize) {
                    ptrl.setCanLoadMore(false);
                }
                ptrl.showView(ViewStatus.CONTENT_STATUS);
                if (!isLoadMore) {
                    lstData.clear();
                }
                if (userInfo.getGrade_id() == AppConfig.StoreGrade.MARKET) {
                    //菜市场数据赋值到实体店 兼容
                    for (GoodsInfo goodsInfo : data.getGoods()) {
                        goodsInfo.setGoods_id(goodsInfo.getProduct_id());
                        goodsInfo.setTitle(goodsInfo.getProduct_name());
                        goodsInfo.setIntro(goodsInfo.getDesc());
                        goodsInfo.setShopcate_id(goodsInfo.getCate_id());
                    }
                }
                lstData.addAll(data.getGoods());
                mAdapter.update(lstData);
            }

            @Override
            public void onError(int code) {
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
                        ptrl.buttonClickNullData(MemberGoodsActivity.this, "refresh");
                    }
                } else {
                    ptrl.showView(ViewStatus.ERROR_STATUS);
                    ptrl.buttonClickError(MemberGoodsActivity.this, "refresh");
                }
            }
        });
    }

    @OnClick({R.id.rl_cancel, R.id.ll_add_goods, R.id.ll_batch_del, R.id.tv_ensure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_cancel:
                mAdapter.setBatchDelete(false);
                tvEnsure.setText("确认");
                rlCancel.setVisibility(View.INVISIBLE);
                break;
            case R.id.ll_add_goods:
                SelectGoodsActivity.actionStart(this);
                break;
            case R.id.ll_batch_del:
                tvEnsure.setText("确定删除");
                mAdapter.setBatchDelete(true);
                rlCancel.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_ensure:
                if (mAdapter.getisBatchDelete()) {
                    batchDelete();
                } else {
                    addMemberGoods();
                }
                break;
        }
    }

    private void batchDelete() {
        new AlertDialog(mContext).builder().setTitle("提示").setMsg("确定批量删除？")
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).setPositiveButton("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<UploadMemberGoodsBean> lstSelectGoods = new ArrayList<>();
                for (GoodsInfo goodsInfo : lstData) {
                    if (!goodsInfo.isSelect()) {
                        continue;
                    }
                    if (goodsInfo.getGoods_attr() != null && goodsInfo.getGoods_attr().size() > 0) {
                        for (SpecInfo specInfo : goodsInfo.getGoods_attr()) {
                            UploadMemberGoodsBean uploadMemberGoodsBean = new UploadMemberGoodsBean();
                            uploadMemberGoodsBean.setGoods_id(goodsInfo.getGoods_id());
                            uploadMemberGoodsBean.setAttr_id(specInfo.getAttr_id());
                            lstSelectGoods.add(uploadMemberGoodsBean);
                        }
                    } else {
                        UploadMemberGoodsBean uploadMemberGoodsBean = new UploadMemberGoodsBean();
                        uploadMemberGoodsBean.setGoods_id(goodsInfo.getGoods_id());
                        uploadMemberGoodsBean.setAttr_id(0);
                        lstSelectGoods.add(uploadMemberGoodsBean);
                    }
                }
                if (lstSelectGoods.size() == 0) {
                    ToastUitl.showShort("请先选择商品");
                    return;
                }
                new MyHttp().doPost(Api.getDefault().deleteMemberGoods(BaseApplication.getInstance().readLoginUser().getSj_login_token(), JSONArray.toJSONString(lstSelectGoods)), new HttpListener() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        ToastUitl.showShort(result.getString("msg"));
                        finish();
                    }

                    @Override
                    public void onError(int code) {

                    }
                });
            }
        }).show();
    }

    private void addMemberGoods() {
        ArrayList<UploadMemberGoodsBean> lstSelectGoods = new ArrayList<>();
        for (GoodsInfo goodsInfo : lstData) {
            if (goodsInfo.getGoods_attr() != null && goodsInfo.getGoods_attr().size() > 0) {
                for (SpecInfo specInfo : goodsInfo.getGoods_attr()) {
                    if (specInfo.getShop_member_price() > 0) {
                        UploadMemberGoodsBean uploadMemberGoodsBean = new UploadMemberGoodsBean();
                        uploadMemberGoodsBean.setGoods_id(goodsInfo.getGoods_id());
                        uploadMemberGoodsBean.setShop_member_price(specInfo.getShop_member_price() / 100);
                        uploadMemberGoodsBean.setAttr_id(specInfo.getAttr_id());
                        lstSelectGoods.add(uploadMemberGoodsBean);
                    } else {
                        ToastUitl.showShort(String.format("请输入%s(%s)的会员价", TextUtils.isEmpty(goodsInfo.getProduct_name()) ? goodsInfo.getTitle() : goodsInfo.getProduct_name(), specInfo.getAttr_name()));
                        return;
                    }
                }
            } else {
                if (goodsInfo.getShop_member_price() > 0) {
                    UploadMemberGoodsBean uploadMemberGoodsBean = new UploadMemberGoodsBean();
                    uploadMemberGoodsBean.setGoods_id(goodsInfo.getGoods_id());
                    uploadMemberGoodsBean.setShop_member_price(goodsInfo.getShop_member_price() / 100);
                    uploadMemberGoodsBean.setAttr_id(0);
                    lstSelectGoods.add(uploadMemberGoodsBean);
                } else {
                    ToastUitl.showShort(String.format("请输入%s的会员价", TextUtils.isEmpty(goodsInfo.getProduct_name()) ? goodsInfo.getTitle() : goodsInfo.getProduct_name()));
                    return;
                }
            }
        }
        if (lstSelectGoods.size() == 0) {
            ToastUitl.showShort("请先选择商品");
            return;
        }
        String selectGoods = JSONArray.toJSONString(lstSelectGoods);
        myHttp.doPost(Api.getDefault().addMemberGoods(userInfo.getSj_login_token(), selectGoods), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                ToastUitl.showShort(result.getString("msg"));
                finish();
            }

            @Override
            public void onError(int code) {

            }
        });
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SelectGoodsActivity.REQUEST_SELECT_GOODS && resultCode == RESULT_OK) {
            ArrayList<GoodsInfo> selectGoodsList = (ArrayList<GoodsInfo>) data.getSerializableExtra("selectGoodsList");
            if (selectGoodsList != null && selectGoodsList.size() > 0) {
                for (GoodsInfo goodsInfo : selectGoodsList) {
                    boolean isContain = false;
                    for (GoodsInfo lstDatum : lstData) {
                        if (goodsInfo.getGoods_id() == lstDatum.getGoods_id()) {
                            isContain = true;
                            break;
                        }
                    }
                    if (!isContain) {
                        goodsInfo.setSelect(false);
                        lstData.add(goodsInfo);
                    }
                }
                ptrl.showView(ViewStatus.CONTENT_STATUS);
                mAdapter.update(lstData);
            }
        }
    }

    public void updateGoodsNum(int memberGoodsNum) {
        tvGoodsNum.setText("参与优惠商品（" + memberGoodsNum + "/" + totalGoodsNum + "）");
    }
}
