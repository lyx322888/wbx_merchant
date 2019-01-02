package com.wbx.merchant.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wushenghui on 2017/6/29.
 */

public class ShopDetailInfo implements Serializable {

    /**
     * logo : http://www.wbx365.com/attachs/2017/03/13/thumb_58c631bd3b1e3.jpg
     * addr : 同安区同城湾小区内
     * contact : 万丰生鲜老板
     * tel : 13850730359
     * peisong_fanwei :
     * delivery_time : 30
     * business_time : 8:00-19:00
     * details : <p>本店经营 生鲜 &nbsp;海鲜 &nbsp;肉类，等.</p>
     * is_radius : 2
     * since_money : 2500
     * is_print_deliver : 0
     * is_ele_print : 1
     * is_goods_print : 1
     * partner : 9770
     * apiKey : f2cd5380f6e898738b8ecd9f32a699835352705b
     * machine_code : 4004524415
     * mKey : k2ir8hqhvnzn
     * photos : [{"photo":"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1498566592884&di=1ebad3341919fe0dd557afccbf0df411&imgtype=0&"},{"photo":"image&quality=80&size=b9999_10000&sec=1498566592884&di=1ebad3341919fe0dd557afccbf0df411&imgtype=0&"}]
     */
    private String shop_name;
    private String logo;
    private String addr;
    private String contact;
    private String tel;
    private String peisong_fanwei;
    private int delivery_time;
    private String business_time;
    private String details;
    private int is_radius;
    private int is_print_deliver;
    private int is_auto_dada;
    private int is_ele_print;
    private int is_goods_print;
    private String partner;
    private String apiKey;
    private String machine_code;
    private String mKey;
    private List<String> photos;
    private int since_money;
    private int logistics;
    private int every_exceed_kilometre;
    private int exceed_every_kilometre_logistics;
    private String lat;
    private String lng;
    private int is_daofu_pay;

    public int getEvery_exceed_kilometre() {
        return every_exceed_kilometre;
    }

    public void setEvery_exceed_kilometre(int every_exceed_kilometre) {
        this.every_exceed_kilometre = every_exceed_kilometre;
    }

    public int getIs_auto_dada() {
        return is_auto_dada;
    }

    public void setIs_auto_dada(int is_auto_dada) {
        this.is_auto_dada = is_auto_dada;
    }

    public int getExceed_every_kilometre_logistics() {
        return exceed_every_kilometre_logistics;
    }

    public void setExceed_every_kilometre_logistics(int exceed_every_kilometre_logistics) {
        this.exceed_every_kilometre_logistics = exceed_every_kilometre_logistics;
    }

    public int getIs_daofu_pay() {
        return is_daofu_pay;
    }

    public void setIs_daofu_pay(int is_daofu_pay) {
        this.is_daofu_pay = is_daofu_pay;
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

    public int getLogistics() {
        return logistics;
    }

    public void setLogistics(int logistics) {
        this.logistics = logistics;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getPeisong_fanwei() {
        return peisong_fanwei;
    }

    public void setPeisong_fanwei(String peisong_fanwei) {
        this.peisong_fanwei = peisong_fanwei;
    }

    public int getDelivery_time() {
        return delivery_time;
    }

    public void setDelivery_time(int delivery_time) {
        this.delivery_time = delivery_time;
    }

    public String getBusiness_time() {
        return business_time;
    }

    public void setBusiness_time(String business_time) {
        this.business_time = business_time;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public int getIs_radius() {
        return is_radius;
    }

    public void setIs_radius(int is_radius) {
        this.is_radius = is_radius;
    }

    public int getSince_money() {
        return since_money;
    }

    public void setSince_money(int since_money) {
        this.since_money = since_money;
    }

    public int getIs_print_deliver() {
        return is_print_deliver;
    }

    public void setIs_print_deliver(int is_print_deliver) {
        this.is_print_deliver = is_print_deliver;
    }

    public int getIs_ele_print() {
        return is_ele_print;
    }

    public void setIs_ele_print(int is_ele_print) {
        this.is_ele_print = is_ele_print;
    }

    public int getIs_goods_print() {
        return is_goods_print;
    }

    public void setIs_goods_print(int is_goods_print) {
        this.is_goods_print = is_goods_print;
    }

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getMachine_code() {
        return machine_code;
    }

    public void setMachine_code(String machine_code) {
        this.machine_code = machine_code;
    }

    public String getMKey() {
        return mKey;
    }

    public void setMKey(String mKey) {
        this.mKey = mKey;
    }


}
