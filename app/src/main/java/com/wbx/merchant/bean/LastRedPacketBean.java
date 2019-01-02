package com.wbx.merchant.bean;

/**
 * @author Zero
 * @date 2018/7/12
 */
public class LastRedPacketBean {

    /**
     * red_packet_id : 5
     * money : 100
     * min_money : 10
     * max_money : 300
     * user_packet_money : 0
     * surplus_red_packet : 100
     */

    private String red_packet_id;
    private String money;
    private String min_money;
    private String max_money;
    private String user_packet_money;
    private String surplus_red_packet;

    public String getRed_packet_id() {
        return red_packet_id;
    }

    public void setRed_packet_id(String red_packet_id) {
        this.red_packet_id = red_packet_id;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getMin_money() {
        return min_money;
    }

    public void setMin_money(String min_money) {
        this.min_money = min_money;
    }

    public String getMax_money() {
        return max_money;
    }

    public void setMax_money(String max_money) {
        this.max_money = max_money;
    }

    public String getUser_packet_money() {
        return user_packet_money;
    }

    public void setUser_packet_money(String user_packet_money) {
        this.user_packet_money = user_packet_money;
    }

    public String getSurplus_red_packet() {
        return surplus_red_packet;
    }

    public void setSurplus_red_packet(String surplus_red_packet) {
        this.surplus_red_packet = surplus_red_packet;
    }
}
