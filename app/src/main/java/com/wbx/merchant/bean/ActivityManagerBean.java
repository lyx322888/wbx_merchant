package com.wbx.merchant.bean;

import java.util.List;

public class ActivityManagerBean {
    /**
     * msg : 成功
     * state : 1
     * data : [{"photo":"http://www.wbx365.com/static/default/wap/image/service1.png","type":1,"title":"新人专享礼包","describe":"新人专享礼包","shop_use_num":"55个商家正在使用","has_create":1},{"photo":"http://www.wbx365.com/static/default/wap/image/service1.png","type":2,"title":"商品推荐","describe":"新人专享礼包","has_create":1}]
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
         * photo : http://www.wbx365.com/static/default/wap/image/service1.png
         * type : 1
         * title : 新人专享礼包
         * describe : 新人专享礼包
         * shop_use_num : 55个商家正在使用
         * has_create : 1
         */

        private String photo;
        private int type;
        private String title;
        private String describe;
        private String shop_use_num;
        private int has_create;

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescribe() {
            return describe;
        }

        public void setDescribe(String describe) {
            this.describe = describe;
        }

        public String getShop_use_num() {
            return shop_use_num;
        }

        public void setShop_use_num(String shop_use_num) {
            this.shop_use_num = shop_use_num;
        }

        public int getHas_create() {
            return has_create;
        }

        public void setHas_create(int has_create) {
            this.has_create = has_create;
        }
    }
}
