package com.wbx.merchant.adapter;

import android.content.Context;

import com.wbx.merchant.R;
import com.wbx.merchant.base.BaseAdapter;
import com.wbx.merchant.base.BaseViewHolder;
import com.wbx.merchant.bean.CateInfo;

import java.util.List;

/**
 * Created by wushenghui on 2017/7/21.
 */

public class ScreenCateAdapter extends BaseAdapter<CateInfo,Context> {

    public ScreenCateAdapter(List<CateInfo> dataList, Context context) {
        super(dataList, context);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_screen_cate;
    }

    @Override
    public void convert(BaseViewHolder holder, CateInfo cateInfo, int position) {
       holder.setText(R.id.cate_name_tv,cateInfo.getCate_name());
    }
}
