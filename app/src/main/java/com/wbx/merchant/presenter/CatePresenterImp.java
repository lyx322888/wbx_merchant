package com.wbx.merchant.presenter;

import com.wbx.merchant.api.OnNetListener;
import com.wbx.merchant.bean.CateBean;
import com.wbx.merchant.bean.CateInfo;
import com.wbx.merchant.model.CateModelImp;
import com.wbx.merchant.presenter.inter.CatePresenter;
import com.wbx.merchant.view.CateView;

public class CatePresenterImp implements CatePresenter {
    CateView cateView;
    CateModelImp cateModelImp;
    public CatePresenterImp(CateView cateView) {
        this.cateView = cateView;
        cateModelImp=  new CateModelImp();
    }

    @Override
    public void getCate(String login_token,int grade_id) {
        cateModelImp.getCate(login_token,grade_id, new OnNetListener() {
            @Override
            public void onSuccess(Object o) {
                cateView.getCate((CateBean) o);
            }
        });
    }
}
