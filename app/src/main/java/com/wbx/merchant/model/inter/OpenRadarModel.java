package com.wbx.merchant.model.inter;

import com.wbx.merchant.api.OnNetListener;

public interface OpenRadarModel {
    void getOpenRadar(String login_token, OnNetListener onNetListener);
}
