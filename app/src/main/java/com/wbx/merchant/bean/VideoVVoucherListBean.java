package com.wbx.merchant.bean;

import java.util.List;

public class VideoVVoucherListBean {
    /**
     * msg : 成功
     * state : 1
     * data : [{"video_promotion_id":13,"video_promotion_classify_id":3,"video":"http://imgs.wbx365.com/FmuJgbAsTNP3oFQToClpxw7h70h1","discounts_type":2,"is_talk":0,"shop_set_meal_id":1,"pay_money":1,"shop_id":1799,"is_paid":0,"pay_time":0,"create_time":0,"is_delete":0,"audit_status":0,"audit_message":"","shop_set_meal":{"shop_set_meal_id":1,"set_meal_name":"想跑","original_price":109,"one_price":500}},{"video_promotion_id":12,"video_promotion_classify_id":3,"video":"http://imgs.wbx365.com/FmuJgbAsTNP3oFQToClpxw7h70h1","discounts_type":2,"is_talk":1,"shop_set_meal_id":1,"pay_money":1,"shop_id":1799,"is_paid":0,"pay_time":0,"create_time":0,"is_delete":0,"audit_status":0,"audit_message":"","shop_set_meal":{"shop_set_meal_id":1,"set_meal_name":"想跑","original_price":109,"one_price":500}},{"video_promotion_id":11,"video_promotion_classify_id":3,"video":"http://imgs.wbx365.com/FmuJgbAsTNP3oFQToClpxw7h70h1","discounts_type":2,"is_talk":1,"shop_set_meal_id":1,"pay_money":1,"shop_id":1799,"is_paid":0,"pay_time":0,"create_time":0,"is_delete":0,"audit_status":0,"audit_message":"","shop_set_meal":{"shop_set_meal_id":1,"set_meal_name":"想跑","original_price":109,"one_price":500}},{"video_promotion_id":6,"video_promotion_classify_id":1,"video":"1000","discounts_type":2,"is_talk":1,"shop_set_meal_id":1,"pay_money":1,"shop_id":1799,"is_paid":0,"pay_time":0,"create_time":0,"is_delete":0,"audit_status":0,"audit_message":"","shop_set_meal":{"shop_set_meal_id":1,"set_meal_name":"想跑","original_price":109,"one_price":500}},{"video_promotion_id":5,"video_promotion_classify_id":1,"video":"1000","discounts_type":2,"is_talk":1,"shop_set_meal_id":1,"pay_money":1,"shop_id":1799,"is_paid":0,"pay_time":0,"create_time":0,"is_delete":0,"audit_status":0,"audit_message":"","shop_set_meal":{"shop_set_meal_id":1,"set_meal_name":"想跑","original_price":109,"one_price":500}},{"video_promotion_id":4,"video_promotion_classify_id":1,"video":"1000","discounts_type":2,"is_talk":0,"shop_set_meal_id":1,"pay_money":1,"shop_id":1799,"is_paid":0,"pay_time":0,"create_time":0,"is_delete":0,"audit_status":0,"audit_message":"","shop_set_meal":{"shop_set_meal_id":1,"set_meal_name":"想跑","original_price":109,"one_price":500}}]
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
         * video_promotion_id : 13
         * video_promotion_classify_id : 3
         * video : http://imgs.wbx365.com/FmuJgbAsTNP3oFQToClpxw7h70h1
         * discounts_type : 2
         * is_talk : 0
         * shop_set_meal_id : 1
         * pay_money : 1
         * shop_id : 1799
         * is_paid : 0
         * pay_time : 0
         * create_time : 0
         * is_delete : 0
         * audit_status : 0
         * audit_message :
         * shop_set_meal : {"shop_set_meal_id":1,"set_meal_name":"想跑","original_price":109,"one_price":500}
         */

        private String video_promotion_id;
        private String video_promotion_classify_id;
        private String video;
        private int discounts_type;
        private int is_talk;
        private String shop_set_meal_id;
        private int pay_money;
        private int shop_id;
        private int is_paid;
        private int pay_time;
        private int create_time;
        private int is_delete;
        private int audit_status;
        private String audit_message;
        private ShopSetMealBean shop_set_meal;

        public String getVideo_promotion_id() {
            return video_promotion_id;
        }

        public void setVideo_promotion_id(String video_promotion_id) {
            this.video_promotion_id = video_promotion_id;
        }

        public String getVideo_promotion_classify_id() {
            return video_promotion_classify_id;
        }

        public void setVideo_promotion_classify_id(String video_promotion_classify_id) {
            this.video_promotion_classify_id = video_promotion_classify_id;
        }

        public String getVideo() {
            return video;
        }

        public void setVideo(String video) {
            this.video = video;
        }

        public int getDiscounts_type() {
            return discounts_type;
        }

        public void setDiscounts_type(int discounts_type) {
            this.discounts_type = discounts_type;
        }

        public int getIs_talk() {
            return is_talk;
        }

        public void setIs_talk(int is_talk) {
            this.is_talk = is_talk;
        }

        public String getShop_set_meal_id() {
            return shop_set_meal_id;
        }

        public void setShop_set_meal_id(String shop_set_meal_id) {
            this.shop_set_meal_id = shop_set_meal_id;
        }

        public int getPay_money() {
            return pay_money;
        }

        public void setPay_money(int pay_money) {
            this.pay_money = pay_money;
        }

        public int getShop_id() {
            return shop_id;
        }

        public void setShop_id(int shop_id) {
            this.shop_id = shop_id;
        }

        public int getIs_paid() {
            return is_paid;
        }

        public void setIs_paid(int is_paid) {
            this.is_paid = is_paid;
        }

        public int getPay_time() {
            return pay_time;
        }

        public void setPay_time(int pay_time) {
            this.pay_time = pay_time;
        }

        public int getCreate_time() {
            return create_time;
        }

        public void setCreate_time(int create_time) {
            this.create_time = create_time;
        }

        public int getIs_delete() {
            return is_delete;
        }

        public void setIs_delete(int is_delete) {
            this.is_delete = is_delete;
        }

        public int getAudit_status() {
            return audit_status;
        }

        public void setAudit_status(int audit_status) {
            this.audit_status = audit_status;
        }

        public String getAudit_message() {
            return audit_message;
        }

        public void setAudit_message(String audit_message) {
            this.audit_message = audit_message;
        }

        public ShopSetMealBean getShop_set_meal() {
            return shop_set_meal;
        }

        public void setShop_set_meal(ShopSetMealBean shop_set_meal) {
            this.shop_set_meal = shop_set_meal;
        }

        public static class ShopSetMealBean {
            /**
             * shop_set_meal_id : 1
             * set_meal_name : 想跑
             * original_price : 109
             * one_price : 500
             */

            private int shop_set_meal_id;
            private String set_meal_name;
            private String original_price;
            private String one_price;

            public String getPhoto() {
                return photo;
            }

            public void setPhoto(String photo) {
                this.photo = photo;
            }

            private String photo;

            public String getAlready_sell_num() {
                return already_sell_num;
            }

            public void setAlready_sell_num(String already_sell_num) {
                this.already_sell_num = already_sell_num;
            }

            public String getAlready_use_num() {
                return already_use_num;
            }

            public void setAlready_use_num(String already_use_num) {
                this.already_use_num = already_use_num;
            }

            private String already_sell_num;
            private String already_use_num;


            public int getShop_set_meal_id() {
                return shop_set_meal_id;
            }

            public void setShop_set_meal_id(int shop_set_meal_id) {
                this.shop_set_meal_id = shop_set_meal_id;
            }

            public String getSet_meal_name() {
                return set_meal_name;
            }

            public void setSet_meal_name(String set_meal_name) {
                this.set_meal_name = set_meal_name;
            }

            public String getOriginal_price() {
                return original_price;
            }

            public void setOriginal_price(String original_price) {
                this.original_price = original_price;
            }

            public String getOne_price() {
                return one_price;
            }

            public void setOne_price(String one_price) {
                this.one_price = one_price;
            }
        }
    }
}
