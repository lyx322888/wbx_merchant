package com.wbx.merchant.bean;

public class ShopIdentityBean {
    /**
     * msg : 成功
     * state : 1
     * data : {"identity_id":8,"shop_id":2620,"identity_card_front":"https://img3.doubanio.com/view/photo/l/public/p611002391.jpg","identity_card_contrary":"https://img9.doubanio.com/view/photo/l/public/p1422071845.jpg","identity_card_in_hand":"https://img3.doubanio.com/view/photo/l/public/p1422071901.jpg","business_license":"","audit":0,"return_reasons":""}
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

    public static class DataBean {
        /**
         * identity_id : 8
         * shop_id : 2620
         * identity_card_front : https://img3.doubanio.com/view/photo/l/public/p611002391.jpg
         * identity_card_contrary : https://img9.doubanio.com/view/photo/l/public/p1422071845.jpg
         * identity_card_in_hand : https://img3.doubanio.com/view/photo/l/public/p1422071901.jpg
         * business_license :
         * audit : 0
         * return_reasons :
         */

        private int identity_id;
        private int shop_id;
        private String identity_card_front;
        private String identity_card_contrary;
        private String identity_card_in_hand;

        public String getFood_business_license() {
            return food_business_license;
        }

        public void setFood_business_license(String food_business_license) {
            this.food_business_license = food_business_license;
        }

        private String food_business_license;
        private String business_license;
        private int audit;
        private String return_reasons;

        public int getIdentity_id() {
            return identity_id;
        }

        public void setIdentity_id(int identity_id) {
            this.identity_id = identity_id;
        }

        public int getShop_id() {
            return shop_id;
        }

        public void setShop_id(int shop_id) {
            this.shop_id = shop_id;
        }

        public String getIdentity_card_front() {
            return identity_card_front;
        }

        public void setIdentity_card_front(String identity_card_front) {
            this.identity_card_front = identity_card_front;
        }

        public String getIdentity_card_contrary() {
            return identity_card_contrary;
        }

        public void setIdentity_card_contrary(String identity_card_contrary) {
            this.identity_card_contrary = identity_card_contrary;
        }

        public String getIdentity_card_in_hand() {
            return identity_card_in_hand;
        }

        public void setIdentity_card_in_hand(String identity_card_in_hand) {
            this.identity_card_in_hand = identity_card_in_hand;
        }

        public String getBusiness_license() {
            return business_license;
        }

        public void setBusiness_license(String business_license) {
            this.business_license = business_license;
        }

        public int getAudit() {
            return audit;
        }

        public void setAudit(int audit) {
            this.audit = audit;
        }

        public String getReturn_reasons() {
            return return_reasons;
        }

        public void setReturn_reasons(String return_reasons) {
            this.return_reasons = return_reasons;
        }
    }
}
