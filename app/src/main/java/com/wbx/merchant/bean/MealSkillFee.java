package com.wbx.merchant.bean;

public class MealSkillFee {
    /**
     * msg : 成功
     * state : 1
     * data : {"set_meal_skill_fee":10}
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
         * set_meal_skill_fee : 10
         */

        private String set_meal_skill_fee;

        public String getSet_meal_skill_fee() {
            return set_meal_skill_fee;
        }

        public void setSet_meal_skill_fee(String set_meal_skill_fee) {
            this.set_meal_skill_fee = set_meal_skill_fee;
        }
    }
}
