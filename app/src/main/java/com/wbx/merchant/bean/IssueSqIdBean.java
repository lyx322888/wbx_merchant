package com.wbx.merchant.bean;

public class IssueSqIdBean {
    /**
     * msg : 成功
     * state : 1
     * data : {"discover_id":"788"}
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
         * discover_id : 788
         */

        private String discover_id;

        public String getDiscover_id() {
            return discover_id;
        }

        public void setDiscover_id(String discover_id) {
            this.discover_id = discover_id;
        }
    }
}
