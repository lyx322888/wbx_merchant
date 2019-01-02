package com.wbx.merchant.common;

import com.wbx.merchant.base.BaseApplication;

/**
 * @author Zero
 * @date 2018/11/12
 */
public class LoginUtil {
    public static String getLoginToken() {
        return BaseApplication.getInstance().readLoginUser().getSj_login_token();
    }
}
