package com.wbx.merchant.bean;

/**
 * @author Zero
 * @date 2018/7/27
 */
public class RedPacketListBean {

    /**
     * user_id : 10796
     * create_time : 2018-07-26 10:53
     * money : 102
     * face : http://imgs.wbx365.com/182059554091501656472
     * nickname : 熊二
     */

    private int user_id;
    private String create_time;
    private float money;
    private String face;
    private String nickname;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
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
}
