package com.wbx.merchant.model;

import com.alibaba.fastjson.JSONObject;
import com.wbx.merchant.api.OnNetListener;
import com.wbx.merchant.bean.GoodsDetailsInfo;
import com.wbx.merchant.model.inter.GoodsDetailsModel;
import com.wbx.merchant.utils.RetrofitUtils;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class GoodsDetailsModelImp implements GoodsDetailsModel {
    @Override
    public void getGoodsDetails(String sj_login_token, int goods_id, final OnNetListener onNetListener) {
        RetrofitUtils.getInstance().server().getGoodsDetails(sj_login_token,goods_id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<GoodsDetailsInfo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(GoodsDetailsInfo goodsInfo) {
                        onNetListener.onSuccess(goodsInfo);
                    }
                });
    }
}
