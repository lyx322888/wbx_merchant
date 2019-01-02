package com.wbx.merchant.chat;

import com.alibaba.fastjson.JSONObject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by wushenghui on 2018/1/3.
 */

public class MyHttpChat {
    public static   void doPost(Observable<JSONObject> observable, final HttpListenerChat listener){

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<JSONObject>() {
            @Override
            public void onCompleted() {
            }
            @Override
            public void onError(Throwable e) {

            }
            @Override
            public void onNext(JSONObject result) {
                if(result.getInteger("state")==1){
                    listener.onSuccess(result);
                }else{
                    listener.onErrorHasInfo(result.getString("msg"));
                }
            }
        });
    }
}
