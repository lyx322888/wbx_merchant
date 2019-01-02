package com.wbx.merchant.bean;

import java.io.Serializable;

/**
 * @author Zero
 * @date 2018/6/4 0004
 */
public class MemberBean implements Serializable {

    /**
     * user_id : 13620
     * face :
     * nickname : 陈福灯
     * mobile : 18359253646
     * hx_username : wbx13620
     * is_shop_member : 0
     */

    private String user_id;
    private String face;
    private String nickname;
    private String mobile;
    private String hx_username;
    private int is_shop_member;
    private int count_order;
    private int sum_money;
    private String last_order_time;
    private String last_order_money;

    public String getLast_order_time() {
        return last_order_time;
    }

    public void setLast_order_time(String last_order_time) {
        this.last_order_time = last_order_time;
    }

    public String getLast_order_money() {
        return last_order_money;
    }

    public void setLast_order_money(String last_order_money) {
        this.last_order_money = last_order_money;
    }

    public int getCount_order() {
        return count_order;
    }

    public void setCount_order(int count_order) {
        this.count_order = count_order;
    }

    public int getSum_money() {
        return sum_money;
    }

    public void setSum_money(int sum_money) {
        this.sum_money = sum_money;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getHx_username() {
        return hx_username;
    }

    public void setHx_username(String hx_username) {
        this.hx_username = hx_username;
    }

    public int getIs_shop_member() {
        return is_shop_member;
    }

    public void setIs_shop_member(int is_shop_member) {
        this.is_shop_member = is_shop_member;
    }
}
