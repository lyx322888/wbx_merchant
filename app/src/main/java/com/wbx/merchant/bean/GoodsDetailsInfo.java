package com.wbx.merchant.bean;

import java.util.List;

public class GoodsDetailsInfo {

    /**
     * msg : 成功
     * state : 1
     * data : {"goods_id":5,"title":"海报","subhead":"副标题","photo":"http://vrzff.com/attachs/2019/06/04/5cf5cf2909041.png","details":"描述","price":5500,"is_handpick":0,"original_price":258000,"sales_volume":1682,"buy_shop_num":1,"buy_shop_photo":[""],"cart_num":3}
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
         * goods_id : 5
         * title : 海报
         * subhead : 副标题
         * photo : http://vrzff.com/attachs/2019/06/04/5cf5cf2909041.png
         * details : 描述
         * price : 5500
         * is_handpick : 0
         * original_price : 258000
         * sales_volume : 1682
         * buy_shop_num : 1
         * buy_shop_photo : [""]
         * cart_num : 3
         */

        private int goods_id;
        private String title;
        private String subhead;
        private String photo;
        private String details;
        private int price;
        private int is_handpick;
        private int original_price;
        private int sales_volume;
        private int buy_shop_num;
        private int cart_num;
        private List<String> buy_shop_photo;
        private int has_printing;


        public int getGoods_id() {
            return goods_id;
        }

        public void setGoods_id(int goods_id) {
            this.goods_id = goods_id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSubhead() {
            return subhead;
        }

        public void setSubhead(String subhead) {
            this.subhead = subhead;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public String getDetails() {
            return details;
        }

        public void setDetails(String details) {
            this.details = details;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public int getIs_handpick() {
            return is_handpick;
        }

        public void setIs_handpick(int is_handpick) {
            this.is_handpick = is_handpick;
        }

        public int getOriginal_price() {
            return original_price;
        }

        public void setOriginal_price(int original_price) {
            this.original_price = original_price;
        }

        public int getSales_volume() {
            return sales_volume;
        }

        public void setSales_volume(int sales_volume) {
            this.sales_volume = sales_volume;
        }

        public int getBuy_shop_num() {
            return buy_shop_num;
        }

        public void setBuy_shop_num(int buy_shop_num) {
            this.buy_shop_num = buy_shop_num;
        }

        public int getCart_num() {
            return cart_num;
        }

        public void setCart_num(int cart_num) {
            this.cart_num = cart_num;
        }

        public List<String> getBuy_shop_photo() {
            return buy_shop_photo;
        }

        public void setBuy_shop_photo(List<String> buy_shop_photo) {
            this.buy_shop_photo = buy_shop_photo;
        }

        public int getHas_printing() {
            return has_printing;
        }

        public void setHas_printing(int has_printing) {
            this.has_printing = has_printing;
        }
    }
}
