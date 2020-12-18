package com.wbx.merchant.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.wbx.merchant.R;


public class AddNumView extends LinearLayout {
    private TextView tvMinus;
    private TextView tvContent;
    private TextView tvAdd;


    private ClickListener clickListener;

    private int num = 1;

    private int minNum = 1;

    private int maxNum = 99999;


    private View view;

    public AddNumView(Context context) {
        super(context);
    }

    public AddNumView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        view = LayoutInflater.from(context).inflate(R.layout.view_add_num, this);
        init();
    }

    private void init() {
        tvMinus = view.findViewById(R.id.tv_minus);
        tvContent = view.findViewById(R.id.tv_content);
        tvAdd = view.findViewById(R.id.tv_add);
        tvContent.setText(String.valueOf(num));

        tvMinus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (num-1<minNum){
                    if (clickListener!=null){
                        clickListener.reachedMinNum();
                    }
                }else {
                    num-=1;
                }

                if (clickListener!=null){
                    clickListener.minusClickListener();
                }
            }
        });

        tvAdd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (num+1>maxNum){
                    if (clickListener!=null){
                        clickListener.reachedMaXNum();
                    }
                }else {
                    num+=1;
                }
                if (clickListener!=null){
                    clickListener.addClickListener();
                }
            }
        });
    }

    public void setNum(int num) {
        this.num = num;
        tvContent.setText(String.valueOf(num));
    }

    public int getNum() {
        return num;
    }

    //设置最小数
    public void setMinNum(int minNum) {
        this.minNum = minNum;
    }

    public void setMaxNum(int maxNum) {
        this.maxNum = maxNum;
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public static abstract class ClickListener {
        public  void minusClickListener(){};
        public  void addClickListener(){};
        //达到最小
        public abstract void reachedMinNum();
        public  void reachedMaXNum(){};
    }

}
