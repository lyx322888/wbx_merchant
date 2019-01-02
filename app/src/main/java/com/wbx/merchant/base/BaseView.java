package com.wbx.merchant.base;

/**
 * des:baseview
 * Created by wushenghui on 2017/4/23.
 */
public interface BaseView {
    /*******内嵌加载*******/
    void showLoading(String title);
    void stopLoading();
    void showErrorTip(String msg);
}
