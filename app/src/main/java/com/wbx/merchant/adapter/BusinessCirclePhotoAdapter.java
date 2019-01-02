package com.wbx.merchant.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.wbx.merchant.R;
import com.wbx.merchant.widget.SquareLayout;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.iwf.photopicker.PhotoPreview;

/**
 * Created by zero on 2017/1/17.
 */

public class BusinessCirclePhotoAdapter extends RecyclerView.Adapter<BusinessCirclePhotoAdapter.MyViewHolder> {
    private Activity mContext;
    private ArrayList<String> lstData = new ArrayList<>();

    public BusinessCirclePhotoAdapter(Activity context) {
        mContext = context;
    }

    public void update(ArrayList<String> data) {
        lstData = data;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_business_circle_photo, parent, false);
        MyViewHolder holder = new MyViewHolder(layout);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        String data = lstData.get(position);
        if (data.startsWith("http://imgs.wbx365.com/")) {
            data = data + "?imageView2/0/w/500/h/500";
        }
        Glide.with(mContext).load(data).error(R.drawable.loading_logo).centerCrop().into(holder.ivPic);
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lstData.size() > 0) {
                    PhotoPreview.builder().setPhotos(lstData).setCurrentItem(position).start(mContext);
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
        @Bind(R.id.root_view)
        SquareLayout rootView;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}