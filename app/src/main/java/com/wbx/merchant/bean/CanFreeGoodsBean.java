package com.wbx.merchant.bean;

import java.io.Serializable;

/**
 * @author Zero
 * @date 2018/11/26
 */
public class CanFreeGoodsBean implements Serializable {

    /**
     * goods_id : 60899
     * title : 33
     * photo :
     * price : 3300
     * mall_price : 2200
     * num : 0
     * sold_num : 0
     */

    private String goods_id;
    private String title;
    private String photo;
    private int price;
    private int num;
    private int sold_num;

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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getSold_num() {
        return sold_num;
    }

    public void setSold_num(int sold_num) {
        this.sold_num = sold_num;
    }
}
