package com.wbx.merchant.bean;

import java.util.List;

public class DistrictBean {
    /**
     * msg : 成功
     * state : 1
     * data : [{"code":"030101","name":"和平区"},{"code":"030102","name":"河东区"},{"code":"030103","name":"河西区"},{"code":"030104","name":"南开区"},{"code":"030105","name":"河北区"},{"code":"030106","name":"红桥区"},{"code":"030107","name":"塘沽区"},{"code":"030108","name":"汉沽区"},{"code":"030109","name":"大港区"},{"code":"030110","name":"东丽区"},{"code":"030111","name":"西青区"},{"code":"030112","name":"津南区"},{"code":"030113","name":"北辰区"},{"code":"030114","name":"武清区"},{"code":"030115","name":"宝坻区"},{"code":"030116","name":"宁河县"},{"code":"030117","name":"静海县"},{"code":"030118","name":"蓟县"}]
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
         * code : 030101
         * name : 和平区
         */

        private String code;
        private String name;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
