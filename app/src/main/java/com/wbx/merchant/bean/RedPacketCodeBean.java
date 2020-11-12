package com.wbx.merchant.bean;

public class RedPacketCodeBean {
    /**
     * msg : 成功
     * state : 1
     * data : {"red_packet_code":"1111111123333333","shop_name":"你好啊"}
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
         * red_packet_code : 1111111123333333
         * shop_name : 你好啊
         */

        private String red_packet_code;
        private String shop_name;

        public String getRed_packet_code() {
            return red_packet_code;
        }

        public void setRed_packet_code(String red_packet_code) {
            this.red_packet_code = red_packet_code;
        }

        public String getShop_name() {
            return shop_name;
        }

        public void setShop_name(String shop_name) {
            this.shop_name = shop_name;
        }
    }
}
