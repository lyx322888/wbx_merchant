package com.wbx.merchant.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.wbx.merchant.R;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.base.BaseApplication;
import com.wbx.merchant.bean.MemberBean;
import com.wbx.merchant.bean.MemberDetailBean;
import com.wbx.merchant.utils.GlideUtils;
import com.wbx.merchant.utils.ToastUitl;
import com.wbx.merchant.widget.CircleImageView;
import com.wbx.merchant.widget.iosdialog.ActionSheetDialog;

import butterknife.Bind;
import butterknife.OnClick;

public class MemberDetailActivity extends BaseActivity {
    public static final int MODIFY_IS_MEMBER = 1000;
    @Bind(R.id.iv_user)
    CircleImageView ivUser;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_member_state1)
    TextView tvMemberState1;
    @Bind(R.id.tv_member_state2)
    TextView tvMemberState2;
    @Bind(R.id.tv_buy_time)
    TextView tvBuyTime;
    @Bind(R.id.tv_recent_buy)
    TextView tvRecentBuy;
    @Bind(R.id.tv_total_buy)
    TextView tvTotalBuy;
    @Bind(R.id.ll_container)
    LinearLayout llContainer;
    @Bind(R.id.ll_check_more)
    LinearLayout llCheckMore;
    private MemberBean data;
    private MemberDetailBean memberDetailBean;

    public static void actionStart(Fragment context, MemberBean memberBean) {
        Intent intent = new Intent(context.getContext(), MemberDetailActivity.class);
        intent.putExtra("member", memberBean);
        context.startActivityForResult(intent, MODIFY_IS_MEMBER);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_member_detail;
    }

    @Override
    public void initPresenter() {
        data = (MemberBean) getIntent().getSerializableExtra("member");
    }

    @Override
    public void initView() {

    }

    @Override
    public void fillData() {
        GlideUtils.showMediumPic(this, ivUser, data.getFace());
        tvName.setText(!TextUtils.isEmpty(data.getNickname()) ? data.getNickname() : data.getHx_username());
        tvMemberState1.setText(data.getIs_shop_member() == 1 ? "会员" : "非会员");
        tvMemberState2.setText(data.getIs_shop_member() == 1 ? "会员" : "非会员");
        new MyHttp().doPost(Api.getDefault().getMemberDetail(data.getUser_id(), userInfo.getSj_login_token()), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                memberDetailBean = result.getObject("data", MemberDetailBean.class);
                tvBuyTime.setText(String.valueOf(memberDetailBean.getCount_all_order_num()));
                tvRecentBuy.setText(memberDetailBean.getRecently_time());
                tvTotalBuy.setText("¥" + memberDetailBean.getSum_all_order_money());
                if (memberDetailBean.getAll_order().size() <= 5) {
                    llCheckMore.setVisibility(View.GONE);
                } else {
                    llCheckMore.setVisibility(View.VISIBLE);
                }
                for (int i = 0; i < memberDetailBean.getAll_order().size(); i++) {
                    if (i == 5) {
                        break;
                    }
                    MemberDetailBean.AllOrderBean allOrderBean = memberDetailBean.getAll_order().get(i);
                    View layout = LayoutInflater.from(MemberDetailActivity.this).inflate(R.layout.item_member_consume_record, null);
                    ((TextView) layout.findViewById(R.id.tv_time)).setText(allOrderBean.getCreate_time());
                    ((TextView) layout.findViewById(R.id.tv_detail)).setText(allOrderBean.getMessage());
                    if (i == 4 || i == memberDetailBean.getAll_order().size() - 1) {
                        layout.findViewById(R.id.index_line).setVisibility(View.GONE);
                    }
                    llContainer.addView(layout);
                }
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    @Override
    public void setListener() {

    }

    @Override
    public void finish() {
        Intent intent = new Intent();
        intent.putExtra("member", data);
        setResult(RESULT_OK, intent);
        super.finish();
    }

    @OnClick({R.id.tv_member_state2, R.id.ll_check_more})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_member_state2:
                showSetSheetDialog();
                break;
            case R.id.ll_check_more:
                checkMore();
                break;
        }
    }

    private void showSetSheetDialog() {
        new ActionSheetDialog(mContext).addSheetItem(data.getIs_shop_member() == 1 ? "取消会员" : "设置为会员", ActionSheetDialog.SheetItemColor.AppColor, new ActionSheetDialog.OnSheetItemClickListener() {
            @Override
            public void onClick(int which) {
                setMember();
            }
        }).builder().show();
    }

    private void setMember() {
        new MyHttp().doPost(Api.getDefault().updateShopMember(BaseApplication.getInstance().readLoginUser().getSj_login_token()
                , data.getUser_id(), data.getIs_shop_member() == 1 ? 1 : 0), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                ToastUitl.showShort(result.getString("msg"));
                data.setIs_shop_member(data.getIs_shop_member() == 1 ? 0 : 1);
                tvMemberState1.setText(data.getIs_shop_member() == 1 ? "会员" : "非会员");
                tvMemberState2.setText(data.getIs_shop_member() == 1 ? "会员" : "非会员");
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    private void checkMore() {
        llContainer.removeAllViews();
        llCheckMore.setVisibility(View.GONE);
        for (int i = 0; i < memberDetailBean.getAll_order().size(); i++) {
            MemberDetailBean.AllOrderBean allOrderBean = memberDetailBean.getAll_order().get(i);
            View layout = LayoutInflater.from(this).inflate(R.layout.item_member_consume_record, null);
            if (i == memberDetailBean.getAll_order().size() - 1) {
                layout.findViewById(R.id.index_line).setVisibility(View.GONE);
            }
            ((TextView) layout.findViewById(R.id.tv_time)).setText(allOrderBean.getCreate_time());
            ((TextView) layout.findViewById(R.id.tv_detail)).setText(allOrderBean.getMessage());
            llContainer.addView(layout);
        }
    }
}
