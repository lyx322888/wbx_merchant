package com.wbx.merchant.activity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.wbx.merchant.R;
import com.wbx.merchant.adapter.RankingListAdapter;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.bean.RanKingListBean;
import com.wbx.merchant.utils.GlideUtils;
import com.wbx.merchant.utils.SpannableStringUtils;
import com.wbx.merchant.widget.CircleImageView;

import butterknife.Bind;
import butterknife.ButterKnife;

//排行榜
public class RankingListActivity extends BaseActivity {

    @Bind(R.id.title_name_tv)
    TextView titleNameTv;
    @Bind(R.id.civ_head)
    CircleImageView civHead;
    @Bind(R.id.tv_user)
    TextView tvUser;
    @Bind(R.id.tv_pm)
    TextView tvPm;
    @Bind(R.id.tv_mysr)
    TextView tvMysr;
    @Bind(R.id.tv_kds)
    TextView tvKds;
    @Bind(R.id.rv_phb)
    RecyclerView rvPhb;
    RankingListAdapter rankingListAdapter;
    @Override
    public int getLayoutId() {
        return R.layout.activity_ranking_list;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        rvPhb.setLayoutManager(new LinearLayoutManager(mContext));
        rankingListAdapter = new RankingListAdapter();
        rvPhb.setAdapter(rankingListAdapter);
    }

    @Override
    public void fillData() {
        new MyHttp().doPost(Api.getDefault().ranKingList(userInfo.getSj_login_token()), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                RanKingListBean ranKingListBean = new Gson().fromJson(result.toString(), RanKingListBean.class);
                GlideUtils.showMediumPic(mContext,civHead,ranKingListBean.getData().getUser().getFace());
                tvUser.setText(ranKingListBean.getData().getUser().getNickname());
                tvKds.setText(ranKingListBean.getData().getUser().getMerchant_num());
                tvMysr.setText(""+ranKingListBean.getData().getUser().getAll_share_bounty()/100);
                tvPm.setText(SpannableStringUtils.getBuilder("我的排名 ")
                        .append(ranKingListBean.getData().getUser().getMy_ranking())
                        .setForegroundColor(ContextCompat.getColor(mContext,R.color.font_phb_s)).create());
                rankingListAdapter.setNewData(ranKingListBean.getData().getAll_user());
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    @Override
    public void setListener() {

    }

}
