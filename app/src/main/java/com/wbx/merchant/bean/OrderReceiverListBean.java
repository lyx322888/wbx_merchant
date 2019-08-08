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
    private List<OrderReceiverBean> assistant_print;

    private String fe_sn;
    private String fe_key;
    private String print_brand;
    private String print_brand_logo;
    private String print_z_logo;
    private String print_f_logo;


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
}
