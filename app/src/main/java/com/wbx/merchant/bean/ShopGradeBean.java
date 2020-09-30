package com.wbx.merchant.bean;

import java.util.List;

public class ShopGradeBean {

    /**
     * msg : 成功
     * state : 1
     * data : [{"grade_id":15,"grade_name":"生鲜蔬菜"},{"grade_id":19,"grade_name":"超市便利"},{"grade_id":20,"grade_name":"餐饮美食"},{"grade_id":23,"grade_name":"甜点饮品"},{"grade_id":22,"grade_name":"特产零食"},{"grade_id":21,"grade_name":"时令水果"}]
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
         * grade_id : 15
         * grade_name : 生鲜蔬菜
         */

        private String grade_id;
        private String grade_name;

        public String getGrade_id() {
            return grade_id;
        }

        public void setGrade_id(String grade_id) {
            this.grade_id = grade_id;
        }

        public String getGrade_name() {
            return grade_name;
        }

        public void setGrade_name(String grade_name) {
            this.grade_name = grade_name;
        }
    }
}
