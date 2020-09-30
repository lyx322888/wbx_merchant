package com.wbx.merchant.bean;

public class JDDetailBean {

    /**
     * completeTime : 2016-07-12 15:43:13
     * orderAmount : 0.01
     * shopNum : 10001214677149716953054
     * orderNum : 10021014683093339018474
     * refundTime : 2016-07-12 15:43:13
     * requestNum : 12778823432427
     * payWay : WX
     * status : REFUND
     */

    private String completeTime;
    private String orderAmount;
    private String shopNum;
    private String orderNum;
    private String refundTime;
    private String requestNum;
    private String payWay;
    private String status;

    public String getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(String completeTime) {
        this.completeTime = completeTime;
    }

    public String getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(String orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getShopNum() {
        return shopNum;
    }

    public void setShopNum(String shopNum) {
        this.shopNum = shopNum;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getRefundTime() {
        return refundTime;
    }

    public void setRefundTime(String refundTime) {
        this.refundTime = refundTime;
    }

    public String getRequestNum() {
        return requestNum;
    }

    public void setRequestNum(String requestNum) {
        this.requestNum = requestNum;
    }

    public String getPayWay() {
        return payWay;
    }

    public void setPayWay(String payWay) {
        this.payWay = payWay;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
