package com.wbx.merchant.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import androidx.appcompat.widget.SwitchCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.wbx.merchant.R;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.bean.NumberOrderSettingBean;
import com.wbx.merchant.utils.GlideUtils;
import com.wbx.merchant.utils.PictureUtil;
import com.wbx.merchant.utils.ToastUitl;
import com.wbx.merchant.widget.LoadingDialog;

import butterknife.Bind;
import butterknife.OnClick;

public class NumberOrderSettingActivity extends BaseActivity {
    @Bind(R.id.switch_button)
    SwitchCompat switchButton;
    @Bind(R.id.iv_qrcode)
    ImageView ivQrcode;
    @Bind(R.id.rl_qrcode)
    RelativeLayout rlQrcode;
    @Bind(R.id.tv_save)
    TextView tvSave;
    @Bind(R.id.et_prefix)
    EditText etPrefix;
    @Bind(R.id.et_start_number)
    EditText etStartNumber;
    private MyHttp myHttp;
    private NumberOrderSettingBean data;

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, NumberOrderSettingActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_number_order_setting;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {

    }

    @Override
    public void fillData() {
        myHttp = new MyHttp();
        LoadingDialog.showDialogForLoading(this);
        myHttp.doPost(Api.getDefault().getNumberOrderSetting(userInfo.getSj_login_token()), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                data = result.getObject("data", NumberOrderSettingBean.class);
                switchButton.setChecked(data.getIs_take_number() == 1);
                etPrefix.setText(data.getTake_number_prefix());
                etStartNumber.setText(data.getTake_number_start());
                GlideUtils.showBigPic(NumberOrderSettingActivity.this, ivQrcode, data.getTake_number_photo());
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    @Override
    public void setListener() {
    }

    @OnClick({R.id.tv_right, R.id.tv_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_right:
                saveSetting();
                break;
            case R.id.tv_save:
                saveQrCodeToGallery();
                break;
        }
    }

    private void saveSetting() {
        myHttp.doPost(Api.getDefault().setNumberOrder(userInfo.getSj_login_token(), etPrefix.getText().toString(), etStartNumber.getText().toString(), switchButton.isChecked() ? 1 : 0), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                ToastUitl.showShort(result.getString("msg"));
                finish();
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    private void saveQrCodeToGallery() {
        if (data == null || TextUtils.isEmpty(data.getTake_number_photo())) {
            ToastUitl.showShort("二维码异常！");
            return;
        }
        tvSave.setEnabled(false);
        LoadingDialog.showDialogForLoading(this, "保存中...", false);
        rlQrcode.setDrawingCacheEnabled(true);
        rlQrcode.buildDrawingCache();
        Bitmap bitmap = rlQrcode.getDrawingCache();
        boolean isSuccess = PictureUtil.saveImageToGallery(this, bitmap);
        LoadingDialog.cancelDialogForLoading();
        if (isSuccess) {
            ToastUitl.showShort("保存成功！");
        } else {
            ToastUitl.showShort("保存失败，请重试！");
        }
        tvSave.setEnabled(true);
    }
}
