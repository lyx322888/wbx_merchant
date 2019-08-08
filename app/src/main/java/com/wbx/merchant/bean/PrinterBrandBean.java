package com.wbx.merchant.bean;

import java.util.List;

/**
 * 打印机品牌信息
 */
public class PrinterBrandBean {
    private String msg;
    private int state;
    private List<PrinterBean> data;

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

    public List<PrinterBean> getData() {
        return data;
    }

    public void setData(List<PrinterBean> data) {
        this.data = data;
    }

    public class PrinterBean {
        private String shop_brand;
        private String shop_brand_logo;

        public String getShop_brand() {
            return shop_brand;
        }

        public void setShop_brand(String shop_brand) {
            this.shop_brand = shop_brand;
        }

        public String getShop_brand_logo() {
            return shop_brand_logo;
        }

        public void setShop_brand_logo(String shop_brand_logo) {
            this.shop_brand_logo = shop_brand_logo;
        }
    }
}
