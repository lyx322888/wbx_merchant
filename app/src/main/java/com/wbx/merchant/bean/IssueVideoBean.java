package com.wbx.merchant.bean;

import java.util.List;

public class IssueVideoBean {

    /**
     * msg : 成功
     * state : 1
     * data : {"video_promotion_id":1,"video_promotion_classify_id":1,"video":"1000","discounts_type":0,"is_talk":0,"shop_set_meal_id":1,"name":"-好吃","shop_set_meal":[{"shop_set_meal_id":1,"set_meal_name":"想跑","original_price":109,"can_hours":"00:00-00:03","business_week_days_str":"周一至周日","photo":"http://imgs.wbx365.com/15888888888weibaixingtc16074175670"}]}
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
         * video_promotion_id : 1
         * video_promotion_classify_id : 1
         * video : 1000
         * discounts_type : 0
         * is_talk : 0
         * shop_set_meal_id : 1
         * name : -好吃
         * shop_set_meal : [{"shop_set_meal_id":1,"set_meal_name":"想跑","original_price":109,"can_hours":"00:00-00:03","business_week_days_str":"周一至周日","photo":"http://imgs.wbx365.com/15888888888weibaixingtc16074175670"}]
         */

        private String video_promotion_id;
        private String video_promotion_classify_id;
        private String video;
        private int discounts_type;
        private int is_talk;
        private String shop_set_meal_id;
        private String name;
        private String pay_money;
        private List<StoreSetMealBean.DataBean> shop_set_meal;

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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<StoreSetMealBean.DataBean> getShop_set_meal() {
            return shop_set_meal;
        }

        public void setShop_set_meal(List<StoreSetMealBean.DataBean> shop_set_meal) {
            this.shop_set_meal = shop_set_meal;
        }

        public String getPay_money() {
            return pay_money;
        }

        public void setPay_money(String pay_money) {
            this.pay_money = pay_money;
        }
    }
}
