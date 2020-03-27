package com.wbx.merchant.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.wbx.merchant.R;
import com.wbx.merchant.activity.MemberDetailActivity;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseApplication;
import com.wbx.merchant.bean.MemberBean;
import com.wbx.merchant.utils.GlideUtils;
import com.wbx.merchant.utils.ToastUitl;
import com.wbx.merchant.widget.CircleImageView;
import com.wbx.merchant.widget.iosdialog.ActionSheetDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zero on 2017/1/17.
 */

public class MyMemberAdapter extends RecyclerView.Adapter<MyMemberAdapter.MyViewHolder> {

    private Fragment fragment;
    private Context mContext;
    private int type;
    private List<MemberBean> lstData = new ArrayList<>();

    public MyMemberAdapter(Fragment fragment, int position) {
        this.fragment = fragment;
        this.type = position;
        mContext = fragment.getContext();
    }

    public void update(List<MemberBean> data) {
        lstData.clear();
        lstData.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_member, parent, false);
        MyViewHolder holder = new MyViewHolder(layout);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final MemberBean data = lstData.get(position);

        holder.llContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MemberDetailActivity.actionStart(fragment, data);
            }
        });
        holder.ivSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSetSheetDialog(data);
            }
        });
        if (TextUtils.isEmpty(data.getFace())) {
            holder.ivUser.setImageResource(R.drawable.loading_logo);
        } else {
            GlideUtils.showSmallPic(mContext, holder.ivUser, data.getFace());
        }
        if (data.getIs_shop_member() == 1) {
            holder.ivVip.setVisibility(View.VISIBLE);
        } else {
            holder.ivVip.setVisibility(View.GONE);
        }
        holder.tvName.setText(!TextUtils.isEmpty(data.getNickname()) ? data.getNickname() : data.getHx_username());
        holder.tvMoney.setText(String.format("¥%.2f", data.getSum_money() / 100.00));
        holder.tvBuyTime.setText(data.getCount_order() + "次");
        if (type == 1) {
            ((View) holder.tvConsumeTime.getParent()).setVisibility(View.VISIBLE);
            holder.tvConsumeTime.setText(data.getLast_order_time());
            holder.tvConsumeMoney.setText("¥" + data.getLast_order_money());
        } else {
            ((View) holder.tvConsumeTime.getParent()).setVisibility(View.GONE);
        }
        if (data.getIs_first_bind()==1){
            holder.ivBind.setVisibility(View.VISIBLE);
        }else{
            holder.ivBind.setVisibility(View.GONE);
        }
    }

    private void showSetSheetDialog(final MemberBean data) {
        new ActionSheetDialog(mContext).addSheetItem(data.getIs_shop_member() == 1 ? "取消会员" : "设置为会员", ActionSheetDialog.SheetItemColor.AppColor, new ActionSheetDialog.OnSheetItemClickListener() {
            @Override
            public void onClick(int which) {
                setMember(data);
            }
        }).addSheetItem("拨打电话", ActionSheetDialog.SheetItemColor.Black, new ActionSheetDialog.OnSheetItemClickListener() {
            @Override
            public void onClick(int which) {
                callPhone(data);
            }
        }).addSheetItem("发送信息", ActionSheetDialog.SheetItemColor.Black, new ActionSheetDialog.OnSheetItemClickListener() {
            @Override
            public void onClick(int which) {
                sendMessage(data);
            }
        }).builder().show();
    }

    private void sendMessage(MemberBean userInfo) {
        if (TextUtils.isEmpty(userInfo.getMobile())) {
            Toast.makeText(mContext, "未获取到客户电话！", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + userInfo.getMobile()));
            mContext.startActivity(intent);
        }
    }

    private void onlineChat(MemberBean userInfo) {
        if (TextUtils.isEmpty(userInfo.getHx_username())) {
            Toast.makeText(mContext, "该客户暂时无法在线聊天！", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    private void callPhone(MemberBean userInfo) {
        if (TextUtils.isEmpty(userInfo.getMobile())) {
            Toast.makeText(mContext, "未获取到客户电话！", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + userInfo.getMobile()));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        }
    }

    private void setMember(final MemberBean userInfo) {
        new MyHttp().doPost(Api.getDefault().updateShopMember(BaseApplication.getInstance().readLoginUser().getSj_login_token()
                , userInfo.getUser_id(), userInfo.getIs_shop_member() == 1 ? 1 : 0), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                ToastUitl.showShort(result.getString("msg"));
                userInfo.setIs_shop_member(userInfo.getIs_shop_member() == 1 ? 0 : 1);
                notifyDataSetChanged();
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return lstData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.iv_user)
        CircleImageView ivUser;
        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(R.id.iv_vip)
        ImageView ivVip;
        @Bind(R.id.iv_setting)
        ImageView ivSetting;
        @Bind(R.id.tv_money)
        TextView tvMoney;
        @Bind(R.id.tv_buy_time)
        TextView tvBuyTime;
        @Bind(R.id.ll_container)
        LinearLayout llContainer;
        @Bind(R.id.tv_consume_time)
        TextView tvConsumeTime;
        @Bind(R.id.tv_consume_money)
        TextView tvConsumeMoney;
        @Bind(R.id.iv_bind)
        ImageView ivBind;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
