package com.wbx.merchant.bean;

import java.io.Serializable;
import java.util.List;

public class OrderBean implements Serializable {

    /**
     * msg : 成功
     * state : 1
     * data : {"shop_info":{"shop_name":"gou肉专卖店","shop_phone":"0","receive_address":"厦门观音山海滨旅游商业街2980号鸿星尔克集团大厦","city_id":362,"area_id":359,"receive_name":"","business_time":"","receive_phone":"0","shop_address":"厦门观音山海滨旅游商业街2980号鸿星尔克集团大厦","has_printing":1},"order_id":"8388","order_goods":[{"goods_id":5,"order_num":1,"photo":"/attachs/2019/06/04/5cf5cf2909041.png","title":"海报","price":1},{"goods_id":6,"order_num":1,"photo":"/attachs/2019/06/04/5cf5cf3cd0cba.png","title":"名片","price":1}],"all_money":2}
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

    public static class DataBean implements  Serializable{
        /**
         * shop_info : {"shop_name":"gou肉专卖店","shop_phone":"0","receive_address":"厦门观音山海滨旅游商业街2980号鸿星尔克集团大厦","city_id":362,"area_id":359,"receive_name":"","business_time":"","receive_phone":"0","shop_address":"厦门观音山海滨旅游商业街2980号鸿星尔克集团大厦","has_printing":1}
         * order_id : 8388
         * order_goods : [{"goods_id":5,"order_num":1,"photo":"/attachs/2019/06/04/5cf5cf2909041.png","title":"海报","price":1},{"goods_id":6,"order_num":1,"photo":"/attachs/2019/06/04/5cf5cf3cd0cba.png","title":"名片","price":1}]
         * all_money : 2
         */

        private ShopInfoBean shop_info;
        private String order_id;
        private int all_money;
        private List<OrderGoodsBean> order_goods;

        public ShopInfoBean getShop_info() {
            return shop_info;
        }

        public void setShop_info(ShopInfoBean shop_info) {
            this.shop_info = shop_info;
        }

        public String getOrder_id() {
            return order_id;
        }

        public void setOrder_id(String order_id) {
            this.order_id = order_id;
        }

        public int getAll_money() {
            return all_money;
        }

        public void setAll_money(int all_money) {
            this.all_money = all_money;
        }

        public List<OrderGoodsBean> getOrder_goods() {
            return order_goods;
        }

        public void setOrder_goods(List<OrderGoodsBean> order_goods) {
            this.order_goods = order_goods;
        }

        public static class ShopInfoBean implements  Serializable {
            /**
             * shop_name : gou肉专卖店
             * shop_phone : 0
             * receive_address : 厦门观音山海滨旅游商业街2980号鸿星尔克集团大厦
             * city_id : 362
             * area_id : 359
             * receive_name :
             * business_time :
             * receive_phone : 0
             * shop_address : 厦门观音山海滨旅游商业街2980号鸿星尔克集团大厦
             * has_printing : 1
             */

            private String shop_name;
            private String shop_phone;
            private String receive_address;
            private int city_id;
            private int area_id;
            private String receive_name;
            private String business_time;
            private String receive_phone;
            private String shop_address;
            private int has_printing;

            public String getShop_name() {
                return shop_name;
            }

            public void setShop_name(String shop_name) {
                this.shop_name = shop_name;
            }

            public String getShop_phone() {
                return shop_phone;
            }

            public void setShop_phone(String shop_phone) {
                this.shop_phone = shop_phone;
            }

            public String getReceive_address() {
                return receive_address;
            }

            public void setReceive_address(String receive_address) {
                this.receive_address = receive_address;
            }

            public int getCity_id() {
                return city_id;
            }

            public void setCity_id(int city_id) {
                this.city_id = city_id;
            }

            public int getArea_id() {
                return area_id;
            }

            public void setArea_id(int area_id) {
                this.area_id = area_id;
            }

            public String getReceive_name() {
                return receive_name;
            }

            public void setReceive_name(String receive_name) {
                this.receive_name = receive_name;
            }

            public String getBusiness_time() {
                return business_time;
            }

            public void setBusiness_time(String business_time) {
                this.business_time = business_time;
            }

            public String getReceive_phone() {
                return receive_phone;
            }

            public void setReceive_phone(String receive_phone) {
                this.receive_phone = receive_phone;
            }

            public String getShop_address() {
                return shop_address;
            }

            public void setShop_address(String shop_address) {
                this.shop_address = shop_address;
            }

            public int getHas_printing() {
                return has_printing;
            }

            public void setHas_printing(int has_printing) {
                this.has_printing = has_printing;
            }
        }

        public static class OrderGoodsBean implements  Serializable{
            /**
             * goods_id : 5
             * order_num : 1
             * photo : /attachs/2019/06/04/5cf5cf2909041.png
             * title : 海报
             * price : 1
             */

            private int goods_id;
            private int order_num;
            private String photo;
            private String title;
            private int price;

            public int getGoods_id() {
                return goods_id;
            }

            public void setGoods_id(int goods_id) {
                this.goods_id = goods_id;
            }

            public int getOrder_num() {
                return order_num;
            }

            public void setOrder_num(int order_num) {
                this.order_num = order_num;
            }

            public String getPhoto() {
                return photo;
            }

            public void setPhoto(String photo) {
                this.photo = photo;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public int getPrice() {
                return price;
            }

            public void setPrice(int price) {
                this.price = price;
            }
        }
    }
}
