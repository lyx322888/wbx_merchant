package com.wbx.merchant.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.flyco.roundview.RoundTextView;
import com.google.gson.Gson;
import com.wbx.merchant.R;
import com.wbx.merchant.adapter.SetMealAdapter;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.bean.StoreSetMealBean;
import com.wbx.merchant.common.LoginUtil;
import com.wbx.merchant.dialog.ConfirmDialog;
import com.wbx.merchant.widget.LoadingDialog;
import com.wbx.merchant.widget.refresh.BaseRefreshListener;
import com.wbx.merchant.widget.refresh.PullToRefreshLayout;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;

//到店套餐列表
public class StoreSetMealListActivity extends BaseActivity {

    @Bind(R.id.order_recycler_view)
    RecyclerView orderRecyclerView;
    @Bind(R.id.refresh_layout)
    PullToRefreshLayout refreshLayout;
    @Bind(R.id.tv_add_ddtc)
    RoundTextView tvAddDdtc;
    private SetMealAdapter setMealAdapter;
    @Override
    public int getLayoutId() {
        return R.layout.activity_store_set_meal_list;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        refreshLayout.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                getdata();
            }

            @Override
            public void loadMore() {

            }
        });
        refreshLayout.setCanLoadMore(false);
        setMealAdapter = new SetMealAdapter();
        orderRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        orderRecyclerView.setAdapter(setMealAdapter);
        setMealAdapter.setEmptyView(R.layout.loadsir_empty_ddtc,orderRecyclerView);

        setMealAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()){
                    case R.id.tv_kq:
                        if (setMealAdapter.getItem(position).getIs_end()==1){
                            //再次发布
                            AddStoreSetMealActivity.actionStart(mActivity,2,setMealAdapter.getItem(position).getShop_set_meal_id());
                        }else {
                            //开启、暂停
                            if (TextUtils.equals(setMealAdapter.getItem(position).getIs_pause(),"1")){
                                LoadingDialog.showDialogForLoading(mActivity);
                                requesPause_shop_set_meal(setMealAdapter.getItem(position).getShop_set_meal_id(),"0");
                            }else {
                                ConfirmDialog confirmDialog =ConfirmDialog.newInstance("暂停后，客户将无法购买此券 重新开启后即可恢复，确认暂停？");
                                confirmDialog.setDialogListener(new ConfirmDialog.DialogListener() {
                                    @Override
                                    public void dialogClickListener() {
                                        LoadingDialog.showDialogForLoading(mActivity);
                                        requesPause_shop_set_meal(setMealAdapter.getItem(position).getShop_set_meal_id(),"1");
                                    }
                                });
                                confirmDialog.show(getSupportFragmentManager(),"");
                            }
                        }
                        break;
                    case R.id.tv_bj:
                        //编辑
                        AddStoreSetMealActivity.actionStart(mActivity,1,setMealAdapter.getItem(position).getShop_set_meal_id());
                        break;
                    case R.id.tv_sc:
                        //删除
                        ConfirmDialog confirmDialog =ConfirmDialog.newInstance("删除后，相关统计数据将消失 您确认要删除？");
                        confirmDialog.setDialogListener(new ConfirmDialog.DialogListener() {
                            @Override
                            public void dialogClickListener() {
                                LoadingDialog.showDialogForLoading(mActivity);
                                requesDelete_shop_set_meal(setMealAdapter.getItem(position).getShop_set_meal_id());
                            }
                        });
                        confirmDialog.show(getSupportFragmentManager(),"");
                        break;
                    case R.id.tv_sjtj:
                        //数据统计
                        ReportFormsSetMealActivity.actionStart(mActivity,setMealAdapter.getItem(position).getShop_set_meal_id());
                        break;
                }
            }
        });

        setMealAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                AddStoreSetMealActivity.actionStart(mActivity,3,setMealAdapter.getItem(position).getShop_set_meal_id());
            }
        });
    }

    @Override
    public void fillData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        getdata();

    }

    private void getdata(){
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("sj_login_token", LoginUtil.getLoginToken());
        new MyHttp().doPost(Api.getDefault().list_shop_set_meal(hashMap), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                StoreSetMealBean storeSetMealBean  = new Gson().fromJson(result.toString(),StoreSetMealBean.class);
                setMealAdapter.setNewData(storeSetMealBean.getData());
                refreshLayout.finishRefresh();
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    //删除
    private void requesDelete_shop_set_meal(String shop_set_meal_id){
        LoadingDialog.showDialogForLoading(mActivity);
        new MyHttp().doPost(Api.getDefault().delete_shop_set_meal(LoginUtil.getLoginToken(), shop_set_meal_id), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                getdata();
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    //开启、暂停
    private void requesPause_shop_set_meal(String shop_set_meal_id, String is_pause){
        LoadingDialog.showDialogForLoading(mActivity);
        new MyHttp().doPost(Api.getDefault().pause_shop_set_meal(LoginUtil.getLoginToken(), shop_set_meal_id,is_pause), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                getdata();
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    @Override
    public void setListener() {

    }


    @OnClick(R.id.tv_add_ddtc)
    public void onClick() {
        //添加到店套餐
        startActivity(new Intent(mContext, AddStoreSetMealActivity.class));
        AddStoreSetMealActivity.actionStart(mActivity,0,"");

    }
}