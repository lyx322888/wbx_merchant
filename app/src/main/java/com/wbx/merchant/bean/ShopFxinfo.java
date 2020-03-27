package com.wbx.merchant.bean;

import java.util.List;

public class ShopFxinfo {
    /**
     * msg : 成功
     * state : 1
     * data : {"shop_id":1799,"grade_id":20,"small_routine_photo":"http://www.wbx365.com/api/small_routine_photo/1799201812.jpg","shop_name":"刺激战场","photo":"http://imgs.wbx365.com/Ft8AnRgA0SkHcFx7LIDyoRXznpj4","goods":[{"price":50,"goods_id":70272,"title":"避风塘酱肉包400g 包子 面点 肉包子 方便速食 早餐","photo":"http://www.wbx365.com/attachs/2018/08/20/5b7a19e3a89bf.jpg"},{"price":100,"goods_id":69269,"title":"雀巢咖啡（Nescafe） 奶茶 原味奶茶固体饮料1000g","photo":"http://www.wbx365.com/attachs/2018/07/23/5b55a53ba1d7b.jpg"}]}
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
         * shop_id : 1799
         * grade_id : 20
         * small_routine_photo : http://www.wbx365.com/api/small_routine_photo/1799201812.jpg
         * shop_name : 刺激战场
         * photo : http://imgs.wbx365.com/Ft8AnRgA0SkHcFx7LIDyoRXznpj4
         * goods : [{"price":50,"goods_id":70272,"title":"避风塘酱肉包400g 包子 面点 肉包子 方便速食 早餐","photo":"http://www.wbx365.com/attachs/2018/08/20/5b7a19e3a89bf.jpg"},{"price":100,"goods_id":69269,"title":"雀巢咖啡（Nescafe） 奶茶 原味奶茶固体饮料1000g","photo":"http://www.wbx365.com/attachs/2018/07/23/5b55a53ba1d7b.jpg"}]
         */

        private int shop_id;
        private int grade_id;
        private String small_routine_photo;
        private String shop_name;
        private String photo;
        private List<GoodsBean> goods;

        public int getShop_id() {
            return shop_id;
        }

        public void setShop_id(int shop_id) {
            this.shop_id = shop_id;
        }

        public int getGrade_id() {
            return grade_id;
        }

        public void setGrade_id(int grade_id) {
            this.grade_id = grade_id;
        }

        public String getSmall_routine_photo() {
            return small_routine_photo;
        }

        public void setSmall_routine_photo(String small_routine_photo) {
            this.small_routine_photo = small_routine_photo;
        }

        public String getShop_name() {
            return shop_name;
        }

        public void setShop_name(String shop_name) {
            this.shop_name = shop_name;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public List<GoodsBean> getGoods() {
            return goods;
        }

        public void setGoods(List<GoodsBean> goods) {
            this.goods = goods;
        }

        public static class GoodsBean {
            /**
             * price : 50
             * goods_id : 70272
             * title : 避风塘酱肉包400g 包子 面点 肉包子 方便速食 早餐
             * photo : http://www.wbx365.com/attachs/2018/08/20/5b7a19e3a89bf.jpg
             */

            private int price;
            private int goods_id;
            private String title;
            private String photo;

            public int getPrice() {
                return price;
            }

            public void setPrice(int price) {
                this.price = price;
            }

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
        }
    }
}
