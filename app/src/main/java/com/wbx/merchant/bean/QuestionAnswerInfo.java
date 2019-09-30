package com.wbx.merchant.bean;

public class QuestionAnswerInfo {

    /**
     * id : 1
     * question_answer : 1
     */
    public QuestionAnswerInfo(int id, int question_answer) {
        this.id = id;
        this.question_answer = question_answer;
    }


    private int id;
    private int question_answer;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuestion_answer() {
        return question_answer;
    }

    public void setQuestion_answer(int question_answer) {
        this.question_answer = question_answer;
    }
}
