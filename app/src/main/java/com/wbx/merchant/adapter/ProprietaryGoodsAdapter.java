package com.wbx.merchant.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.wbx.merchant.R;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseAdapter;
import com.wbx.merchant.base.BaseViewHolder;
import com.wbx.merchant.bean.ProprietaryGoodsBean;
import com.wbx.merchant.common.LoginUtil;
import com.wbx.merchant.utils.GlideUtils;
import com.wbx.merchant.widget.CicleAddAndSubView;

import java.util.List;


public class ProprietaryGoodsAdapter extends BaseAdapter<ProprietaryGoodsBean.DataBean, Context> {


    public ProprietaryGoodsAdapter(List<ProprietaryGoodsBean.DataBean> dataList, Context context) {
        super(dataList, context);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_proproetary_goods;
    }

    @Override
    public void convert(BaseViewHolder holder, final ProprietaryGoodsBean.DataBean dataBean, int position) {
        TextView tvName = holder.getView(R.id.tv_name);
        TextView tvPs = holder.getView(R.id.tv_ps);
        TextView tvPrice = holder.getView(R.id.tv_price);

        ImageView tvPhoto = holder.getView(R.id.iv_photo);
        final CicleAddAndSubView addorsub = holder.getView(R.id.addorsub);
        tvName.setText(dataBean.getTitle());
        tvPs.setText(dataBean.getDescribe());
        tvPrice.setText("¥" + (float) dataBean.getPrice() / 100 + "");
        GlideUtils.showMediumPic(mContext, tvPhoto, dataBean.getPhoto());
        addorsub.setNum(dataBean.getOrder_num());
        addorsub.setOnNumChangeListener(new CicleAddAndSubView.OnNumChangeListener() {
            @Override
            public void onNumChange(View view, String stype, final int num) {
                new MyHttp().doPost(Api.getDefault().getUpdate(LoginUtil.getLoginToken(), dataBean.getGoods_id(), stype), new HttpListener() {
                    @Override
                    public void onSuccess(JSONObject result) {

                    }

                    @Override
                    public void onError(int code) {

                    }
                });
            }
        });
    }
}
