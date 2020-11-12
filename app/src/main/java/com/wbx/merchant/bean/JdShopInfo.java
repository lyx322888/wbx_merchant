package com.wbx.merchant.bean;

import java.util.List;

public class JdShopInfo {

    /**
     * msg : 成功
     * state : 1
     * data : {"shopList":[{"address":"望海路望海路76号","microBizType":"MICRO_TYPE_STORE","mobilePhone":"15860088234","oneIndustry":"美食","shopName":"鹿皮铜","shopNum":"10001216011787982513575","twoIndustry":"东北菜"}]}
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
        private List<ShopListBean> shopList;

        public List<ShopListBean> getShopList() {
            return shopList;
        }

        public void setShopList(List<ShopListBean> shopList) {
            this.shopList = shopList;
        }

        public static class ShopListBean {
            /**
             * address : 望海路望海路76号
             * microBizType : MICRO_TYPE_STORE
             * mobilePhone : 15860088234
             * oneIndustry : 美食
             * shopName : 鹿皮铜
             * shopNum : 10001216011787982513575
             * twoIndustry : 东北菜
             */

            private String address;
            private String microBizType;
            private String mobilePhone;
            private String oneIndustry;
            private String shopName;
            private String shopNum;
            private String twoIndustry;

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getMicroBizType() {
                return microBizType;
            }

            public void setMicroBizType(String microBizType) {
                this.microBizType = microBizType;
            }

            public String getMobilePhone() {
                return mobilePhone;
            }

            public void setMobilePhone(String mobilePhone) {
                this.mobilePhone = mobilePhone;
            }

            public String getOneIndustry() {
                return oneIndustry;
            }

            public void setOneIndustry(String oneIndustry) {
                this.oneIndustry = oneIndustry;
            }

            public String getShopName() {
                return shopName;
            }

            public void setShopName(String shopName) {
                this.shopName = shopName;
            }

            public String getShopNum() {
                return shopNum;
            }

            public void setShopNum(String shopNum) {
                this.shopNum = shopNum;
            }

            public String getTwoIndustry() {
                return twoIndustry;
            }

            public void setTwoIndustry(String twoIndustry) {
                this.twoIndustry = twoIndustry;
            }
        }
    }
}
