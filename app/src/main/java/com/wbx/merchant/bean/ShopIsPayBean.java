package com.wbx.merchant.bean;

public class ShopIsPayBean {
    /**
     * msg : 成功
     * state : 1
     * data : {"type":"web_apply","is_paid":0}
     */

    private String msg;
    private int state;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * type : web_apply
         * is_paid : 0
         */

        private String type;
        private int is_paid;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getIs_paid() {
            return is_paid;
        }

        public void setIs_paid(int is_paid) {
            this.is_paid = is_paid;
        }
    }
}
