package com.wbx.merchant.adapter;

import android.widget.CompoundButton;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.kyleduo.switchbutton.SwitchButton;
import com.wbx.merchant.R;
import com.wbx.merchant.bean.PayBankListBean;

public class StartServiceAdapter extends BaseQuickAdapter<PayBankListBean.DataBean, BaseViewHolder> {

    public StartServiceAdapter() {
        super(R.layout.item_start_service);
    }

    @Override
    protected void convert(BaseViewHolder helper, final PayBankListBean.DataBean item) {
        helper.setText(R.id.tv_name,item.getName())
                .setText(R.id.et_fl,item.getRate());
        SwitchButton switchButton = helper.getView(R.id.btn_switch);
        switchButton.setChecked(item.isChoice());

        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                item.setChoice(isChecked);
            }
        });

    }

}
