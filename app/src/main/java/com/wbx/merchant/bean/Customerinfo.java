package com.wbx.merchant.bean;

public class Customerinfo {

    /**
     * msg : 成功
     * state : 1
     * data : {"canUpdate":true,"certNum":"","certType":"","certificateCode":"445202199002108316","certificateEndDate":"","certificateName":"王","certificateStartDate":"2020-09-27","certificateType":"IDENTIFICATION","city":"天津","contactPhoneNum":"14460088741","customerNum":"10001116013621423958148","district":"和平区","fullName":"去去去测试","haseAttach":true,"hashMobile":"","industry":"美食","linkMan":"林","linkManId":"445202199002108316","linkPhone":"14460088741","postalAddress":"","province":"天津","shortName":"磊"}
     */

    private String msg;
    private int state;
    private DataBean data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * canUpdate : true
         * certNum :
         * certType :
         * certificateCode : 445202199002108316
         * certificateEndDate :
         * certificateName : 王
         * certificateStartDate : 2020-09-27
         * certificateType : IDENTIFICATION
         * city : 天津
         * contactPhoneNum : 14460088741
         * customerNum : 10001116013621423958148
         * district : 和平区
         * fullName : 去去去测试
         * haseAttach : true
         * hashMobile :
         * industry : 美食
         * linkMan : 林
         * linkManId : 445202199002108316
         * linkPhone : 14460088741
         * postalAddress :
         * province : 天津
         * shortName : 磊
         */

        private boolean canUpdate;
        private String certNum;
        private String certType;
        private String certificateCode;
        private String certificateEndDate;
        private String certificateName;
        private String certificateStartDate;
        private String certificateType;
        private String city;
        private String contactPhoneNum;
        private String customerNum;
        private String district;
        private String fullName;
        private boolean haseAttach;
        private String hashMobile;
        private String industry;
        private String linkMan;
        private String linkManId;
        private String linkPhone;
        private String postalAddress;
        private String province;
        private String shortName;

        public boolean isCanUpdate() {
            return canUpdate;
        }

        public void setCanUpdate(boolean canUpdate) {
            this.canUpdate = canUpdate;
        }

        public String getCertNum() {
            return certNum;
        }

        public void setCertNum(String certNum) {
            this.certNum = certNum;
        }

        public String getCertType() {
            return certType;
        }

        public void setCertType(String certType) {
            this.certType = certType;
        }

        public String getCertificateCode() {
            return certificateCode;
        }

        public void setCertificateCode(String certificateCode) {
            this.certificateCode = certificateCode;
        }

        public String getCertificateEndDate() {
            return certificateEndDate;
        }

        public void setCertificateEndDate(String certificateEndDate) {
            this.certificateEndDate = certificateEndDate;
        }

        public String getCertificateName() {
            return certificateName;
        }

        public void setCertificateName(String certificateName) {
            this.certificateName = certificateName;
        }

        public String getCertificateStartDate() {
            return certificateStartDate;
        }

        public void setCertificateStartDate(String certificateStartDate) {
            this.certificateStartDate = certificateStartDate;
        }

        public String getCertificateType() {
            return certificateType;
        }

        public void setCertificateType(String certificateType) {
            this.certificateType = certificateType;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getContactPhoneNum() {
            return contactPhoneNum;
        }

        public void setContactPhoneNum(String contactPhoneNum) {
            this.contactPhoneNum = contactPhoneNum;
        }

        public String getCustomerNum() {
            return customerNum;
        }

        public void setCustomerNum(String customerNum) {
            this.customerNum = customerNum;
        }

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public boolean isHaseAttach() {
            return haseAttach;
        }

        public void setHaseAttach(boolean haseAttach) {
            this.haseAttach = haseAttach;
        }

        public String getHashMobile() {
            return hashMobile;
        }

        public void setHashMobile(String hashMobile) {
            this.hashMobile = hashMobile;
        }

        public String getIndustry() {
            return industry;
        }

        public void setIndustry(String industry) {
            this.industry = industry;
        }

        public String getLinkMan() {
            return linkMan;
        }

        public void setLinkMan(String linkMan) {
            this.linkMan = linkMan;
        }

        public String getLinkManId() {
            return linkManId;
        }

        public void setLinkManId(String linkManId) {
            this.linkManId = linkManId;
        }

        public String getLinkPhone() {
            return linkPhone;
        }

        public void setLinkPhone(String linkPhone) {
            this.linkPhone = linkPhone;
        }

        public String getPostalAddress() {
            return postalAddress;
        }

        public void setPostalAddress(String postalAddress) {
            this.postalAddress = postalAddress;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getShortName() {
            return shortName;
        }

        public void setShortName(String shortName) {
            this.shortName = shortName;
        }
    }
}
