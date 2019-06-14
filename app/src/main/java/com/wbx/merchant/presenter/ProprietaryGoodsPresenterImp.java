package com.wbx.merchant.presenter;

import com.wbx.merchant.api.OnNetListener;
import com.wbx.merchant.bean.ProprietaryGoodsBean;
import com.wbx.merchant.model.ProprietaryGoodsModelImp;
import com.wbx.merchant.presenter.inter.ProprietaryGoodsPresenter;
import com.wbx.merchant.view.ProprietaryGoodsView;

public class ProprietaryGoodsPresenterImp implements ProprietaryGoodsPresenter {
    ProprietaryGoodsView proprietaryGoodsView;
    ProprietaryGoodsModelImp proprietaryGoodsModelImp;

    public ProprietaryGoodsPresenterImp(ProprietaryGoodsView proprietaryGoodsView) {
        this.proprietaryGoodsView = proprietaryGoodsView;
        proprietaryGoodsModelImp = new ProprietaryGoodsModelImp();
    }

    @Override
    public void getProprietaryGoods(String sj_login_token, int page, int num) {
        proprietaryGoodsModelImp.getProprietaryGoods(sj_login_token, page, num, new OnNetListener() {
            @Override
            public void onSuccess(Object o) {
                proprietaryGoodsView.getProprietaryGoods((ProprietaryGoodsBean) o);
            }
        });
    }
}
