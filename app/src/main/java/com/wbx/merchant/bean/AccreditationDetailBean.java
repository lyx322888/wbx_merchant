package com.wbx.merchant.bean;

import java.io.Serializable;

/**
 * @author Zero
 * @date 2018/6/6 0006
 */
public class AccreditationDetailBean implements Serializable {

    /**
     * photo : http://imgs.wbx365.com/FsEJJ0lRNRwKkkNC6cjZ5FQMUrcT
     * hygiene_photo : http://imgs.wbx365.com/Fs_9OUn_D6RaI-dqrKiFg6KdmEtH
     * has_hygiene_photo : 1
     * name : 科技
     * zhucehao : 了
     * addr : 哦
     * end_date : 2018-05-25
     * audit : 0
     */

    private String photo;
    private String hygiene_photo;
    private int has_hygiene_photo;
    private String name;
    private String zhucehao;
    private String addr;
    private String end_date;
    private int audit;

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getZhucehao() {
        return zhucehao;
    }

    public void setZhucehao(String zhucehao) {
        this.zhucehao = zhucehao;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public int getAudit() {
        return audit;
    }

    public void setAudit(int audit) {
        this.audit = audit;
    }
}
