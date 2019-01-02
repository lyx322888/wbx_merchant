package com.wbx.merchant.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author Zero
 * @date 2018/8/16
 */
public class NumberOrderBean implements Serializable {

    /**
     * order_id : 3559
     * create_time : 1534323525
     * total_price : 5200
     * express_price : 0
     * status : 1
     * is_daofu : 0
     * address_id : 720
     * message :
     * casing_price : 0
     * user_subsidy_money : 0
     * full_money_reduce : 0
     * coupon_money : 0
     * reserve_table_id : 0
     * red_packet_money : 0
     * is_take_number : 1
     * order_take_number : 7
     * goods : [{"goods_id":42209,"price":200,"total_price":2200,"num":11,"title":"怡泉+C柠檬味汽水330ml听","intro":"","photo":"http://www.wbx365.com/attachs/2017/07/07/thumb_595ef06539a2f.jpg","shop_name":"天天乐平价超市","attr_name":""},{"goods_id":53662,"price":5200,"total_price":5200,"num":1,"title":"美味羊肉煲小","intro":"<p>美味羊肉煲<\/p>","photo":"http://www.wbx365.com/attachs/2017/10/31/thumb_59f7e1357674c.jpg","shop_name":"阿火重庆鸡公煲","attr_name":""}]
     * num : 12
     */

    private String order_id;
    private int create_time;
    private int total_price;
    private int express_price;
    private int status;
    private int is_daofu;
    private int address_id;
    private String message;
    private int casing_price;
    private int user_subsidy_money;
    private int full_money_reduce;
    private int coupon_money;
    private int reserve_table_id;
    private int red_packet_money;
    private int is_take_number;
    private String order_take_number;
    private int num;
    private List<GoodsBean> goods;

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public int getCreate_time() {
        return create_time;
    }

    public void setCreate_time(int create_time) {
        this.create_time = create_time;
    }

    public int getTotal_price() {
        return total_price;
    }

    public void setTotal_price(int total_price) {
        this.total_price = total_price;
    }

    public int getExpress_price() {
        return express_price;
    }

    public void setExpress_price(int express_price) {
        this.express_price = express_price;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getIs_daofu() {
        return is_daofu;
    }

    public void setIs_daofu(int is_daofu) {
        this.is_daofu = is_daofu;
    }

    public int getAddress_id() {
        return address_id;
    }

    public void setAddress_id(int address_id) {
        this.address_id = address_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCasing_price() {
        return casing_price;
    }

    public void setCasing_price(int casing_price) {
        this.casing_price = casing_price;
    }

    public int getUser_subsidy_money() {
        return user_subsidy_money;
    }

    public void setUser_subsidy_money(int user_subsidy_money) {
        this.user_subsidy_money = user_subsidy_money;
    }

    public int getFull_money_reduce() {
        return full_money_reduce;
    }

    public void setFull_money_reduce(int full_money_reduce) {
        this.full_money_reduce = full_money_reduce;
    }

    public int getCoupon_money() {
        return coupon_money;
    }

    public void setCoupon_money(int coupon_money) {
        this.coupon_money = coupon_money;
    }

    public int getReserve_table_id() {
        return reserve_table_id;
    }

    public void setReserve_table_id(int reserve_table_id) {
        this.reserve_table_id = reserve_table_id;
    }

    public int getRed_packet_money() {
        return red_packet_money;
    }

    public void setRed_packet_money(int red_packet_money) {
        this.red_packet_money = red_packet_money;
    }

    public int getIs_take_number() {
        return is_take_number;
    }

    public void setIs_take_number(int is_take_number) {
        this.is_take_number = is_take_number;
    }

    public String getOrder_take_number() {
        return order_take_number;
    }

    public void setOrder_take_number(String order_take_number) {
        this.order_take_number = order_take_number;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public List<GoodsBean> getGoods() {
        return goods;
    }

    public void setGoods(List<GoodsBean> goods) {
        this.goods = goods;
    }

    public static class GoodsBean implements Serializable {
        /**
         * goods_id : 42209
         * price : 200
         * total_price : 2200
         * num : 11
         * title : 怡泉+C柠檬味汽水330ml听
         * intro :
         * photo : http://www.wbx365.com/attachs/2017/07/07/thumb_595ef06539a2f.jpg
         * shop_name : 天天乐平价超市
         * attr_name :
         */

        private int goods_id;
        private int price;
        private int total_price;
        private int num;
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
