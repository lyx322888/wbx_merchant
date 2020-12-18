package com.wbx.merchant.bean;

public class BusinessHoursBaen {

    public BusinessHoursBaen(boolean choice, String title){
        setChoice(choice);
        setTitle(title);
    }
    private boolean choice;

    public boolean getChoice() {
        return choice;
    }

    public void setChoice(boolean choice) {
        this.choice = choice;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private String title;
}
