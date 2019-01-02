package com.wbx.merchant.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.wbx.merchant.R;
import com.wbx.merchant.base.BaseAdapter;
import com.wbx.merchant.base.BaseViewHolder;
import com.wbx.merchant.bean.SpecInfo;
import com.wbx.merchant.utils.PriceUtil;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by wushenghui on 2017/12/13.
 */

public class SecKillAttrAdapter extends BaseAdapter<SpecInfo, Context> {
    private DecimalFormat decimalFormat = new DecimalFormat("0.00");

    public SecKillAttrAdapter(List<SpecInfo> dataList, Context context) {
        super(dataList, context);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_seckill_attr;
    }

    @Override
    public void convert(BaseViewHolder holder, final SpecInfo specInfo, int position) {
        holder.setText(R.id.attr_name_tv, specInfo.getAttr_name());
        final EditText priceEdit = holder.getView(R.id.attr_seckill_price_edit);
        priceEdit.setText(decimalFormat.format(specInfo.getSeckill_price()));
        EditText numEdit = holder.getView(R.id.attr_seckill_num_edit);
        numEdit.setText("" + specInfo.getSeckill_num());
        priceEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!PriceUtil.isCorrectPrice(priceEdit, charSequence)) {
                    return;
                }
                if (charSequence.length() == 0) {
                    specInfo.setFloatSeckill_price(0);
                } else {
                    specInfo.setFloatSeckill_price(Float.valueOf(charSequence.toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        numEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 0) {
                    specInfo.setSeckill_num(0);
                } else {
                    specInfo.setSeckill_num(Integer.valueOf(charSequence.toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}
