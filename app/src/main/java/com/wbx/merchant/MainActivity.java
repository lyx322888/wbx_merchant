package com.wbx.merchant;

import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.base.BaseApplication;
import com.wbx.merchant.bean.NoticeBean;
import com.wbx.merchant.dialog.NoticeDialog;
import com.wbx.merchant.fragment.IndexFragment;
import com.wbx.merchant.fragment.MineFragment;
import com.wbx.merchant.fragment.UpdateDialogFragment;
import com.wbx.merchant.utils.SPUtils;
import com.wbx.merchant.widget.MoreWindow;
import com.wbx.merchant.widget.TabFragmentHost;

import butterknife.Bind;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {
    @Bind(android.R.id.tabhost)
    TabFragmentHost mTabHost;
    TabWidget mTabWidget;
    private long mExitTime;
    MoreWindow mMoreWindow;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initPresenter() {
    }

    @Override
    public void initView() {
        initTabHost();
        initFragment();
        registerMsgListener();
    }

    private void registerMsgListener() {

    }

    private void initTabHost() {
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        mTabWidget = mTabHost.getTabWidget();
        mTabWidget.setDividerDrawable(null);
        mTabWidget.setStripEnabled(false);
    }

    private void initFragment() {
        initIndexFragment();//首页
        initPublishFragment();//发布
        initMineFragment();//我的
    }

    private void initMineFragment() {
        View mainTabView = getTabItemView(R.drawable.yst_mine_tab_btn_selector, "我的");
        TabHost.TabSpec tabSpec = mTabHost.newTabSpec("mine").setIndicator(mainTabView);
        mTabHost.addTab(tabSpec, MineFragment.class, null);
    }

    private void initPublishFragment() {
        View mainTabView = getTabItemView(R.drawable.yst_mine_tab_btn_selector, "发布产品");
        TabHost.TabSpec tabSpec = mTabHost.newTabSpec("publish").setIndicator(mainTabView);
        mTabHost.addTab(tabSpec, MineFragment.class, null);
    }

    private void initIndexFragment() {
        View mainTabView = getTabItemView(R.drawable.yst_index_tab_btn_selector, "首页");
        TabHost.TabSpec tabSpec = mTabHost.newTabSpec("index").setIndicator(mainTabView);
        mTabHost.addTab(tabSpec, IndexFragment.class, null);
    }

    @Override
    public void fillData() {
        new MyHttp().doPost(Api.getDefault().getVersion(), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                JSONObject data = result.getJSONObject("data");
                int serviceVersion = Integer.parseInt(data.getString("version").replace(".", ""));
                String version = BaseApplication.getInstance().getVersion();
                int appVersion = Integer.parseInt(version.replace(".", ""));
                if (appVersion < serviceVersion) {
                    upDateApp(JSONObject.toJSONString(result));
                } else {
                    getNotice();
                }
            }

            @Override
            public void onError(int code) {
                getNotice();
            }
        });
    }

    private void upDateApp(String jsonStr) {
        UpdateDialogFragment.newInstent(jsonStr).show(getSupportFragmentManager(), "");
    }

    @Override
    public void setListener() {
        mTabWidget.getChildTabViewAt(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMoreWindow(view);
            }
        });
    }

    private void showMoreWindow(View view) {
        mMoreWindow = new MoreWindow(this);
        mMoreWindow.init();
        mMoreWindow.showMoreWindow(view, 100);
    }

    private View getTabItemView(int tabImageId, String tabStr) {
        View view = getLayoutInflater().inflate(
                R.layout.yst_main_tab_item_view, null);
        ImageView imageView = view.findViewById(R.id.tabItemIV);
        TextView textView = view.findViewById(R.id.tab_item_tv);
        textView.setText(tabStr);
        imageView.setImageResource(tabImageId);
        if ("发布产品".equals(tabStr)) {
            imageView.setVisibility(View.INVISIBLE);
        }
        return view;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                showShortToast("再按一次退出程序");
                mExitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @OnClick(R.id.iv_publish)
    public void onViewClicked() {
        showMoreWindow(mTabHost);
    }

    private void getNotice() {
        if (userInfo == null) {
            return;
        }
        new MyHttp().doPost(Api.getDefault().getNotice(userInfo.getSj_login_token()), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                NoticeBean data = result.getObject("data", NoticeBean.class);
                if (data != null && !(userInfo.getShop_id() + ":" + data.getId()).equals(SPUtils.getSharedStringData(mContext, "NoticeId"))) {
                    NoticeDialog.newInstance(data, String.valueOf(userInfo.getShop_id())).show(getSupportFragmentManager(), "");
                }
            }

            @Override
            public void onError(int code) {

            }
        });
    }
}
