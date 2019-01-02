package com.wbx.merchant.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;

import com.wbx.merchant.R;
import com.wbx.merchant.base.BaseAdapter;
import com.wbx.merchant.base.BaseViewHolder;
import com.wbx.merchant.bean.MaterialInfoBean;
import com.wbx.merchant.utils.GlideUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/11/7 0007.
 */

public class ProductReleaseAdapter extends BaseAdapter<MaterialInfoBean.ProductBean, Context> {
    private List<MaterialInfoBean.ProductBean> mDataList;
    public boolean isDel = false;

    public ProductReleaseAdapter(List<MaterialInfoBean.ProductBean> saveList, Context context) {
        super(saveList, context);
        mDataList = saveList;
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_recycler_release;
    }

    @Override
    public void convert(BaseViewHolder holder, final MaterialInfoBean.ProductBean productInfo, int position) {
        ImageView recycler_img = holder.getView(R.id.item_recycler_img);
        GlideUtils.showMediumPic(mContext, recycler_img, productInfo.getPhoto());
        EditText titleEdit = holder.getView(R.id.product_title_edit);
        EditText moneyEdit = holder.getView(R.id.product_money_edit);
        if (titleEdit.getTag() instanceof TextWatcher) {
            titleEdit.removeTextChangedListener((TextWatcher) titleEdit.getTag());
        }
        titleEdit.setText(productInfo.getName());
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!isDel) {
                    productInfo.setName(s.toString());
                }
            }
        };
        titleEdit.addTextChangedListener(textWatcher);
        titleEdit.setTag(textWatcher);

        if (moneyEdit.getTag() instanceof TextWatcher) {
            moneyEdit.removeTextChangedListener((TextWatcher) moneyEdit.getTag());
        }
        moneyEdit.setText(productInfo.getPrice());
        TextWatcher moneyTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!isDel && !TextUtils.isEmpty(s)) {
                    productInfo.setPrice(s.toString());
                }
            }
        };
        moneyEdit.addTextChangedListener(moneyTextWatcher);
        moneyEdit.setTag(moneyTextWatcher);
        if (position == mDataList.size() - 1) {
            isDel = false;
        }
    }
}
