package com.wbx.merchant.bean;

public class SenDSettingBean {
    /**
     * msg : 成功
     * state : 1
     * data : {"since_money":2000,"logistics":300,"delivery_scope":0,"is_full_minus_shipping_fee":0,"full_minus_shipping_fee":0}
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
         * since_money : 2000
         * logistics : 300
         * delivery_scope : 0
         * is_full_minus_shipping_fee : 0
         * full_minus_shipping_fee : 0
         */

        private float since_money;
        private float logistics;
        private float delivery_scope;
        private int is_full_minus_shipping_fee;
        private float full_minus_shipping_fee;

        public float getSince_money() {
            return since_money;
        }

        public void setSince_money(float since_money) {
            this.since_money = since_money;
        }

        public float getLogistics() {
            return logistics;
        }

        public void setLogistics(float logistics) {
            this.logistics = logistics;
        }

        public float getDelivery_scope() {
            return delivery_scope;
        }

        public void setDelivery_scope(float delivery_scope) {
            this.delivery_scope = delivery_scope;
        }

        public int getIs_full_minus_shipping_fee() {
            return is_full_minus_shipping_fee;
        }

        public void setIs_full_minus_shipping_fee(int is_full_minus_shipping_fee) {
            this.is_full_minus_shipping_fee = is_full_minus_shipping_fee;
        }

        public float getFull_minus_shipping_fee() {
            return full_minus_shipping_fee;
        }

        public void setFull_minus_shipping_fee(float full_minus_shipping_fee) {
            this.full_minus_shipping_fee = full_minus_shipping_fee;
        }
    }
}
