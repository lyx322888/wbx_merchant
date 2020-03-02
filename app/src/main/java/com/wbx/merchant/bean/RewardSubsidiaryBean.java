package com.wbx.merchant.bean;

import java.util.List;

public class RewardSubsidiaryBean {
    /**
     * msg : 成功
     * state : 1
     * data : {"merchant_invitation_log":[{"id":1,"is_paid":0,"bounty":0,"invitee_user_id":5249,"create_time":0,"face":"http://wx.qlogo.cn/mmopen/uGibN1kvGKKlYtoR6iaEKvHfCicqYDkGHZh9LU9AkNN4QT7K8ppUTjf5ox9j3rQPpia8dic0SIRjwBQ3pJEKuia9tC5hWyYtkJgjQ9/0","nickname":"老猪高黑土猪肉专卖店"}],"share_bounty":0}
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
         * merchant_invitation_log : [{"id":1,"is_paid":0,"bounty":0,"invitee_user_id":5249,"create_time":0,"face":"http://wx.qlogo.cn/mmopen/uGibN1kvGKKlYtoR6iaEKvHfCicqYDkGHZh9LU9AkNN4QT7K8ppUTjf5ox9j3rQPpia8dic0SIRjwBQ3pJEKuia9tC5hWyYtkJgjQ9/0","nickname":"老猪高黑土猪肉专卖店"}]
         * share_bounty : 0
         */

        private int share_bounty;
        private List<MerchantInvitationLogBean> merchant_invitation_log;

        public int getShare_bounty() {
            return share_bounty;
        }

        public void setShare_bounty(int share_bounty) {
            this.share_bounty = share_bounty;
        }

        public List<MerchantInvitationLogBean> getMerchant_invitation_log() {
            return merchant_invitation_log;
        }

        public void setMerchant_invitation_log(List<MerchantInvitationLogBean> merchant_invitation_log) {
            this.merchant_invitation_log = merchant_invitation_log;
        }

        public static class MerchantInvitationLogBean {
            /**
             * id : 1
             * is_paid : 0
             * bounty : 0
             * invitee_user_id : 5249
             * create_time : 0
             * face : http://wx.qlogo.cn/mmopen/uGibN1kvGKKlYtoR6iaEKvHfCicqYDkGHZh9LU9AkNN4QT7K8ppUTjf5ox9j3rQPpia8dic0SIRjwBQ3pJEKuia9tC5hWyYtkJgjQ9/0
             * nickname : 老猪高黑土猪肉专卖店
             */

            private int id;
            private int is_paid;
            private int bounty;
            private int invitee_user_id;
            private String create_time;
            private String face;
            private String nickname;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getIs_paid() {
                return is_paid;
            }

            public void setIs_paid(int is_paid) {
                this.is_paid = is_paid;
            }

            public int getBounty() {
                return bounty;
            }

            public void setBounty(int bounty) {
                this.bounty = bounty;
            }

            public int getInvitee_user_id() {
                return invitee_user_id;
            }

            public void setInvitee_user_id(int invitee_user_id) {
                this.invitee_user_id = invitee_user_id;
            }

            public String getCreate_time() {
                return create_time;
            }

            public void setCreate_time(String create_time) {
                this.create_time = create_time;
            }

            public String getFace() {
                return face;
            }

            public void setFace(String face) {
                this.face = face;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }
        }
    }
}
