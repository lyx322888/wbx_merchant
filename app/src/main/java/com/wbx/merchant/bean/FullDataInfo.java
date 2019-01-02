package com.wbx.merchant.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/10/27 0027.
 */

public class FullDataInfo implements Serializable {
    private int id;
    private int reduce_money;//减多少钱
    private int full_money;//满多少钱

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReduce_money() {
        return reduce_money;
    }

    public void setReduce_money(int reduce_money) {
        this.reduce_money = reduce_money;
    }

    public int getFull_money() {
        return full_money;
    }

    public void setFull_money(int full_money) {
        this.full_money = full_money;
    }
}
