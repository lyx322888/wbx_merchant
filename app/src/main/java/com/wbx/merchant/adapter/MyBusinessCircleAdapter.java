package com.wbx.merchant.adapter;

import android.app.Activity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.wbx.merchant.R;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.bean.BusinessCircleBean;
import com.wbx.merchant.bean.UserInfo;
import com.wbx.merchant.utils.GlideUtils;
import com.wbx.merchant.utils.ShareUtils;
import com.wbx.merchant.utils.ToastUitl;
import com.wbx.merchant.widget.CircleImageView;
import com.wbx.merchant.widget.decoration.SpacesItemDecoration;
import com.wbx.merchant.widget.iosdialog.AlertDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zero on 2017/1/17.
 */

public class MyBusinessCircleAdapter extends RecyclerView.Adapter<MyBusinessCircleAdapter.MyViewHolder> {
    private Activity mContext;
    private UserInfo userInfo;
    private List<BusinessCircleBean> lstData = new ArrayList<>();

    public MyBusinessCircleAdapter(Activity context, UserInfo userInfo) {
        mContext = context;
        this.userInfo = userInfo;
    }

    public void update(List<BusinessCircleBean> data) {
        lstData.clear();
        lstData.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_business_circle, parent, false);
        MyViewHolder holder = new MyViewHolder(layout);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final BusinessCircleBean data = lstData.get(position);
        GlideUtils.showSmallPic(mContext, holder.ivUser, userInfo.getShopPhoto());
        holder.tvUser.setText(userInfo.getShop_name());
        holder.tvTime.setText(data.getCreate_time());
        holder.ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareUtils.getInstance().share(mContext, userInfo.getShop_name(), data.getText(), data.getPhotos() != null && data.getPhotos().size() > 0 ? data.getPhotos().get(0) : userInfo.getShopPhoto(), data.getShare_url());
            }
        });
        holder.tvContent.setText(data.getText());
        if (data.getPhotos() != null && data.getPhotos().size() > 2) {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 3);
            holder.recyclerView.setLayoutManager(gridLayoutManager);
        } else {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 2);
            holder.recyclerView.setLayoutManager(gridLayoutManager);
        }
        holder.recyclerView.addItemDecoration(new SpacesItemDecoration(2));
        BusinessCirclePhotoAdapter businessCirclePhotoAdapter = new BusinessCirclePhotoAdapter(mContext);
        holder.recyclerView.setAdapter(businessCirclePhotoAdapter);
        businessCirclePhotoAdapter.update(data.getPhotos());
        holder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete(data);
            }
        });
    }

    private void delete(final BusinessCircleBean data) {
        new AlertDialog(mContext).builder().setTitle("提示").setMsg("确定删除该条商圈动态吗？").setNegativeButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        }).setPositiveButton("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MyHttp().doPost(Api.getDefault().deleteDiscovery(userInfo.getSj_login_token(), data.getDiscover_id()), new HttpListener() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        ToastUitl.showShort(result.getString("msg"));
                        lstData.remove(data);
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onError(int code) {

                    }
                });
            }
        }).show();
    }

    @Override
    public int getItemCount() {
        return lstData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.iv_user)
        CircleImageView ivUser;
        @Bind(R.id.tv_user)
        TextView tvUser;
        @Bind(R.id.tv_time)
        TextView tvTime;
        @Bind(R.id.iv_share)
        ImageView ivShare;
        @Bind(R.id.tv_content)
        TextView tvContent;
        @Bind(R.id.recycler_view)
        RecyclerView recyclerView;
        @Bind(R.id.tv_delete)
        TextView tvDelete;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
