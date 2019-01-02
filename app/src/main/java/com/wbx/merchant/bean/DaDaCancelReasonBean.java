package com.wbx.merchant.bean;

/**
 * @author Zero
 * @date 2018/11/12
 */
public class DaDaCancelReasonBean {

    /**
     * reason : 没有配送员接单
     * id : 1
     */

    private String reason;
    private String id;
    private boolean isSelect;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
