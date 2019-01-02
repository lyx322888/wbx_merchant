package com.wbx.merchant.bean;

import java.io.Serializable;

/**
 * Created by wushenghui on 2017/12/6.
 */

public class SeatInfo implements Serializable {
    private int id;
    private int shop_id;
    private String qrcode;
    private String create_time;
    private String  table_number;
    private String dinner_people_min;
    private String dinner_people_max;
    private boolean isSelect;
    private String small_routine_photo;

    public String getSmall_routine_photo() {
        return small_routine_photo;
    }

    public void setSmall_routine_photo(String small_routine_photo) {
        this.small_routine_photo = small_routine_photo;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getDinner_people_min() {
        return dinner_people_min;
    }

    public void setDinner_people_min(String dinner_people_min) {
        this.dinner_people_min = dinner_people_min;
    }

    public String getDinner_people_max() {
        return dinner_people_max;
    }

    public void setDinner_people_max(String dinner_people_max) {
        this.dinner_people_max = dinner_people_max;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getShop_id() {
        return shop_id;
    }

    public void setShop_id(int shop_id) {
        this.shop_id = shop_id;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getTable_number() {
        return table_number;
    }

    public void setTable_number(String table_number) {
        this.table_number = table_number;
    }
}
