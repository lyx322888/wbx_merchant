package com.wbx.merchant.bean;

public class NeedLoginBean {

    /**
     * msg : 成功
     * state : 1
     * data : {"need_login":0,"registration_id":"7bd49cde45ad47e8940beb86a20b8d4c"}
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
         * need_login : 0
         * registration_id : 7bd49cde45ad47e8940beb86a20b8d4c
         */

        private int need_login;
        private String registration_id;

        public int getNeed_login() {
            return need_login;
        }

        public void setNeed_login(int need_login) {
            this.need_login = need_login;
        }

        public String getRegistration_id() {
            return registration_id;
        }

        public void setRegistration_id(String registration_id) {
            this.registration_id = registration_id;
        }
    }
}
