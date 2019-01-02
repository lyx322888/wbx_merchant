package com.wbx.merchant.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wushenghui on 2017/12/12.
 */

public class SecKillJsonBean implements Serializable {
    private int goods_id;
    private int seckill_price;
    private int seckill_num;
    private List<SecKillSpecJsonBean> goods_attr;
    public int getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(int goods_id) {
        this.goods_id = goods_id;
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

    public List<SecKillSpecJsonBean> getGoods_attr() {
        return goods_attr;
    }

    public void setGoods_attr(List<SecKillSpecJsonBean> goods_attr) {
        this.goods_attr = goods_attr;
    }
}
