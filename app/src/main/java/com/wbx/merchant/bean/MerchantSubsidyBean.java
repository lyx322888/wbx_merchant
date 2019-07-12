package com.wbx.merchant.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/3/6 0006.
 */

public class MerchantSubsidyBean {

    /**
     * list : [{"money":200,"can_use":1,"order_time":"2018-04-24 14:45:43","order_id":4178,"subsidy_type":"正常下单补贴","user_id":13620,"face":"http://imgs.wbx365.com/FguQwo0SgjselRO7Cza9a6rn5mvH","nickname":"陈福灯***"},{"money":200,"can_use":1,"order_time":"2018-04-18 14:03:38","order_id":4152,"subsidy_type":"正常下单补贴","user_id":13620,"face":"http://imgs.wbx365.com/FguQwo0SgjselRO7Cza9a6rn5mvH","nickname":"陈福灯***"}]
     * all_money : 400
     * shop_subsidy_money : 0
     * rule : 1、订单30元及以上（不包含运费），每单补贴1元
     2、订单60元及以上（不包含运费），每单补贴2元
     3、商家每个月最多可获得600元奖励金
     4、每月15号可提现上个月的奖励金
     5、本活动最终解释权归微百姓（厦门）科技有限公司所有
     */

    private int all_money;
    private int shop_subsidy_money;
    private String rule;
    private List<ListBean> list;

    public int getAll_money() {
        return all_money;
    }

    public void setAll_money(int all_money) {
        this.all_money = all_money;
    }

    public int getShop_subsidy_money() {
        return shop_subsidy_money;
    }

    public void setShop_subsidy_money(int shop_subsidy_money) {
        this.shop_subsidy_money = shop_subsidy_money;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * money : 200
         * can_use : 1
         * order_time : 2018-04-24 14:45:43
         * order_id : 4178
         * subsidy_type : 正常下单补贴
         * user_id : 13620
         * face : http://imgs.wbx365.com/FguQwo0SgjselRO7Cza9a6rn5mvH
         * nickname : 陈福灯***
         */

        private int money;
        private int can_use;
        private String order_time;
        private int order_id;
        private String subsidy_type;
        private int user_id;
        private String face;
        private String nickname;
        private int is_first_bind;

        public int getIs_first_bind() {
            return is_first_bind;
        }

        public void setIs_first_bind(int is_first_bind) {
            this.is_first_bind = is_first_bind;
        }

        public int getMoney() {
            return money;
        }

        public void setMoney(int money) {
            this.money = money;
        }

        public int getCan_use() {
            return can_use;
        }

        public void setCan_use(int can_use) {
            this.can_use = can_use;
        }

        public String getOrder_time() {
            return order_time;
        }

        public void setOrder_time(String order_time) {
            this.order_time = order_time;
        }

        public int getOrder_id() {
            return order_id;
        }

        public void setOrder_id(int order_id) {
            this.order_id = order_id;
        }

        public String getSubsidy_type() {
            return subsidy_type;
        }

        public void setSubsidy_type(String subsidy_type) {
            this.subsidy_type = subsidy_type;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public String getFace() {
            return face;
        }

        public void setFace(String face) {
            this.face = face;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }
    }
}
