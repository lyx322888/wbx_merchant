package com.wbx.merchant.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wbx.merchant.R;
import com.wbx.merchant.bean.DaDaCancelReasonBean;

import java.util.List;

/**
 * @author Zero
 * @date 2018/11/12
 */
public class DaDaCancelReasonAdapter extends BaseQuickAdapter<DaDaCancelReasonBean, BaseViewHolder> {
    public DaDaCancelReasonAdapter(@Nullable List<DaDaCancelReasonBean> data) {
        super(R.layout.item_dada_cancel_reason, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DaDaCancelReasonBean item) {
        helper.setText(R.id.tv_reason, item.getReason());
        helper.addOnClickListener(R.id.ll_root);
        helper.getView(R.id.iv_select).setSelected(item.isSelect());
    }
}
