package com.wbx.merchant.bean;

import com.bigkoo.pickerview.model.IPickerViewData;

/**
 * Created by wushenghui on 2017/6/23.
 * 店铺分类 实体类
 */

public class CateInfo implements IPickerViewData {
    private int cate_id;
    private String cate_name;
    private String orderby_cate;
    private boolean isSelect;
    private String orderby;
    private String photo;
    private int shop_grade;
    private String name;
    private int money;
    private String detail_url;

    public String getDetail_url() {
        return detail_url;
    }

    public void setDetail_url(String detail_url) {
        this.detail_url = detail_url;
    }

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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getOrderby() {
        return orderby;
    }

    public void setOrderby(String orderby) {
        this.orderby = orderby;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getOrderby_cate() {
        return orderby_cate;
    }

    public void setOrderby_cate(String orderby_cate) {
        this.orderby_cate = orderby_cate;
    }

    public int getCate_id() {
        return cate_id;
    }

    public void setCate_id(int cate_id) {
        this.cate_id = cate_id;
    }

    public String getCate_name() {
        return cate_name;
    }

    public void setCate_name(String cate_name) {
        this.cate_name = cate_name;
    }

    @Override
    public String getPickerViewText() {
        return cate_name;
    }
}
