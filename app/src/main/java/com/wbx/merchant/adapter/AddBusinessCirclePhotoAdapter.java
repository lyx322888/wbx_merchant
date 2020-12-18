package com.wbx.merchant.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.wbx.merchant.R;
import com.wbx.merchant.activity.IssueSqActivity;
import com.wbx.merchant.activity.PublishBusinessCircleActivity;
import com.wbx.merchant.utils.GlideUtils;
import com.wbx.merchant.widget.SquareLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zero on 2017/1/17.
 */

public class AddBusinessCirclePhotoAdapter extends RecyclerView.Adapter<AddBusinessCirclePhotoAdapter.MyViewHolder> {
    private PublishBusinessCircleActivity mContext;
    private List<String> lstData = new ArrayList<>();

    public AddBusinessCirclePhotoAdapter(PublishBusinessCircleActivity context) {
        mContext = context;
    }

    public void update(List<String> data) {
        lstData = data;
        notifyDataSetChanged();
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
        } else {
            GlideUtils.showMediumPic(mContext, holder.ivPic, data);
            holder.ivDelete.setVisibility(View.VISIBLE);
        }
        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(data)) {
                    return;
                }
                lstData.remove(data);
                if (lstData.size() == mContext.MAX_PICTURE_NUM - 1 && !TextUtils.isEmpty(lstData.get(mContext.MAX_PICTURE_NUM - 2))) {
                    lstData.add("");
                }
                notifyDataSetChanged();
            }
        });
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(data)) {
                    mContext.addPhoto();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return lstData.size();
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
