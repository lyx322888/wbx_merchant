package com.wbx.merchant.utils;

import android.widget.EditText;

/**
 * @author Zero
 * @date 2018/7/25
 */
public class PriceUtil {

    public static boolean isCorrectPrice(EditText editText, CharSequence charSequence) {
        //判断第一位输入是否是“.”
        if (charSequence.toString().startsWith(".")) {
            charSequence = "0" + charSequence;
            editText.setText(charSequence);
            if (charSequence.toString().length() == 2) {
                editText.setSelection(charSequence.length());
            }
            return false;
        }
        //判断首位是否是“0”
        if (charSequence.toString().startsWith("0") && charSequence.toString().length() > 1) {
            //判断第二位不是“.”
            if (!charSequence.toString().substring(1, 2).equals(".")) {

                charSequence = charSequence.toString().substring(1, charSequence.toString().length());
                editText.setText(charSequence);
                editText.setSelection(charSequence.length());
                return false;
            }
        }

        //判断小数点后两位
        if (charSequence.toString().contains(".")) {
            if (charSequence.length() - 1 - charSequence.toString().indexOf(".") > 2) {
                charSequence = charSequence.toString().subSequence(0, charSequence.toString().indexOf(".") + 3);
                editText.setText(charSequence);
                editText.setSelection(charSequence.length());
                return false;
            }
        }
        return true;
    }
}
