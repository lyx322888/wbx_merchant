package com.wbx.merchant.bean;

public class ScanOrderBean {

    /**
     * create_time : 2018-04-15 10:59:19
     * out_trade_no : 201804151059192417768892
     * seat : 1
     * need_price : 30.00
     */

    private String create_time;
    private String out_trade_no;
    private String seat;
    private int people_num;
    private String need_price;

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getSeat() {
        return seat;
    }

    public void setSeat(String seat) {
        this.seat = seat;
    }

    public int getPeople_num() {
        return people_num;
    }

    public void setPeople_num(int people_num) {
        this.people_num = people_num;
    }

    public String getNeed_price() {
        return need_price;
    }

    public void setNeed_price(String need_price) {
        this.need_price = need_price;
    }
}
