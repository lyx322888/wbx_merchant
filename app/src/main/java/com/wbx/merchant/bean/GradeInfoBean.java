package com.wbx.merchant.bean;

import java.util.List;

public class GradeInfoBean {

    /**
     * msg : 成功
     * state : 1
     * data : [{"grade_id":19,"grade_name":"实体店","money":1},{"grade_id":15,"grade_name":"菜市场","money":1},{"grade_id":20,"grade_name":"美食街","money":1},{"grade_id":21,"grade_name":"水果生鲜","money":500},{"grade_id":22,"grade_name":"特产零食","money":500},{"grade_id":23,"grade_name":"果汁饮品","money":500}]
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
         * grade_id : 19
         * grade_name : 实体店
         * money : 1
         */

        private int grade_id;
        private String grade_name;
        private int money;

        public int getGrade_id() {
            return grade_id;
        }

        public void setGrade_id(int grade_id) {
            this.grade_id = grade_id;
        }

        public String getGrade_name() {
            return grade_name;
        }

        public void setGrade_name(String grade_name) {
            this.grade_name = grade_name;
        }

        public int getMoney() {
            return money;
        }

        public void setMoney(int money) {
            this.money = money;
        }
    }
}
