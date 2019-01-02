package com.wbx.merchant.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wushenghui on 2017/12/26.
 */

public class BookSeatInfo implements Serializable {

    /**
     * reserve_table_id : 118
     * name : 苹果
     * mobile : 15255553333
     * number : 2
     * subscribe_money : 8000
     * note :
     * type : 2
     * shop_id : 1395
     * user_id : 7955
     * order_id : 2566
     * create_time : 1513914204
     * reserve_time : 1513913100
     * status : 1
     * order_goods : [{"goods_id":53662,"price":4000,"total_price":8000,"num":2,"attr_id":0,"title":"美味羊肉煲小","intro":"美味羊肉煲","photo":"http://www.wbx365.com/attachs/2017/10/31/thumb_59f7e1357674c.jpg","shop_name":"阿火重庆鸡公煲","attr_name":""}]
     */

    private int reserve_table_id;
    private String name;
    private String mobile;
    private int number;
    private int subscribe_money;
    private String note;
    private int type;
    private int shop_id;
    private int user_id;
    private int order_id;
    private int create_time;
    private int reserve_time;
    private int status;
    private List<OrderGoodsBean> order_goods;

    public int getReserve_table_id() {
        return reserve_table_id;
    }

    public void setReserve_table_id(int reserve_table_id) {
        this.reserve_table_id = reserve_table_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getSubscribe_money() {
        return subscribe_money;
    }

    public void setSubscribe_money(int subscribe_money) {
        this.subscribe_money = subscribe_money;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getShop_id() {
        return shop_id;
    }

    public void setShop_id(int shop_id) {
        this.shop_id = shop_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getCreate_time() {
        return create_time;
    }

    public void setCreate_time(int create_time) {
        this.create_time = create_time;
    }

    public int getReserve_time() {
        return reserve_time;
    }

    public void setReserve_time(int reserve_time) {
        this.reserve_time = reserve_time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<OrderGoodsBean> getOrder_goods() {
        return order_goods;
    }

    public void setOrder_goods(List<OrderGoodsBean> order_goods) {
        this.order_goods = order_goods;
    }

    public static class OrderGoodsBean implements Serializable {
        /**
         * goods_id : 53662
         * price : 4000
         * total_price : 8000
         * num : 2
         * attr_id : 0
         * title : 美味羊肉煲小
         * intro : 美味羊肉煲
         * photo : http://www.wbx365.com/attachs/2017/10/31/thumb_59f7e1357674c.jpg
         * shop_name : 阿火重庆鸡公煲
         * attr_name :
         */

        private int goods_id;
        private int price;
        private int total_price;
        private int num;
        private int attr_id;
        private String title;
        private String intro;
        private String photo;
        private String shop_name;
        private String attr_name;
        private String nature_name;

        public String getNature_name() {
            return nature_name;
        }

        public void setNature_name(String nature_name) {
            this.nature_name = nature_name;
        }

        public int getGoods_id() {
            return goods_id;
        }

        public void setGoods_id(int goods_id) {
            this.goods_id = goods_id;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public int getTotal_price() {
            return total_price;
        }

        public void setTotal_price(int total_price) {
            this.total_price = total_price;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public int getAttr_id() {
            return attr_id;
        }

        public void setAttr_id(int attr_id) {
            this.attr_id = attr_id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getIntro() {
            return intro;
        }

        public void setIntro(String intro) {
            this.intro = intro;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public String getShop_name() {
            return shop_name;
        }

        public void setShop_name(String shop_name) {
            this.shop_name = shop_name;
        }

        public String getAttr_name() {
            return attr_name;
        }

        public void setAttr_name(String attr_name) {
            this.attr_name = attr_name;
        }
    }
}
