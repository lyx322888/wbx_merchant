package com.wbx.merchant.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wbx.merchant.R;
import com.wbx.merchant.bean.StoreSetMealBean;
import com.wbx.merchant.utils.GlideUtils;
import com.wbx.merchant.utils.SpannableStringUtils;

public class AddDdtcAdapter extends BaseQuickAdapter<StoreSetMealBean.DataBean, BaseViewHolder>{
    public AddDdtcAdapter() {
        super(R.layout.item_select_ddtc);
    }

    @Override
    protected void convert(BaseViewHolder helper, StoreSetMealBean.DataBean item) {
        GlideUtils.showBigPic(mContext,helper.getView(R.id.iv_shop),item.getPhoto());
        helper.setText(R.id.tv_title,item.getSet_meal_name())
                .setText(R.id.tv_time,item.getBusiness_week_days_str()+"  "+item.getCan_hours())
                .setText(R.id.tv_price,"¥"+item.getOne_price()/100.00);
    }
}
