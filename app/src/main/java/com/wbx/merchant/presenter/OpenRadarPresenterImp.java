package com.wbx.merchant.presenter;

import com.wbx.merchant.api.OnNetListener;
import com.wbx.merchant.bean.OpenRadarBean;
import com.wbx.merchant.model.OpenRadarModelImp;
import com.wbx.merchant.presenter.inter.OpenRadarPresenter;
import com.wbx.merchant.view.OpenRadarView;

public class OpenRadarPresenterImp implements OpenRadarPresenter {
    OpenRadarView openRadarView;
    OpenRadarModelImp openRadarModelImp;

    public OpenRadarPresenterImp(OpenRadarView openRadarView) {
        this.openRadarView = openRadarView;
        openRadarModelImp = new OpenRadarModelImp();
    }

    @Override
    public void getOpenRadar(String login_token,int page,int num) {
        openRadarModelImp.getOpenRadar(login_token,page,num, new OnNetListener() {
            @Override
            public void onSuccess(Object o) {
                openRadarView.getOpenRadar((OpenRadarBean) o);
            }
        });
    }
}
