package com.wbx.merchant.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;

import androidx.core.content.ContextCompat;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.google.gson.Gson;
import com.wbx.merchant.MainActivity;
import com.wbx.merchant.R;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.base.BaseApplication;
import com.wbx.merchant.baseapp.AppConfig;
import com.wbx.merchant.bean.UpdataBean;
import com.wbx.merchant.bean.UserInfo;
import com.wbx.merchant.common.LoginUtil;
import com.wbx.merchant.fragment.UpdateDialogFragment;
import com.wbx.merchant.utils.APPUtil;
import com.wbx.merchant.utils.DeviceUtils;
import com.wbx.merchant.utils.FormatUtil;
import com.wbx.merchant.utils.GlideUtils;
import com.wbx.merchant.utils.MD5;
import com.wbx.merchant.utils.SPUtils;
import com.wbx.merchant.utils.ToastUitl;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by wushenghui on 2017/6/22.
 * 登录
 */
public class LoginActivity extends BaseActivity {
    @Bind(R.id.login_account_edit)
    EditText accountEdit;
    @Bind(R.id.login_psw_edit)
    EditText pswEdit;
    @Bind(R.id.iv_user_img)
    ImageView ivUserImg;
    @Bind(R.id.tv_shop_name)
    TextView tvShopName;
    @Bind(R.id.login_login_btn)
    Button loginLoginBtn;
    @Bind(R.id.register_tv)
    TextView registerTv;
    @Bind(R.id.forget_psw_tv)
    TextView forgetPswTv;
    private UserInfo userInfo;
    private int hxErrorCount = 0;
    private Dialog mLoadingDialog;
    private static final int REQUEST_CODE = 1234;

    @Override
    public int getLayoutId() {
        FormatUtil.setStatubarColor(mActivity, R.color.white);
        return R.layout.activity_login;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        final String phone = SPUtils.getSharedStringData(mContext, AppConfig.LOGIN_MOBILE);
        final String shopNeme = SPUtils.getSharedStringData(mContext, AppConfig.LOGIN_NAME);
        final String photo = SPUtils.getSharedStringData(mContext, AppConfig.LOGIN_PHOTO);
        accountEdit.setText(phone);
        tvShopName.setText(shopNeme);
        tvShopName.setHint("欢迎您，使用"+getString(R.string.app_name));
        if (!TextUtils.isEmpty(photo)){
            GlideUtils.showMediumPic(mContext,ivUserImg,photo);
        }
        accountEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(phone)&&TextUtils.equals(phone,s)){
                    tvShopName.setText(shopNeme);
                    GlideUtils.showMediumPic(mContext,ivUserImg,photo);
                }else {
                    tvShopName.setText("");
                    ivUserImg.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.shop_photo_mr));
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

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
//                else {
//                    getNotice();
//                }
            }

            @Override
            public void onError(int code) {
//                getNotice();
            }
        });
    }

    private void upDateApp(String jsonStr) {
        UpdateDialogFragment.newInstent(jsonStr).show(getSupportFragmentManager(), "");
    }

    @Override
    public void setListener() {

    }

    @OnClick({R.id.login_login_btn, R.id.register_tv, R.id.forget_psw_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_login_btn:
                doLogin();
                break;
            case R.id.register_tv:
                startActivityForResult(new Intent(mContext, RegisterActivity.class),REQUEST_CODE);
                break;
            case R.id.forget_psw_tv:
                startActivity(new Intent(mContext, ForgetPswActivity.class));
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE:
                //注册返回
                if (resultCode == Activity.RESULT_OK) {
                    accountEdit.setText(data.getStringExtra("mobile"));
                    pswEdit.setText(data.getStringExtra("password"));
                    doLogin();
                }
                break;
            default:
                break;
        }

    }

    //执行登录操作
    private void doLogin() {
        String account = accountEdit.getText().toString();
        String password = pswEdit.getText().toString();
        if (TextUtils.isEmpty(account) || TextUtils.isEmpty(password)) {
            ToastUitl.showShort("请输入账号密码");
            return;
        }
        if (account.length() != 11) {
            ToastUitl.showShort("请输入正确的手机号");
            return;
        }
        showLoading();
        String md5Psw = "";
        try {
            md5Psw = MD5.EncoderByMd5(password);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        HashMap<String, Object> params = new HashMap<>();
        params.put("account", account);
        params.put("password", md5Psw);
        params.put("app_type", "android");
        params.put("version", BaseApplication.getInstance().getVersion());
//        params.put("registration_id", JPushInterface.getRegistrationID(this));
        params.put("registration_id", PushServiceFactory.getCloudPushService().getDeviceId());
        params.put("push_type", AppConfig.ALIPUSH);
        params.put("phone_type", DeviceUtils.getManufacturer() + "/" + DeviceUtils.getModel() + "/" + DeviceUtils.getSDKVersionName());
        new MyHttp().doPost(Api.getDefault().login(params), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                userInfo = JSONObject.parseObject(result.getString("data"), UserInfo.class);
                userInfo.setMobile(accountEdit.getText().toString());
                BaseApplication.getInstance().saveUserInfo(userInfo);
                //保存登录的手机
                SPUtils.setSharedStringData(mContext, AppConfig.LOGIN_MOBILE, accountEdit.getText().toString());
                //保存首页奖励活动是否显示判断  is_view_withdraw_commission 1显示  0不显示
                SPUtils.setSharedIntData(mContext, AppConfig.IS_VIEW_WITHDRAW_COMMISSION, userInfo.getIs_view_withdraw_commission());
                nextStep();
            }

            @Override
            public void onError(int code) {
                dimissLoading();
            }
        });
    }

    private void showLoading() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_loading, null);
        TextView loadingText = view.findViewById(R.id.id_tv_loading_dialog_text);
        loadingText.setText("登录中...");
        ImageView loadingIm = view.findViewById(R.id.loading_im);
        AnimationDrawable mAnim = (AnimationDrawable) loadingIm.getDrawable();
        mAnim.start();
        mLoadingDialog = new Dialog(this, R.style.CustomProgressDialog);
        mLoadingDialog.setCancelable(true);
        mLoadingDialog.setCanceledOnTouchOutside(false);
        mLoadingDialog.setContentView(view, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        mLoadingDialog.show();
    }

    private void dimissLoading() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
            mLoadingDialog.cancel();
        }
    }

    private void nextStep() {
        dimissLoading();
        if (0 == userInfo.getShop_id()) {
            //未填写信息
            startActivity(new Intent(mContext, ShopInfoPrwActivity.class));
        } else if (1== userInfo.getTry_shop()){
            //判断是否试用版
            startActivity(new Intent(mContext, ChooseShopVersionsPrwActivity.class));
        }
        else if (0 == userInfo.getEnd_date()) { //未缴费
            startActivity(new Intent(mContext, ChooseShopVersionsPrwActivity.class));
        }
//         else if (0 == userInfo.getGrade_id()) {
//            startActivity(new Intent(mContext, ChooseShopTypeActivity.class));
//        }
        else if (userInfo.getEnd_date() <= System.currentTimeMillis() / 1000) {
            //过期
          Intent intent = new Intent(mContext, PayActivity.class);
          intent.putExtra("gradeId", userInfo.getGrade_id());
          intent.putExtra("isRenew", true);
          startActivity(intent);
        }
//        else if (userInfo.getAudit() == 0) {
//            //缴费完成 还在审核中
//            startActivity(new Intent(mContext, AuditingActivity.class));
//        }
        else {
            SPUtils.setSharedBooleanData(mContext, AppConfig.LOGIN_STATE, true);
            startActivity(new Intent(mContext, MainActivity.class));
            finish();
        }

    }

}
