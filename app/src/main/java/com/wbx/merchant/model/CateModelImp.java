package com.wbx.merchant.model;

import com.wbx.merchant.api.OnNetListener;
import com.wbx.merchant.bean.CateBean;
import com.wbx.merchant.bean.CateInfo;
import com.wbx.merchant.model.inter.CateModel;
import com.wbx.merchant.utils.RetrofitUtils;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CateModelImp implements CateModel {
    @Override
    public void getCate(String login_token,int grade_id, final OnNetListener onNetListener) {
        RetrofitUtils.getInstance().server().getShopCate(login_token,grade_id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<CateBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(CateBean cateBean) {
                        onNetListener.onSuccess(cateBean);
                    }
                });
    }
}
