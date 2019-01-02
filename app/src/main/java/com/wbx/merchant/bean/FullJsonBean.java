package com.wbx.merchant.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/10/27 0027.
 */

public class FullJsonBean implements Serializable {
    private float full_money;
    private float reduce_money;

    public float getFull_money() {
        return full_money;
    }

    public void setFull_money(float full_money) {
        this.full_money = full_money;
    }

    public float getReduce_money() {
        return reduce_money;
    }

    public void setReduce_money(float reduce_money) {
        this.reduce_money = reduce_money;
    }
}
