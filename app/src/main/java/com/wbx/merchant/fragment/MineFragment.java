package com.wbx.merchant.fragment;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.wbx.merchant.R;
import com.wbx.merchant.activity.AboutActivity;
import com.wbx.merchant.activity.BindAccountActivity;
import com.wbx.merchant.activity.LoginActivity;
import com.wbx.merchant.activity.ModifyPswActivity;
import com.wbx.merchant.activity.MyBusinessCircleActivity;
import com.wbx.merchant.activity.PayActivity;
import com.wbx.merchant.activity.ReceiverListActivity;
import com.wbx.merchant.activity.ResetPayPswActivity;
import com.wbx.merchant.activity.WebActivity;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseFragment;
import com.wbx.merchant.baseapp.AppConfig;
import com.wbx.merchant.baseapp.AppManager;
import com.wbx.merchant.utils.SPUtils;
import com.wbx.merchant.utils.ToastUitl;
import com.wbx.merchant.widget.LoadingDialog;
import com.wbx.merchant.widget.iosdialog.AlertDialog;

import butterknife.Bind;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by wushenghui on 2017/6/20.
 */

public class MineFragment extends BaseFragment {

    @Bind(R.id.ll_title)
    LinearLayout llTitle;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_mine;
    }

    @Override
    public void initPresenter() {
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void fillData() {
    }

    @Override
    protected void bindEven() {
    }

    public void hiddenTitle() {
        llTitle.setVisibility(View.GONE);
    }

    @OnClick({R.id.ll_modify_password, R.id.ll_bind_account, R.id.ll_set_pay_password, R.id.ll_printer, R.id.ll_renewal, R.id.ll_my_business_circle, R.id.ll_about, R.id.ll_evaluate, R.id.ll_help_center, R.id.ll_logout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_modify_password:
                startActivity(new Intent(getActivity(), ModifyPswActivity.class));
                break;
            case R.id.ll_bind_account:
                startActivity(new Intent(getActivity(), BindAccountActivity.class));
                break;
            case R.id.ll_set_pay_password:
                startActivity(new Intent(getActivity(), ResetPayPswActivity.class));
                break;
            case R.id.ll_printer:
                startActivity(new Intent(getActivity(), ReceiverListActivity.class));
//                startActivity(new Intent(getActivity(), BluetoothActivity.class));
                break;
            case R.id.ll_renewal:
                Intent intent = new Intent(getActivity(), PayActivity.class);
                intent.putExtra("gradeId", loginUser.getGrade_id());
                intent.putExtra("isRenew", true);
                startActivity(intent);
                break;
            case R.id.ll_my_business_circle:
                MyBusinessCircleActivity.actionStart(getActivity());
                break;
            case R.id.ll_about:
                startActivity(new Intent(getActivity(), AboutActivity.class));
                break;
            case R.id.ll_evaluate:
                evaluate();
                break;
            case R.id.ll_help_center:
                WebActivity.actionStart(getContext(), "http://www.wbx365.com/Wbxwaphome/help/help", "帮助中心");
                break;
            case R.id.ll_logout:
                logout();
                break;
            default:
                break;
        }
    }

    private void evaluate() {
        try {
            Uri uri = Uri.parse("market://details?id=" + getActivity().getPackageName());
            Intent intentpf = new Intent(Intent.ACTION_VIEW, uri);
            intentpf.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intentpf);
        } catch (Exception e) {
            ToastUitl.showShort("您的手机没有安装应用市场");
        }
    }

    private void logout() {
        new AlertDialog(getActivity()).builder()
                .setTitle("温馨提示")
                .setMsg("确定注销登录吗？")
                .setNegativeButton("再想想", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                })
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        clearUserData();
                    }
                }).show();
    }

    private void clearUserData() {
        LoadingDialog.showDialogForLoading(getActivity(), "退出中...", true);
        new MyHttp().doPost(Api.getDefault().logout(loginUser.getSj_login_token(), "android", PushServiceFactory.getCloudPushService().getDeviceId()), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                SPUtils.setSharedBooleanData(getActivity(), AppConfig.LOGIN_STATE, false);
                SPUtils.setSharedBooleanData(getContext(), AppConfig.NO_ASK_AGAIN_ACCREDITATION, false);
                AppManager.getAppManager().finishAllActivity();
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }

            @Override
            public void onError(int code) {
                SPUtils.setSharedBooleanData(getActivity(), AppConfig.LOGIN_STATE, false);
                SPUtils.setSharedBooleanData(getContext(), AppConfig.NO_ASK_AGAIN_ACCREDITATION, false);
                AppManager.getAppManager().finishAllActivity();
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });
    }
}
