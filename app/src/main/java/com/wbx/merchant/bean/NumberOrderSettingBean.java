package com.wbx.merchant.bean;

/**
 * @author Zero
 * @date 2018/8/16
 */
public class NumberOrderSettingBean {

    /**
     * is_take_number : 0
     * take_number_photo : http://img5.imgtn.bdimg.com/it/u=959180627,3212676048&fm=27&gp=0.jpg
     * grade_id : 20
     * shop_id : 1423
     * photo : http://imgs.wbx365.com/18159661581_1517906310.jpg
     * take_number_start : 0
     * take_number_prefix :
     */

    private int is_take_number;
    private String take_number_photo;
    private int grade_id;
    private int shop_id;
    private String photo;
    private String take_number_start;
    private String take_number_prefix;

    public int getIs_take_number() {
        return is_take_number;
    }

    public void setIs_take_number(int is_take_number) {
        this.is_take_number = is_take_number;
    }

    public String getTake_number_photo() {
        return take_number_photo;
    }

    public void setTake_number_photo(String take_number_photo) {
        this.take_number_photo = take_number_photo;
    }

    public int getGrade_id() {
        return grade_id;
    }

    public void setGrade_id(int grade_id) {
        this.grade_id = grade_id;
    }

    public int getShop_id() {
        return shop_id;
    }

    public void setShop_id(int shop_id) {
        this.shop_id = shop_id;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getTake_number_start() {
        return take_number_start;
    }

    public void setTake_number_start(String take_number_start) {
        this.take_number_start = take_number_start;
    }

    public String getTake_number_prefix() {
        return take_number_prefix;
    }

    public void setTake_number_prefix(String take_number_prefix) {
        this.take_number_prefix = take_number_prefix;
    }
}
