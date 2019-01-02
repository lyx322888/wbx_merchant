package com.wbx.merchant.bean;

import java.util.ArrayList;

/**
 * @author Zero
 * @date 2018/6/15
 */
public class BusinessCircleBean {

    /**
     * discover_id : 5
     * text : 222
     * photos : ["http://imgs.wbx365.com/13799842085busine15236515330","http://imgs.wbx365.com/lhDNhEG36KYWpX2epC_zDMDqi82T"]
     */

    private int discover_id;
    private String text;
    private ArrayList<String> photos;
    private String share_url;
    private String create_time;

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public int getDiscover_id() {
        return discover_id;
    }

    public void setDiscover_id(int discover_id) {
        this.discover_id = discover_id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ArrayList<String> getPhotos() {
        return photos;
    }

    public void setPhotos(ArrayList<String> photos) {
        this.photos = photos;
    }

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }
}
