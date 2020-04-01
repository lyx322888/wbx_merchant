package com.wbx.merchant.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;


import com.wbx.merchant.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

//配送和自提的选择按钮
public class SelectorButton extends LinearLayout {
    @Bind(R.id.selector_one)
    CheckedTextView selectorOne;
    @Bind(R.id.selector_tow)
    CheckedTextView selectorTow;
    //当前选择第一个
    private boolean isSelectionOne = true;
    private final View view;
    private SelectorListen selectorListen;

    public SelectorButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        view = LayoutInflater.from(context).inflate(R.layout.selector_rz_button_layout, this);
        ButterKnife.bind(this, view);
    }
    //根据当前选择项改变按钮背景颜色
    private void setSelectionUi(boolean isSelectionOne){
        if (isSelectionOne){
            selectorOne.setChecked(true);
            selectorOne.setTextColor(ContextCompat.getColor(getContext(),R.color.white));
            selectorTow.setChecked(false);
            selectorTow.setTextColor(ContextCompat.getColor(getContext(),R.color.app_color));
        }else {
            selectorOne.setChecked(false);
            selectorOne.setTextColor(ContextCompat.getColor(getContext(),R.color.app_color));
            selectorTow.setChecked(true);
            selectorTow.setTextColor(ContextCompat.getColor(getContext(),R.color.white));
        }
    }
    //返回isSelectionOne
    public boolean getSelectionOne(){
        return isSelectionOne;
    }
    @OnClick({R.id.selector_one, R.id.selector_tow})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.selector_one:
                isSelectionOne = true;
                setSelectionUi(isSelectionOne);
                if (selectorListen!=null){
                    selectorListen.onSelector(isSelectionOne);
                }
                break;
            case R.id.selector_tow:
                isSelectionOne = false;
                setSelectionUi(isSelectionOne);
                if (selectorListen!=null){
                    selectorListen.onSelector(isSelectionOne);
                }
                break;
        }
    }
    //设置
    public void setSelection(boolean isSelectionOne){
        this.isSelectionOne = isSelectionOne;
        setSelectionUi(isSelectionOne);
    }
    //监听
    public void setOnselectorListen(SelectorListen selectorListen){
        this.selectorListen = selectorListen;
    }

    public interface SelectorListen{
        public void onSelector(boolean isSelectionOne);
    }
}
