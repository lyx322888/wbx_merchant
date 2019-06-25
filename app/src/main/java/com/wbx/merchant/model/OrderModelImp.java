package com.wbx.merchant.model;

import com.wbx.merchant.api.OnNetListener;
import com.wbx.merchant.bean.GoodsDetailsInfo;
import com.wbx.merchant.bean.OrderBean;
import com.wbx.merchant.model.inter.OrderModel;
import com.wbx.merchant.utils.RetrofitUtils;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class OrderModelImp implements OrderModel {
    @Override
    public void getOrder(String sj_login_token, final OnNetListener onNetListener) {
        RetrofitUtils.getInstance().server().getOeder(sj_login_token)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<OrderBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(OrderBean orderBean) {
                        if (orderBean!=null){
                            onNetListener.onSuccess(orderBean);
                        }
                    }
                });
    }
}
