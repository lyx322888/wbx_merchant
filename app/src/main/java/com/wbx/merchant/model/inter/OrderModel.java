package com.wbx.merchant.model.inter;

import com.wbx.merchant.api.OnNetListener;

public interface OrderModel {
    void getOrder(String sj_login_token, OnNetListener onNetListener);
}
