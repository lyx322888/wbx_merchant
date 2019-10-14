package com.wbx.merchant.bean;

public class DeliveryFeeInfo {
    /**
     * msg : 成功
     * state : 1
     * data : {"is_full_minus_shipping_fee":0,"full_minus_shipping_fee":0}
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
         * is_full_minus_shipping_fee : 0
         * full_minus_shipping_fee : 0
         */

        private int is_full_minus_shipping_fee;
        private int full_minus_shipping_fee;

        public int getIs_full_minus_shipping_fee() {
            return is_full_minus_shipping_fee;
        }

        public void setIs_full_minus_shipping_fee(int is_full_minus_shipping_fee) {
            this.is_full_minus_shipping_fee = is_full_minus_shipping_fee;
        }

        public int getFull_minus_shipping_fee() {
            return full_minus_shipping_fee;
        }

        public void setFull_minus_shipping_fee(int full_minus_shipping_fee) {
            this.full_minus_shipping_fee = full_minus_shipping_fee;
        }
    }
}
