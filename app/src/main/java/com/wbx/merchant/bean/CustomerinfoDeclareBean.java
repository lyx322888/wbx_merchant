package com.wbx.merchant.bean;

public class CustomerinfoDeclareBean {
    /**
     * msg : 成功
     * state : 1
     * data : {"accountType":"PRIVATE","bankAccountName":"李亚雄","bankAccountNum":"4367427183540061627","bankBranchId":372026,"bankBranchName":"中国建设银行股份有限公司温岭新河支行","bankName":"建设银行","city":"厦门","customerNum":"10001116011774035055465","hasAttach":true,"phone":"15860088234","privateType":"PERSON","province":"福建","settleAmount":"1.0","settleNum":"10051516011787699029068","settlerCertificateCode":"350521199212053535","settlerCertificateEndDate":"2022-09-27","settlerCertificateStartDate":"1998-09-27","status":"INIT"}
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
         * accountType : PRIVATE
         * bankAccountName : 李亚雄
         * bankAccountNum : 4367427183540061627
         * bankBranchId : 372026
         * bankBranchName : 中国建设银行股份有限公司温岭新河支行
         * bankName : 建设银行
         * city : 厦门
         * customerNum : 10001116011774035055465
         * hasAttach : true
         * phone : 15860088234
         * privateType : PERSON
         * province : 福建
         * settleAmount : 1.0
         * settleNum : 10051516011787699029068
         * settlerCertificateCode : 350521199212053535
         * settlerCertificateEndDate : 2022-09-27
         * settlerCertificateStartDate : 1998-09-27
         * status : INIT
         */

        private String accountType;
        private String bankAccountName;
        private String bankAccountNum;
        private int bankBranchId;
        private String bankBranchName;
        private String bankName;
        private String city;
        private String customerNum;
        private boolean hasAttach;
        private String phone;
        private String privateType;
        private String province;
        private String settleAmount;
        private String settleNum;
        private String settlerCertificateCode;
        private String settlerCertificateEndDate;
        private String settlerCertificateStartDate;
        private String status;

        public String getAccountType() {
            return accountType;
        }

        public void setAccountType(String accountType) {
            this.accountType = accountType;
        }

        public String getBankAccountName() {
            return bankAccountName;
        }

        public void setBankAccountName(String bankAccountName) {
            this.bankAccountName = bankAccountName;
        }

        public String getBankAccountNum() {
            return bankAccountNum;
        }

        public void setBankAccountNum(String bankAccountNum) {
            this.bankAccountNum = bankAccountNum;
        }

        public int getBankBranchId() {
            return bankBranchId;
        }

        public void setBankBranchId(int bankBranchId) {
            this.bankBranchId = bankBranchId;
        }

        public String getBankBranchName() {
            return bankBranchName;
        }

        public void setBankBranchName(String bankBranchName) {
            this.bankBranchName = bankBranchName;
        }

        public String getBankName() {
            return bankName;
        }

        public void setBankName(String bankName) {
            this.bankName = bankName;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCustomerNum() {
            return customerNum;
        }

        public void setCustomerNum(String customerNum) {
            this.customerNum = customerNum;
        }

        public boolean isHasAttach() {
            return hasAttach;
        }

        public void setHasAttach(boolean hasAttach) {
            this.hasAttach = hasAttach;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getPrivateType() {
            return privateType;
        }

        public void setPrivateType(String privateType) {
            this.privateType = privateType;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getSettleAmount() {
            return settleAmount;
        }

        public void setSettleAmount(String settleAmount) {
            this.settleAmount = settleAmount;
        }

        public String getSettleNum() {
            return settleNum;
        }

        public void setSettleNum(String settleNum) {
            this.settleNum = settleNum;
        }

        public String getSettlerCertificateCode() {
            return settlerCertificateCode;
        }

        public void setSettlerCertificateCode(String settlerCertificateCode) {
            this.settlerCertificateCode = settlerCertificateCode;
        }

        public String getSettlerCertificateEndDate() {
            return settlerCertificateEndDate;
        }

        public void setSettlerCertificateEndDate(String settlerCertificateEndDate) {
            this.settlerCertificateEndDate = settlerCertificateEndDate;
        }

        public String getSettlerCertificateStartDate() {
            return settlerCertificateStartDate;
        }

        public void setSettlerCertificateStartDate(String settlerCertificateStartDate) {
            this.settlerCertificateStartDate = settlerCertificateStartDate;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
