package com.wbx.merchant.bean;

/**
 * Created by wushenghui on 2017/11/17.
 */

public class WeChatBusEvent {
    private String openId;
    private String nickName;

    public WeChatBusEvent(String openId, String nickName) {
        this.openId = openId;
        this.nickName = nickName;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
