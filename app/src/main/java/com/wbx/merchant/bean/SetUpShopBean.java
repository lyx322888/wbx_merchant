package com.wbx.merchant.bean;

import java.util.List;

public class SetUpShopBean {
    /**
     * msg : 成功
     * state : 1
     * data : {"current_task":{"checkpoint":1,"bounty":12000,"reward_items":"微百姓","reward_items_photo":"http://www.wbx365.com/static/default/qiye/picture/wbxhq.jpg"},"my_bounty":0,"incentive_plan":[{"checkpoint":1,"bounty":12000,"reward_items":"微百姓","reward_items_photo":"http://www.wbx365.com/static/default/qiye/picture/wbxhq.jpg","status":2},{"checkpoint":2,"bounty":15000,"reward_items":"","reward_items_photo":"","status":3}],"merchant_invitation_log":[],"user_id":11816}
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
         * current_task : {"checkpoint":1,"bounty":12000,"reward_items":"微百姓","reward_items_photo":"http://www.wbx365.com/static/default/qiye/picture/wbxhq.jpg"}
         * my_bounty : 0
         * incentive_plan : [{"checkpoint":1,"bounty":12000,"reward_items":"微百姓","reward_items_photo":"http://www.wbx365.com/static/default/qiye/picture/wbxhq.jpg","status":2},{"checkpoint":2,"bounty":15000,"reward_items":"","reward_items_photo":"","status":3}]
         * merchant_invitation_log : []
         * user_id : 11816
         */

        private CurrentTaskBean current_task;
        private int my_bounty;
        private String user_id;
        private int share_bounty;
        private List<IncentivePlanBean> incentive_plan;

        public CurrentTaskBean getCurrent_task() {
            return current_task;
        }

        public void setCurrent_task(CurrentTaskBean current_task) {
            this.current_task = current_task;
        }

        public int getMy_bounty() {
            return my_bounty;
        }

        public void setMy_bounty(int my_bounty) {
            this.my_bounty = my_bounty;
        }

        public String getUser_id() {
            return user_id;
        }
        public int getShare_bounty() {
            return share_bounty;
        }

        public void setShare_bounty(int share_bounty) {
            this.share_bounty = share_bounty;
        }
        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public List<IncentivePlanBean> getIncentive_plan() {
            return incentive_plan;
        }

        public void setIncentive_plan(List<IncentivePlanBean> incentive_plan) {
            this.incentive_plan = incentive_plan;
        }



        public static class CurrentTaskBean {
            /**
             * checkpoint : 1
             * bounty : 12000
             * reward_items : 微百姓
             * reward_items_photo : http://www.wbx365.com/static/default/qiye/picture/wbxhq.jpg
             */

            private int checkpoint;
            private int bounty;
            private String reward_items;
            private String reward_items_photo;

            public int getCheckpoint() {
                return checkpoint;
            }

            public void setCheckpoint(int checkpoint) {
                this.checkpoint = checkpoint;
            }

            public int getBounty() {
                return bounty;
            }

            public void setBounty(int bounty) {
                this.bounty = bounty;
            }

            public String getReward_items() {
                return reward_items;
            }

            public void setReward_items(String reward_items) {
                this.reward_items = reward_items;
            }

            public String getReward_items_photo() {
                return reward_items_photo;
            }

            public void setReward_items_photo(String reward_items_photo) {
                this.reward_items_photo = reward_items_photo;
            }
        }

        public static class IncentivePlanBean {
            /**
             * checkpoint : 1
             * bounty : 12000
             * reward_items : 微百姓
             * reward_items_photo : http://www.wbx365.com/static/default/qiye/picture/wbxhq.jpg
             * status : 2
             */

            private int checkpoint;
            private int bounty;
            private String reward_items;
            private String reward_items_photo;
            private int status;

            public int getCheckpoint() {
                return checkpoint;
            }

            public void setCheckpoint(int checkpoint) {
                this.checkpoint = checkpoint;
            }

            public int getBounty() {
                return bounty;
            }

            public void setBounty(int bounty) {
                this.bounty = bounty;
            }

            public String getReward_items() {
                return reward_items;
            }

            public void setReward_items(String reward_items) {
                this.reward_items = reward_items;
            }

            public String getReward_items_photo() {
                return reward_items_photo;
            }

            public void setReward_items_photo(String reward_items_photo) {
                this.reward_items_photo = reward_items_photo;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }
        }
    }
}
