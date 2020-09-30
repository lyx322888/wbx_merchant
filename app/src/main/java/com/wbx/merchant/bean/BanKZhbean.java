package com.wbx.merchant.bean;

import java.util.List;

public class BanKZhbean {
    /**
     * msg : 成功
     * state : 1
     * data : ["工行福建省厦门思明支行"]
     */

    private String msg;
    private int state;
    private List<String> data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}
