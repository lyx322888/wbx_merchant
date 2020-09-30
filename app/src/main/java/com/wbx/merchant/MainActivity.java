package com.wbx.merchant;

import android.content.Intent;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.wbx.merchant.activity.LoginActivity;
import com.wbx.merchant.activity.OrderActivity;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.baseapp.AppConfig;
import com.wbx.merchant.baseapp.AppManager;
import com.wbx.merchant.bean.CountBean;
import com.wbx.merchant.bean.NeedLoginBean;
import com.wbx.merchant.bean.NoticeBean;
import com.wbx.merchant.bean.UpdataBean;
import com.wbx.merchant.common.LoginUtil;
import com.wbx.merchant.dialog.ConfirmDialog;
import com.wbx.merchant.dialog.NoticeDialog;
import com.wbx.merchant.fragment.HomeFragment;
import com.wbx.merchant.fragment.IndexFragment;
import com.wbx.merchant.fragment.MemberManageFragment;
import com.wbx.merchant.fragment.MineFragment;
import com.wbx.merchant.fragment.MybusinessFragment;
import com.wbx.merchant.fragment.UpdateDialogFragment;
import com.wbx.merchant.utils.APPUtil;
import com.wbx.merchant.utils.SPUtils;
import com.wbx.merchant.widget.LoadingDialog;
import com.wbx.merchant.widget.MoreWindow;
import com.wbx.merchant.widget.TabFragmentHost;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {
    @Bind(android.R.id.tabhost)
    TabFragmentHost mTabHost;
    TabWidget mTabWidget;
    private long mExitTime;
    MoreWindow mMoreWindow;
    private TextView orderNum;

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
        initOrderFragment();//订单
        initPublishFragment();//发布
        initSqFragment();//商圈
        initMineFragment();//客户

    }

    private void initSqFragment() {
        View mainTabView = getTabItemView(R.drawable.yst_fabu_tab_btn_selector, "发商圈");
        TabHost.TabSpec tabSpec = mTabHost.newTabSpec("sq").setIndicator(mainTabView);
        mTabHost.addTab(tabSpec, MybusinessFragment.class, null);
    }

    private void initOrderFragment() {
        View mainTabView = getTabItemView(R.drawable.yst_order_tab_btn_selector, "订单");
        orderNum = mainTabView.findViewById(R.id.tv_scan_order_num);
        TabHost.TabSpec tabSpec = mTabHost.newTabSpec("order").setIndicator(mainTabView);
        mTabHost.addTab(tabSpec, null, null);

    }

    private void initMineFragment() {
        View mainTabView = getTabItemView(R.drawable.yst_mine_tab_btn_selector, "客户");
        TabHost.TabSpec tabSpec = mTabHost.newTabSpec("mine").setIndicator(mainTabView);
        mTabHost.addTab(tabSpec, MemberManageFragment.class, null);
    }

    private void initPublishFragment() {
        View mainTabView = getTabItemView(R.drawable.yst_fabu_tab_btn_selector, "发布产品");
        TabHost.TabSpec tabSpec = mTabHost.newTabSpec("publish").setIndicator(mainTabView);
        mTabHost.addTab(tabSpec, MineFragment.class, null);
    }

    private void initIndexFragment() {
        View mainTabView = getTabItemView(R.drawable.yst_index_tab_btn_selector, "首页");
        TabHost.TabSpec tabSpec = mTabHost.newTabSpec("index").setIndicator(mainTabView);
        mTabHost.addTab(tabSpec, HomeFragment.class, null);
    }

    @Override
    public void fillData() {
            new MyHttp().doPost(Api.getDefault().getVersion(AppConfig.apptype, String.valueOf(APPUtil.getVersionCode(mContext))), new HttpListener() {
                @Override
                public void onSuccess(JSONObject result) {
                    UpdataBean updataBean = new Gson().fromJson(result.toString(),UpdataBean.class);
                    if (updataBean.getData().getIs_update()==1){
                        upDateApp(JSONObject.toJSONString(result));
                    }

                }

                @Override
                public void onError(int code) {
                }
            });

        //判断是否需要重新登录
        new MyHttp().doPost(Api.getDefault().getNeedLogin(LoginUtil.getLoginToken(),String.valueOf(APPUtil.getVersionCode(mContext))), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                final NeedLoginBean needLoginBean = new Gson().fromJson(result.toString(),NeedLoginBean.class);
                if (needLoginBean.getData().getNeed_login()==1){
                    //重新登录适配推送
                        ConfirmDialog confirmDialog = ConfirmDialog.newInstance("我们在推送上做了升级，请您重新登录来获得更加及时的推送！");
                        confirmDialog.setCancelable(false);
                        confirmDialog.show(getSupportFragmentManager(),"");
                        confirmDialog.setDialogListener(new ConfirmDialog.DialogListener() {
                            @Override
                            public void dialogClickListener() {
                                LoadingDialog.showDialogForLoading(mActivity, "退出中...", true);
                                new MyHttp().doPost(Api.getDefault().logout(LoginUtil.getLoginToken(), "android", needLoginBean.getData().getRegistration_id()), new HttpListener() {
                                    @Override
                                    public void onSuccess(JSONObject result) {
                                        SPUtils.setSharedBooleanData(mContext, AppConfig.LOGIN_STATE, false);
                                        SPUtils.setSharedBooleanData(mContext, AppConfig.NO_ASK_AGAIN_ACCREDITATION, false);
                                        AppManager.getAppManager().finishAllActivity();
                                        startActivity(new Intent(mContext, LoginActivity.class));
                                    }

                                    @Override
                                    public void onError(int code) {
                                        SPUtils.setSharedBooleanData(mContext, AppConfig.LOGIN_STATE, false);
                                        SPUtils.setSharedBooleanData(mContext, AppConfig.NO_ASK_AGAIN_ACCREDITATION, false);
                                        AppManager.getAppManager().finishAllActivity();
                                        startActivity(new Intent(mContext, LoginActivity.class));
                                    }
                                });
                            }
                        });
                }
            }

            @Override
            public void onError(int code) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        upDataCornerMark();
    }

    //更新角标
    private void upDataCornerMark(){
        if (!TextUtils.isEmpty(LoginUtil.getLoginToken())){
            HashMap<String, Object> mParams = new HashMap<>();
            mParams.put("sj_login_token", LoginUtil.getLoginToken());
            new MyHttp().doPost(Api.getDefault().getCountOrder(mParams), new HttpListener() {
                @Override
                public void onSuccess(JSONObject result) {
                    CountBean bean = new Gson().fromJson(result.toString(),CountBean.class);
                    int num =bean.getData().getCount_no_shipped_order()+bean.getData().getCount_afhalen_order();
                    if (num==0){
                        orderNum.setVisibility(View.GONE);
                    }else {
                        if (num<=99){
                            orderNum.setText(String.valueOf(num));
                        }else {
                            orderNum.setText("99+");
                        }
                        orderNum.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onError(int code) {

                }
            });
        }else {
            orderNum.setVisibility(View.GONE);
        }
    }

    private void upDateApp(String jsonStr) {
        UpdateDialogFragment.newInstent(jsonStr).show(getSupportFragmentManager(), "");
    }

    @Override
    public void setListener() {
        //发布产品
        mTabWidget.getChildTabViewAt(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMoreWindow(view);
            }
        });

        //订单
        mTabWidget.getChildTabViewAt(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderActivity.actionStart(mContext);
            }
        });
    }

    private void showMoreWindow(View view) {
        mMoreWindow = new MoreWindow(this);
        mMoreWindow.init();
        mMoreWindow.showMoreWindow(view);
    }

    private View getTabItemView(int tabImageId, String tabStr) {
        View view = getLayoutInflater().inflate(R.layout.yst_main_tab_item_view, null);
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
