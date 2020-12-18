package com.wbx.merchant.bean;

public class CountUserSetMealBean {


    /**
     * msg : 成功
     * state : 1
     * data : {"use_all_num":1,"buy_all_num":1,"all_pay_money":0,"all_refund_money":0,"all_income_money":0}
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
         * use_all_num : 1
         * buy_all_num : 1
         * all_pay_money : 0
         * all_refund_money : 0
         * all_income_money : 0
         */

        private String use_all_num;
        private String buy_all_num;
        private int all_pay_money;
        private int all_refund_money;
        private int all_income_money;

        public String getUse_all_num() {
            return use_all_num;
        }

        public void setUse_all_num(String use_all_num) {
            this.use_all_num = use_all_num;
        }

        public String getBuy_all_num() {
            return buy_all_num;
        }

        public void setBuy_all_num(String buy_all_num) {
            this.buy_all_num = buy_all_num;
        }

        public int getAll_pay_money() {
            return all_pay_money;
        }

        public void setAll_pay_money(int all_pay_money) {
            this.all_pay_money = all_pay_money;
        }

        public int getAll_refund_money() {
            return all_refund_money;
        }

        public void setAll_refund_money(int all_refund_money) {
            this.all_refund_money = all_refund_money;
        }

        public int getAll_income_money() {
            return all_income_money;
        }

        public void setAll_income_money(int all_income_money) {
            this.all_income_money = all_income_money;
        }
    }
}
