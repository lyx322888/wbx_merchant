package com.wbx.merchant.api;

import android.content.Intent;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.wbx.merchant.activity.LoginActivity;
import com.wbx.merchant.base.BaseApplication;
import com.wbx.merchant.baseapp.AppConfig;
import com.wbx.merchant.baseapp.AppManager;
import com.wbx.merchant.utils.NetWorkUtils;
import com.wbx.merchant.utils.SPUtils;
import com.wbx.merchant.utils.ToastUitl;
import com.wbx.merchant.widget.LoadingDialog;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by wushenghui on 2017/6/20.
 */

public class MyHttp {
    private int opNum;

    public MyHttp() {
    }

    public void doPost(Observable<JSONObject> observable, final HttpListener listener) {


        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<JSONObject>() {
            @Override
            public void onCompleted() {
                //请求完成
                LoadingDialog.cancelDialogForLoading();
            }

            @Override
            public void onError(Throwable e) {
                Log.e("dfdf", "onError: "+e );
                //请求错误
                LoadingDialog.cancelDialogForLoading();
                if (!NetWorkUtils.isNetConnected(BaseApplication.getInstance())) {
                    //网络
                    listener.onError(AppConfig.ERROR_STATE.NO_NETWORK);
                } else {
                    //服务器
                    listener.onError(AppConfig.ERROR_STATE.SERVICE_ERROR);
                    ToastUitl.showShort("网络错误，请重试！");
                }
            }

            @Override
            public void onNext(JSONObject result) {
                //数据返回
                if (result.getInteger("state") == 1) {
                    if (result.getString("msg").equals("暂无数据")) {
                        //无数据
                        listener.onError(AppConfig.ERROR_STATE.NULLDATA);
                    } else {
                        listener.onSuccess(result);
                    }
                } else {
                    if (result.getString("msg").equals("暂无数据")) {
                        //无数据
                        listener.onError(AppConfig.ERROR_STATE.NULLDATA);
                    } else if (result.getString("msg").equals("请先登陆")) {
                        opNum++;
                        if (opNum == 1) {
                            ToastUitl.showShort("请先登录");
                            SPUtils.setSharedBooleanData(BaseApplication.getInstance(), AppConfig.LOGIN_STATE, false);
                            AppManager.getAppManager().getTopActivity().startActivity(new Intent(AppManager.getAppManager().getTopActivity(), LoginActivity.class));
                            AppManager.getAppManager().finishAllExpectSpecifiedActivity(LoginActivity.class);
                        }
                    } else if (result.getString("msg").equals("未设置支付密码")) {
                        listener.onError(AppConfig.ERROR_STATE.NULL_PAY_PSW);
                    } else if (result.getString("msg").contains("配送金额不足")) {
                        listener.onError(AppConfig.ERROR_STATE.SEND_FEE_NO_ENOUGH);
                    }else if (result.getString("msg").contains("试用店最多能上传10个商品")
                            ||result.getString("msg").contains("试用店无法使用红包功能")
                            ||result.getString("msg").contains("试用店无法使用达达配送")
                            ||result.getString("msg").contains("试用店最多可以管理10个客户")
                            ||result.getString("msg").contains("试用店最多可以发三条商圈")){
                        listener.onError(AppConfig.ERROR_STATE.JURISDICTION);
                    } else {
                        ToastUitl.showShort(result.getString("msg"));
                        listener.onError(result.getInteger("state"));
                    }
                }
            }
        });
    }
}
