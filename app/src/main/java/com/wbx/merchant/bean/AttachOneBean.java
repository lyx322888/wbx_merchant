package com.wbx.merchant.bean;

import java.util.List;

public class AttachOneBean {

    /**
     * msg : 成功
     * state : 1
     * data : {"accountType":"PUBLIC","attachList":[{"attachNum":"10001716009337863906005","attachType":"AUTHORIZATION","fileName":"1600933786356.png","url":"http://storage.jd.com/dlbcustomerimage/1600933786356.png?Expires=1600940461&AccessKey=Tha00CNQ0OtTE5vr&Signature=0udW5IU3ioLIdoxGEvIF3XNB0AU%3D"}]}
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
         * accountType : PUBLIC
         * attachList : [{"attachNum":"10001716009337863906005","attachType":"AUTHORIZATION","fileName":"1600933786356.png","url":"http://storage.jd.com/dlbcustomerimage/1600933786356.png?Expires=1600940461&AccessKey=Tha00CNQ0OtTE5vr&Signature=0udW5IU3ioLIdoxGEvIF3XNB0AU%3D"}]
         */

        private String accountType;
        private List<AttachListBean> attachList;

        public String getAccountType() {
            return accountType;
        }

        public void setAccountType(String accountType) {
            this.accountType = accountType;
        }

        public List<AttachListBean> getAttachList() {
            return attachList;
        }

        public void setAttachList(List<AttachListBean> attachList) {
            this.attachList = attachList;
        }

        public static class AttachListBean {
            /**
             * attachNum : 10001716009337863906005
             * attachType : AUTHORIZATION
             * fileName : 1600933786356.png
             * url : http://storage.jd.com/dlbcustomerimage/1600933786356.png?Expires=1600940461&AccessKey=Tha00CNQ0OtTE5vr&Signature=0udW5IU3ioLIdoxGEvIF3XNB0AU%3D
             */

            private String attachNum;
            private String attachType;
            private String fileName;
            private String url;

            public String getAttachNum() {
                return attachNum;
            }

            public void setAttachNum(String attachNum) {
                this.attachNum = attachNum;
            }

            public String getAttachType() {
                return attachType;
            }

            public void setAttachType(String attachType) {
                this.attachType = attachType;
            }

            public String getFileName() {
                return fileName;
            }

            public void setFileName(String fileName) {
                this.fileName = fileName;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }
    }
}
