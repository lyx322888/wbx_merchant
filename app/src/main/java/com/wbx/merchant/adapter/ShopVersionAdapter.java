package com.wbx.merchant.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wbx.merchant.R;
import com.wbx.merchant.base.BaseAdapter;
import com.wbx.merchant.base.BaseViewHolder;
import com.wbx.merchant.bean.ShopVersionBean;
import com.wbx.merchant.utils.GlideUtils;

import java.util.List;

/**
 * Created by wushenghui on 2017/6/23.
 */

public class ShopVersionAdapter extends BaseAdapter<ShopVersionBean.DataBean, Context> {

    public ShopVersionAdapter(List<ShopVersionBean.DataBean> dataList, Context context) {
        super(dataList, context);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_shop_version;
    }

    @Override
    public void convert(BaseViewHolder holder, ShopVersionBean.DataBean dataBean, int position) {
        if (dataBean.isSelect()) {
            holder.setImageResource(R.id.sel_im, R.drawable.ic_ok);
        } else {
            holder.setImageResource(R.id.sel_im, R.drawable.ic_round);
        }
        TextView tvVersionName = holder.getView(R.id.tv_version_name);
        tvVersionName.setText(dataBean.getTitle());
        GlideUtils.showMediumPic(mContext, (ImageView) holder.getView(R.id.iv_photo),dataBean.getImg());
        holder.setText(R.id.tv_need_money,dataBean.getPrompt());
    }
}
