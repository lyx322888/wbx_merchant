package com.wbx.merchant.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.wbx.merchant.R;
import com.wbx.merchant.activity.GoodsPictureActivity;
import com.wbx.merchant.activity.ReleaseActivity;
import com.wbx.merchant.utils.GlideUtils;
import com.wbx.merchant.widget.SquareLayout;
import com.wbx.merchant.widget.iosdialog.AlertDialog;

import java.io.File;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zero on 2017/1/17.
 */

public class GoodsPictureAdapter extends RecyclerView.Adapter<GoodsPictureAdapter.MyViewHolder> {
    private GoodsPictureActivity mContext;

    private List<String> lstData;

    public GoodsPictureAdapter(GoodsPictureActivity context, List<String> data) {
        mContext = context;
        lstData = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo, parent, false);
        MyViewHolder holder = new MyViewHolder(layout);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final String data = lstData.get(position);
        if (TextUtils.isEmpty(data)) {
            holder.ivPic.setImageResource(R.drawable.btn_add_pic);
            holder.ivDelete.setVisibility(View.GONE);
        } else if (data.startsWith("http")) {
            holder.ivDelete.setVisibility(View.VISIBLE);
            GlideUtils.showBigPic(mContext, holder.ivPic, data);
        } else {
            holder.ivDelete.setVisibility(View.VISIBLE);
            GlideUtils.displayUri(mContext, holder.ivPic, Uri.fromFile(new File(data)));
        }
        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog(mContext).builder().setTitle("提示").setMsg("删除该图片？").setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).setPositiveButton("删除", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (TextUtils.isEmpty(data)) {
                            return;
                        }
                        lstData.remove(data);
                        if (lstData.size() == ReleaseActivity.MAX_GOODS_PIC_NUM - 1 && !TextUtils.isEmpty(lstData.get(ReleaseActivity.MAX_GOODS_PIC_NUM - 2))) {
                            lstData.add("");
                        }
                        notifyDataSetChanged();
                    }
                }).show();
            }
        });
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(data)) {
                    mContext.addPhoto();
                } else {
                    mContext.showPicDetail(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return lstData == null ? 0 : lstData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.iv_pic)
        ImageView ivPic;
        @Bind(R.id.iv_delete)
        ImageView ivDelete;
        @Bind(R.id.root_view)
        SquareLayout rootView;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}