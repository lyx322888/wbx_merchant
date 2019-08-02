package com.wbx.merchant.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.wbx.merchant.R;
import com.wbx.merchant.base.BaseAdapter;
import com.wbx.merchant.base.BaseViewHolder;
import com.wbx.merchant.bean.SpecInfo;
import com.wbx.merchant.utils.PriceUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wushenghui on 2017/7/25.
 */

public class SpecAdapter extends BaseAdapter<SpecInfo, Context> {
    private List<SpecInfo> mListData;
    private boolean mIsInventory;//是否启用库存
    private boolean mIsSales;//是否促销
    private boolean mIsShop;//是否实体店
    private boolean blnIsFoodStreet;//是否是美食街

    public SpecAdapter(List<SpecInfo> dataList, Context context) {
        super(dataList, context);
        this.mListData = dataList;
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_spec;
    }

    @Override
    public void convert(BaseViewHolder holder, SpecInfo specInfo, int position) {
        SpecInfo specInfoData = mListData.get(position);
        if (mIsInventory) {
            holder.getView(R.id.stock_layout).setVisibility(View.VISIBLE);
        }
        if (mIsSales) {
            holder.getView(R.id.sales_layout).setVisibility(View.VISIBLE);
        }
        if (mIsShop) {
            holder.getView(R.id.show_shop_price_ll).setVisibility(View.VISIBLE);
        }
        final EditText specNameEdit = holder.getView(R.id.spec_name_edit);
        final EditText marketPriceEdit = holder.getView(R.id.market_price_edit);
        final EditText priceEdit = holder.getView(R.id.price_edit);
        final EditText promotionalPriceEdit = holder.getView(R.id.promotional_price_edit);
        final EditText stockEdit = holder.getView(R.id.stock_edit);
        final EditText etPackingFee = holder.getView(R.id.et_packing_fee);
        holder.getView(R.id.ll_packing_fee).setVisibility(blnIsFoodStreet ? View.VISIBLE : View.GONE);
        specNameEdit.setTag(position);
        marketPriceEdit.setTag(position);
        priceEdit.setTag(position);
        promotionalPriceEdit.setTag(position);
        stockEdit.setTag(position);
        etPackingFee.setTag(position);
        specNameEdit.setText(specInfoData.getAttr_name());
        marketPriceEdit.setText(specInfoData.getPrice() == 0 ? "" : String.format("%.2f", specInfoData.getPrice()/100));
        priceEdit.setText(specInfoData.getPrice() == 0 ? "" : String.format("%.2f", specInfoData.getMall_price()/100));
        promotionalPriceEdit.setText(specInfoData.getPrice() == 0 ? "" : String.format("%.2f", specInfoData.getSales_promotion_price()/100));
        stockEdit.setText(specInfoData.getNum() + "");
        etPackingFee.setText(specInfoData.getPrice() == 0 ? "" : String.format("%.2f", specInfoData.getCasing_price()/100));
        specNameEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int tag = (int) specNameEdit.getTag();
                if (TextUtils.isEmpty(charSequence)) {
                    mListData.get(tag).setAttr_name("");
                } else {
                    mListData.get(tag).setAttr_name(charSequence.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        marketPriceEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!PriceUtil.isCorrectPrice(marketPriceEdit, charSequence)) {
                    return;
                }
                int tag = (int) marketPriceEdit.getTag();
                if (TextUtils.isEmpty(charSequence)) {
                    mListData.get(tag).setPrice(0.0);
                } else {
                    mListData.get(tag).setPrice(Double.valueOf(charSequence.toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        priceEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!PriceUtil.isCorrectPrice(priceEdit, charSequence)) {
                    return;
                }
                int tag = (int) priceEdit.getTag();
                if (TextUtils.isEmpty(charSequence)) {
                    mListData.get(tag).setMall_price(0.0);
                } else {
                    mListData.get(tag).setMall_price(Double.valueOf(charSequence.toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        promotionalPriceEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!PriceUtil.isCorrectPrice(promotionalPriceEdit, charSequence)) {
                    return;
                }
                int tag = (int) promotionalPriceEdit.getTag();
                if (TextUtils.isEmpty(charSequence)) {
                    mListData.get(tag).setSales_promotion_price(0.0);
                } else {
                    mListData.get(tag).setSales_promotion_price(Double.valueOf(charSequence.toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        stockEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int tag = (int) stockEdit.getTag();
                if (TextUtils.isEmpty(charSequence)) {
                    mListData.get(tag).setNum(0);
                } else {
                    mListData.get(tag).setNum(Integer.valueOf(charSequence.toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        etPackingFee.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!PriceUtil.isCorrectPrice(etPackingFee, charSequence)) {
                    return;
                }
                int tag = (int) etPackingFee.getTag();
                if (TextUtils.isEmpty(charSequence)) {
                    mListData.get(tag).setCasing_price(0.0);
                } else {
                    mListData.get(tag).setCasing_price(Double.valueOf(charSequence.toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    public void setShowing(boolean isInventory, boolean isSales, boolean isShop, boolean isFoodStreet) {
        this.mIsInventory = isInventory;
        this.mIsSales = isSales;
        this.mIsShop = isShop;
        this.blnIsFoodStreet = isFoodStreet;
    }

    public void setListData(List<SpecInfo> listData) {
        this.mListData = listData;
        notifyDataSetChanged();
    }
}
