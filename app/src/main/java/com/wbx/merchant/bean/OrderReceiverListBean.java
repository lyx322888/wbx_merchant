package com.wbx.merchant.bean;

import java.util.List;

/**
 * @author Zero
 * @date 2018/4/11 0011
 */
public class OrderReceiverListBean {

    /**
     * apiKey : 2269b759d46ed3cc162f233194882e5477eecf04
     * mKey :
     * partner : 11603
     * machine_code :
     * print_num : 2
     * assistant_print : []
     */

    private String apiKey;
    private String mKey;
    private String partner;
    private String machine_code;
    private int print_num;
    private String print_brand;//现有打印机品牌
    private String print_z_logo;
    private String print_f_logo;
    private String fe_sn;
    private String fe_key;
    private String print_brand_logo;

    private List<OrderReceiverBean> assistant_print;

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getMKey() {
        return mKey;
    }

    public void setMKey(String mKey) {
        this.mKey = mKey;
    }

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public String getMachine_code() {
        return machine_code;
    }

    public void setMachine_code(String machine_code) {
        this.machine_code = machine_code;
    }

    public int getPrint_num() {
        return print_num;
    }

    public void setPrint_num(int print_num) {
        this.print_num = print_num;
    }

    public List<OrderReceiverBean> getAssistant_print() {
        return assistant_print;
    }

    public void setAssistant_print(List<OrderReceiverBean> assistant_print) {
        this.assistant_print = assistant_print;
    }

    public String getPrint_z_logo() {
        return print_z_logo;
    }

    public void setPrint_z_logo(String print_z_logo) {
        this.print_z_logo = print_z_logo;
    }

    public String getPrint_f_logo() {
        return print_f_logo;
    }

    public void setPrint_f_logo(String print_f_logo) {
        this.print_f_logo = print_f_logo;
    }

    public String getFe_sn() {
        return fe_sn;
    }

    public void setFe_sn(String fe_sn) {
        this.fe_sn = fe_sn;
    }

    public String getFe_key() {
        return fe_key;
    }

    public void setFe_key(String fe_key) {
        this.fe_key = fe_key;
    }

    public String getPrint_brand() {
        return print_brand;
    }

    public void setPrint_brand(String print_brand) {
        this.print_brand = print_brand;
    }

    public String getPrint_brand_logo() {
        return print_brand_logo;
    }

    public void setPrint_brand_logo(String print_brand_logo) {
        this.print_brand_logo = print_brand_logo;
    }
}
