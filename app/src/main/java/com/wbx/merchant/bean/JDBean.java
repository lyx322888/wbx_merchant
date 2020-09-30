package com.wbx.merchant.bean;

import java.util.List;

public class JDBean {
    /**
     * msg : 成功
     * state : 1
     * data : {"cates":[{"cate_id":166,"cate_name":" 自助餐"},{"cate_id":167,"cate_name":"烧烤"},{"cate_id":168,"cate_name":"小吃快餐"},{"cate_id":169,"cate_name":"西餐"},{"cate_id":170,"cate_name":"甜点饮品"},{"cate_id":171,"cate_name":"火锅"},{"cate_id":172,"cate_name":"川湘菜"},{"cate_id":173,"cate_name":"海鲜"}],"shop_grade":{"grade_id":20,"grade_name":"美食街","money":"1"}}
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

    public static class DataBean{
        public List<JDDetailBean> getOrderList() {
            return orderList;
        }

        public void setOrderList(List<JDDetailBean> orderList) {
            this.orderList = orderList;
        }

        public List<JDDetailBean> orderList;
    }
}
