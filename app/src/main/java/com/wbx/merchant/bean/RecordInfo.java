package com.wbx.merchant.bean;

import java.io.Serializable;

/**
 * Created by wushenghui on 2017/6/30.
 */

public class RecordInfo implements Serializable {

    /**
     * gold : 32272
     * addtime : 1492271335
     * bank_name : 中国民生银行
     * bank_num : ***9479
     */

    private int gold;
    private int addtime;
    private String bank_name;
    private String bank_num;
    private String status;
    private String type;

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public int getAddtime() {
        return addtime;
    }

    public void setAddtime(int addtime) {
        this.addtime = addtime;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getBank_num() {
        return bank_num;
    }

    public void setBank_num(String bank_num) {
        this.bank_num = bank_num;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
