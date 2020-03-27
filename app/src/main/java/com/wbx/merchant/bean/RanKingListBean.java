package com.wbx.merchant.bean;

import java.util.List;

public class RanKingListBean {
    /**
     * msg : 成功
     * state : 1
     * data : {"user":{"user_id":1012806,"all_share_bounty":0,"nickname":"全村的希望","face":"http://imgs.wbx365.com/jo5j9z88tdr","my_ranking":8},"all_user":[{"user_id":1060842,"all_share_bounty":1000000,"nickname":"18888888880","face":"","merchant_num":0},{"user_id":1049912,"all_share_bounty":890000,"nickname":"15888888888","face":"","merchant_num":8},{"user_id":1048860,"all_share_bounty":110000,"nickname":"淡风","face":"https://wx.qlogo.cn/mmopen/vi_32/DYAIOgq83epvD7gF3wQfnDjOpLeibmgSLXFoZLathvw9vfFxPf1X7U6SVSWl1ibeYF91ndX1TU2X2lZPO20va0Mg/132","merchant_num":16},{"user_id":10796,"all_share_bounty":66000,"nickname":"熊二","face":"http://imgs.wbx365.com/182059554091501656472","merchant_num":8},{"user_id":191,"all_share_bounty":39000,"nickname":"林以贴","face":"http://imgs.wbx365.com/151677008001568635014","merchant_num":2},{"user_id":1061014,"all_share_bounty":27000,"nickname":"18888888812","face":"","merchant_num":4},{"user_id":1061011,"all_share_bounty":24000,"nickname":"18888888810","face":"","merchant_num":2}]}
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
         * user : {"user_id":1012806,"all_share_bounty":0,"nickname":"全村的希望","face":"http://imgs.wbx365.com/jo5j9z88tdr","my_ranking":8}
         * all_user : [{"user_id":1060842,"all_share_bounty":1000000,"nickname":"18888888880","face":"","merchant_num":0},{"user_id":1049912,"all_share_bounty":890000,"nickname":"15888888888","face":"","merchant_num":8},{"user_id":1048860,"all_share_bounty":110000,"nickname":"淡风","face":"https://wx.qlogo.cn/mmopen/vi_32/DYAIOgq83epvD7gF3wQfnDjOpLeibmgSLXFoZLathvw9vfFxPf1X7U6SVSWl1ibeYF91ndX1TU2X2lZPO20va0Mg/132","merchant_num":16},{"user_id":10796,"all_share_bounty":66000,"nickname":"熊二","face":"http://imgs.wbx365.com/182059554091501656472","merchant_num":8},{"user_id":191,"all_share_bounty":39000,"nickname":"林以贴","face":"http://imgs.wbx365.com/151677008001568635014","merchant_num":2},{"user_id":1061014,"all_share_bounty":27000,"nickname":"18888888812","face":"","merchant_num":4},{"user_id":1061011,"all_share_bounty":24000,"nickname":"18888888810","face":"","merchant_num":2}]
         */

        private UserBean user;
        private List<AllUserBean> all_user;

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public List<AllUserBean> getAll_user() {
            return all_user;
        }

        public void setAll_user(List<AllUserBean> all_user) {
            this.all_user = all_user;
        }

        public static class UserBean {
            /**
             * user_id : 1012806
             * all_share_bounty : 0
             * nickname : 全村的希望
             * face : http://imgs.wbx365.com/jo5j9z88tdr
             * my_ranking : 8
             */

            private int user_id;
            private int all_share_bounty;
            private String nickname;
            private String face;
            private String my_ranking;

            public String getMerchant_num() {
                return merchant_num;
            }

            public void setMerchant_num(String merchant_num) {
                this.merchant_num = merchant_num;
            }

            private String merchant_num;

            public int getUser_id() {
                return user_id;
            }

            public void setUser_id(int user_id) {
                this.user_id = user_id;
            }

            public int getAll_share_bounty() {
                return all_share_bounty;
            }

            public void setAll_share_bounty(int all_share_bounty) {
                this.all_share_bounty = all_share_bounty;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getFace() {
                return face;
            }

            public void setFace(String face) {
                this.face = face;
            }

            public String getMy_ranking() {
                return my_ranking;
            }

            public void setMy_ranking(String my_ranking) {
                this.my_ranking = my_ranking;
            }
        }

        public static class AllUserBean {
            /**
             * user_id : 1060842
             * all_share_bounty : 1000000
             * nickname : 18888888880
             * face :
             * merchant_num : 0
             */

            private int user_id;
            private int all_share_bounty;
            private String nickname;
            private String face;
            private String merchant_num;

            public int getUser_id() {
                return user_id;
            }

            public void setUser_id(int user_id) {
                this.user_id = user_id;
            }

            public int getAll_share_bounty() {
                return all_share_bounty;
            }

            public void setAll_share_bounty(int all_share_bounty) {
                this.all_share_bounty = all_share_bounty;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getFace() {
                return face;
            }

            public void setFace(String face) {
                this.face = face;
            }

            public String getMerchant_num() {
                return merchant_num;
            }

            public void setMerchant_num(String merchant_num) {
                this.merchant_num = merchant_num;
            }
        }
    }
}
