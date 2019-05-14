package com.wbx.merchant.model.inter;

import com.wbx.merchant.api.OnNetListener;

public interface CateModel {
    void getCate(String login_token,int grade_id, OnNetListener onNetListener);
}
