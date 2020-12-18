package com.wbx.merchant.bean;

import java.util.List;

public class SelectGoodsInfo {
    /**
     * msg : 成功
     * state : 1
     * data : [{"goods_id":107467,"title":"甘源 蟹黄味瓜子仁 75g","photo":"http://www.wbx365.com/attachs/2018/04/18/5ad6db99719e0.jpg","mall_price":10000,"is_secret":0,"is_attr":0,"nature":[],"goods_attr":[]},{"goods_id":107466,"title":"疯狂的蚕豆220克","photo":"http://www.wbx365.com/attachs/2018/04/18/5ad6c7508072a.jpg","mall_price":1000,"is_secret":0,"is_attr":0,"nature":[],"goods_attr":[]},{"goods_id":107465,"title":"大好大奶油香瓜子60克","photo":"http://www.wbx365.com/attachs/2018/04/18/5ad6c738c0f68.jpg","mall_price":1000,"is_secret":0,"is_attr":0,"nature":[],"goods_attr":[]}]
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
         * goods_id : 107467
         * title : 甘源 蟹黄味瓜子仁 75g
         * photo : http://www.wbx365.com/attachs/2018/04/18/5ad6db99719e0.jpg
         * mall_price : 10000
         * is_secret : 0
         * is_attr : 0
         * nature : []
         * goods_attr : []
         */

        private String goods_id;
        private String title;
        private String photo;
        private String mall_price;
        private int is_secret;
        private int is_attr;


        public String getGoods_id() {
            return goods_id;
        }

        public void setGoods_id(String goods_id) {
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

        public String getMall_price() {
            return mall_price;
        }

        public void setMall_price(String mall_price) {
            this.mall_price = mall_price;
        }

        public int getIs_secret() {
            return is_secret;
        }

        public void setIs_secret(int is_secret) {
            this.is_secret = is_secret;
        }

        public int getIs_attr() {
            return is_attr;
        }

        public void setIs_attr(int is_attr) {
            this.is_attr = is_attr;
        }


    }
}
