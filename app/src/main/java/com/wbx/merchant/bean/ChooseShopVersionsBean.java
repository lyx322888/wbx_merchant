package com.wbx.merchant.bean;

import java.util.List;

public class ChooseShopVersionsBean {
    /**
     * msg : 成功
     * state : 1
     * data : [{"shop_grade":6,"title":"开店宝（试用版）","grade_type":1,"subhead":"","money":0,"time":"","original_price":"","present":"快速体验智能店铺","is_recommend ":0,"order_img":"http://www.wbx365.com/static/default/wap/image/service1.png"},{"shop_grade":6,"title":"开店宝（旗舰版）","grade_type":2,"subhead":"","money":68000,"time":"/12个月","original_price":"原价998","present":"\u203b领199元开店大礼包，赠一级分销功能（店长）","is_recommend ":1,"order_img":"http://www.wbx365.com/static/default/wap/image/service1.png"},{"shop_grade":6,"title":"开店宝（旗舰版）","grade_type":3,"subhead":"（含自动接单器）","money":68000,"time":"/12个月","original_price":"原价998","present":"\u203b领199元开店大礼包，赠一级分销功能（店长）","is_recommend ":0,"order_img":"http://www.wbx365.com/static/default/wap/image/service1.png"}]
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
         * shop_grade : 6
         * title : 开店宝（试用版）
         * grade_type : 1
         * subhead :
         * money : 0
         * time :
         * original_price :
         * present : 快速体验智能店铺
         * is_recommend  : 0
         * order_img : http://www.wbx365.com/static/default/wap/image/service1.png
         */

        private String shop_grade;
        private String title;
        private int grade_type;
        private String subhead;
        private int money;
        private String time;
        private String original_price;
        private String present;
        private int is_recommend;
        private String order_img;
        private String payment_code;
        public boolean is_choose = false;

        public String getShop_grade() {
            return shop_grade;
        }

        public void setShop_grade(String shop_grade) {
            this.shop_grade = shop_grade;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
        public String getPayment_code() {
            return payment_code;
        }

        public void setPayment_code(String payment_code) {
            this.payment_code = payment_code;
        }
        public int getGrade_type() {
            return grade_type;
        }

        public void setGrade_type(int grade_type) {
            this.grade_type = grade_type;
        }

        public String getSubhead() {
            return subhead;
        }

        public void setSubhead(String subhead) {
            this.subhead = subhead;
        }

        public int getMoney() {
            return money;
        }

        public void setMoney(int money) {
            this.money = money;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getOriginal_price() {
            return original_price;
        }

        public void setOriginal_price(String original_price) {
            this.original_price = original_price;
        }

        public String getPresent() {
            return present;
        }

        public void setPresent(String present) {
            this.present = present;
        }

        public int getIs_recommend() {
            return is_recommend;
        }

        public void setIs_recommend(int is_recommend) {
            this.is_recommend = is_recommend;
        }

        public String getOrder_img() {
            return order_img;
        }

        public void setOrder_img(String order_img) {
            this.order_img = order_img;
        }
    }
}
