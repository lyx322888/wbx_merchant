package com.wbx.merchant.adapter;


import android.widget.CheckBox;
import android.widget.CompoundButton;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wbx.merchant.R;
import com.wbx.merchant.bean.BusinessHoursBaen;

public class BusinessHoursAdapter extends BaseQuickAdapter<BusinessHoursBaen, BaseViewHolder> {
    public BusinessHoursAdapter() {
        super(R.layout.item_business_hours);
    }

    @Override
    protected void convert(BaseViewHolder helper, BusinessHoursBaen item) {
        CheckBox checkBox = helper.getView(R.id.check_box);
        checkBox.setText(item.getTitle());
        checkBox.setChecked(item.getChoice());
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                item.setChoice(isChecked);
            }
        });
    }
}
