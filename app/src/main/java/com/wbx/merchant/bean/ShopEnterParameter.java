package com.wbx.merchant.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/3/30 0030.
 */

public class ShopEnterParameter implements Serializable {
    private String gradeName;
    private int shopGradeId;
    private int needPayPrice;
    private int gradeId;

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public int getShopGradeId() {
        return shopGradeId;
    }

    public void setShopGradeId(int shopGradeId) {
        this.shopGradeId = shopGradeId;
    }

    public int getNeedPayPrice() {
        return needPayPrice;
    }

    public void setNeedPayPrice(int needPayPrice) {
        this.needPayPrice = needPayPrice;
    }

    public int getGradeId() {
        return gradeId;
    }

    public void setGradeId(int gradeId) {
        this.gradeId = gradeId;
    }
}
