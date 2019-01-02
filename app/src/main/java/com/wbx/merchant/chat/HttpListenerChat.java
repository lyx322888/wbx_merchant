package com.wbx.merchant.chat;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by wushenghui on 2017/6/20.
 */

public interface HttpListenerChat {
    void onSuccess(JSONObject result);
    void onError(int code);
    void onErrorHasInfo(String msg);
}
