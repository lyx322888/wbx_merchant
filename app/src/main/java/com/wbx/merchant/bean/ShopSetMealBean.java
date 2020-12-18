package com.wbx.merchant.bean;

import java.util.List;

public class ShopSetMealBean {


    /**
     * msg : 成功
     * state : 1
     * data : {"shop_set_meal_id":1,"set_meal_name":"想跑","elect_kind":[{"goods_id":"97792","title":"草莓","goods_num":"1","price":8}],"need_kind":[{"goods_id":"108854","title":"鸡翅根","goods_num":"1","price":1}],"business_week_days":["1","1","1","1","1","1","1"],"original_price":0.1,"one_price":5,"two_price":5,"vip_price":0,"place_commission_pct":25,"set_meal_skill_fee":0,"can_hours":"00:00-00:03","use_end_time":2020,"sell_end_time":2020,"use_rule_info":"寇娜我哦","create_time":0,"photos":["http://imgs.wbx365.com/15888888888weibaixingtc16074175670","http://imgs.wbx365.com/15888888888weibaixingtc16074175671"],"has_rice":1,"rice_price":0.02,"rice_num":2,"has_table":1,"table_price":0.02,"table_num":2,"has_napkin":0,"napkin_price":0,"napkin_num":1,"has_self_fruits":0,"self_fruits_price":0,"self_fruits_num":1,"is_pause":0,"is_delete":0,"audit_status":2,"audit_message":"2222222","shop_id":1799,"shop_type":2}
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
         * shop_set_meal_id : 1
         * set_meal_name : 想跑
         * elect_kind : [{"goods_id":"97792","title":"草莓","goods_num":"1","price":8}]
         * need_kind : [{"goods_id":"108854","title":"鸡翅根","goods_num":"1","price":1}]
         * business_week_days : ["1","1","1","1","1","1","1"]
         * original_price : 0.1
         * one_price : 5
         * two_price : 5
         * vip_price : 0
         * place_commission_pct : 25
         * set_meal_skill_fee : 0
         * can_hours : 00:00-00:03
         * use_end_time : 2020
         * sell_end_time : 2020
         * use_rule_info : 寇娜我哦
         * create_time : 0
         * photos : ["http://imgs.wbx365.com/15888888888weibaixingtc16074175670","http://imgs.wbx365.com/15888888888weibaixingtc16074175671"]
         * has_rice : 1
         * rice_price : 0.02
         * rice_num : 2
         * has_table : 1
         * table_price : 0.02
         * table_num : 2
         * has_napkin : 0
         * napkin_price : 0
         * napkin_num : 1
         * has_self_fruits : 0
         * self_fruits_price : 0
         * self_fruits_num : 1
         * is_pause : 0
         * is_delete : 0
         * audit_status : 2
         * audit_message : 2222222
         * shop_id : 1799
         * shop_type : 2
         */

        private int shop_set_meal_id;
        private String set_meal_name;
        private float original_price;
        private int one_price;
        private int two_price;
        private int vip_price;
        private String set_meal_skill_fee;
        private String can_hours;
        private String use_end_time;
        private String sell_end_time;
        private String use_rule_info;
        private int has_rice;
        private String rice_price;
        private int rice_num;
        private int has_table;
        private String table_price;
        private int table_num;
        private int has_napkin;
        private String napkin_price;
        private int napkin_num;
        private int has_self_fruits;
        private String self_fruits_price;
        private int self_fruits_num;
        private int is_pause;
        private int is_delete;
        private int audit_status;
        private String audit_message;
        private int shop_id;
        private int shop_type;
        private List<ElectKindBean> elect_kind;
        private List<NeedKindBean> need_kind;
        private List<String> business_week_days;
        private List<String> photos;

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

        public float getOriginal_price() {
            return original_price;
        }

        public void setOriginal_price(float original_price) {
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


        public int getHas_rice() {
            return has_rice;
        }

        public void setHas_rice(int has_rice) {
            this.has_rice = has_rice;
        }

        public String getRice_price() {
            return rice_price;
        }

        public void setRice_price(String rice_price) {
            this.rice_price = rice_price;
        }

        public int getRice_num() {
            return rice_num;
        }

        public void setRice_num(int rice_num) {
            this.rice_num = rice_num;
        }

        public int getHas_table() {
            return has_table;
        }

        public void setHas_table(int has_table) {
            this.has_table = has_table;
        }

        public String getTable_price() {
            return table_price;
        }

        public void setTable_price(String table_price) {
            this.table_price = table_price;
        }

        public int getTable_num() {
            return table_num;
        }

        public void setTable_num(int table_num) {
            this.table_num = table_num;
        }

        public int getHas_napkin() {
            return has_napkin;
        }

        public void setHas_napkin(int has_napkin) {
            this.has_napkin = has_napkin;
        }

        public String getNapkin_price() {
            return napkin_price;
        }

        public void setNapkin_price(String napkin_price) {
            this.napkin_price = napkin_price;
        }

        public int getNapkin_num() {
            return napkin_num;
        }

        public void setNapkin_num(int napkin_num) {
            this.napkin_num = napkin_num;
        }

        public int getHas_self_fruits() {
            return has_self_fruits;
        }

        public void setHas_self_fruits(int has_self_fruits) {
            this.has_self_fruits = has_self_fruits;
        }

        public String getSelf_fruits_price() {
            return self_fruits_price;
        }

        public void setSelf_fruits_price(String self_fruits_price) {
            this.self_fruits_price = self_fruits_price;
        }

        public int getSelf_fruits_num() {
            return self_fruits_num;
        }

        public void setSelf_fruits_num(int self_fruits_num) {
            this.self_fruits_num = self_fruits_num;
        }

        public int getIs_pause() {
            return is_pause;
        }

        public void setIs_pause(int is_pause) {
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

        public int getShop_id() {
            return shop_id;
        }

        public void setShop_id(int shop_id) {
            this.shop_id = shop_id;
        }

        public int getShop_type() {
            return shop_type;
        }

        public void setShop_type(int shop_type) {
            this.shop_type = shop_type;
        }

        public List<ElectKindBean> getElect_kind() {
            return elect_kind;
        }

        public void setElect_kind(List<ElectKindBean> elect_kind) {
            this.elect_kind = elect_kind;
        }

        public List<NeedKindBean> getNeed_kind() {
            return need_kind;
        }

        public void setNeed_kind(List<NeedKindBean> need_kind) {
            this.need_kind = need_kind;
        }

        public List<String> getBusiness_week_days() {
            return business_week_days;
        }

        public void setBusiness_week_days(List<String> business_week_days) {
            this.business_week_days = business_week_days;
        }

        public List<String> getPhotos() {
            return photos;
        }

        public void setPhotos(List<String> photos) {
            this.photos = photos;
        }

        public static class ElectKindBean {
            /**
             * goods_id : 97792
             * title : 草莓
             * goods_num : 1
             * price : 8
             */

            private String goods_id;
            private String title;
            private String goods_num;
            private String price;

            public String getGoods_id() {
                return goods_id;
            }

            public void setGoods_id(String goods_id) {
                this.goods_id = goods_id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getGoods_num() {
                return goods_num;
            }

            public void setGoods_num(String goods_num) {
                this.goods_num = goods_num;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }
        }

        public static class NeedKindBean {
            /**
             * goods_id : 108854
             * title : 鸡翅根
             * goods_num : 1
             * price : 1
             */

            private String goods_id;
            private String title;
            private String goods_num;
            private String price;

            public String getGoods_id() {
                return goods_id;
            }

            public void setGoods_id(String goods_id) {
                this.goods_id = goods_id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getGoods_num() {
                return goods_num;
            }

            public void setGoods_num(String goods_num) {
                this.goods_num = goods_num;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }
        }
    }
}
