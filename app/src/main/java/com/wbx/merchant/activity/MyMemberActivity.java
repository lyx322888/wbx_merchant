package com.wbx.merchant.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.wbx.merchant.R;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.fragment.MemberManageFragment;

import butterknife.Bind;
import butterknife.OnClick;

public class MyMemberActivity extends BaseActivity {

    @Bind(R.id.tv_member_num)
    TextView tvMemberNum;
    @Bind(R.id.tv_goods_num)
    TextView tvGoodsNum;
    private MyHttp myHttp;

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, MyMemberActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_member;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        myHttp.doPost(Api.getDefault().getShopMemberGoodsNum(userInfo.getSj_login_token()), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                JSONObject data = result.getJSONObject("data");
                tvMemberNum.setText(String.format("%s个购买过，%s个关注，%s个会员", data.getString("shopping_member_num"), data.getString("favorites_member_num"), data.getString("shop_member_num")));
                tvGoodsNum.setText(data.getString("shop_member_goods_num") + "件商品");
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    @Override
    public void initView() {
        MemberManageFragment mineFragment = (MemberManageFragment) getSupportFragmentManager().getFragments().get(0);
        mineFragment.setVisibilityHead(View.VISIBLE);
    }

    @Override
    public void fillData() {
        myHttp = new MyHttp();
    }

    @Override
    public void setListener() {

    }

    @OnClick({R.id.rl_member_set, R.id.ll_my_memeber, R.id.ll_goods})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_member_set:
                MemberSettingActivity.actionStart(this);
                break;
            case R.id.ll_my_memeber:
                CustomerManagerActivity.actionStart(this);
                break;
            case R.id.ll_goods:
                MemberGoodsActivity.actionStart(this);
                break;
        }
    }
}
