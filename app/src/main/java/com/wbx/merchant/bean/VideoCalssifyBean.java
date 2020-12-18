package com.wbx.merchant.bean;

import java.util.List;

public class VideoCalssifyBean {

    /**
     * msg : 成功
     * state : 1
     * data : [{"video_promotion_classify_id":1,"name":"好吃","junior":[{"video_promotion_classify_id":3,"name":"火锅"},{"video_promotion_classify_id":4,"name":"饮料"}]},{"video_promotion_classify_id":2,"name":"好逛","junior":[{"video_promotion_classify_id":5,"name":"电影"},{"video_promotion_classify_id":6,"name":"景点"}]}]
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
         * video_promotion_classify_id : 1
         * name : 好吃
         * junior : [{"video_promotion_classify_id":3,"name":"火锅"},{"video_promotion_classify_id":4,"name":"饮料"}]
         */

        private int video_promotion_classify_id;
        private String name;
        private List<JuniorBean> junior;

        public int getVideo_promotion_classify_id() {
            return video_promotion_classify_id;
        }

        public void setVideo_promotion_classify_id(int video_promotion_classify_id) {
            this.video_promotion_classify_id = video_promotion_classify_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<JuniorBean> getJunior() {
            return junior;
        }

        public void setJunior(List<JuniorBean> junior) {
            this.junior = junior;
        }

        public static class JuniorBean {
            /**
             * video_promotion_classify_id : 3
             * name : 火锅
             */

            private String video_promotion_classify_id;
            private String name;

            public String getVideo_promotion_classify_id() {
                return video_promotion_classify_id;
            }

            public void setVideo_promotion_classify_id(String video_promotion_classify_id) {
                this.video_promotion_classify_id = video_promotion_classify_id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }
}
