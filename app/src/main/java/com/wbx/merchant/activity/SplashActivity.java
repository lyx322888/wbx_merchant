package com.wbx.merchant.activity;

import android.content.Intent;
import android.os.Handler;

import com.wbx.merchant.MainActivity;
import com.wbx.merchant.R;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.base.BaseApplication;
import com.wbx.merchant.baseapp.AppConfig;
import com.wbx.merchant.utils.SPUtils;

/**
 * Created by wushenghui on 2017/6/20.
 */

public class SplashActivity extends BaseActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        long showTime = 2000 - (System.currentTimeMillis() - BaseApplication.getInstance().getAppInitTime());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (SPUtils.getSharedBooleanData(mContext, AppConfig.LOGIN_STATE)) {
                    startActivity(new Intent(mContext, MainActivity.class));
                } else {
                    startActivity(new Intent(mContext, LoginActivity.class));
                }
                finish();
            }
        }, showTime < 0 ? 2000 : showTime);//温启动时application没有重新创建，showTime会为负值
    }

    @Override
    public void fillData() {

    }

    @Override
    public void setListener() {

    }
}
