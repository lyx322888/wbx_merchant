package com.wbx.merchant.bean;

import java.io.Serializable;

/**
 * @author Zero
 * @date 2018/4/11 0011
 */
public class FengNiaoOrderTrackBean implements Serializable {

    /**
     * fengniao_status : 1
     * lat : 24.485145
     * lng : 118.196876
     * carrier_driver_name : 邵俊豪
     * carrier_driver_phone : 18792463072
     * create_time : 1509692035
     */

    private int fengniao_status;
    private double lat;
    private double lng;
    private String carrier_driver_name;
    private String carrier_driver_phone;
    private int create_time;

    public int getFengniao_status() {
        return fengniao_status;
    }

    public void setFengniao_status(int fengniao_status) {
        this.fengniao_status = fengniao_status;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getCarrier_driver_name() {
        return carrier_driver_name;
    }

    public void setCarrier_driver_name(String carrier_driver_name) {
        this.carrier_driver_name = carrier_driver_name;
    }

    public String getCarrier_driver_phone() {
        return carrier_driver_phone;
    }

    public void setCarrier_driver_phone(String carrier_driver_phone) {
        this.carrier_driver_phone = carrier_driver_phone;
    }

    public int getCreate_time() {
        return create_time;
    }

    public void setCreate_time(int create_time) {
        this.create_time = create_time;
    }
}
