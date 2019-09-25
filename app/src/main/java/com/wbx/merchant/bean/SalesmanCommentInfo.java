package com.wbx.merchant.bean;

import java.util.List;

public class SalesmanCommentInfo {
    /**
     * msg : 成功
     * state : 1
     * data : {"salesman_headimg":"wbx365web.com/attachs/2016/12/21/thumb_585a1b71b4d4b.png","salesman_nickname":"锦绣新天地","salesman_phone":"","comment_rank":0,"comment_text":"","comment_photo":[],"question":[{"id":1,"title":"您觉得业务员态度怎么样","question_answer":-1},{"id":2,"title":"您觉得为什么中国没有超人","question_answer":-1}]}
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
         * salesman_headimg : wbx365web.com/attachs/2016/12/21/thumb_585a1b71b4d4b.png
         * salesman_nickname : 锦绣新天地
         * salesman_phone :
         * comment_rank : 0
         * comment_text :
         * comment_photo : []
         * question : [{"id":1,"title":"您觉得业务员态度怎么样","question_answer":-1},{"id":2,"title":"您觉得为什么中国没有超人","question_answer":-1}]
         */

        private String salesman_headimg;
        private String salesman_nickname;
        private String salesman_phone;
        private int comment_rank;
        private String comment_text;
        private List<String> comment_photo;
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

        public List<?> getComment_photo() {
            return comment_photo;
        }

        public void setComment_photo(List<String> comment_photo) {
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
             * id : 1
             * title : 您觉得业务员态度怎么样
             * question_answer : -1
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
