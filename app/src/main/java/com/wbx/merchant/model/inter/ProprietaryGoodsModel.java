package com.wbx.merchant.model.inter;

import com.wbx.merchant.api.OnNetListener;

public interface ProprietaryGoodsModel {
    void getProprietaryGoods(String sj_login_token, int page, int num, OnNetListener onNetListener);
}
