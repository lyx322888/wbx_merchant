package com.wbx.merchant.bean;

import java.util.List;

public class ListIndustrySecondBean {
    /**
     * msg : 成功
     * state : 1
     * data : [{"industryName":"健身中心","industryNum":"10081614701426099620077"},{"industryName":"瑜伽","industryNum":"10081614701426099630078"},{"industryName":"舞蹈","industryNum":"10081614701426099630079"},{"industryName":"游泳馆","industryNum":"10081614701426099640080"},{"industryName":"羽毛球馆","industryNum":"10081614701426099640081"},{"industryName":"乒乓球馆","industryNum":"10081614701426099650082"},{"industryName":"网球场","industryNum":"10081614701426099650083"},{"industryName":"高尔夫场","industryNum":"10081614701426099660084"},{"industryName":"保龄球馆","industryNum":"10081614701426099660085"},{"industryName":"台球馆","industryNum":"10081614701426099670086"},{"industryName":"体育馆","industryNum":"10081614701426099680087"},{"industryName":"滑雪场","industryNum":"10081614701426099680088"},{"industryName":"其它运动","industryNum":"10081614701426099690089"}]
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
         * industryName : 健身中心
         * industryNum : 10081614701426099620077
         */

        private String industryName;
        private String industryNum;

        public String getIndustryName() {
            return industryName;
        }

        public void setIndustryName(String industryName) {
            this.industryName = industryName;
        }

        public String getIndustryNum() {
            return industryNum;
        }

        public void setIndustryNum(String industryNum) {
            this.industryNum = industryNum;
        }
    }
}
