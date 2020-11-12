package com.wbx.merchant.bean;

public class CricePayBaen {

    public CricePayBaen(String price, boolean isSelect) {
        this.price = price;
        this.isSelect = isSelect;
    }

    public String price;
    public boolean isSelect = false;
}
