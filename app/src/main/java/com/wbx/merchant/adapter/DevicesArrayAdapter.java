package com.wbx.merchant.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wbx.merchant.R;
import com.wbx.merchant.bean.DevicesBean;

import java.util.List;

public class DevicesArrayAdapter extends BaseQuickAdapter<DevicesBean, BaseViewHolder> {
    public DevicesArrayAdapter(int layoutResId, @Nullable List<DevicesBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DevicesBean item) {
        helper.setText(R.id.tv_content,item.content);
    }
}
