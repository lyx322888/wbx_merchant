package com.wbx.merchant.bean;

import java.util.List;

public class OpenRadarBean {


    /**
     * msg : 成功
     * state : 1
     * data : [{"account":"585a2fa3aa271@wbx365.com","face":"http://wx.qlogo.cn/mmopen/4wld8fOmn75eYCFvTGGwFM81uhkERdADeIiaX6OCVoiaK86Suvia3HI8TDQOLEb6dV7gHicw5zI7GQ2ZmJJaSNCB1Kqia6WQPflicH/0","nickname":"A雅吖手机带你赚票票","user_id":142,"lat":"24.490532","lng":"118.183812","headimgurl":"http://wx.qlogo.cn/mmopen/4wld8fOmn75eYCFvTGGwFM81uhkERdADeIiaX6OCVoiaK86Suvia3HI8TDQOLEb6dV7gHicw5zI7GQ2ZmJJaSNCB1Kqia6WQPflicH/0","distance":"1.45千米","is_bind":0},{"account":"15260879162","face":"http://imgs.wbx365.com/152608791621553222810","nickname":"在一起","user_id":7955,"lat":"24.492849","lng":"118.178859","headimgurl":"","distance":"2.02千米","is_bind":0},{"account":"18304142598@qq.com","face":"/attachs/2016/12/21/thumb_585a1b71b4d4b.png","nickname":"锦绣新天地","user_id":139,"lat":"24.490432","lng":"118.183712","headimgurl":"","distance":"1.46千米","is_bind":0},{"account":"18304142599@qq.com","face":"/attachs/2016/12/21/thumb_585a1bf0aa01e.png","nickname":"新塘新世界花园","user_id":140,"lat":"24.490532","lng":"118.183812","headimgurl":"","distance":"1.45千米","is_bind":0},{"account":"183041425100@qq.com","face":"/attachs/2016/12/21/thumb_585a1c741dce4.png","nickname":"锦绣御景苑","user_id":141,"lat":"24.490632","lng":"118.183812","headimgurl":"","distance":"1.46千米","is_bind":0},{"account":"1355925754625@qq.com","face":"","nickname":"西亭村","user_id":168,"lat":"24.490832","lng":"118.183812","headimgurl":"","distance":"1.47千米","is_bind":0},{"account":"1355925754627@qq.com","face":"","nickname":"后田","user_id":169,"lat":"24.490532","lng":"118.183812","headimgurl":"","distance":"1.45千米","is_bind":0},{"account":"1355925754628@qq.com","face":"","nickname":"杏林镇","user_id":170,"lat":"24.490532","lng":"118.183812","headimgurl":"","distance":"1.45千米","is_bind":0},{"account":"1355925754629@qq.com","face":"","nickname":"后溪镇","user_id":171,"lat":"24.490532","lng":"118.183812","headimgurl":"","distance":"1.45千米","is_bind":0},{"account":"1355925754630@qq.com","face":"","nickname":"灌口镇","user_id":172,"lat":"24.490532","lng":"118.183812","headimgurl":"","distance":"1.45千米","is_bind":0},{"account":"1355925754631@qq.com","face":"","nickname":"角美镇","user_id":173,"lat":"24.490532","lng":"118.183812","headimgurl":"","distance":"1.45千米","is_bind":0},{"account":"1355925754632@qq.com","face":"","nickname":"新垵","user_id":174,"lat":"24.490532","lng":"118.183812","headimgurl":"","distance":"1.45千米","is_bind":0},{"account":"1355925754633@qq.com","face":"","nickname":"许厝","user_id":175,"lat":"24.490532","lng":"118.183812","headimgurl":"","distance":"1.45千米","is_bind":0},{"account":"1355925754634@qq.com","face":"","nickname":"东屿村","user_id":176,"lat":"24.490532","lng":"118.183812","headimgurl":"","distance":"1.45千米","is_bind":0},{"account":"1355925754635@qq.com","face":"","nickname":"渐美村","user_id":177,"lat":"24.490532","lng":"118.183812","headimgurl":"","distance":"1.45千米","is_bind":0},{"account":"1355925754636@qq.com","face":"","nickname":"霞阳","user_id":178,"lat":"24.490532","lng":"118.183812","headimgurl":"","distance":"1.45千米","is_bind":0},{"account":"1355925754637@qq.com","face":"","nickname":"墨村","user_id":179,"lat":"24.490532","lng":"118.183812","headimgurl":"","distance":"1.45千米","is_bind":0}]
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
         * account : 585a2fa3aa271@wbx365.com
         * face : http://wx.qlogo.cn/mmopen/4wld8fOmn75eYCFvTGGwFM81uhkERdADeIiaX6OCVoiaK86Suvia3HI8TDQOLEb6dV7gHicw5zI7GQ2ZmJJaSNCB1Kqia6WQPflicH/0
         * nickname : A雅吖手机带你赚票票
         * user_id : 142
         * lat : 24.490532
         * lng : 118.183812
         * headimgurl : http://wx.qlogo.cn/mmopen/4wld8fOmn75eYCFvTGGwFM81uhkERdADeIiaX6OCVoiaK86Suvia3HI8TDQOLEb6dV7gHicw5zI7GQ2ZmJJaSNCB1Kqia6WQPflicH/0
         * distance : 1.45千米
         * is_bind : 0
         */

        private String account;
        private String face;
        private String nickname;
        private int user_id;
        private String lat;
        private String lng;
        private String headimgurl;
        private String distance;
        private int is_bind;
        private int Is_shop_member;

        public int getIs_shop_member() {
            return Is_shop_member;
        }

        public void setIs_shop_member(int is_shop_member) {
            Is_shop_member = is_shop_member;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getFace() {
            return face;
        }

        public void setFace(String face) {
            this.face = face;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLng() {
            return lng;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }

        public String getHeadimgurl() {
            return headimgurl;
        }

        public void setHeadimgurl(String headimgurl) {
            this.headimgurl = headimgurl;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public int getIs_bind() {
            return is_bind;
        }

        public void setIs_bind(int is_bind) {
            this.is_bind = is_bind;
        }
    }
}
