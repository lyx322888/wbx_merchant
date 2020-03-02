package com.wbx.merchant.bean;

import java.util.List;

/**
 * Created by wushenghui on 2017/6/23.
 * 店铺版本
 */

public class ShopVersionBean {

    /**
     * msg : 成功
     * state : 1
     * data : [{"title":"开店宝【标准版】","original_price":998,"special_price":680,"img":"http://www.wbx365.com/static/default/wap/image/service1.png","prompt":"原价998元, 限时特价680元","shop_grade":6},{"title":"开店宝【套餐一】","original_price":1980,"special_price":1080,"img":"http://www.wbx365.com/static/default/wap/image/service2.png","prompt":"原价1980元, 限时特价1080元 (含自动接单器）","shop_grade":8}]
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
         * title : 开店宝【标准版】
         * original_price : 998
         * special_price : 680
         * img : http://www.wbx365.com/static/default/wap/image/service1.png
         * prompt : 原价998元, 限时特价680元
         * shop_grade : 6
         */

        private String title;
        private int original_price;
        private int special_price;
        private String img;
        private String prompt;

        public boolean isSelect() {
            return isSelect;
        }

        public void setSelect(boolean select) {
            isSelect = select;
        }

        private boolean isSelect;
        private int shop_grade;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getOriginal_price() {
            return original_price;
        }

        public void setOriginal_price(int original_price) {
            this.original_price = original_price;
        }

        public int getSpecial_price() {
            return special_price;
        }

        public void setSpecial_price(int special_price) {
            this.special_price = special_price;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getPrompt() {
            return prompt;
        }

        public void setPrompt(String prompt) {
            this.prompt = prompt;
        }

        public int getShop_grade() {
            return shop_grade;
        }

        public void setShop_grade(int shop_grade) {
            this.shop_grade = shop_grade;
        }
    }
}
