package com.wbx.merchant.bean;

public class CircleFansMoneyBean {

    /**
     * msg : 成功
     * state : 1
     * data : {"draw_fans_money":3}
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
         * draw_fans_money : 3
         */

        private long draw_fans_money;

        public long getDraw_fans_money() {
            return draw_fans_money;
        }

        public void setDraw_fans_money(long draw_fans_money) {
            this.draw_fans_money = draw_fans_money;
        }
    }
}
