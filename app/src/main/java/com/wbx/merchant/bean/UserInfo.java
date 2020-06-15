package com.wbx.merchant.bean;

import java.io.Serializable;

/**
 * Created by wushenghui on 2017/6/20.
 */

public class UserInfo implements Serializable {


    /**
     * sj_login_token : 09f1905a05777171e93476ef28132f83
     * grade_id : 19
     * closed : 0
     * audit : 1
     * end_date : 0
     * shop_id : 966
     */

    private String sj_login_token;
    private int grade_id;
    private int closed;
    private int audit;
    private long end_date;
    private int shop_id;
    private String nickname;
    private String mobile;
    private String face;
    private String hx_username;
    private String hx_password;
    private String shop_name;
    private String shop_grade_name;
    private String shopPhoto;
    private String lat;
    private String lng;
    private int isSubscribe;
    private int has_shop_pact;
    private int has_sign_photo;
    private int shop_grade;
    private int shop_grade_price;
    private int is_new_shop;
    private int scan_order_type;
    private int try_shop;
    private int is_view_withdraw_commission;

    public int getTry_shop() {
        return try_shop;
    }

    public void setTry_shop(int try_shop) {
        this.try_shop = try_shop;
    }


    public int getIs_view_withdraw_commission() {
        return is_view_withdraw_commission;
    }

    public void setIs_view_withdraw_commission(int is_view_withdraw_commission) {
        this.is_view_withdraw_commission = is_view_withdraw_commission;
    }


    public int getScan_order_type() {
        return scan_order_type;
    }

    public void setScan_order_type(int scan_order_type) {
        this.scan_order_type = scan_order_type;
    }

    public String getShop_grade_name() {
        return shop_grade_name;
    }

    public void setShop_grade_name(String shop_grade_name) {
        this.shop_grade_name = shop_grade_name;
    }

    public int getShop_grade_price() {
        return shop_grade_price;
    }

    public void setShop_grade_price(int shop_grade_price) {
        this.shop_grade_price = shop_grade_price;
    }

    public int getHas_shop_pact() {
        return has_shop_pact;
    }

    public void setHas_shop_pact(int has_shop_pact) {
        this.has_shop_pact = has_shop_pact;
    }

    public int getHas_sign_photo() {
        return has_sign_photo;
    }

    public void setHas_sign_photo(int has_sign_photo) {
        this.has_sign_photo = has_sign_photo;
    }

    public int getIs_new_shop() {
        return is_new_shop;
    }

    public void setIs_new_shop(int is_new_shop) {
        this.is_new_shop = is_new_shop;
    }

    public int getShop_grade() {
        return shop_grade;
    }

    public void setShop_grade(int shop_grade) {
        this.shop_grade = shop_grade;
    }

    public int getIsSubscribe() {
        return isSubscribe;
    }

    public void setIsSubscribe(int isSubscribe) {
        this.isSubscribe = isSubscribe;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getShopPhoto() {
        return shopPhoto;
    }

    public void setShopPhoto(String shopPhoto) {
        this.shopPhoto = shopPhoto;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getHx_password() {
        return hx_password;
    }

    public void setHx_password(String hx_password) {
        this.hx_password = hx_password;
    }

    public String getHx_username() {
        return hx_username;
    }

    public void setHx_username(String hx_username) {
        this.hx_username = hx_username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public String getSj_login_token() {
        return sj_login_token;
    }

    public void setSj_login_token(String sj_login_token) {
        this.sj_login_token = sj_login_token;
    }

    public int getGrade_id() {
        return grade_id;
    }

    public void setGrade_id(int grade_id) {
        this.grade_id = grade_id;
    }

    public int getClosed() {
        return closed;
    }

    public void setClosed(int closed) {
        this.closed = closed;
    }

    public int getAudit() {
        return audit;
    }

    public void setAudit(int audit) {
        this.audit = audit;
    }

    public long getEnd_date() {
        return end_date;
    }

    public void setEnd_date(long end_date) {
        this.end_date = end_date;
    }

    public int getShop_id() {
        return shop_id;
    }

    public void setShop_id(int shop_id) {
        this.shop_id = shop_id;
    }
}
