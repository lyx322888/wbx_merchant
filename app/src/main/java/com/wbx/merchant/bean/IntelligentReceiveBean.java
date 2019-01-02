package com.wbx.merchant.bean;

/**
 * Created by Administrator on 2018/3/12 0012.
 */

public class IntelligentReceiveBean {

    /**
     * order_id : 24
     * shop_id : 1318
     * create_time : 1520905934
     * money : 10000
     * pay_type : 余额支付
     * nickname : 画缥缈，谁为风？
     */

    private String order_id;
    private String shop_id;
    private int create_time;
    private int money;
    private String pay_type;
    private String nickname;

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public int getCreate_time() {
        return create_time;
    }

    public void setCreate_time(int create_time) {
        this.create_time = create_time;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getPay_type() {
        return pay_type;
    }

    public void setPay_type(String pay_type) {
        this.pay_type = pay_type;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
