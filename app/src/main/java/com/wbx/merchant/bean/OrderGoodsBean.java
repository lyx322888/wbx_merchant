package com.wbx.merchant.bean;

import java.util.List;

public class OrderGoodsBean {

    /**
     * order_id : 8685
     * create_time : 1561368459
     * need_pay : 1
     * express_number :
     * is_shipments : 0
     * express_company :
     * goods : [{"order_num":1,"price":1,"total_price":1,"original_price":258000,"photo":"http://xm32.cn/attachs/2019/06/04/5cf5cf2909041.png","title":"海报"}]
     * all_order_num : 1
     * commision_text
     */

    private int order_id;
    private int create_time;
    private float need_pay;
    private String express_number;
    private int is_shipments;
    private String express_company;
    private int all_order_num;
    private List<GoodsBean> goods;

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

    public float getNeed_pay() {
        return need_pay;
    }

    public void setNeed_pay(float need_pay) {
        this.need_pay = need_pay;
    }

    public String getExpress_number() {
        return express_number;
    }

    public void setExpress_number(String express_number) {
        this.express_number = express_number;
    }

    public int getIs_shipments() {
        return is_shipments;
    }

    public void setIs_shipments(int is_shipments) {
        this.is_shipments = is_shipments;
    }

    public String getExpress_company() {
        return express_company;
    }

    public void setExpress_company(String express_company) {
        this.express_company = express_company;
    }

    public int getAll_order_num() {
        return all_order_num;
    }

    public void setAll_order_num(int all_order_num) {
        this.all_order_num = all_order_num;
    }

    public List<GoodsBean> getGoods() {
        return goods;
    }

    public void setGoods(List<GoodsBean> goods) {
        this.goods = goods;
    }

    public static class GoodsBean {
        /**
         * order_num : 1
         * price : 1
         * total_price : 1
         * original_price : 258000
         * photo : http://xm32.cn/attachs/2019/06/04/5cf5cf2909041.png
         * title : 海报
         */

        private int order_num;
        private float price;
        private float total_price;
        private float original_price;
        private String photo;
        private String title;

        public int getOrder_num() {
            return order_num;
        }

        public void setOrder_num(int order_num) {
            this.order_num = order_num;
        }

        public float getPrice() {
            return price;
        }

        public void setPrice(float price) {
            this.price = price;
        }

        public float getTotal_price() {
            return total_price;
        }

        public void setTotal_price(float total_price) {
            this.total_price = total_price;
        }

        public float getOriginal_price() {
            return original_price;
        }

        public void setOriginal_price(float original_price) {
            this.original_price = original_price;
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
