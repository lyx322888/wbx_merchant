package com.wbx.merchant.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wbx.merchant.R;
import com.wbx.merchant.adapter.CateAdapter;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.base.BaseAdapter;
import com.wbx.merchant.baseapp.AppConfig;
import com.wbx.merchant.bean.CateInfo;
import com.wbx.merchant.widget.LoadingDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by wushenghui on 2017/6/22.
 * 选择店铺类型
 */

public class ChooseShopTypeActivity extends BaseActivity {
    private int gradeId;
    private List<CateInfo> cateInfoList = new ArrayList<>();
    @Bind(R.id.cate_recycler_view)
    RecyclerView mRecyclerView;
    private CateAdapter mAdapter;
    @Bind(R.id.need_pay_tv)
    TextView needPayTv;
    @Bind(R.id.show_money_tv)
    TextView showMoneyTv;
    private List<String> selectCateId = new ArrayList<>();
    private String gradeName;
    private double needPayPrice;

    @Override
    public int getLayoutId() {
        return R.layout.activity_choose_shop_type;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new CateAdapter(cateInfoList, mContext);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void fillData() {
        gradeId = userInfo.getGrade_id();
        getShopCate();
    }

    //获取店铺类别
    private void getShopCate() {
        LoadingDialog.showDialogForLoading(mActivity, "加载中...", true);
        new MyHttp().doPost(Api.getDefault().getShopCate(gradeId), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                JSONObject data = result.getJSONObject("data");
                JSONObject shopGradeData = JSONObject.parseObject(data.getString("shop_grade"));
                gradeName = shopGradeData.getString("grade_name");
                needPayTv.setText(String.format("开通%s所需费用为 ", gradeName));
                needPayPrice = shopGradeData.getIntValue("money") / 100.00;
                showMoneyTv.setText(String.format("¥%.2f", needPayPrice));
                cateInfoList.addAll(JSONArray.parseArray(data.getString("cates"), CateInfo.class));
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    @Override
    public void setListener() {
        mAdapter.setOnItemClickListener(R.id.root_view, new BaseAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(View view, int position) {
                boolean select = cateInfoList.get(position).isSelect();
                if (gradeId == AppConfig.StoreGrade.MARKET) {
                    //多选
                    if (select) {
                        selectCateId.remove(String.valueOf(cateInfoList.get(position).getCate_id()));
                    } else {
                        selectCateId.add(String.valueOf(cateInfoList.get(position).getCate_id()));
                    }
                    cateInfoList.get(position).setSelect(!select);
                } else {
                    //单选
                    for (CateInfo cateInfo : cateInfoList) {
                        //反选全部
                        cateInfo.setSelect(false);
                    }
                    //设置选中
                    selectCateId.clear();
                    selectCateId.add(String.valueOf(cateInfoList.get(position).getCate_id()));
                    cateInfoList.get(position).setSelect(true);
                }

                mAdapter.notifyDataSetChanged();

            }
        });
    }

    @OnClick({R.id.go_pay_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.go_pay_btn:
                if (selectCateId.size() == 0) {
                    showShortToast("请先选择店铺类型");
                    return;
                }
                setShopCate();
                break;
        }
    }

    //设置店铺分类
    private void setShopCate() {
        LoadingDialog.showDialogForLoading(mActivity, "信息提交中...", true);
        new MyHttp().doPost(Api.getDefault().upDateShopCate(userInfo.getSj_login_token(), userInfo.getShop_id(), TextUtils.join(",", selectCateId)), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                Intent intent = new Intent(mContext, ChooseVersionActivity.class);
                intent.putExtra("gradeName", gradeName);
                intent.putExtra("needPayPrice", needPayPrice);
                intent.putExtra("gradeId", gradeId);
                startActivity(intent);
            }

            @Override
            public void onError(int code) {

            }
        });
    }
}
