package com.wbx.merchant.bean;

/**
 * @author Zero
 * @date 2018/9/8
 */
public class PrinterGoodsCateBean {

    /**
     * cate_id : 120
     * cate_name : 蔬菜
     * is_print : 1
     */

    private int cate_id;
    private String cate_name;
    private int is_print;

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

    public int getIs_print() {
        return is_print;
    }

    public void setIs_print(int is_print) {
        this.is_print = is_print;
    }
}
