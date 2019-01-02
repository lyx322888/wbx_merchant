package com.wbx.merchant.bean;

import java.util.List;

/**
 * @author Zero
 * @date 2018/6/5 0005
 */
public class MemberGoodsListBean {

    /**
     * goods_num : 57
     * shop_member_goods_num : 0
     */

    private int goods_num;
    private int shop_member_goods_num;
    private List<GoodsInfo> goods;

    public List<GoodsInfo> getGoods() {
        return goods;
    }

    public void setGoods(List<GoodsInfo> goods) {
        this.goods = goods;
    }

    public int getGoods_num() {
        return goods_num;
    }

    public void setGoods_num(int goods_num) {
        this.goods_num = goods_num;
    }

    public int getShop_member_goods_num() {
        return shop_member_goods_num;
    }

    public void setShop_member_goods_num(int shop_member_goods_num) {
        this.shop_member_goods_num = shop_member_goods_num;
    }
}
