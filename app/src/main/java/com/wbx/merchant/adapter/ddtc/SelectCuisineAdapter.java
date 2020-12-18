package com.wbx.merchant.adapter.ddtc;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.databinding.Observable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wbx.merchant.R;
import com.wbx.merchant.activity.AddStoreSetMealActivity;
import com.wbx.merchant.bean.CuisineOhterBean;
import com.wbx.merchant.widget.AddNumView;
import com.wbx.merchant.widget.PriceEditText;


public class SelectCuisineAdapter extends BaseQuickAdapter<CuisineOhterBean, BaseViewHolder> {
    AddStoreSetMealActivity addStoreSetMealActivity;
    public SelectCuisineAdapter( AddStoreSetMealActivity addStoreSetMealActivity) {
        super(R.layout.item_cuisine_other);
        this.addStoreSetMealActivity = addStoreSetMealActivity;
    }

    @Override
    protected void convert(BaseViewHolder helper, CuisineOhterBean item) {
        CheckBox checkBox = helper.getView(R.id.check_box);
        checkBox.setText(item.getTitle());
        checkBox.setChecked(item.isChoice());
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                item.setChoice(isChecked);
            }
        });

        PriceEditText priceEditText = helper.getView(R.id.et_price);
        if (TextUtils.isEmpty(item.getPrice())){
            helper.setText(R.id.tv_price, "¥ " + 0.00);
        }else {
            helper.setText(R.id.tv_price, "¥ " + Float.valueOf(item.getPrice()) * item.getNum());
        }

        priceEditText.setText(item.getPrice());
        priceEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                    item.setPrice(s.toString());
                    if (TextUtils.isEmpty(s)){
                        helper.setText(R.id.tv_price, "¥ " + 0.00);
                    }else {
                        helper.setText(R.id.tv_price, "¥ " + Float.valueOf(item.getPrice()) * item.getNum());
                    }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

//        helper.setText(R.id.tv_price,"¥ "+Float.valueOf(item.getPrice())*item.getNum());
        AddNumView addNumView = helper.getView(R.id.addview);
        addNumView.setNum(item.getNum());
        addNumView.setClickListener(new AddNumView.ClickListener() {
            @Override
            public void reachedMinNum() {

            }

            @Override
            public void minusClickListener() {
                super.minusClickListener();
                item.setNum(addNumView.getNum());
                notifyItemChanged(helper.getLayoutPosition());

            }

            @Override
            public void addClickListener() {
                super.addClickListener();
                item.setNum(addNumView.getNum());
                notifyItemChanged(helper.getLayoutPosition());
            }

        });


        item.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                addStoreSetMealActivity.dataChangedCallback();
            }
        });
    }

    public Float getPrice(){
        Float price = new Float(0.0);;
        for (int i = 0; i < getData().size(); i++) {
            if (getData().get(i).isChoice()){
                if (!TextUtils.isEmpty(getData().get(i).getPrice())){
                    price+= Float.valueOf(getData().get(i).getPrice())*getData().get(i).getNum();
                }
            }
        }
        return price;
    }
}
