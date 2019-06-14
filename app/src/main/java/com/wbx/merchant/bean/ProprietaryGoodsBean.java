package com.wbx.merchant.bean;

import java.util.List;

public class ProprietaryGoodsBean {

    /**
     * msg : 成功
     * state : 1
     * data : [{"goods_id":5,"title":"海报","photo":"http://vrzff.com/attachs/2019/06/04/5cf5cf2909041.png","details":"描述","create_time":1559613229,"create_ip":"110.87.83.115","closed":0,"orderby":100,"price":5500,"describe":"副标题","is_handpick":0,"order_num":0},{"goods_id":6,"title":"名片","photo":"http://vrzff.com/attachs/2019/06/04/5cf5cf3cd0cba.png","details":"","create_time":1559613248,"create_ip":"110.87.83.115","closed":0,"orderby":100,"price":300,"describe":"","is_handpick":0,"order_num":0}]
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
         * goods_id : 5
         * title : 海报
         * photo : http://vrzff.com/attachs/2019/06/04/5cf5cf2909041.png
         * details : 描述
         * create_time : 1559613229
         * create_ip : 110.87.83.115
         * closed : 0
         * orderby : 100
         * price : 5500
         * describe : 副标题
         * is_handpick : 0
         * order_num : 0
         */

        private int goods_id;
        private String title;
        private String photo;
        private String details;
        private int create_time;
        private String create_ip;
        private int closed;
        private int orderby;
        private int price;
        private String describe;
        private int is_handpick;
        private int order_num;

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

        public int getCreate_time() {
            return create_time;
        }

        public void setCreate_time(int create_time) {
            this.create_time = create_time;
        }

        public String getCreate_ip() {
            return create_ip;
        }

        public void setCreate_ip(String create_ip) {
            this.create_ip = create_ip;
        }

        public int getClosed() {
            return closed;
        }

        public void setClosed(int closed) {
            this.closed = closed;
        }

        public int getOrderby() {
            return orderby;
        }

        public void setOrderby(int orderby) {
            this.orderby = orderby;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public String getDescribe() {
            return describe;
        }

        public void setDescribe(String describe) {
            this.describe = describe;
        }

        public int getIs_handpick() {
            return is_handpick;
        }

        public void setIs_handpick(int is_handpick) {
            this.is_handpick = is_handpick;
        }

        public int getOrder_num() {
            return order_num;
        }

        public void setOrder_num(int order_num) {
            this.order_num = order_num;
        }
    }
}
