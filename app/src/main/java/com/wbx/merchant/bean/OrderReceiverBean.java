package com.wbx.merchant.bean;

import java.io.Serializable;

/**
 * @author Zero
 * @date 2018/4/11 0011
 */
public class OrderReceiverBean implements Serializable {

    /**
     * apiKey : 2269b759d46ed3cc162f233194882e5477eecf04
     * mKey :
     * partner : 11603
     * machine_code :
     * print_num : 2
     */

    private String apiKey;
    private String mKey;
    private String partner;
    private String machine_code;
    private int print_num;//打印联数
    private String print_id;
    private String print_name;
    private int is_edit;//如果修改print_num 则传1
    private String print_brand;//打印机品牌 1微百姓 2飞鹅 3易联云
    private String fe_sn;//飞鹅编号
    private String fe_key;//飞鹅key
    private String logo;//打印机LOGO
    private String print_brand_logo;
    private String print_f_logo;

    public String getPrint_name() {
        return print_name;
    }

    public void setPrint_name(String print_name) {
        this.print_name = print_name;
    }

    public String getPrint_id() {
        return print_id;
    }

    public void setPrint_id(String print_id) {
        this.print_id = print_id;
    }

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

    public int getIs_edit() {
        return is_edit;
    }

    public void setIs_edit(int is_edit) {
        this.is_edit = is_edit;
    }

    public String getPrint_brand() {
        return print_brand;
    }

    public void setPrint_brand(String print_brand) {
        this.print_brand = print_brand;
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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getPrint_brand_logo() {
        return print_brand_logo;
    }

    public void setPrint_brand_logo(String print_brand_logo) {
        this.print_brand_logo = print_brand_logo;
    }

    public String getPrint_f_logo() {
        return print_f_logo;
    }

    public void setPrint_f_logo(String print_f_logo) {
        this.print_f_logo = print_f_logo;
    }
}
