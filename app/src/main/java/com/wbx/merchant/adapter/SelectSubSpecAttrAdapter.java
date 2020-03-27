package com.wbx.merchant.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wbx.merchant.R;
import com.wbx.merchant.bean.GoodsInfo;

import java.util.List;

/**
 * @author Zero
 * @date 2018/10/11
 */
public class SelectSubSpecAttrAdapter extends BaseQuickAdapter<GoodsInfo.Nature_attr, BaseViewHolder> {
    public SelectSubSpecAttrAdapter(int layoutResId, @Nullable List<GoodsInfo.Nature_attr> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodsInfo.Nature_attr item) {
        helper.getView(R.id.iv_select).setSelected(item.isSelect());
        helper.setText(R.id.tv_name, item.getNature_name());
        helper.addOnClickListener(R.id.ll_attr)
                .addOnClickListener(R.id.iv_delete);
    }
}
