package com.wbx.merchant.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/11/9 0009.
 */

public class ProductJson implements Serializable {
    private String name;
    private  String price;
    private String photo;
    private  int cate_id;

    public int getCate_id() {
        return cate_id;
    }

    public void setCate_id(int cate_id) {
        this.cate_id = cate_id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
