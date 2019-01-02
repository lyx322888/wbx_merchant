package com.wbx.merchant.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/10/24 0024.
 */

public class ThirdOrderInfo implements Serializable {

    /**
     * order_id : 5372
     * order_type : 1
     * order_status : 待接单
     * dm_name :
     * dm_mobile :
     * cancel_reason :
     * dispatching_type : 达达配送
     */

    private String order_id;
    private int order_type;
    private String order_status;
    private String dm_name;
    private String dm_mobile;
    private String cancel_reason;
    private int money;
    private String dispatching_type;

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public int getOrder_type() {
        return order_type;
    }

    public void setOrder_type(int order_type) {
        this.order_type = order_type;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getDm_name() {
        return dm_name;
    }

    public void setDm_name(String dm_name) {
        this.dm_name = dm_name;
    }

    public String getDm_mobile() {
        return dm_mobile;
    }

    public void setDm_mobile(String dm_mobile) {
        this.dm_mobile = dm_mobile;
    }

    public String getCancel_reason() {
        return cancel_reason;
    }

    public void setCancel_reason(String cancel_reason) {
        this.cancel_reason = cancel_reason;
    }

    public String getDispatching_type() {
        return dispatching_type;
    }

    public void setDispatching_type(String dispatching_type) {
        this.dispatching_type = dispatching_type;
    }
}
