package com.wbx.merchant.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import com.wbx.merchant.R;
import com.wbx.merchant.base.BaseAdapter;
import com.wbx.merchant.base.BaseViewHolder;
import com.wbx.merchant.bean.SpecInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wushenghui on 2017/9/30.
 * //修改库存
 */

public class UpDateNumAdapter extends BaseAdapter<SpecInfo, Context> {

    private List<SpecInfo> mListData = new ArrayList<>();

    public UpDateNumAdapter(List<SpecInfo> dataList, Context context) {
        super(dataList, context);
        this.mListData = dataList;
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_update_num;
    }

    @Override
    public void convert(BaseViewHolder holder, SpecInfo specInfo, int position) {
        holder.setText(R.id.spec_name_tv, specInfo.getAttr_name()).setText(R.id.number_eidt, specInfo.getNum() + "").setText(R.id.loss_edit, specInfo.getLoss() + "");
        final EditText numEdit = holder.getView(R.id.number_eidt);
        final EditText lossEdit = holder.getView(R.id.loss_edit);
        numEdit.setTag(position);
        lossEdit.setTag(position);
        numEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int tag = (int) numEdit.getTag();
                if (TextUtils.isEmpty(charSequence)) {
                    mListData.get(tag).setNum(0);
                } else if (charSequence.equals("-")) {
                    mListData.get(tag).setNum(0);
                } else {
                    mListData.get(tag).setNum(Integer.valueOf(charSequence.toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        lossEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int tag = (int) numEdit.getTag();
                if (TextUtils.isEmpty(charSequence)) {
                    mListData.get(tag).setLoss(0);
                } else {
                    mListData.get(tag).setLoss(Integer.valueOf(charSequence.toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public List<SpecInfo> getSpecData() {
        return mListData;
    }
}
