package com.wbx.merchant.bean;

import java.util.List;

public class ScanOrderDetailBean {

    /**
     * seat : 1
     * out_trade_no : 20180415101721517301602
     * need_price : 90.00
     * coupon_money : 0.00
     * full_money_reduce : 0.00
     * goods : [{"goods_id":59918,"num":1,"price":"10.00","total_price":"10.00","photo":"","title":""},{"goods_id":59917,"num":1,"price":"10.00","total_price":"10.00","photo":"","title":""},{"goods_id":59919,"num":1,"price":"10.00","total_price":"10.00","photo":"","title":""},{"goods_id":59923,"num":1,"price":"10.00","total_price":"10.00","photo":"","title":""},{"goods_id":59922,"num":1,"price":"10.00","total_price":"10.00","photo":"","title":""},{"goods_id":59918,"num":1,"price":"10.00","total_price":"10.00","photo":"","title":""},{"goods_id":59917,"num":1,"price":"10.00","total_price":"10.00","photo":"","title":""},{"goods_id":59901,"num":1,"price":"10.00","total_price":"10.00","photo":"","title":""},{"goods_id":59900,"num":1,"price":"10.00","total_price":"10.00","photo":"","title":""}]
     */

    private String seat;
    private String out_trade_no;
    private String need_price;
    private String coupon_money;
    private String full_money_reduce;
    private int status;
    private List<GoodsBean> goods;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getSeat() {
        return seat;
    }

    public void setSeat(String seat) {
        this.seat = seat;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getNeed_price() {
        return need_price;
    }

    public void setNeed_price(String need_price) {
        this.need_price = need_price;
    }

    public String getCoupon_money() {
        return coupon_money;
    }

    public void setCoupon_money(String coupon_money) {
        this.coupon_money = coupon_money;
    }

    public String getFull_money_reduce() {
        return full_money_reduce;
    }

    public void setFull_money_reduce(String full_money_reduce) {
        this.full_money_reduce = full_money_reduce;
    }

    public List<GoodsBean> getGoods() {
        return goods;
    }

    public void setGoods(List<GoodsBean> goods) {
        this.goods = goods;
    }

    public static class GoodsBean {
        /**
         * goods_id : 59918
         * num : 1
         * price : 10.00
         * total_price : 10.00
         * photo :
         * title :
         */

        private int goods_id;
        private int attr_id;
        private int num;
        private int is_add;
        private int id;
        private String price;
        private String total_price;
        private String photo;
        private String title;
        private String attr_name;
        private String nature_name;

        public String getNature_name() {
            return nature_name;
        }

        public void setNature_name(String nature_name) {
            this.nature_name = nature_name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getIs_add() {
            return is_add;
        }

        public void setIs_add(int is_add) {
            this.is_add = is_add;
        }

        public int getAttr_id() {
            return attr_id;
        }

        public void setAttr_id(int attr_id) {
            this.attr_id = attr_id;
        }

        public String getAttr_name() {
            return attr_name;
        }

        public void setAttr_name(String attr_name) {
            this.attr_name = attr_name;
        }

        public int getGoods_id() {
            return goods_id;
        }

        public void setGoods_id(int goods_id) {
            this.goods_id = goods_id;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getTotal_price() {
            return total_price;
        }

        public void setTotal_price(String total_price) {
            this.total_price = total_price;
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
    }
}
