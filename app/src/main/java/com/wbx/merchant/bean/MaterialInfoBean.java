package com.wbx.merchant.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/11/9 0009.
 */

public class MaterialInfoBean implements Serializable {

    /**
     * cate_id : 1
     * name : 全部
     * photo :
     * product : [{"product_library_id":2,"name":"苹果","price":1,"photo":"http://vrzff.com/attachs/2017/10/19/thumb_59e80c88b7fa0.jpg"},{"product_library_id":3,"name":"西瓜","price":540,"photo":"http://vrzff.com/attachs/2017/11/07/thumb_5a01576674c1d.jpg"},{"product_library_id":4,"name":"黄瓜","price":330,"photo":"http://vrzff.com/attachs/2017/11/07/thumb_5a01577a46300.jpg"},{"product_library_id":5,"name":"荔枝","price":340,"photo":"http://vrzff.com/attachs/2017/11/07/thumb_5a0157a5d1848.jpg"},{"product_library_id":6,"name":"火龙果","price":350,"photo":"http://vrzff.com/attachs/2017/11/07/thumb_5a0157bac1312.jpg"},{"product_library_id":7,"name":"黄花菜","price":540,"photo":"http://vrzff.com/attachs/2017/11/07/thumb_5a0157d1eb49a.jpg"},{"product_library_id":8,"name":"香蕉","price":560,"photo":"http://vrzff.com/attachs/2017/11/07/thumb_5a0157e45b126.jpg"},{"product_library_id":9,"name":"菠萝","price":670,"photo":"http://vrzff.com/attachs/2017/11/07/thumb_5a0157fd0e79d.jpg"}]
     */

    private int cate_id;
    private String name;
    private String photo;
    private List<ProductBean> product;
    private boolean isCheck;

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public int getCate_id() {
        return cate_id;
    }

    public void setCate_id(int cate_id) {
        this.cate_id = cate_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public List<ProductBean> getProduct() {
        return product;
    }

    public void setProduct(List<ProductBean> product) {
        this.product = product;
    }

    public static class ProductBean implements Serializable {
        /**
         * product_library_id : 2
         * name : 苹果
         * price : 1
         * photo : http://vrzff.com/attachs/2017/10/19/thumb_59e80c88b7fa0.jpg
         */

        private int product_library_id;
        private String name;
        private String price;
        private String photo;
        private boolean isCheck;

        public boolean getIsCheck() {
            return isCheck;
        }

        public void setIsCheck(boolean check) {
            isCheck = check;
        }

        public int getProduct_library_id() {
            return product_library_id;
        }

        public void setProduct_library_id(int product_library_id) {
            this.product_library_id = product_library_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }
    }
}
