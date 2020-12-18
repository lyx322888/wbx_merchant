package com.wbx.merchant.bean;

import androidx.databinding.BaseObservable;

public class CuisineBean extends BaseObservable {

    public CuisineBean(String title, String price, int num, String goods_id) {
        this.title = title;
        this.price = price;
        this.goods_num = num;
        this.goods_id = goods_id;
    }

    private String title;
    private String price;
    private int goods_num;
    private String goods_id;


    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getGoods_num() {
        return goods_num;
    }

    public void setGoods_num(int goods_num) {
        this.goods_num = goods_num;
        notifyChange();
    }


}
