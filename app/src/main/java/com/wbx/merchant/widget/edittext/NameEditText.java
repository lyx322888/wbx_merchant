package com.wbx.merchant.widget.edittext;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.AttributeSet;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NameEditText extends AppCompatEditText {
    public NameEditText(Context context) {
        super(context);
        init();
    }

    public NameEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NameEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        InputFilter inputFilter = new InputFilter() {

            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                String specChar = "[-`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#¥%……&*（）——+|{}【】‘；：”“’。，、？\n]";
                Pattern pattern = Pattern.compile(specChar);
                Matcher matcher = pattern.matcher(source.toString());
                if (matcher.find()) {
                    return "";
                }
                return null;
            }
        };
        setFilters(new InputFilter[]{inputFilter, new InputFilter.LengthFilter(10)});
    }
}
