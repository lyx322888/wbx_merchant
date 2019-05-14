package com.wbx.merchant.presenter;

import com.wbx.merchant.api.OnNetListener;
import com.wbx.merchant.bean.GradeInfoBean;
import com.wbx.merchant.bean.ShopGradeInfo;
import com.wbx.merchant.model.ShopGradeModelImp;
import com.wbx.merchant.presenter.inter.ShopGradePresenter;
import com.wbx.merchant.view.ShopGradeView;

public class ShopGradePresenterImp implements ShopGradePresenter {

    ShopGradeView shopGradeView;
    ShopGradeModelImp shopGradeModelImp;

    public ShopGradePresenterImp(ShopGradeView shopGradeView) {
        this.shopGradeView = shopGradeView;
        shopGradeModelImp = new ShopGradeModelImp();
    }

    @Override
    public void getGrade() {
        shopGradeModelImp.getGrade(new OnNetListener() {
            @Override
            public void onSuccess(Object o) {
                shopGradeView.getGrade((GradeInfoBean) o);
            }
        });
    }
}
