package com.wbx.merchant.bean;

import java.util.List;

public class FollowersDrainageBean {

    /**
     * msg : 成功
     * state : 1
     * data : {"city_name":"海口","sex":0,"distance":"","age_group_type":0,"scope_type":1,"hobby":[{"hobby_id":4,"name":"音乐","is_select":1},{"hobby_id":3,"name":"健身","is_select":1},{"hobby_id":2,"name":"旅游","is_select":1},{"hobby_id":1,"name":"运动","is_select":1}]}
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
         * city_name : 海口
         * sex : 0
         * distance :
         * age_group_type : 0
         * scope_type : 1
         * hobby : [{"hobby_id":4,"name":"音乐","is_select":1},{"hobby_id":3,"name":"健身","is_select":1},{"hobby_id":2,"name":"旅游","is_select":1},{"hobby_id":1,"name":"运动","is_select":1}]
         */

        private String city_name;
        private int sex;
        private String distance;
        private int age_group_type;
        private int scope_type;
        private List<HobbyBean> hobby;

        public String getCity_name() {
            return city_name;
        }

        public void setCity_name(String city_name) {
            this.city_name = city_name;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public int getAge_group_type() {
            return age_group_type;
        }

        public void setAge_group_type(int age_group_type) {
            this.age_group_type = age_group_type;
        }

        public int getScope_type() {
            return scope_type;
        }

        public void setScope_type(int scope_type) {
            this.scope_type = scope_type;
        }

        public List<HobbyBean> getHobby() {
            return hobby;
        }

        public void setHobby(List<HobbyBean> hobby) {
            this.hobby = hobby;
        }

        public static class HobbyBean {
            /**
             * hobby_id : 4
             * name : 音乐
             * is_select : 1
             */

            private String hobby_id;
            private String name;
            private int is_select;

            public String getHobby_id() {
                return hobby_id;
            }

            public void setHobby_id(String hobby_id) {
                this.hobby_id = hobby_id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getIs_select() {
                return is_select;
            }

            public void setIs_select(int is_select) {
                this.is_select = is_select;
            }
        }
    }
}
