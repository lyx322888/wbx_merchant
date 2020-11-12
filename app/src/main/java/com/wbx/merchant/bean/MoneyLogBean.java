package com.wbx.merchant.bean;

import java.util.List;

public class MoneyLogBean {

    /**
     * msg : 成功
     * state : 1
     * data : [{"money_log_id":6,"change_status":1,"create_time":1602483364,"money":1,"remark":"圈粉金额充值"}]
     */

    private String msg;
    private int state;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * money_log_id : 6
         * change_status : 1
         * create_time : 1602483364
         * money : 1
         * remark : 圈粉金额充值
         */

        private int money_log_id;
        private int change_status;
        private String create_time;
        private long money;
        private String remark;

        public int getMoney_log_id() {
            return money_log_id;
        }

        public void setMoney_log_id(int money_log_id) {
            this.money_log_id = money_log_id;
        }

        public int getChange_status() {
            return change_status;
        }

        public void setChange_status(int change_status) {
            this.change_status = change_status;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public long getMoney() {
            return money;
        }

        public void setMoney(long money) {
            this.money = money;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }
    }
}
