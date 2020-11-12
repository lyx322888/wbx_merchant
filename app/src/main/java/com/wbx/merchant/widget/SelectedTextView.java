package com.wbx.merchant.widget;

import android.content.Context;
import android.util.AttributeSet;

import androidx.core.content.ContextCompat;

import com.wbx.merchant.R;

public class SelectedTextView extends androidx.appcompat.widget.AppCompatTextView {
    boolean isSelect = true;
    public SelectedTextView(Context context) {
        super(context);
    }

    public SelectedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }



    public SelectedTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void setSelect(boolean select) {
        isSelect = select;
        if (isSelect){
            setBackground(ContextCompat.getDrawable(getContext(), R.drawable.tv_selected));
            setTextColor(ContextCompat.getColor(getContext(),R.color.white));
        }else {
            setBackground(ContextCompat.getDrawable(getContext(), R.drawable.tv_unselect));
            setTextColor(ContextCompat.getColor(getContext(),R.color.gray));
        }
    }
}
