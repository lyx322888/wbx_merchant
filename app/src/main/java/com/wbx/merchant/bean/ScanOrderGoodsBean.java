package com.wbx.merchant.bean;

import java.util.List;

/**
 * @author Zero
 * @date 2018/4/26 0026
 */
public class ScanOrderGoodsBean {

    /**
     * cate_id : 3685
     * cate_name : 主食
     * goods : [{"title":"杰克丹尼（Jack Daniel`s）洋酒 美国田纳西州 威士忌700ml","price":60000,"is_attr":0,"attr_name":"","photo":"http://www.wbx365.com/attachs/2018/03/14/5aa8e0f639fd1.jpg"},{"title":"法国欧邑拿破仑XO 40℃ 白兰地 700ml","price":99900,"is_attr":0,"attr_name":"","photo":"http://www.wbx365.com/attachs/2018/03/14/5aa8e085f21a4.jpg"},{"title":"皇家礼炮（Royal Salute）洋酒 21年苏格兰威士忌 700ml","price":800,"is_attr":1,"attr_name":"小份","photo":"http://www.wbx365.com/attachs/2018/03/14/5aa8e0c872e68.jpg"},{"title":"皇家礼炮（Royal Salute）洋酒 21年苏格兰威士忌 700ml","price":500,"is_attr":1,"attr_name":"中份","photo":"http://www.wbx365.com/attachs/2018/03/14/5aa8e0c872e68.jpg"}]
     */

    private int cate_id;
    private String cate_name;
    private List<GoodsBean> goods;

    public int getCate_id() {
        return cate_id;
    }

    public void setCate_id(int cate_id) {
        this.cate_id = cate_id;
    }

    public String getCate_name() {
        return cate_name;
    }

    public void setCate_name(String cate_name) {
        this.cate_name = cate_name;
    }

    public List<GoodsBean> getGoods() {
        return goods;
    }

    public void setGoods(List<GoodsBean> goods) {
        this.goods = goods;
    }

    public static class GoodsBean {
        /**
         * title : 杰克丹尼（Jack Daniel`s）洋酒 美国田纳西州 威士忌700ml
         * price : 60000
         * is_attr : 0
         * attr_name :
         * photo : http://www.wbx365.com/attachs/2018/03/14/5aa8e0f639fd1.jpg
         */
        private int goods_id;
        private int attr_id;
        private String title;
        private int price;
        private int is_attr;
        private String attr_name;
        private String photo;
        private int num;

        public int getAttr_id() {
            return attr_id;
        }

        public void setAttr_id(int attr_id) {
            this.attr_id = attr_id;
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

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public int getIs_attr() {
            return is_attr;
        }

        public void setIs_attr(int is_attr) {
            this.is_attr = is_attr;
        }

        public String getAttr_name() {
            return attr_name;
        }

        public void setAttr_name(String attr_name) {
            this.attr_name = attr_name;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }
    }
}
