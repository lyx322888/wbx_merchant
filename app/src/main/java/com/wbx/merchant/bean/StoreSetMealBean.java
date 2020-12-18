package com.wbx.merchant.bean;

import java.util.List;

public class StoreSetMealBean {

    /**
     * msg : 成功
     * state : 1
     * data : [{"shop_set_meal_id":4,"set_meal_name":"图来看看","elect_kind":"[{\"goods_id\":\"107466\",\"goods_num\":1,\"price\":\"10\",\"title\":\"疯狂的蚕豆220克\"}]","need_kind":"[{\"goods_id\":\"107467\",\"goods_num\":1,\"price\":\"100\",\"title\":\"甘源 蟹黄味瓜子仁 75g\"}]","business_week_days":["1","1","1","1","1","1","1"],"original_price":13000,"one_price":3000,"two_price":2000,"vip_price":1000,"place_commission_pct":"","set_meal_skill_fee":1000,"can_hours":"15:35-15:44","use_end_time":2020,"sell_end_time":2020,"use_rule_info":"我自己","create_time":0,"photos":"http://imgs.wbx365.com/FtClIrH1Qx5oT7NXfLVPyU2yfeZQ,http://imgs.wbx365.com/Fh7pKncAGMK6CMK-PEprhqTcZrfK","has_rice":1,"rice_price":"2000.00","rice_num":1,"has_table":0,"table_price":"0.00","table_num":"","has_napkin":0,"napkin_price":"0.00","napkin_num":"","has_self_fruits":0,"self_fruits_price":"0.00","self_fruits_num":"","is_pause":0,"is_delete":0,"audit_status":0,"audit_message":"","shop_id":3155,"shop_type":2,"business_week_days_str":"周一至周日","photo":"http://imgs.wbx365.com/FtClIrH1Qx5oT7NXfLVPyU2yfeZQ","use_count":0,"buy_count":0,"is_end":0}]
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
         * shop_set_meal_id : 4
         * set_meal_name : 图来看看
         * elect_kind : [{"goods_id":"107466","goods_num":1,"price":"10","title":"疯狂的蚕豆220克"}]
         * need_kind : [{"goods_id":"107467","goods_num":1,"price":"100","title":"甘源 蟹黄味瓜子仁 75g"}]
         * business_week_days : ["1","1","1","1","1","1","1"]
         * original_price : 13000
         * one_price : 3000
         * two_price : 2000
         * vip_price : 1000
         * place_commission_pct :
         * set_meal_skill_fee : 1000
         * can_hours : 15:35-15:44
         * use_end_time : 2020
         * sell_end_time : 2020
         * use_rule_info : 我自己
         * create_time : 0
         * photos : http://imgs.wbx365.com/FtClIrH1Qx5oT7NXfLVPyU2yfeZQ,http://imgs.wbx365.com/Fh7pKncAGMK6CMK-PEprhqTcZrfK
         * has_rice : 1
         * rice_price : 2000.00
         * rice_num : 1
         * has_table : 0
         * table_price : 0.00
         * table_num :
         * has_napkin : 0
         * napkin_price : 0.00
         * napkin_num :
         * has_self_fruits : 0
         * self_fruits_price : 0.00
         * self_fruits_num :
         * is_pause : 0
         * is_delete : 0
         * audit_status : 0
         * audit_message :
         * shop_id : 3155
         * shop_type : 2
         * business_week_days_str : 周一至周日
         * photo : http://imgs.wbx365.com/FtClIrH1Qx5oT7NXfLVPyU2yfeZQ
         * use_count : 0
         * buy_count : 0
         * is_end : 0
         */

        private String shop_set_meal_id;
        private String set_meal_name;
        private String elect_kind;
        private String need_kind;
        private int original_price;
        private int one_price;
        private int two_price;
        private int vip_price;
        private String place_commission_pct;
        private String set_meal_skill_fee;
        private String can_hours;
        private String use_end_time;
        private String sell_end_time;
        private String use_rule_info;
        private String create_time;
        private String photos;
        private String has_rice;
        private String rice_price;
        private String rice_num;
        private String has_table;
        private String table_price;
        private String table_num;
        private String has_napkin;
        private String napkin_price;
        private String napkin_num;
        private String has_self_fruits;
        private String self_fruits_price;
        private String self_fruits_num;
        private String is_pause;
        private int is_delete;
        private int audit_status;
        private String audit_message;
        private String shop_id;
        private String shop_type;
        private String business_week_days_str;
        private String photo;
        private int use_count;
        private int buy_count;
        private int is_end;
        private List<String> business_week_days;

        public String getShop_set_meal_id() {
            return shop_set_meal_id;
        }

        public void setShop_set_meal_id(String shop_set_meal_id) {
            this.shop_set_meal_id = shop_set_meal_id;
        }

        public String getSet_meal_name() {
            return set_meal_name;
        }

        public void setSet_meal_name(String set_meal_name) {
            this.set_meal_name = set_meal_name;
        }

        public String getElect_kind() {
            return elect_kind;
        }

        public void setElect_kind(String elect_kind) {
            this.elect_kind = elect_kind;
        }

        public String getNeed_kind() {
            return need_kind;
        }

        public void setNeed_kind(String need_kind) {
            this.need_kind = need_kind;
        }

        public int getOriginal_price() {
            return original_price;
        }

        public void setOriginal_price(int original_price) {
            this.original_price = original_price;
        }

        public int getOne_price() {
            return one_price;
        }

        public void setOne_price(int one_price) {
            this.one_price = one_price;
        }

        public int getTwo_price() {
            return two_price;
        }

        public void setTwo_price(int two_price) {
            this.two_price = two_price;
        }

        public int getVip_price() {
            return vip_price;
        }

        public void setVip_price(int vip_price) {
            this.vip_price = vip_price;
        }

        public String getPlace_commission_pct() {
            return place_commission_pct;
        }

        public void setPlace_commission_pct(String place_commission_pct) {
            this.place_commission_pct = place_commission_pct;
        }

        public String getSet_meal_skill_fee() {
            return set_meal_skill_fee;
        }

        public void setSet_meal_skill_fee(String set_meal_skill_fee) {
            this.set_meal_skill_fee = set_meal_skill_fee;
        }

        public String getCan_hours() {
            return can_hours;
        }

        public void setCan_hours(String can_hours) {
            this.can_hours = can_hours;
        }

        public String getUse_end_time() {
            return use_end_time;
        }

        public void setUse_end_time(String use_end_time) {
            this.use_end_time = use_end_time;
        }

        public String getSell_end_time() {
            return sell_end_time;
        }

        public void setSell_end_time(String sell_end_time) {
            this.sell_end_time = sell_end_time;
        }

        public String getUse_rule_info() {
            return use_rule_info;
        }

        public void setUse_rule_info(String use_rule_info) {
            this.use_rule_info = use_rule_info;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getPhotos() {
            return photos;
        }

        public void setPhotos(String photos) {
            this.photos = photos;
        }

        public String getHas_rice() {
            return has_rice;
        }

        public void setHas_rice(String has_rice) {
            this.has_rice = has_rice;
        }

        public String getRice_price() {
            return rice_price;
        }

        public void setRice_price(String rice_price) {
            this.rice_price = rice_price;
        }

        public String getRice_num() {
            return rice_num;
        }

        public void setRice_num(String rice_num) {
            this.rice_num = rice_num;
        }

        public String getHas_table() {
            return has_table;
        }

        public void setHas_table(String has_table) {
            this.has_table = has_table;
        }

        public String getTable_price() {
            return table_price;
        }

        public void setTable_price(String table_price) {
            this.table_price = table_price;
        }

        public String getTable_num() {
            return table_num;
        }

        public void setTable_num(String table_num) {
            this.table_num = table_num;
        }

        public String getHas_napkin() {
            return has_napkin;
        }

        public void setHas_napkin(String has_napkin) {
            this.has_napkin = has_napkin;
        }

        public String getNapkin_price() {
            return napkin_price;
        }

        public void setNapkin_price(String napkin_price) {
            this.napkin_price = napkin_price;
        }

        public String getNapkin_num() {
            return napkin_num;
        }

        public void setNapkin_num(String napkin_num) {
            this.napkin_num = napkin_num;
        }

        public String getHas_self_fruits() {
            return has_self_fruits;
        }

        public void setHas_self_fruits(String has_self_fruits) {
            this.has_self_fruits = has_self_fruits;
        }

        public String getSelf_fruits_price() {
            return self_fruits_price;
        }

        public void setSelf_fruits_price(String self_fruits_price) {
            this.self_fruits_price = self_fruits_price;
        }

        public String getSelf_fruits_num() {
            return self_fruits_num;
        }

        public void setSelf_fruits_num(String self_fruits_num) {
            this.self_fruits_num = self_fruits_num;
        }

        public String getIs_pause() {
            return is_pause;
        }

        public void setIs_pause(String is_pause) {
            this.is_pause = is_pause;
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

        public String getShop_id() {
            return shop_id;
        }

        public void setShop_id(String shop_id) {
            this.shop_id = shop_id;
        }

        public String getShop_type() {
            return shop_type;
        }

        public void setShop_type(String shop_type) {
            this.shop_type = shop_type;
        }

        public String getBusiness_week_days_str() {
            return business_week_days_str;
        }

        public void setBusiness_week_days_str(String business_week_days_str) {
            this.business_week_days_str = business_week_days_str;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public int getUse_count() {
            return use_count;
        }

        public void setUse_count(int use_count) {
            this.use_count = use_count;
        }

        public int getBuy_count() {
            return buy_count;
        }

        public void setBuy_count(int buy_count) {
            this.buy_count = buy_count;
        }

        public int getIs_end() {
            return is_end;
        }

        public void setIs_end(int is_end) {
            this.is_end = is_end;
        }

        public List<String> getBusiness_week_days() {
            return business_week_days;
        }

        public void setBusiness_week_days(List<String> business_week_days) {
            this.business_week_days = business_week_days;
        }
    }
}
