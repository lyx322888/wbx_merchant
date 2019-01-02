package com.wbx.merchant.bean;

/**
 * Created by wushenghui on 2017/6/23.
 * 店铺版本
 */

public class ShopVersionBean {

    /**
     * shop_grade : 1
     * name : 通用版
     * money : 1
     * detail_url : http://www.wbx365.com/Wbxwaphome/help/newratail2.html
     * original_price : 988000
     * surplus_num : 849
     */
    private boolean isSelect;
    private int shop_grade;
    private String name;
    private int money;
    private String detail_url;
    private int original_price;
    private int surplus_num;

    public int getShop_grade() {
        return shop_grade;
    }

    public void setShop_grade(int shop_grade) {
        this.shop_grade = shop_grade;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getDetail_url() {
        return detail_url;
    }

    public void setDetail_url(String detail_url) {
        this.detail_url = detail_url;
    }

    public int getOriginal_price() {
        return original_price;
    }

    public void setOriginal_price(int original_price) {
        this.original_price = original_price;
    }

    public int getSurplus_num() {
        return surplus_num;
    }

    public void setSurplus_num(int surplus_num) {
        this.surplus_num = surplus_num;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
