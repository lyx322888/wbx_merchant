package com.wbx.merchant.bean;

import java.util.List;

/**
 * @author Zero
 * @date 2018/7/12
 */
public class MemberDetailBean {

    /**
     * recently_money : 10
     * recently_time : 2月前
     * count_all_order_num : 4
     * sum_all_order_money : 220
     * average_order_money : 55
     * all_order : [{"money":10,"message":"通过购物订单消费10元","create_time":"2018-05-07"},{"money":10,"message":"通过购物订单消费10元","create_time":"2018-05-07"},{"money":100,"message":"通过购物订单消费100元","create_time":"2018-04-24"},{"money":100,"message":"通过购物订单消费100元","create_time":"2018-04-18"}]
     */

    private int recently_money;
    private String recently_time;
    private int count_all_order_num;
    private String sum_all_order_money;
    private int average_order_money;
    private List<AllOrderBean> all_order;

    public int getRecently_money() {
        return recently_money;
    }

    public void setRecently_money(int recently_money) {
        this.recently_money = recently_money;
    }

    public String getRecently_time() {
        return recently_time;
    }

    public void setRecently_time(String recently_time) {
        this.recently_time = recently_time;
    }

    public int getCount_all_order_num() {
        return count_all_order_num;
    }

    public void setCount_all_order_num(int count_all_order_num) {
        this.count_all_order_num = count_all_order_num;
    }

    public String getSum_all_order_money() {
        return sum_all_order_money;
    }

    public void setSum_all_order_money(String sum_all_order_money) {
        this.sum_all_order_money = sum_all_order_money;
    }

    public int getAverage_order_money() {
        return average_order_money;
    }

    public void setAverage_order_money(int average_order_money) {
        this.average_order_money = average_order_money;
    }

    public List<AllOrderBean> getAll_order() {
        return all_order;
    }

    public void setAll_order(List<AllOrderBean> all_order) {
        this.all_order = all_order;
    }

    public static class AllOrderBean {
        /**
         * money : 10
         * message : 通过购物订单消费10元
         * create_time : 2018-05-07
         */

        private int money;
        private String message;
        private String create_time;

        public int getMoney() {
            return money;
        }

        public void setMoney(int money) {
            this.money = money;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }
    }
}
