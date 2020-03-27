package com.wbx.merchant.adapter;

import android.graphics.Color;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wbx.merchant.R;
import com.wbx.merchant.bean.MaterialInfoBean;

import java.util.List;

public class MaterailaCenterDrawerAdapter extends BaseQuickAdapter<MaterialInfoBean, BaseViewHolder> {

    public MaterailaCenterDrawerAdapter(@Nullable List<MaterialInfoBean> data) {
        super(R.layout.item_material_center_drawer, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MaterialInfoBean item) {
        TextView tvType = helper.getView(R.id.tv_type);
        tvType.setText(item.getName());
        if (item.isCheck()) {
            tvType.setTextColor(mContext.getResources().getColor(R.color.app_color));
            helper.getView(R.id.iv_check).setVisibility(View.VISIBLE);
        } else {
            tvType.setTextColor(Color.BLACK);
            helper.getView(R.id.iv_check).setVisibility(View.GONE);
        }
        helper.addOnClickListener(R.id.rl_classify);
    }
}