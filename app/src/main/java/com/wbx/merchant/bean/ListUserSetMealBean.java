package com.wbx.merchant.bean;

import java.util.List;

public class ListUserSetMealBean {
    /**
     * msg : 成功
     * state : 1
     * data : [{"use_time":1607569503,"pay_time":1607569503,"pay_num":1,"use_num":1,"pay_money":0,"set_meal_skill_fee":0,"account":"158*****8888","confirm_time":1607569503,"confirm_num":1}]
     */

    private String msg;
    private int state;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * use_time : 1607569503
         * pay_time : 1607569503
         * pay_num : 1
         * use_num : 1
         * pay_money : 0
         * set_meal_skill_fee : 0
         * account : 158*****8888
         * confirm_time : 1607569503
         * confirm_num : 1
         */

        private int use_time;
        private int pay_time;
        private int pay_num;
        private int use_num;
        private float pay_money;
        private float set_meal_skill_fee;
        private String account;
        private String confirm_time;
        private String confirm_num;
        private int is_refund;

        public int getIs_refund() {
            return is_refund;
        }

        public void setIs_refund(int is_refund) {
            this.is_refund = is_refund;
        }


        public int getUse_time() {
            return use_time;
        }

        public void setUse_time(int use_time) {
            this.use_time = use_time;
        }

        public int getPay_time() {
            return pay_time;
        }

        public void setPay_time(int pay_time) {
            this.pay_time = pay_time;
        }

        public int getPay_num() {
            return pay_num;
        }

        public void setPay_num(int pay_num) {
            this.pay_num = pay_num;
        }

        public int getUse_num() {
            return use_num;
        }

        public void setUse_num(int use_num) {
            this.use_num = use_num;
        }

        public float getPay_money() {
            return pay_money;
        }

        public void setPay_money(int pay_money) {
            this.pay_money = pay_money;
        }

        public float getSet_meal_skill_fee() {
            return set_meal_skill_fee;
        }

        public void setSet_meal_skill_fee(float set_meal_skill_fee) {
            this.set_meal_skill_fee = set_meal_skill_fee;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getConfirm_time() {
            return confirm_time;
        }

        public void setConfirm_time(String confirm_time) {
            this.confirm_time = confirm_time;
        }

        public String getConfirm_num() {
            return confirm_num;
        }

        public void setConfirm_num(String confirm_num) {
            this.confirm_num = confirm_num;
        }
    }
}
