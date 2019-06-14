package com.wbx.merchant.model;

import com.wbx.merchant.api.OnNetListener;
import com.wbx.merchant.bean.ProprietaryGoodsBean;
import com.wbx.merchant.model.inter.ProprietaryGoodsModel;
import com.wbx.merchant.utils.RetrofitUtils;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ProprietaryGoodsModelImp implements ProprietaryGoodsModel {
    @Override
    public void getProprietaryGoods(String sj_login_token, int page, int num, final OnNetListener onNetListener) {
        RetrofitUtils.getInstance().server().getProprietaryGoods(sj_login_token, page, num)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<ProprietaryGoodsBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ProprietaryGoodsBean goodsBean) {
                        onNetListener.onSuccess(goodsBean);
                    }
                });
    }
}
