package com.wbx.merchant.common;

import android.content.Intent;
import android.text.TextUtils;

import com.wbx.merchant.activity.LoginActivity;
import com.wbx.merchant.base.BaseApplication;
import com.wbx.merchant.baseapp.AppConfig;
import com.wbx.merchant.baseapp.AppManager;
import com.wbx.merchant.bean.UserInfo;
import com.wbx.merchant.utils.SPUtils;
import com.wbx.merchant.utils.ToastUitl;

/**
 * @author Zero
 * @date 2018/11/12
 */
public class LoginUtil {
    //获取token
    public static String getLoginToken() {
        UserInfo userInfo = BaseApplication.getInstance().readLoginUser();
        if (userInfo!=null){
          return  userInfo. getSj_login_token();
        }else {
            return null;
        }
    }

    //获取token 当token或者UserInfo不存在时 重新登录
    public static String getLoginTokenRegister() {
        UserInfo userInfo = BaseApplication.getInstance().readLoginUser();
        if (userInfo==null||TextUtils.isEmpty(userInfo.getSj_login_token())){
                ToastUitl.showShort("请重新登录");
                SPUtils.setSharedBooleanData(BaseApplication.getInstance(), AppConfig.LOGIN_STATE, false);
                AppManager.getAppManager().getTopActivity().startActivity(new Intent(AppManager.getAppManager().getTopActivity(), LoginActivity.class));
                AppManager.getAppManager().finishAllExpectSpecifiedActivity(LoginActivity.class);
                return null;
        }else {
            return  userInfo. getSj_login_token();
        }
    }
}
