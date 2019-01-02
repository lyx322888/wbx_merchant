package com.wbx.merchant.adapter;

import android.content.Context;

import com.wbx.merchant.R;
import com.wbx.merchant.base.BaseAdapter;
import com.wbx.merchant.base.BaseViewHolder;
import com.wbx.merchant.bean.CateInfo;

import java.util.List;

/**
 * Created by wushenghui on 2017/6/23.
 */

public class CateAdapter extends BaseAdapter<CateInfo, Context> {
    public CateAdapter(List<CateInfo> dataList, Context context) {
        super(dataList, context);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_cate;
    }

    @Override
    public void convert(BaseViewHolder holder, CateInfo cateInfo, int position) {
        if (cateInfo.isSelect()) {
            holder.setImageResource(R.id.sel_im, R.drawable.ic_ok);
        } else {
            holder.setImageResource(R.id.sel_im, R.drawable.ic_round);
        }
        holder.setText(R.id.cate_name_tv, cateInfo.getCate_name());
    }
}
