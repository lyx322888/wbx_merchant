package com.wbx.merchant.widget;

import android.content.Context;
import androidx.appcompat.widget.AppCompatEditText;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;

/**
 * Created by wushenghui on 2018/1/26.
 * 只允许输入小数点
 */

public class PriceEditText extends AppCompatEditText {
    /**
     * 小数点前的价格长度
     * 123456789.00 价格长度为 9
     */
    private final int PRICE_LENGTH = 9;
    private CharSequence defaultHint;
    private String hint = "输入数据过大，请重新输入";

    public PriceEditText(Context context) {
        super(context);
        init();
    }

    public PriceEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PriceEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        /**
         * 设置输入类型：数字和小数点
         */
        setInputType(EditorInfo.TYPE_CLASS_NUMBER | EditorInfo.TYPE_NUMBER_FLAG_DECIMAL);
        setMaxLines(1);
        setFilters(new InputFilter[]{new InputFilter.LengthFilter(PRICE_LENGTH + 3)});
        defaultHint = getHint();

        addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //判断第一位输入是否是“.”
                if (s.toString().startsWith(".")) {
                    s = "0" + s;
                    setText(s);
                    setSelection(s.length());
                    return;
                }
                //判断首位是否是“0”
                if (s.toString().startsWith("0") && s.toString().length() > 1) {
                    //判断第二位不是“.”
                    if (!s.toString().substring(1, 2).equals(".")) {
                        s = s.toString().substring(1);
                        setText(s);
                        return;
                    }
                }
                //判断小数点后两位
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0, s.toString().indexOf(".") + 3);
                        setText(s);
                        setSelection(s.length());
                        return;
                    }
                }
                //判断输入位数
                if (s.toString().length() > PRICE_LENGTH) {
                    if (s.toString().contains(".")) {
                        if (s.toString().indexOf(".") > PRICE_LENGTH) {
                            setText("");
                            setHint(hint);
                            setSelection(0);
                            return;
                        }
                    } else if (s.toString().length() == PRICE_LENGTH + 1) {
                        if (start + 1 == s.toString().length()) {
                            s = s.toString().subSequence(0, PRICE_LENGTH);
                            setText(s);
                            setSelection(s.length());
                            return;
                        } else {
                            setText("");
                            setHint(hint);
                            setSelection(0);
                            return;
                        }
                    } else if (s.toString().length() > PRICE_LENGTH + 1) {
                        setText("");
                        setHint(hint);
                        setSelection(0);
                        return;
                    }
                }

                //还原提示内容
                if (defaultHint != getHint()) {
                    setHint(defaultHint);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}