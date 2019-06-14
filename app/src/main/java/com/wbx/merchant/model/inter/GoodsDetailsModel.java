package com.wbx.merchant.model.inter;

import com.wbx.merchant.api.OnNetListener;

public interface GoodsDetailsModel {
    void getGoodsDetails(String sj_login_token,int goods_id,OnNetListener onNetListener);
}
