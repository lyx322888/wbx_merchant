package com.wbx.merchant.bean;

import com.bigkoo.pickerview.model.IPickerViewData;

/**
 * Created by wushenghui on 2017/6/22.
 * 店铺等级
 */

public class ShopGradeInfo implements IPickerViewData {

    /**
     * grade_id : 19
     * grade_name : 实体店
     * money : 258000
     */

    private int grade_id;
    private String grade_name;
    private int money;

    public int getGrade_id() {
        return grade_id;
    }

    public void setGrade_id(int grade_id) {
        this.grade_id = grade_id;
    }

    public String getGrade_name() {
        return grade_name;
    }

    public void setGrade_name(String grade_name) {
        this.grade_name = grade_name;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    @Override
    public String getPickerViewText() {
        return grade_name;
    }
}
