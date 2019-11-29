package com.wbx.merchant.bean;

import java.util.List;

public class CityCommunityBean {

    /**
     * data : [{"address":"厦门市厦门市","city_community_id":1,"community_name":"何厝社区","distance":"0.28KM","lat":"24.490455","lng":"118.183717"}]
     * msg : 成功
     * state : 1
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
         * address : 厦门市厦门市
         * city_community_id : 1
         * community_name : 何厝社区
         * distance : 0.28KM
         * lat : 24.490455
         * lng : 118.183717
         */

        private String address;
        private String city_community_id;
        private String community_name;
        private String distance;
        private String lat;
        private String lng;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getCity_community_id() {
            return city_community_id;
        }

        public void setCity_community_id(String city_community_id) {
            this.city_community_id = city_community_id;
        }

        public String getCommunity_name() {
            return community_name;
        }

        public void setCommunity_name(String community_name) {
            this.community_name = community_name;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
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
    }
}
