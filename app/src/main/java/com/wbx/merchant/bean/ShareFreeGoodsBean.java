package com.wbx.merchant.bean;

import java.io.Serializable;

/**
 * @author Zero
 * @date 2018/12/6
 */
public class ShareFreeGoodsBean implements Serializable {

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
    private int share_free_num;
    private int share_free_amount;
    private long share_free_duration;

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

    public int getShare_free_num() {
        return share_free_num;
    }

    public void setShare_free_num(int share_free_num) {
        this.share_free_num = share_free_num;
    }

    public int getShare_free_amount() {
        return share_free_amount;
    }

    public void setShare_free_amount(int share_free_amount) {
        this.share_free_amount = share_free_amount;
    }

    public long getShare_free_duration() {
        return share_free_duration;
    }

    public void setShare_free_duration(long share_free_duration) {
        this.share_free_duration = share_free_duration;
    }
}
