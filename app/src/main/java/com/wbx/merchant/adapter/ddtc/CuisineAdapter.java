package com.wbx.merchant.adapter.ddtc;


import androidx.databinding.Observable;
import androidx.fragment.app.FragmentActivity;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wbx.merchant.R;
import com.wbx.merchant.activity.AddStoreSetMealActivity;
import com.wbx.merchant.bean.CuisineBean;
import com.wbx.merchant.dialog.ConfirmDialog;
import com.wbx.merchant.widget.AddNumView;

public class CuisineAdapter extends BaseQuickAdapter<CuisineBean, BaseViewHolder>  {
    private AddStoreSetMealActivity addStoreSetMealActivity;
    public CuisineAdapter(AddStoreSetMealActivity addStoreSetMealActivity) {
        super(R.layout.itemm_cuisine);
        this.addStoreSetMealActivity = addStoreSetMealActivity;
    }

    @Override
    protected void convert(BaseViewHolder helper, CuisineBean item) {
        helper.setText(R.id.tv_title,item.getTitle()+"/份")
                .setText(R.id.tv_price,"¥ "+Float.valueOf(item.getPrice())*item.getGoods_num());
        AddNumView  addNumView = helper.getView(R.id.addview);
        addNumView.setNum(item.getGoods_num());
        addNumView.setClickListener(new AddNumView.ClickListener() {
            @Override
            public void reachedMinNum() {
                ConfirmDialog confirmDialog = ConfirmDialog.newInstance("您确定删除该商品吗？");
                confirmDialog.setDialogListener(new ConfirmDialog.DialogListener() {
                    @Override
                    public void dialogClickListener() {
                        mData.remove(item);
                        notifyDataSetChanged();
                    }
                });
                confirmDialog.show(((FragmentActivity)(mContext)).getSupportFragmentManager(),"");

            }

            @Override
            public void minusClickListener() {
                super.minusClickListener();
                item.setGoods_num(addNumView.getNum());
                notifyItemChanged(helper.getLayoutPosition());
            }

            @Override
            public void addClickListener() {
                super.addClickListener();
                item.setGoods_num(addNumView.getNum());
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

    //取总价
    public Float getPrice(){
        float price = 0;
        for (int i = 0; i < getData().size(); i++) {
            price+= Float.valueOf(getData().get(i).getPrice())*getData().get(i).getGoods_num();
        }
        return price;
    }

    //取最高价
    public Float getTopPrice(){
        float price = 0;
        for (int i = 0; i < getData().size(); i++) {
            if (Float.valueOf(getData().get(i).getPrice())*getData().get(i).getGoods_num()>price){
                price = Float.valueOf(getData().get(i).getPrice())*getData().get(i).getGoods_num();
            }
        }
        return price;
    }





}
