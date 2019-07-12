package com.wbx.merchant.presenter;

import com.wbx.merchant.api.OnNetListener;
import com.wbx.merchant.bean.OrderBean;
import com.wbx.merchant.model.OrderModelImp;
import com.wbx.merchant.presenter.inter.OrderPresenter;
import com.wbx.merchant.view.OrderView;

public class OrderPresenterImp implements OrderPresenter {
    OrderView orderView;
    OrderModelImp orderModelImp;

    public OrderPresenterImp(OrderView orderView) {
        this.orderView = orderView;
        orderModelImp = new OrderModelImp();
    }

    @Override
    public void getOrder(String sj_login_token) {
        orderModelImp.getOrder(sj_login_token, new OnNetListener() {
            @Override
            public void onSuccess(Object o) {
                orderView.getOrder((OrderBean) o);
            }
        });
    }
}
