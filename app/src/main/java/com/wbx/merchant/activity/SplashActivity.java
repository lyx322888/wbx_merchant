package com.wbx.merchant.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;

import com.wbx.merchant.MainActivity;
import com.wbx.merchant.R;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.base.BaseApplication;
import com.wbx.merchant.baseapp.AppConfig;
import com.wbx.merchant.common.PermissioinSettingPage;
import com.wbx.merchant.utils.SPUtils;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;


/**
 * Created by wushenghui on 2017/6/20.
 */
@RuntimePermissions
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
                SplashActivityPermissionsDispatcher.showMainContentWithPermissionCheck(SplashActivity.this);
            }
        }, showTime < 0 ? 2000 : showTime);//温启动时application没有重新创建，showTime会为负值
    }

    @Override
    public void fillData() {

    }

    @Override
    public void setListener() {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        SplashActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_PHONE_STATE, Manifest.permission.CALL_PHONE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void showMainContent() {
        if (SPUtils.getSharedBooleanData(mContext, AppConfig.LOGIN_STATE)) {
            startActivity(new Intent(mContext, MainActivity.class));
        } else {
            startActivity(new Intent(mContext, LoginActivity.class));
        }
        finish();
    }

    @OnShowRationale({Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_PHONE_STATE, Manifest.permission.CALL_PHONE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void showPermissionDialog(final PermissionRequest request) {
        new AlertDialog.Builder(this, R.style.SplashDialog).setTitle("提示").setMessage("请授予相关权限，否则微百姓无法正常工作")
                .setCancelable(false).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                request.cancel();
                finish();
            }
        }).setPositiveButton("授权", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                request.proceed();
            }
        }).create().show();
    }

    @OnPermissionDenied({Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_PHONE_STATE, Manifest.permission.CALL_PHONE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void permissionDeny() {
        SplashActivityPermissionsDispatcher.showMainContentWithPermissionCheck(this);
    }

    @OnNeverAskAgain({Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_PHONE_STATE, Manifest.permission.CALL_PHONE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void permissionNeverAskAgain() {
        new AlertDialog.Builder(this, R.style.SplashDialog).setTitle("提示").setMessage("为保证软件的正常工作，请在设置中授予相关权限")
                .setCancelable(false).setNegativeButton("暂不", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        }).setPositiveButton("去设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                PermissioinSettingPage.start(SplashActivity.this, true);
            }
        }).create().show();
    }
}
