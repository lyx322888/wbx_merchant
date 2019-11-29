package com.wbx.merchant.bean;

public class CountBean {
    /**
     * msg : 成功
     * state : 1
     * data : {"count_no_shipped_order":946,"count_shipped_order":71}
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
         * count_no_shipped_order : 946
         * count_shipped_order : 71
         */

        private int count_no_shipped_order;
        private int count_shipped_order;

        public int getCount_no_shipped_order() {
            return count_no_shipped_order;
        }

        public void setCount_no_shipped_order(int count_no_shipped_order) {
            this.count_no_shipped_order = count_no_shipped_order;
        }

        public int getCount_shipped_order() {
            return count_shipped_order;
        }

        public void setCount_shipped_order(int count_shipped_order) {
            this.count_shipped_order = count_shipped_order;
        }
    }
}
