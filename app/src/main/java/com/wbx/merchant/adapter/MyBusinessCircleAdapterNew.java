package com.wbx.merchant.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
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

public class MyBusinessCircleAdapterNew extends BaseQuickAdapter<BusinessCircleBean, BaseViewHolder> {
    private UserInfo userInfo;

    public MyBusinessCircleAdapterNew(UserInfo userInfo) {
        super(R.layout.item_my_business_circle);
        this.userInfo = userInfo;
    }

    @Override
    protected void convert(BaseViewHolder helper, final BusinessCircleBean item) {
        GlideUtils.showSmallPic(mContext, (ImageView) helper.getView(R.id.iv_user), userInfo.getShopPhoto());
        helper.setText(R.id.tv_user,userInfo.getShop_name())
                .setText(R.id.tv_time,item.getCreate_time())
                .setText(R.id.tv_content,item.getText());
        helper.getView(R.id.iv_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path = "pages/found/found?shop_id=" + userInfo.getShop_id();
                ShareUtils.getInstance().shareMiniProgram(mContext, userInfo.getShop_name(), "", item.getPhotos().size() == 0 ? userInfo.getShopPhoto() : item.getPhotos().get(0), path, "www.wbx365.com");
//                ShareUtils.getInstance().share(mContext, userInfo.getShop_name(), data.getText(), data.getPhotos() != null && data.getPhotos().size() > 0 ? data.getPhotos().get(0) : userInfo.getShopPhoto(), data.getShare_url());
            }
        });
        RecyclerView  recyclerView=helper.getView(R.id.recycler_view);
        if (item.getPhotos() != null && item.getPhotos().size() > 2) {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 3);
            recyclerView.setLayoutManager(gridLayoutManager);
        } else {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 2);
            recyclerView.setLayoutManager(gridLayoutManager);
        }
        recyclerView.addItemDecoration(new SpacesItemDecoration(2));
        BusinessCirclePhotoAdapter businessCirclePhotoAdapter = new BusinessCirclePhotoAdapter((Activity) mContext);
        recyclerView.setAdapter(businessCirclePhotoAdapter);
        businessCirclePhotoAdapter.update(item.getPhotos());
        helper.getView(R.id.tv_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete(item);
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
                        getData().remove(data);
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onError(int code) {

                    }
                });
            }
        }).show();
    }

}
