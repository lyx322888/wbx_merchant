package com.wbx.merchant.fragment;


import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.alibaba.fastjson.JSONObject;
import com.wbx.merchant.R;
import com.wbx.merchant.activity.CustomerManagerActivity;
import com.wbx.merchant.activity.MemberGoodsActivity;
import com.wbx.merchant.activity.MemberSettingActivity;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseFragment;
import com.wbx.merchant.common.LoginUtil;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 我的会员
 * 由anvitity转移过来
 * --------------------------------------------------------
 * A simple {@link Fragment} subclass.
 */
public class MemberManageFragment extends BaseFragment {

    @Bind(R.id.tv_member_num)
    TextView tvMemberNum;
    @Bind(R.id.tv_goods_num)
    TextView tvGoodsNum;
    @Bind(R.id.rv_r)
    RelativeLayout rvR;
    private MyHttp myHttp;

    //隐藏返回键
    public void setVisibilityHead(int visibility) {
        rvR.setVisibility(visibility);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.framemt_member_mannge;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void fillData() {
        myHttp = new MyHttp();
    }

    @Override
    protected void bindEven() {

    }


    @Override
    public void onResume() {
        super.onResume();
        myHttp.doPost(Api.getDefault().getShopMemberGoodsNum(LoginUtil.getLoginToken()), new HttpListener() {
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

    @OnClick({R.id.rl_member_set, R.id.ll_my_memeber, R.id.ll_goods})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_member_set:
                MemberSettingActivity.actionStart(getActivity());
                break;
            case R.id.ll_my_memeber:
                CustomerManagerActivity.actionStart(getActivity());
                break;
            case R.id.ll_goods:
                MemberGoodsActivity.actionStart(getActivity());
                break;
        }
    }
}
