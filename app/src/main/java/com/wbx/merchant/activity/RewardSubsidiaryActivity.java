package com.wbx.merchant.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.wbx.merchant.R;
import com.wbx.merchant.adapter.RewardSubsidiaryAdapter;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.bean.RewardSubsidiaryBean;
import com.wbx.merchant.utils.FormatUtil;
import com.wbx.merchant.utils.ShareUtils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;

//奖励明细
public class RewardSubsidiaryActivity extends BaseActivity {

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_tcje)
    TextView tvTcje;
    @Bind(R.id.tv_ljtx)
    TextView tvLjtx;
    @Bind(R.id.rv_jl)
    RecyclerView rvJl;
    @Bind(R.id.tv_yqgd)
    TextView tvYqgd;
    private RewardSubsidiaryAdapter subsidiaryAdapter;
    private RewardSubsidiaryBean subsidiaryBean;
    private String userId;

    public static void actionStart(Context context,String userid) {
        Intent intent = new Intent(context, RewardSubsidiaryActivity.class);
        intent.putExtra("userid", userid);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        FormatUtil.setStatubarColor(mActivity, R.color.white);
        return R.layout.activity_reward_subsidiary;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        userId = getIntent().getStringExtra("userid");
        rvJl.setLayoutManager(new LinearLayoutManager(mContext));
        subsidiaryAdapter = new RewardSubsidiaryAdapter(new ArrayList<RewardSubsidiaryBean.DataBean.MerchantInvitationLogBean>());
        rvJl.setAdapter(subsidiaryAdapter);
        subsidiaryAdapter.setEmptyView(R.layout.layout_empty,rvJl);
        subsidiaryAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()){
                    case R.id.tv_ckhd:
                        WebSetUpShopActivity.actionStart(mContext, String.format("http://www.wbx365.com/Wbxwaphome/openstore#/record?uid=%s&vid=%s",userInfo.getSj_login_token(),subsidiaryAdapter.getData().get(position).getInvitee_user_id()));
                        break;
                }
            }
        });
    }

    @Override
    public void fillData() {
        new MyHttp().doPost(Api.getDefault().listShareUser(userInfo.getSj_login_token()), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                subsidiaryBean = new Gson().fromJson(result.toString(),RewardSubsidiaryBean.class);
                tvTcje.setText(String.format("%.2f ",((float)subsidiaryBean.getData().getShare_bounty()/100.00)));
                subsidiaryAdapter.setNewData(subsidiaryBean.getData().getMerchant_invitation_log());

            }

            @Override
            public void onError(int code) {

            }
        });
    }

    @Override
    public void setListener() {

    }


    @OnClick({R.id.tv_ljtx, R.id.tv_yqgd})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_ljtx:
                //立即提现
                if (subsidiaryBean!=null){
                    Intent intentCash = new Intent(mContext, CashActivity.class);
                    intentCash.putExtra("type", CashActivity.TYPE_REWARD_YQKD);
                    intentCash.putExtra("balance", subsidiaryBean.getData().getShare_bounty());
                    startActivity(intentCash);
                }else {
                    showShortToast("网络加载失败，请重新进入页面");
                }
                break;
            case R.id.tv_yqgd:
                //邀请更多好友
                ShareUtils.getInstance().share(mContext,"您的好友邀请您开店赚钱啦！","开店宝-轻松开店赚钱，有营业执照即可！送小程序[立即查看]","",String.format("http://www.wbx365.com/Wbxwaphome/invite?id=",userId));
                break;
        }
    }
}
