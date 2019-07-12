package com.wbx.merchant.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.wbx.merchant.R;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseAdapter;
import com.wbx.merchant.base.BaseViewHolder;
import com.wbx.merchant.bean.OrderBean;
import com.wbx.merchant.common.LoginUtil;
import com.wbx.merchant.utils.GlideUtils;
import com.wbx.merchant.widget.CicleAddAndSubView;

import java.util.List;

public class OrderGoodsAadpter extends BaseAdapter<OrderBean.DataBean.OrderGoodsBean, Context>{

    private int number;

    public OrderGoodsAadpter(List<OrderBean.DataBean.OrderGoodsBean> dataList, Context context) {
        super(dataList, context);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_order_goods_info;
    }

    @Override
    public void convert(BaseViewHolder holder, final OrderBean.DataBean.OrderGoodsBean orderBean, final int position) {
        TextView tvName = holder.getView(R.id.tv_name);
        TextView tvPrice = holder.getView(R.id.tv_price);

        ImageView tvPhoto = holder.getView(R.id.iv_photo);
        final CicleAddAndSubView addorsub = holder.getView(R.id.addorsub);
        tvName.setText(orderBean.getTitle());
        tvPrice.setText("¥" + (float) orderBean.getPrice() / 100 + "");
        addorsub.setNum(orderBean.getOrder_num());
        GlideUtils.showRoundSmallPic(mContext, tvPhoto, orderBean.getPhoto());
        addorsub.setOnNumChangeListener(new CicleAddAndSubView.OnNumChangeListener() {
            @Override
            public void onNumChange(View view, String stype, final int num) {
                new MyHttp().doPost(Api.getDefault().getUpdate(LoginUtil.getLoginToken(), orderBean.getGoods_id(), stype), new HttpListener() {
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
