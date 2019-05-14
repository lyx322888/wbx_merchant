package com.wbx.merchant.model;

import com.wbx.merchant.api.OnNetListener;
import com.wbx.merchant.bean.GradeInfoBean;
import com.wbx.merchant.bean.ShopGradeInfo;
import com.wbx.merchant.model.inter.ShopGradeModel;
import com.wbx.merchant.utils.RetrofitUtils;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ShopGradeModelImp implements ShopGradeModel {
    @Override
    public void getGrade(final OnNetListener onNetListener) {
        RetrofitUtils.getInstance().server().getShopGrade()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<GradeInfoBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(GradeInfoBean gradeInfoBean) {
                            onNetListener.onSuccess(gradeInfoBean);

                    }
                });
    }
}
