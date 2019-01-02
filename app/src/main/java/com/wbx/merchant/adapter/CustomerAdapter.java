package com.wbx.merchant.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.wbx.merchant.R;
import com.wbx.merchant.base.BaseAdapter;
import com.wbx.merchant.base.BaseViewHolder;
import com.wbx.merchant.bean.UserInfo;
import com.wbx.merchant.utils.GlideUtils;

import java.util.List;

/**
 * Created by wushenghui on 2017/7/25.
 */

public class CustomerAdapter extends BaseAdapter<UserInfo, Context> {
    private boolean mIsAttention;

    public CustomerAdapter(List<UserInfo> dataList, Context context) {
        super(dataList, context);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_customer;
    }

    @Override
    public void convert(BaseViewHolder holder, UserInfo userInfo, int position) {
        if (mIsAttention) {
            holder.getView(R.id.phone_tv).setVisibility(View.GONE);
            holder.getView(R.id.call_im).setVisibility(View.GONE);
            holder.getView(R.id.message_im).setVisibility(View.GONE);
            holder.getView(R.id.chat_im).setVisibility(View.GONE);
        }
        ImageView userPic = holder.getView(R.id.user_pic_im);
        holder.setText(R.id.nick_name_tv, userInfo.getNickname()).setText(R.id.phone_tv, String.format("联系方式：%s", userInfo.getMobile()));
        GlideUtils.showRoundSmallPic(mContext, userPic, userInfo.getFace());
    }

    public void isAttention(boolean isAttention) {
        this.mIsAttention = isAttention;
    }
}
