package com.wbx.merchant.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.wbx.merchant.R;
import com.wbx.merchant.base.BaseAdapter;
import com.wbx.merchant.base.BaseViewHolder;
import com.wbx.merchant.bean.CateInfo;
import com.wbx.merchant.utils.GlideUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/11/6 0006.
 */

public class ProductAdapter extends BaseAdapter<CateInfo, Context> {

    public ProductAdapter(List<CateInfo> dataList, Context context) {
        super(dataList, context);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_product;
    }

    @Override
    public void convert(BaseViewHolder holder, CateInfo cate, int position) {
        ImageView img_product = holder.getView(R.id.img_product);
        GlideUtils.showMediumPic(mContext, img_product, cate.getPhoto());
    }
}
