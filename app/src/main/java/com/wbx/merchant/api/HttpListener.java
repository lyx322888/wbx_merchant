package com.wbx.merchant.api;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by wushenghui on 2017/6/20.
 */

public interface HttpListener {
    void onSuccess(JSONObject result);

    void onError(int code);
}
