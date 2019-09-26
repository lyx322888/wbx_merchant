package com.wbx.merchant.widget;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;


import com.wbx.merchant.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

//配送和自提的选择按钮
public class SelectorCommentButton extends LinearLayout {
    //问题答案 -1未选择 0否 1是
    public static final int NO_CHOICE = -1;
    public static final int CHOICE_ONE = 1;
    public static final int CHOICE_TOW = 0;

    public int answerQuestionType;
    //Retention 是元注解，简单地讲就是系统提供的，用于定义注解的“注解”
    @Retention(RetentionPolicy.SOURCE)
    //这里指定int的取值只能是以下范围
    @IntDef({NO_CHOICE, CHOICE_ONE, CHOICE_TOW})
    @interface SelectorTYPE {
    }

    @Bind(R.id.selector_one)
    CheckedTextView selectorOne;
    @Bind(R.id.selector_tow)
    CheckedTextView selectorTow;
    private final View view;
    private SelectorListen selectorListen;

    public SelectorCommentButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        view = LayoutInflater.from(context).inflate(R.layout.selector_button_layout, this);
        ButterKnife.bind(this, view);
    }

    //根据当前选择项改变按钮背景颜色
    private void setSelectionUi(int answerQuestionType) {
        switch (answerQuestionType){
            case NO_CHOICE:
                selectorOne.setChecked(false);
                selectorOne.setTextColor(ContextCompat.getColor(getContext(), R.color.no_selector_text_color));
                selectorTow.setChecked(false);
                selectorTow.setTextColor(ContextCompat.getColor(getContext(), R.color.no_selector_text_color));
                break;
            case CHOICE_ONE:
                selectorOne.setChecked(true);
                selectorOne.setTextColor(ContextCompat.getColor(getContext(), R.color.selector_text_color));
                selectorTow.setChecked(false);
                selectorTow.setTextColor(ContextCompat.getColor(getContext(), R.color.no_selector_text_color));
                break;
            case CHOICE_TOW:
                selectorOne.setChecked(false);
                selectorOne.setTextColor(ContextCompat.getColor(getContext(), R.color.no_selector_text_color));
                selectorTow.setChecked(true);
                selectorTow.setTextColor(ContextCompat.getColor(getContext(), R.color.selector_text_color));
                break;
        }

    }

    //返回answerQuestionType
    public int getAnswerQuestionType() {
        return answerQuestionType;
    }

    @OnClick({R.id.selector_one, R.id.selector_tow})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.selector_one:
                answerQuestionType = CHOICE_ONE;
                setSelectionUi(CHOICE_ONE);
                if (selectorListen != null) {
                    selectorListen.onSelector(answerQuestionType);
                }
                break;
            case R.id.selector_tow:
                answerQuestionType = CHOICE_TOW;
                setSelectionUi(CHOICE_TOW);
                if (selectorListen != null) {
                    selectorListen.onSelector(answerQuestionType);
                }
                break;
        }
    }

    //设置
    public void setSelection(@SelectorTYPE  int answerQuestionType) {
        this.answerQuestionType = answerQuestionType;
        setSelectionUi(answerQuestionType);
    }


    //监听
    public void setOnselectorListen(SelectorListen selectorListen) {
        this.selectorListen = selectorListen;
    }

    public interface SelectorListen {
        public void onSelector(@SelectorTYPE int answerQuestionType);
    }
}
