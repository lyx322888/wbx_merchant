package com.wbx.merchant.presenter;

import com.wbx.merchant.api.OnNetListener;
import com.wbx.merchant.bean.GoodsDetailsInfo;
import com.wbx.merchant.model.GoodsDetailsModelImp;
import com.wbx.merchant.presenter.inter.GoodsDetailsPresenter;
import com.wbx.merchant.view.GoodsDetailsView;

public class GoodsDetailsPresenterImp implements GoodsDetailsPresenter {

    GoodsDetailsView goodsDetailsView;
    GoodsDetailsModelImp goodsDetailsModelImp;

    public GoodsDetailsPresenterImp(GoodsDetailsView goodsDetailsView) {
        this.goodsDetailsView = goodsDetailsView;
        goodsDetailsModelImp=new GoodsDetailsModelImp();
    }

    @Override
    public void getGoodsDetails(String sj_login_token, int goods_id) {
        goodsDetailsModelImp.getGoodsDetails(sj_login_token, goods_id, new OnNetListener() {
            @Override
            public void onSuccess(Object o) {
                goodsDetailsView.getGoodsDetails((GoodsDetailsInfo) o);
            }
        });
    }
}
