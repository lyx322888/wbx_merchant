package com.wbx.merchant.chat;

import java.io.Serializable;

/**
 * Created by wushenghui on 2018/1/3.
 */

public class QuestionInfo implements Serializable {
    private int question_id;
    private String title;

    public int getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(int question_id) {
        this.question_id = question_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
