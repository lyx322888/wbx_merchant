package com.wbx.merchant.bean;

import java.io.Serializable;

/**
 * Created by wushenghui on 2017/12/12.
 */

public class SecKillSpecJsonBean implements Serializable {
    private int attr_id;
    private int seckill_price;
    private int seckill_num;

    public int getAttr_id() {
        return attr_id;
    }

    public void setAttr_id(int attr_id) {
        this.attr_id = attr_id;
    }

    public int getSeckill_price() {
        return seckill_price;
    }

    public void setSeckill_price(int seckill_price) {
        this.seckill_price = seckill_price;
    }

    public int getSeckill_num() {
        return seckill_num;
    }

    public void setSeckill_num(int seckill_num) {
        this.seckill_num = seckill_num;
    }
}
