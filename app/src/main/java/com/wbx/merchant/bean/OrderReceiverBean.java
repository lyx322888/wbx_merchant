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
    private int print_num;
    private String print_id;
    private String print_name;

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
}
