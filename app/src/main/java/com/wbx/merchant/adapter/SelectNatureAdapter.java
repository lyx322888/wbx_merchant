package com.wbx.merchant.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wbx.merchant.R;
import com.wbx.merchant.bean.GoodsInfo;

import java.util.List;

/**
 * @author Zero
 * @date 2018/10/11
 */
public class SelectNatureAdapter extends BaseQuickAdapter<GoodsInfo.Nature, BaseViewHolder> {
    public SelectNatureAdapter(int layoutResId, @Nullable List<GoodsInfo.Nature> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodsInfo.Nature item) {
        helper.setText(R.id.tv_name, item.getItem_name());
        helper.addOnClickListener(R.id.ll_project)
                .addOnClickListener(R.id.iv_delete)
                .addOnClickListener(R.id.iv_edit);
        helper.getView(R.id.tv_already_use).setVisibility(item.getIs_use() == 1 ? View.VISIBLE : View.GONE);
    }
}
