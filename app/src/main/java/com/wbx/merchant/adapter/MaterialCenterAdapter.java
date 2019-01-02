package com.wbx.merchant.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.wbx.merchant.R;
import com.wbx.merchant.base.BaseAdapter;
import com.wbx.merchant.base.BaseViewHolder;
import com.wbx.merchant.bean.MaterialInfoBean;
import com.wbx.merchant.utils.GlideUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/11/9 0009.
 */

public class MaterialCenterAdapter extends BaseAdapter<MaterialInfoBean.ProductBean, Context> {
    public MaterialCenterAdapter(List<MaterialInfoBean.ProductBean> dataList, Context context) {
        super(dataList, context);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_release;
    }

    @Override
    public void convert(BaseViewHolder holder, MaterialInfoBean.ProductBean cateInfo, int position) {
        TextView fb_tv = holder.getView(R.id.item_fb_tv);
        fb_tv.setText(cateInfo.getName());
        ImageView cb = holder.getView(R.id.item_release_im);
        ImageView productIm = holder.getView(R.id.item_recycler_img);
        GlideUtils.showMediumPic(mContext, productIm, cateInfo.getPhoto());
        boolean select = cateInfo.getIsCheck();
        if (select) {
            cb.setImageResource(R.drawable.ic_ok);
        } else {
            cb.setImageResource(R.drawable.ic_round);
        }
    }
}
