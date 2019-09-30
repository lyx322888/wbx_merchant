package com.wbx.merchant.bean;

import java.util.ArrayList;
import java.util.List;

public class SalesmanCommentInfo {


    /**
     * msg : 成功
     * state : 1
     * data : {"salesman_headimg":"","salesman_nickname":"15959375882","salesman_phone":"15959375882","comment_rank":5,"comment_text":"扣我努力","superaddition_comment_text":"","superaddition_comment_photo":["http://imgs.wbx365.com/18206062707busine15694694102","http://imgs.wbx365.com/18206062707busine15694694100","http://imgs.wbx365.com/18206062707busine15694694101"],"update_num":2,"comment_photo":["http://imgs.wbx365.com/18206062707busine15694693970","http://imgs.wbx365.com/18206062707busine15694693971","http://imgs.wbx365.com/18206062707busine15694693972"],"question":[{"id":3,"title":"你感觉微百姓怎么样？","question_answer":1},{"id":4,"title":"你感觉业务员怎么样？","question_answer":0}]}
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
         * salesman_headimg :
         * salesman_nickname : 15959375882
         * salesman_phone : 15959375882
         * comment_rank : 5
         * comment_text : 扣我努力
         * superaddition_comment_text :
         * superaddition_comment_photo : ["http://imgs.wbx365.com/18206062707busine15694694102","http://imgs.wbx365.com/18206062707busine15694694100","http://imgs.wbx365.com/18206062707busine15694694101"]
         * update_num : 2
         * comment_photo : ["http://imgs.wbx365.com/18206062707busine15694693970","http://imgs.wbx365.com/18206062707busine15694693971","http://imgs.wbx365.com/18206062707busine15694693972"]
         * question : [{"id":3,"title":"你感觉微百姓怎么样？","question_answer":1},{"id":4,"title":"你感觉业务员怎么样？","question_answer":0}]
         */

        private String salesman_headimg;
        private String salesman_nickname;
        private String salesman_phone;
        private int comment_rank;
        private String comment_text;
        private String superaddition_comment_text;
        private int update_num;
        private ArrayList<String> superaddition_comment_photo;
        private ArrayList<String> comment_photo;
        private List<QuestionBean> question;

        public String getSalesman_headimg() {
            return salesman_headimg;
        }

        public void setSalesman_headimg(String salesman_headimg) {
            this.salesman_headimg = salesman_headimg;
        }

        public String getSalesman_nickname() {
            return salesman_nickname;
        }

        public void setSalesman_nickname(String salesman_nickname) {
            this.salesman_nickname = salesman_nickname;
        }

        public String getSalesman_phone() {
            return salesman_phone;
        }

        public void setSalesman_phone(String salesman_phone) {
            this.salesman_phone = salesman_phone;
        }

        public int getComment_rank() {
            return comment_rank;
        }

        public void setComment_rank(int comment_rank) {
            this.comment_rank = comment_rank;
        }

        public String getComment_text() {
            return comment_text;
        }

        public void setComment_text(String comment_text) {
            this.comment_text = comment_text;
        }

        public String getSuperaddition_comment_text() {
            return superaddition_comment_text;
        }

        public void setSuperaddition_comment_text(String superaddition_comment_text) {
            this.superaddition_comment_text = superaddition_comment_text;
        }

        public int getUpdate_num() {
            return update_num;
        }

        public void setUpdate_num(int update_num) {
            this.update_num = update_num;
        }

        public ArrayList<String> getSuperaddition_comment_photo() {
            return superaddition_comment_photo;
        }

        public void setSuperaddition_comment_photo(ArrayList<String> superaddition_comment_photo) {
            this.superaddition_comment_photo = superaddition_comment_photo;
        }

        public ArrayList<String> getComment_photo() {
            return comment_photo;
        }

        public void setComment_photo(ArrayList<String> comment_photo) {
            this.comment_photo = comment_photo;
        }

        public List<QuestionBean> getQuestion() {
            return question;
        }

        public void setQuestion(List<QuestionBean> question) {
            this.question = question;
        }

        public static class QuestionBean {
            /**
             * id : 3
             * title : 你感觉微百姓怎么样？
             * question_answer : 1
             */

            private int id;
            private String title;
            private int question_answer;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public int getQuestion_answer() {
                return question_answer;
            }

            public void setQuestion_answer(int question_answer) {
                this.question_answer = question_answer;
            }
        }
    }
}
