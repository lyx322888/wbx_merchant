package com.wbx.merchant.bean;

import java.io.Serializable;

/**
 * @author Zero
 * @date 2018/12/19
 */
public class IntegralFreeGoodsBean implements Serializable {

    /**
     * goods_id : 60898
     * title : 斑马
     * photo : http://imgs.wbx365.com/FgTls_enB9tHcOtEE9R_TUlegIQM
     * price : 1
     * accumulate_free_need_num : 5
     * accumulate_free_get_num : 2
     * sold_num : 0
     * num : 0
     */

    private String goods_id;
    private String title;
    private String photo;
    private int price;
    private int accumulate_free_need_num;
    private int accumulate_free_get_num;
    private int sold_num;
    private int num;

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

    public int getAccumulate_free_need_num() {
        return accumulate_free_need_num;
    }

    public void setAccumulate_free_need_num(int accumulate_free_need_num) {
        this.accumulate_free_need_num = accumulate_free_need_num;
    }

    public int getAccumulate_free_get_num() {
        return accumulate_free_get_num;
    }

    public void setAccumulate_free_get_num(int accumulate_free_get_num) {
        this.accumulate_free_get_num = accumulate_free_get_num;
    }

    public int getSold_num() {
        return sold_num;
    }

    public void setSold_num(int sold_num) {
        this.sold_num = sold_num;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
