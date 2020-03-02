package com.wbx.merchant.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.wbx.merchant.R;
import com.wbx.merchant.adapter.CateAdapter;
import com.wbx.merchant.adapter.GradeAdapter;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.base.BaseApplication;
import com.wbx.merchant.baseapp.AppManager;
import com.wbx.merchant.bean.CateBean;
import com.wbx.merchant.bean.GradeInfoBean;
import com.wbx.merchant.bean.ShopIsPayBean;
import com.wbx.merchant.common.LoginUtil;
import com.wbx.merchant.presenter.CatePresenterImp;
import com.wbx.merchant.presenter.ShopGradePresenterImp;
import com.wbx.merchant.utils.SPUtils;
import com.wbx.merchant.view.CateView;
import com.wbx.merchant.view.ShopGradeView;
import com.wbx.merchant.widget.LoadingDialog;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by wushenghui on 2017/6/22.
 * 选择店铺类型
 */

public class ChooseShopTypeActivity extends BaseActivity implements ShopGradeView, CateView {
    private int gradeId;
    @Bind(R.id.cate_recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.grade_recycler_view)
    RecyclerView gRecyclerView;
    private CateAdapter mAdapter;
    private GradeAdapter gAdapter;
    @Bind(R.id.need_pay_tv)
    TextView needPayTv;
    @Bind(R.id.show_money_tv)
    TextView showMoneyTv;
    private String gradeName;
    private int needPayPrice;
    private CatePresenterImp catePresenterImp;

    @Override
    public int getLayoutId() {
        return R.layout.activity_choose_shop_type;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {

    }

    @Override
    public void fillData() {
        gRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        ShopGradePresenterImp shopGradePresenterImp = new ShopGradePresenterImp(this);
        shopGradePresenterImp.getGrade();
        catePresenterImp = new CatePresenterImp(this);
        catePresenterImp.getCate(LoginUtil.getLoginToken(), 19);
    }

    @Override
    public void setListener() {

    }


    @OnClick(R.id.go_pay_btn)
    public void onClick(View view) {
        if (SPUtils.getSharedIntData(mContext, "catePosition") < 0) {
            showShortToast("请先选择店铺类型");
            return;
        }
        setShopCate();
    }

    //设置店铺分类
    private void setShopCate() {
        LoadingDialog.showDialogForLoading(mActivity, "信息提交中...", true);
        new MyHttp().doPost(Api.getDefault().upDateShopCate(userInfo.getSj_login_token(), userInfo.getShop_id(), SPUtils.getString("cateId", "")), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                judgeShopIsPay();
            }

            @Override
            public void onError(int code) {

            }
        });
    }
    //判断账号是否已经付过款
    private void judgeShopIsPay(){
        new MyHttp().doPost(Api.getDefault().getShopIsPay(userInfo.getSj_login_token()), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                ShopIsPayBean shopIsPayBean = new Gson().fromJson(result.toString(),ShopIsPayBean.class);
                //如果没有在h5付过款就继续下一步
                if (shopIsPayBean.getData().getIs_paid()!=1){
                    Intent intent = new Intent(mContext, ChooseVersionActivity.class);
                    gradeName = SPUtils.getString("gradeName", "");
                    needPayPrice = SPUtils.getSharedIntData(mContext, "needPayPrice");
                    gradeId = SPUtils.getSharedIntData(mContext, "gradeId");
                    intent.putExtra("gradeName", gradeName);
                    intent.putExtra("needPayPrice", needPayPrice);
                    intent.putExtra("gradeId", gradeId);
                    startActivity(intent);
                }else {
                    //如果付过就直接跳到审核页面
                    getServiceEndTime();
                }

            }

            @Override
            public void onError(int code) {

            }
        });
    }

    //支付成功，获取服务到期时间
    private void getServiceEndTime() {
        new MyHttp().doPost(Api.getDefault().getServiceEndTime(userInfo.getSj_login_token(), userInfo.getShop_id()), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                JSONObject data = result.getJSONObject("data");
                userInfo.setEnd_date(data.getIntValue(data.getString("end_date")));
                BaseApplication.getInstance().saveUserInfo(userInfo);
                AppManager.getAppManager().finishAllActivity();
                startActivity(new Intent(mContext, AuditingActivity.class));
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    @Override
    public void getGrade(final GradeInfoBean gradeInfoBean) {
        gAdapter = new GradeAdapter(mContext, gradeInfoBean.getData());
        gRecyclerView.setAdapter(gAdapter);
        gAdapter.setRecyclerViewItemClick(new GradeAdapter.RecyclerViewItemClick() {
            @Override
            public void recyclerViewItemClick(int position, View view, RecyclerView.ViewHolder viewHolder) {
                GradeAdapter.setGetPosition(position);
                SPUtils.setSharedIntData(mContext, "gradeId", gradeInfoBean.getData().get(position).getGrade_id());
                catePresenterImp.getCate(LoginUtil.getLoginToken(), gradeInfoBean.getData().get(position).getGrade_id());
                gAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void getCate(final CateBean cateBean) {
        mAdapter = new CateAdapter(mContext, cateBean.getData().getCates());
        mRecyclerView.setAdapter(mAdapter);
        SPUtils.put("gradeName", cateBean.getData().getShop_grade().getGrade_name(), mContext);
        SPUtils.setSharedIntData(mContext, "needPayPrice", cateBean.getData().getShop_grade().getMoney() / 100);
        SPUtils.setSharedIntData(mContext, "gradeId", cateBean.getData().getShop_grade().getGrade_id());
        mAdapter.setRecyclerViewItemClick(new CateAdapter.RecyclerViewItemClick() {
            @Override
            public void recyclerViewItemClick(int position, View view) {
                CateAdapter.setGetPosition(position);
                SPUtils.setSharedIntData(mContext, "catePosition", position);
                SPUtils.put("cateId", cateBean.getData().getCates().get(position).getCate_id(), mContext);
                mAdapter.notifyDataSetChanged();
            }
        });
    }
}
