package com.wbx.merchant.bean;

import java.util.List;

public class ProvinceBean {
    /**
     * msg : 成功
     * state : 1
     * data : [{"code":"01","name":"北京"},{"code":"02","name":"上海"},{"code":"03","name":"天津"},{"code":"04","name":"重庆"},{"code":"05","name":"河北"},{"code":"06","name":"山西"},{"code":"07","name":"内蒙古"},{"code":"08","name":"辽宁"},{"code":"09","name":"吉林"},{"code":"10","name":"黑龙江"},{"code":"11","name":"江苏"},{"code":"12","name":"浙江"},{"code":"13","name":"安徽"},{"code":"14","name":"福建"},{"code":"15","name":"江西"},{"code":"16","name":"山东"},{"code":"17","name":"河南"},{"code":"18","name":"湖北"},{"code":"19","name":"湖南"},{"code":"20","name":"广东"},{"code":"21","name":"广西"},{"code":"22","name":"海南"},{"code":"23","name":"四川"},{"code":"24","name":"贵州"},{"code":"25","name":"云南"},{"code":"26","name":"西藏"},{"code":"27","name":"陕西"},{"code":"28","name":"甘肃"},{"code":"29","name":"宁夏"},{"code":"30","name":"青海"},{"code":"31","name":"新疆"},{"code":"32","name":"香港"},{"code":"33","name":"澳门"},{"code":"34","name":"台湾"}]
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
         * code : 01
         * name : 北京
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
