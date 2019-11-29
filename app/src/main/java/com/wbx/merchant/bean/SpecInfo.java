package com.wbx.merchant.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * Created by wushenghui on 2017/7/25.
 */

public class SpecInfo implements Serializable {

    private int attr_id;
    private String attr_name;
    private double price;//商城价
    private double mall_price;//市场价
    private double sales_promotion_price;//促销价
    private double casing_price;//餐盒费
    private long num;//库存
    private int loss;//损耗
    private float seckill_price;
    private int seckill_num;
    private float shop_member_price;//会员价
    private String subhead;
    @JSONField(serialize = false)
    private float min_price;

    public float getShop_member_price() {
        return shop_member_price;
    }

    public void setShop_member_price(float shop_member_price) {
        this.shop_member_price = shop_member_price;
    }

    public float getSeckill_price() {
        return seckill_price;
    }

    public void setSeckill_price(float seckill_price) {
        this.seckill_price = seckill_price;
    }

    public void setFloatSeckill_price(float seckill_price) {
        this.seckill_price = seckill_price;
    }

    public int getSeckill_num() {
        return seckill_num;
    }

    public void setSeckill_num(int seckill_num) {
        this.seckill_num = seckill_num;
    }

    public int getAttr_id() {
        return attr_id;
    }

    public void setAttr_id(int attr_id) {
        this.attr_id = attr_id;
    }

    public int getLoss() {
        return loss;
    }

    public void setLoss(int loss) {
        this.loss = loss;
    }

    public String getAttr_name() {
        return attr_name;
    }

    public void setAttr_name(String attr_name) {
        this.attr_name = attr_name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getMall_price() {
        return mall_price;
    }

    public void setMall_price(double mall_price) {
        this.mall_price = mall_price;
    }

    public double getSales_promotion_price() {
        return sales_promotion_price;
    }

    public void setSales_promotion_price(double sales_promotion_price) {
        this.sales_promotion_price = sales_promotion_price;
    }

    public long getNum() {
        return num;
    }

    public void setNum(long num) {
        this.num = num;
    }

    public double getCasing_price() {
        return casing_price;
    }

    public void setCasing_price(double casing_price) {
        this.casing_price = casing_price;
    }

    public String getSubhead() {
        return subhead;
    }

    public void setSubhead(String subhead) {
        this.subhead = subhead;
    }

    public float getMin_price() {
        return min_price;
    }

    public void setMin_price(float min_price) {
        this.min_price = min_price;
    }
}
