package com.wbx.merchant.bean;

import androidx.databinding.BaseObservable;


public class CuisineOhterBean extends BaseObservable {

    public CuisineOhterBean(String title, String price, int num, boolean choice) {
        this.title = title;
        this.price = price;
        this.num = num;
        this.choice = choice;
    }

    private String title;
    private String price;
    private int num;

    private boolean choice;

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
        notifyChange();
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
        notifyChange();
    }
    public boolean isChoice() {
        return choice;
    }

    public void setChoice(boolean choice) {
        this.choice = choice;
        notifyChange();
    }

}
