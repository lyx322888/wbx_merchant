package com.wbx.merchant.bean;

import java.io.Serializable;

/**
 * @author Zero
 * @date 2018/4/11 0011
 */
public class DaDaOrderTrackBean implements Serializable {

    /**
     * dada_status : 1
     * lat : 24.485145
     * lng : 118.196876
     * carrier_driver_name : 邵俊豪
     * carrier_driver_phone : 18792463072
     * create_time : 1509692035
     */

    private int dada_status;
    private double lat;
    private double lng;
    private String dm_name;
    private String dm_mobile;
    private int update_time;

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

    public int getDada_status() {
        return dada_status;
    }

    public void setDada_status(int dada_status) {
        this.dada_status = dada_status;
    }

    public String getDm_name() {
        return dm_name;
    }

    public void setDm_name(String dm_name) {
        this.dm_name = dm_name;
    }

    public String getDm_mobile() {
        return dm_mobile;
    }

    public void setDm_mobile(String dm_mobile) {
        this.dm_mobile = dm_mobile;
    }

    public int getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(int update_time) {
        this.update_time = update_time;
    }
}
