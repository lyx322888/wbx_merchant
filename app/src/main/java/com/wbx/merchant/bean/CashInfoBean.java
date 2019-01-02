package com.wbx.merchant.bean;

import java.io.Serializable;

/**
 * @author Zero
 * @date 2018/5/24 0024
 */
public class CashInfoBean implements Serializable {

    /**
     * account : 18206062707
     * gold : 72167
     * nickname : 叫一声
     * bank_name :
     * bank_num :
     * bank_branch :
     * bank_realname :
     * shop_name : 呆呆水果店
     * cash_money : 100
     * cash_money_big : 50000
     * shop_cash_commission : 0.7
     * mobile : 18206062707
     * user_alipay : {"withdraw_id":3,"user_id":4901,"alipay_account":"1321325456456","alipay_name":"陈伟文111111","create_time":1510640597,"is_delete":0,"pay_code":"alipay","image":"http://imgs.wbx365.com/aliPay@2x.png","depict":"提现到（1321***)支付宝帐号上"}
     * user_weixinpay : {"withdraw_id":8,"user_id":4901,"weixinpay_account":"","weixinpay_name":"吴大哥","create_time":1511244451,"is_delete":0,"openid":"orh22wktsLPUH2BkndA-UjLToO2A","nick_name":"啊吴","app_style":"sjapi","pay_code":"weixinpay","image":"http://imgs.wbx365.com/weixin@2x.png","depict":"提现到昵称为（啊吴***)微信号上"}
     * user_bank : {"withdraw_id":47,"user_id":4901,"bank_name":"交通银行","bank_num":"6222520791335103","bank_branch":"厦门支行","bank_realname":"张伟柱","create_time":1511160746,"pay_code":"bank","image":"http://imgs.wbx365.com/bank@2x.png","depict":"提现到尾号为（5103)银行卡上"}
     */

    private String account;
    private int gold;
    private String nickname;
    private String bank_name;
    private String bank_num;
    private String bank_branch;
    private String bank_realname;
    private String shop_name;
    private String cash_money;
    private String cash_money_big;
    private String shop_cash_commission;
    private String mobile;
    private UserAlipayBean user_alipay;
    private UserWeixinpayBean user_weixinpay;
    private UserBankBean user_bank;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getBank_num() {
        return bank_num;
    }

    public void setBank_num(String bank_num) {
        this.bank_num = bank_num;
    }

    public String getBank_branch() {
        return bank_branch;
    }

    public void setBank_branch(String bank_branch) {
        this.bank_branch = bank_branch;
    }

    public String getBank_realname() {
        return bank_realname;
    }

    public void setBank_realname(String bank_realname) {
        this.bank_realname = bank_realname;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getCash_money() {
        return cash_money;
    }

    public void setCash_money(String cash_money) {
        this.cash_money = cash_money;
    }

    public String getCash_money_big() {
        return cash_money_big;
    }

    public void setCash_money_big(String cash_money_big) {
        this.cash_money_big = cash_money_big;
    }

    public String getShop_cash_commission() {
        return shop_cash_commission;
    }

    public void setShop_cash_commission(String shop_cash_commission) {
        this.shop_cash_commission = shop_cash_commission;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public UserAlipayBean getUser_alipay() {
        return user_alipay;
    }

    public void setUser_alipay(UserAlipayBean user_alipay) {
        this.user_alipay = user_alipay;
    }

    public UserWeixinpayBean getUser_weixinpay() {
        return user_weixinpay;
    }

    public void setUser_weixinpay(UserWeixinpayBean user_weixinpay) {
        this.user_weixinpay = user_weixinpay;
    }

    public UserBankBean getUser_bank() {
        return user_bank;
    }

    public void setUser_bank(UserBankBean user_bank) {
        this.user_bank = user_bank;
    }

    public static class UserAlipayBean implements Serializable {
        /**
         * withdraw_id : 3
         * user_id : 4901
         * alipay_account : 1321325456456
         * alipay_name : 陈伟文111111
         * create_time : 1510640597
         * is_delete : 0
         * pay_code : alipay
         * image : http://imgs.wbx365.com/aliPay@2x.png
         * depict : 提现到（1321***)支付宝帐号上
         */

        private int withdraw_id;
        private int user_id;
        private String alipay_account;
        private String alipay_name;
        private int create_time;
        private int is_delete;
        private String pay_code;
        private String image;
        private String depict;

        public int getWithdraw_id() {
            return withdraw_id;
        }

        public void setWithdraw_id(int withdraw_id) {
            this.withdraw_id = withdraw_id;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public String getAlipay_account() {
            return alipay_account;
        }

        public void setAlipay_account(String alipay_account) {
            this.alipay_account = alipay_account;
        }

        public String getAlipay_name() {
            return alipay_name;
        }

        public void setAlipay_name(String alipay_name) {
            this.alipay_name = alipay_name;
        }

        public int getCreate_time() {
            return create_time;
        }

        public void setCreate_time(int create_time) {
            this.create_time = create_time;
        }

        public int getIs_delete() {
            return is_delete;
        }

        public void setIs_delete(int is_delete) {
            this.is_delete = is_delete;
        }

        public String getPay_code() {
            return pay_code;
        }

        public void setPay_code(String pay_code) {
            this.pay_code = pay_code;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getDepict() {
            return depict;
        }

        public void setDepict(String depict) {
            this.depict = depict;
        }
    }

    public static class UserWeixinpayBean implements Serializable {
        /**
         * withdraw_id : 8
         * user_id : 4901
         * weixinpay_account :
         * weixinpay_name : 吴大哥
         * create_time : 1511244451
         * is_delete : 0
         * openid : orh22wktsLPUH2BkndA-UjLToO2A
         * nick_name : 啊吴
         * app_style : sjapi
         * pay_code : weixinpay
         * image : http://imgs.wbx365.com/weixin@2x.png
         * depict : 提现到昵称为（啊吴***)微信号上
         */

        private int withdraw_id;
        private int user_id;
        private String weixinpay_account;
        private String weixinpay_name;
        private int create_time;
        private int is_delete;
        private String openid;
        private String nick_name;
        private String app_style;
        private String pay_code;
        private String image;
        private String depict;

        public int getWithdraw_id() {
            return withdraw_id;
        }

        public void setWithdraw_id(int withdraw_id) {
            this.withdraw_id = withdraw_id;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public String getWeixinpay_account() {
            return weixinpay_account;
        }

        public void setWeixinpay_account(String weixinpay_account) {
            this.weixinpay_account = weixinpay_account;
        }

        public String getWeixinpay_name() {
            return weixinpay_name;
        }

        public void setWeixinpay_name(String weixinpay_name) {
            this.weixinpay_name = weixinpay_name;
        }

        public int getCreate_time() {
            return create_time;
        }

        public void setCreate_time(int create_time) {
            this.create_time = create_time;
        }

        public int getIs_delete() {
            return is_delete;
        }

        public void setIs_delete(int is_delete) {
            this.is_delete = is_delete;
        }

        public String getOpenid() {
            return openid;
        }

        public void setOpenid(String openid) {
            this.openid = openid;
        }

        public String getNick_name() {
            return nick_name;
        }

        public void setNick_name(String nick_name) {
            this.nick_name = nick_name;
        }

        public String getApp_style() {
            return app_style;
        }

        public void setApp_style(String app_style) {
            this.app_style = app_style;
        }

        public String getPay_code() {
            return pay_code;
        }

        public void setPay_code(String pay_code) {
            this.pay_code = pay_code;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getDepict() {
            return depict;
        }

        public void setDepict(String depict) {
            this.depict = depict;
        }
    }

    public static class UserBankBean implements Serializable {
        /**
         * withdraw_id : 47
         * user_id : 4901
         * bank_name : 交通银行
         * bank_num : 6222520791335103
         * bank_branch : 厦门支行
         * bank_realname : 张伟柱
         * create_time : 1511160746
         * pay_code : bank
         * image : http://imgs.wbx365.com/bank@2x.png
         * depict : 提现到尾号为（5103)银行卡上
         */

        private int withdraw_id;
        private int user_id;
        private String bank_name;
        private String bank_num;
        private String bank_branch;
        private String bank_realname;
        private int create_time;
        private String pay_code;
        private String image;
        private String depict;

        public int getWithdraw_id() {
            return withdraw_id;
        }

        public void setWithdraw_id(int withdraw_id) {
            this.withdraw_id = withdraw_id;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public String getBank_name() {
            return bank_name;
        }

        public void setBank_name(String bank_name) {
            this.bank_name = bank_name;
        }

        public String getBank_num() {
            return bank_num;
        }

        public void setBank_num(String bank_num) {
            this.bank_num = bank_num;
        }

        public String getBank_branch() {
            return bank_branch;
        }

        public void setBank_branch(String bank_branch) {
            this.bank_branch = bank_branch;
        }

        public String getBank_realname() {
            return bank_realname;
        }

        public void setBank_realname(String bank_realname) {
            this.bank_realname = bank_realname;
        }

        public int getCreate_time() {
            return create_time;
        }

        public void setCreate_time(int create_time) {
            this.create_time = create_time;
        }

        public String getPay_code() {
            return pay_code;
        }

        public void setPay_code(String pay_code) {
            this.pay_code = pay_code;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getDepict() {
            return depict;
        }

        public void setDepict(String depict) {
            this.depict = depict;
        }
    }
}
