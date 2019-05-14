package com.wbx.merchant.bean;

import java.util.List;

public class CateBean {

    /**
     * msg : 成功
     * state : 1
     * data : {"cates":[{"cate_id":166,"cate_name":" 自助餐"},{"cate_id":167,"cate_name":"烧烤"},{"cate_id":168,"cate_name":"小吃快餐"},{"cate_id":169,"cate_name":"西餐"},{"cate_id":170,"cate_name":"甜点饮品"},{"cate_id":171,"cate_name":"火锅"},{"cate_id":172,"cate_name":"川湘菜"},{"cate_id":173,"cate_name":"海鲜"}],"shop_grade":{"grade_id":20,"grade_name":"美食街","money":"1"}}
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
         * cates : [{"cate_id":166,"cate_name":" 自助餐"},{"cate_id":167,"cate_name":"烧烤"},{"cate_id":168,"cate_name":"小吃快餐"},{"cate_id":169,"cate_name":"西餐"},{"cate_id":170,"cate_name":"甜点饮品"},{"cate_id":171,"cate_name":"火锅"},{"cate_id":172,"cate_name":"川湘菜"},{"cate_id":173,"cate_name":"海鲜"}]
         * shop_grade : {"grade_id":20,"grade_name":"美食街","money":"1"}
         */

        private ShopGradeBean shop_grade;
        private List<CatesBean> cates;

        public ShopGradeBean getShop_grade() {
            return shop_grade;
        }

        public void setShop_grade(ShopGradeBean shop_grade) {
            this.shop_grade = shop_grade;
        }

        public List<CatesBean> getCates() {
            return cates;
        }

        public void setCates(List<CatesBean> cates) {
            this.cates = cates;
        }

        public static class ShopGradeBean {
            /**
             * grade_id : 20
             * grade_name : 美食街
             * money : 1
             */

            private int grade_id;
            private String grade_name;
            private int money;

            public int getGrade_id() {
                return grade_id;
            }

            public void setGrade_id(int grade_id) {
                this.grade_id = grade_id;
            }

            public String getGrade_name() {
                return grade_name;
            }

            public void setGrade_name(String grade_name) {
                this.grade_name = grade_name;
            }

            public int getMoney() {
                return money;
            }

            public void setMoney(int money) {
                this.money = money;
            }
        }

        public static class CatesBean {
            /**
             * cate_id : 166
             * cate_name :  自助餐
             */

            private String cate_id;
            private String cate_name;

            public String getCate_id() {
                return cate_id;
            }

            public void setCate_id(String cate_id) {
                this.cate_id = cate_id;
            }

            public String getCate_name() {
                return cate_name;
            }

            public void setCate_name(String cate_name) {
                this.cate_name = cate_name;
            }
        }
    }
}
