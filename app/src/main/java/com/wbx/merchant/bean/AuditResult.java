package com.wbx.merchant.bean;

public class AuditResult {

    /**
     * msg : 成功
     * state : 1
     * data : {"status":"NOTPASS","auditOpinion":"手持身份证照片需法人手持身份证拍摄上传。 3 驳回五次后不予审核；;结算人身份证有效期，录入错误请核对2;商户名称与营业执照不符合，请修改后重新提交； 3 驳回五次后不予审核；;"}
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
         * status : NOTPASS
         * auditOpinion : 手持身份证照片需法人手持身份证拍摄上传。 3 驳回五次后不予审核；;结算人身份证有效期，录入错误请核对2;商户名称与营业执照不符合，请修改后重新提交； 3 驳回五次后不予审核；;
         */

        private String status;
        private String auditOpinion;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getAuditOpinion() {
            return auditOpinion;
        }

        public void setAuditOpinion(String auditOpinion) {
            this.auditOpinion = auditOpinion;
        }
    }
}
