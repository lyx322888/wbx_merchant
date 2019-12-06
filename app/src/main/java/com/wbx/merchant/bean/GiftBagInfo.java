package com.wbx.merchant.bean;

import java.util.List;

public class GiftBagInfo {
    /**
     * msg : 成功
     * state : 1
     * data : {"give_goods":[{"give_goods_id":5,"photo":"http://imgs.wbx365.com/18159661581busine15470163070"}],"red_packet":[{"red_packet_id":68,"money":500},{"red_packet_id":69,"money":500},{"red_packet_id":70,"money":500},{"red_packet_id":71,"money":500},{"red_packet_id":72,"money":500},{"red_packet_id":73,"money":500},{"red_packet_id":74,"money":500}]}
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
        private List<GiveGoodsBean> give_goods;
        private List<RedPacketBean> red_packet;
        private String red_packet_valid_day;
        private String give_goods_valid_day;

        public String getRed_packet_valid_day() {
            return red_packet_valid_day;
        }

        public void setRed_packet_valid_day(String red_packet_valid_day) {
            this.red_packet_valid_day = red_packet_valid_day;
        }

        public String getGive_goods_valid_day() {
            return give_goods_valid_day;
        }

        public void setGive_goods_valid_day(String give_goods_valid_day) {
            this.give_goods_valid_day = give_goods_valid_day;
        }


        public List<GiveGoodsBean> getGive_goods() {
            return give_goods;
        }

        public void setGive_goods(List<GiveGoodsBean> give_goods) {
            this.give_goods = give_goods;
        }

        public List<RedPacketBean> getRed_packet() {
            return red_packet;
        }

        public void setRed_packet(List<RedPacketBean> red_packet) {
            this.red_packet = red_packet;
        }

        public static class GiveGoodsBean {
            /**
             * give_goods_id : 5
             * photo : http://imgs.wbx365.com/18159661581busine15470163070
             * goodsInfo.getProduct_name()
             */
            private String give_goods_id;
            private String photo;
            private String title;
            private String goods_id;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getGoods_id() {
                return goods_id;
            }

            public void setGoods_id(String goods_id) {
                this.goods_id = goods_id;
            }
            public String getGive_goods_id() {
                return give_goods_id;
            }

            public void setGive_goods_id(String give_goods_id) {
                this.give_goods_id = give_goods_id;
            }

            public String getPhoto() {
                return photo;
            }

            public void setPhoto(String photo) {
                this.photo = photo;
            }
        }

        public static class RedPacketBean {
            /**
             * red_packet_id : 68
             * money : 500
             */

            private String red_packet_id;
            private float money;

            public String getRed_packet_id() {
                return red_packet_id;
            }

            public void setRed_packet_id(String red_packet_id) {
                this.red_packet_id = red_packet_id;
            }

            public float getMoney() {
                return money;
            }

            public void setMoney(float money) {
                this.money = money;
            }
        }
    }
}
