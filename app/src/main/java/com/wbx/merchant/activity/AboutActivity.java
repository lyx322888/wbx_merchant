package com.wbx.merchant.activity;

import android.widget.TextView;

import com.wbx.merchant.R;
import com.wbx.merchant.base.BaseApplication;
import com.wbx.merchant.base.BaseActivity;

import butterknife.Bind;

/**
 * Created by wushenghui on 2017/6/21.
 *  关于我们
 */

public class AboutActivity extends BaseActivity {
    @Bind(R.id.version_tv)
    TextView versionTv;
    @Override
    public int getLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    public void initPresenter() {
//     String.format("%s%d%s","http://www.wbx365.com/wap/ele/shop/shop_id/",123,".html");
    }

    @Override
    public void initView() {

    }

    @Override
    public void fillData() {
        versionTv.setText("V"+ BaseApplication.getInstance().getVersion());
    }

    @Override
    public void setListener() {

    }
}
