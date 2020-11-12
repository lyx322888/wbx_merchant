package com.wbx.merchant.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wbx.merchant.R;
import com.wbx.merchant.bean.FollowersDrainageBean;
import com.wbx.merchant.widget.SelectedTextView;

import java.util.List;

public class FollowersDrainageAdapter extends BaseQuickAdapter<FollowersDrainageBean.DataBean.HobbyBean, BaseViewHolder> {
    public FollowersDrainageAdapter() {
        super(R.layout.item_xqah);
    }

    @Override
    protected void convert(BaseViewHolder helper, FollowersDrainageBean.DataBean.HobbyBean item) {
        SelectedTextView selectedTextView = helper.getView(R.id.tv_content);
        selectedTextView.setText(item.getName());
        if (item.getIs_select()==1){
            selectedTextView.setSelect(true);
        }else {
            selectedTextView.setSelect(false);
        }
    }
}
