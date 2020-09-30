package com.wbx.merchant.bean;

import java.io.Serializable;
import java.util.List;

public class PayBankListBean implements Serializable {

    /**
     * msg : 成功
     * state : 1
     * data : [{"name":"京东","num":"10031414639876930831001","rate":"0.38"},{"name":"微信","num":"10031414639876930831004","rate":"0.38"},{"name":"支付宝","num":"10031414639876930831005","rate":"0.38"},{"name":"微信小程序","num":"10031414639876930831011","rate":"0.38"},{"name":"银联","num":"10031414639876930831013","rate":"0.38"}]
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

    public static class DataBean implements Serializable{
        /**
         * name : 京东
         * num : 10031414639876930831001
         * rate : 0.38
         */

        private String name;
        private String num;
        private String rate;
        private boolean isChoice = true ;

        public boolean isChoice() {
            return isChoice;
        }

        public void setChoice(boolean choice) {
            isChoice = choice;
        }


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public String getRate() {
            return rate;
        }

        public void setRate(String rate) {
            this.rate = rate;
        }
    }
}
