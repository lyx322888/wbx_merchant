package com.wbx.merchant.bean;

/**
 * @author Zero
 * @date 2018/6/5 0005
 */
public class UploadMemberGoodsBean {
    private int goods_id;
    private float shop_member_price;
    private int attr_id;

    public int getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(int goods_id) {
        this.goods_id = goods_id;
    }

    public float getShop_member_price() {
        return shop_member_price;
    }

    public void setShop_member_price(float shop_member_price) {
        this.shop_member_price = shop_member_price;
    }

    public int getAttr_id() {
        return attr_id;
    }

    public void setAttr_id(int attr_id) {
        this.attr_id = attr_id;
    }
}
