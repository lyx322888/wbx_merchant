package com.wbx.merchant.bean;

import java.util.List;

public class ListIndustryBean {

    /**
     * msg : 成功
     * state : 1
     * data : [{"industryName":"美食","industryNum":"10081614701426099160002"},{"industryName":"生活方式","industryNum":"10081614701426099190004"},{"industryName":"生活服务","industryNum":"10081614701426099190005"},{"industryName":"购物","industryNum":"10081614701426099450047"},{"industryName":"丽人","industryNum":"10081614701426099560066"},{"industryName":"健身","industryNum":"10081614701426099620076"},{"industryName":"酒店","industryNum":"10081614701426099700090"},{"industryName":"教育","industryNum":"10081614701426099190006"},{"industryName":"私立院校","industryNum":"10081614701426099190010"},{"industryName":"公立院校","industryNum":"10081614701426099190011"},{"industryName":"数字娱乐","industryNum":"10081614701426099260016"},{"industryName":"母婴/玩具","industryNum":"10081614701426099260018"},{"industryName":"其它","industryNum":"10081614701426099260019"},{"industryName":"生活缴费","industryNum":"10081614701426099260032"}]
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
         * industryName : 美食
         * industryNum : 10081614701426099160002
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
