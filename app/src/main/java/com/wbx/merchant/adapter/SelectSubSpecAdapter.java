package com.wbx.merchant.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wbx.merchant.R;
import com.wbx.merchant.bean.GoodsInfo;

import java.util.List;

/**
 * @author Zero
 * @date 2018/10/11
 */
public class SelectSubSpecAdapter extends BaseQuickAdapter<GoodsInfo.Nature, BaseViewHolder> {
    public SelectSubSpecAdapter(int layoutResId, @Nullable List<GoodsInfo.Nature> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodsInfo.Nature item) {
        helper.setText(R.id.tv_project, item.getItem_name());
        StringBuilder stringBuilder = new StringBuilder();
        if (item.getNature_arr() != null) {
            for (int i = 0; i < item.getNature_arr().size(); i++) {
                GoodsInfo.Nature_attr nature_attr = item.getNature_arr().get(i);
                stringBuilder.append(nature_attr.getNature_name());
                if (i != item.getNature_arr().size() - 1) {
                    stringBuilder.append(";");
                }
            }
        }
        helper.setText(R.id.tv_attr, stringBuilder.toString());
        helper.addOnClickListener(R.id.ll_project)
                .addOnClickListener(R.id.iv_delete)
                .addOnClickListener(R.id.ll_attr);
    }
}
