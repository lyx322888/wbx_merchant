package com.wbx.merchant.model;

import com.wbx.merchant.api.OnNetListener;
import com.wbx.merchant.bean.OpenRadarBean;
import com.wbx.merchant.model.inter.OpenRadarModel;
import com.wbx.merchant.utils.RetrofitUtils;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class OpenRadarModelImp implements OpenRadarModel {
    @Override
    public void getOpenRadar(String login_token, final OnNetListener onNetListener) {
        RetrofitUtils.getInstance().server().getRadar(login_token)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<OpenRadarBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(OpenRadarBean openRadarBean) {
                        if (openRadarBean.getMsg().equals("成功")){
                            onNetListener.onSuccess(openRadarBean);
                        }
                    }
                });
    }
}
