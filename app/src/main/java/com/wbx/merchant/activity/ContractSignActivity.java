package com.wbx.merchant.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.wbx.merchant.MainActivity;
import com.wbx.merchant.R;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.ApiConstants;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.baseapp.AppConfig;
import com.wbx.merchant.baseapp.AppManager;
import com.wbx.merchant.bean.ShopEnterParameter;
import com.wbx.merchant.utils.MD5;
import com.wbx.merchant.utils.PictureUtil;
import com.wbx.merchant.utils.SPUtils;
import com.wbx.merchant.utils.UpLoadPicUtils;
import com.wbx.merchant.widget.LoadingDialog;
import com.wbx.merchant.widget.SignView;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import butterknife.Bind;
import butterknife.OnClick;

public class ContractSignActivity extends BaseActivity {
    @Bind(R.id.tv_sign)
    TextView tvSign;
    @Bind(R.id.web_view)
    WebView webView;
    @Bind(R.id.ll_dialog)
    LinearLayout llDialog;
    @Bind(R.id.sign_view)
    SignView signView;
    @Bind(R.id.ll_sign)
    LinearLayout llSign;
    private ShopEnterParameter shopEnterParameter;
    private boolean hasSign = false;

    public static void actionStart(Context context, ShopEnterParameter shopEnterParameter) {
        Intent intent = new Intent(context, ContractSignActivity.class);
        intent.putExtra("shopEnterParameter", shopEnterParameter);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_contract_sign;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {

    }

    @Override
    public void fillData() {
        shopEnterParameter = (ShopEnterParameter) getIntent().getSerializableExtra("shopEnterParameter");
        String contractUrl;
        try {
            contractUrl = (ApiConstants.DEBUG ? "http://vrzff.com" : "http://www.wbx365.com") + "/wap/message/fapiao/shop_id/" + userInfo.getShop_id() + "/shop_key/" + MD5.EncoderByMd5(userInfo.getShop_id() + "wbxpacit365");
            WebSettings settings = webView.getSettings();
            settings.setSupportZoom(true);
            settings.setBuiltInZoomControls(true);
            settings.setDisplayZoomControls(false);
            settings.setUseWideViewPort(true);
            settings.setLoadWithOverviewMode(true);
            webView.loadUrl(contractUrl);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setListener() {
    }

    @OnClick({R.id.rl_back, R.id.tv_sign, R.id.iv_close, R.id.tv_sign_online, R.id.tv_review, R.id.tv_submit, R.id.tv_cancel_sign, R.id.tv_resign, R.id.tv_ensure_sign})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.tv_sign:
                llDialog.setVisibility(View.VISIBLE);
                break;
            case R.id.iv_close:
                llDialog.setVisibility(View.GONE);
                break;
            case R.id.tv_sign_online:
                llDialog.setVisibility(View.GONE);
                llSign.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_review:
                llDialog.setVisibility(View.GONE);
                webView.reload();
                break;
            case R.id.tv_submit:
                llDialog.setVisibility(View.GONE);
                submit();
                break;
            case R.id.tv_cancel_sign:
                llSign.setVisibility(View.GONE);
                break;
            case R.id.tv_resign:
                signView.reDraw();
                break;
            case R.id.tv_ensure_sign:
                llSign.setVisibility(View.GONE);
                uploadSign();
                break;
        }
    }

    private void submit() {
        if (!hasSign) {
            Toast.makeText(mContext, "请先签名", Toast.LENGTH_SHORT).show();
            return;
        }
        if (0 == userInfo.getEnd_date()) {
            //未缴费
            Intent intent = new Intent(mContext, PayActivity.class);
            intent.putExtra("gradeName", shopEnterParameter.getGradeName());
            intent.putExtra("shopGradeId", shopEnterParameter.getShopGradeId());
//            intent.putExtra("needPayPrice", shopEnterParameter.getNeedPayPrice());
            intent.putExtra("gradeId", shopEnterParameter.getGradeId());
            startActivity(intent);
        } else {
            SPUtils.setSharedBooleanData(mContext, AppConfig.LOGIN_STATE, true);
            startActivity(new Intent(mContext, MainActivity.class));
            AppManager.getAppManager().finishAllExpectSpecifiedActivity(MainActivity.class);
        }
    }

    private void uploadSign() {
        Bitmap signBitmap = signView.save();
        if (signBitmap == null) {
            return;
        }
        LoadingDialog.showDialogForLoading(this, "上传中...", false);
        try {
            String signPath = PictureUtil.saveBitmap(signBitmap, "sign");
            UpLoadPicUtils.upOnePic(signPath, new UpLoadPicUtils.UpLoadPicListener() {
                @Override
                public void success(String qiNiuPath) {
                    new MyHttp().doPost(Api.getDefault().addSignPhoto(userInfo.getSj_login_token(), qiNiuPath), new HttpListener() {
                        @Override
                        public void onSuccess(JSONObject result) {
                            LoadingDialog.cancelDialogForLoading();
                            hasSign = true;
                            webView.reload();
                        }

                        @Override
                        public void onError(int code) {

                        }
                    });
                }

                @Override
                public void error() {

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
