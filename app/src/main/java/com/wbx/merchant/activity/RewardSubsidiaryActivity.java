package com.wbx.merchant.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.zxing.WriterException;
import com.wbx.merchant.R;
import com.wbx.merchant.adapter.RewardSubsidiaryAdapter;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.bean.RewardSubsidiaryBean;
import com.wbx.merchant.bean.SetUpShopBean;
import com.wbx.merchant.utils.EncodingHandler;
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
    private SetUpShopBean setUpShopBean;

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
        //获取用户id奖励额度
        new MyHttp().doPost(Api.getDefault().invitation(userInfo.getSj_login_token()), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                setUpShopBean = new Gson().fromJson(result.toString(), SetUpShopBean.class);
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
                final View hb =  LayoutInflater.from(mContext).inflate(R.layout.pop_prw_hb, null);
                ImageView ivEwm = hb.findViewById(R.id.iv_ewm);
                TextView tv_number = hb.findViewById(R.id.tv_number);
                TextView tv_money = hb.findViewById(R.id.tv_money);
                if (setUpShopBean !=null){
                    tv_number.setText(String.format("邀请第%s家商铺入驻", setUpShopBean.getData().getCurrent_task().getCheckpoint()));
                    tv_money.setText(String.format("%s", setUpShopBean.getData().getCurrent_task().getBounty()/100));
                }
                String url = String.format("http://www.wbx365.com/Wbxwaphome/invite?id=%s",userId);
                try {
                    ivEwm.setImageBitmap(EncodingHandler.createQRCodeNoPading(url, 800));
                } catch (WriterException e) {
                    e.printStackTrace();
                }
                ShareUtils.getInstance().shareHb(mContext,hb,"您的好友邀请您开店赚钱啦!","开店宝-轻松开店赚钱，有营业执照即可！送小程序[立即查看]",  BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.bg_prw), url);
                break;
        }
    }
}
