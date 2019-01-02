package com.wbx.merchant.bean;

import java.io.Serializable;

/**
 * @author Zero
 * @date 2018/12/6
 */
public class ConsumeFreeGoodsBean implements Serializable {

    /**
     * goods_id : 60898
     * title : 斑马
     * photo : http://imgs.wbx365.com/FgTls_enB9tHcOtEE9R_TUlegIQM
     * mall_price : 1
     * consume_free_num : 100
     * consume_free_amount : 5
     * consume_free_price : 500
     * consume_free_duration : 5
     */

    private String goods_id;
    private String title;
    private String photo;
    private int price;
    private int num;
    private int sold_num;
    private int consume_free_num;
    private int consume_free_amount;
    private int consume_free_price;
    private long consume_free_duration;

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

    public int getConsume_free_num() {
        return consume_free_num;
    }

    public void setConsume_free_num(int consume_free_num) {
        this.consume_free_num = consume_free_num;
    }

    public int getConsume_free_amount() {
        return consume_free_amount;
    }

    public void setConsume_free_amount(int consume_free_amount) {
        this.consume_free_amount = consume_free_amount;
    }

    public int getConsume_free_price() {
        return consume_free_price;
    }

    public void setConsume_free_price(int consume_free_price) {
        this.consume_free_price = consume_free_price;
    }

    public long getConsume_free_duration() {
        return consume_free_duration;
    }

    public void setConsume_free_duration(long consume_free_duration) {
        this.consume_free_duration = consume_free_duration;
    }
}
