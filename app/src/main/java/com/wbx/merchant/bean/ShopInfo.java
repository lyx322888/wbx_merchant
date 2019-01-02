package com.wbx.merchant.bean;

import java.io.Serializable;

/**
 * Created by wushenghui on 2017/6/24.
 */

public class ShopInfo implements Serializable {

    /**
     * cate_id : 104
     * user_id : 683
     * photo : http://www.wbx365.com/attachs/2017/03/13/thumb_58c631bb3ba15.jpg
     * grade_id : 15
     * shop_name : 万丰生鲜店
     * is_renzheng : 1
     * gold : 4100
     * money_day : 0
     * money_yesterday : 0
     * cate_name : 蔬菜
     * new_order_num : 0
     * is_add_audit : 1
     * audit : 1
     */

    private int cate_id;
    private int user_id;
    private String photo;
    private int grade_id;
    private String shop_name;
    private int is_renzheng;
    private int gold;
    private int money_day;
    private int money_yesterday;
    private String cate_name;
    private int new_order_num;
    private int goods_num;
    private int is_add_audit;
    private int audit;
    private int is_open;
    private int shop_id;
    private int end_date;
    private String notice;
    private int is_subscribe;
    private int is_dispatching_money_activity;
    private String small_routine_photo;
    private String hygiene_photo;
    private int has_hygiene_photo;
    private int scan_order_type;
    private int dtk_order_num;
    private int take_number_no_read_num;
    private int later_subscribe_order_num;
    private int unfinished_scan_order_num;

    public int getIs_dispatching_money_activity() {
        return is_dispatching_money_activity;
    }

    public void setIs_dispatching_money_activity(int is_dispatching_money_activity) {
        this.is_dispatching_money_activity = is_dispatching_money_activity;
    }

    public int getDtk_order_num() {
        return dtk_order_num;
    }

    public void setDtk_order_num(int dtk_order_num) {
        this.dtk_order_num = dtk_order_num;
    }

    public int getTake_number_no_read_num() {
        return take_number_no_read_num;
    }

    public void setTake_number_no_read_num(int take_number_no_read_num) {
        this.take_number_no_read_num = take_number_no_read_num;
    }

    public int getLater_subscribe_order_num() {
        return later_subscribe_order_num;
    }

    public void setLater_subscribe_order_num(int later_subscribe_order_num) {
        this.later_subscribe_order_num = later_subscribe_order_num;
    }

    public int getUnfinished_scan_order_num() {
        return unfinished_scan_order_num;
    }

    public void setUnfinished_scan_order_num(int unfinished_scan_order_num) {
        this.unfinished_scan_order_num = unfinished_scan_order_num;
    }

    public int getScan_order_type() {
        return scan_order_type;
    }

    public void setScan_order_type(int scan_order_type) {
        this.scan_order_type = scan_order_type;
    }

    public int getGoods_num() {
        return goods_num;
    }

    public void setGoods_num(int goods_num) {
        this.goods_num = goods_num;
    }

    public String getHygiene_photo() {
        return hygiene_photo;
    }

    public void setHygiene_photo(String hygiene_photo) {
        this.hygiene_photo = hygiene_photo;
    }

    public int getHas_hygiene_photo() {
        return has_hygiene_photo;
    }

    public void setHas_hygiene_photo(int has_hygiene_photo) {
        this.has_hygiene_photo = has_hygiene_photo;
    }

    public int getIs_subscribe() {
        return is_subscribe;
    }

    public void setIs_subscribe(int is_subscribe) {
        this.is_subscribe = is_subscribe;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public int getEnd_date() {
        return end_date;
    }

    public void setEnd_date(int end_date) {
        this.end_date = end_date;
    }

    public int getShop_id() {
        return shop_id;
    }

    public void setShop_id(int shop_id) {
        this.shop_id = shop_id;
    }

    public int getIs_open() {
        return is_open;
    }

    public void setIs_open(int is_open) {
        this.is_open = is_open;
    }

    public int getCate_id() {
        return cate_id;
    }

    public void setCate_id(int cate_id) {
        this.cate_id = cate_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getGrade_id() {
        return grade_id;
    }

    public void setGrade_id(int grade_id) {
        this.grade_id = grade_id;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public int getIs_renzheng() {
        return is_renzheng;
    }

    public void setIs_renzheng(int is_renzheng) {
        this.is_renzheng = is_renzheng;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public int getMoney_day() {
        return money_day;
    }

    public void setMoney_day(int money_day) {
        this.money_day = money_day;
    }

    public int getMoney_yesterday() {
        return money_yesterday;
    }

    public void setMoney_yesterday(int money_yesterday) {
        this.money_yesterday = money_yesterday;
    }

    public String getCate_name() {
        return cate_name;
    }

    public void setCate_name(String cate_name) {
        this.cate_name = cate_name;
    }

    public int getNew_order_num() {
        return new_order_num;
    }

    public void setNew_order_num(int new_order_num) {
        this.new_order_num = new_order_num;
    }

    public int getIs_add_audit() {
        return is_add_audit;
    }

    public void setIs_add_audit(int is_add_audit) {
        this.is_add_audit = is_add_audit;
    }

    public int getAudit() {
        return audit;
    }

    public void setAudit(int audit) {
        this.audit = audit;
    }

    public String getSmall_routine_photo() {
        return small_routine_photo;
    }

    public void setSmall_routine_photo(String small_routine_photo) {
        this.small_routine_photo = small_routine_photo;
    }
}
