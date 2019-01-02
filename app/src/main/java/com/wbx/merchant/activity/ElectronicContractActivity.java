package com.wbx.merchant.activity;

import android.content.Context;
import android.content.Intent;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.wbx.merchant.R;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.bean.ShopEnterParameter;
import com.wbx.merchant.utils.GlideUtils;

import com.wbx.merchant.utils.UpLoadPicUtils;
import com.wbx.merchant.widget.LoadingDialog;
import com.wbx.merchant.widget.edittext.NameEditText;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPicker;

public class ElectronicContractActivity extends BaseActivity {
    public static final int REQUEST_CARD_HEAD = 1000;
    public static final int REQUEST_CARD_BACK = 1001;
    @Bind(R.id.et_name)
    NameEditText etName;
    @Bind(R.id.et_card_num)
    EditText etCardNum;
    @Bind(R.id.iv_card_head)
    ImageView ivCardHead;
    @Bind(R.id.iv_card_back)
    ImageView ivCardBack;
    private String cardHeadPath;
    private String cardBackPath;
    private ShopEnterParameter shopEnterParameter;

    public static void actionStart(Context context, ShopEnterParameter shopEnterParameter) {
        Intent intent = new Intent(context, ElectronicContractActivity.class);
        intent.putExtra("shopEnterParameter", shopEnterParameter);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_electronic_contract;
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
    }

    @Override
    public void setListener() {
        InputFilter inputFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                String allowStr = "[1234567890Xx]";
                Pattern pattern = Pattern.compile(allowStr);
                Matcher matcher = pattern.matcher(source.toString());
                if (matcher.matches()) {
                    return source;
                } else {
                    return "";
                }
            }
        };
        etCardNum.setFilters(new InputFilter[]{inputFilter, new InputFilter.LengthFilter(18)});
    }

    @OnClick({R.id.iv_card_head, R.id.iv_card_back, R.id.tv_sign})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_card_head:
                PhotoPicker.builder().setPhotoCount(1).setShowCamera(true).start(this, REQUEST_CARD_HEAD);
                break;
            case R.id.iv_card_back:
                PhotoPicker.builder().setPhotoCount(1).setShowCamera(true).start(this, REQUEST_CARD_BACK);
                break;
            case R.id.tv_sign:
                sign();
                break;
        }
    }

    private void sign() {
        final String name = etName.getText().toString();
        final String cardNum = etCardNum.getText().toString();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(mContext, "请输入姓名", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(cardNum)) {
            Toast.makeText(mContext, "请输入身份证号码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (cardNum.length() != 18) {
            Toast.makeText(mContext, "身份证号码不正确", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(cardHeadPath) || TextUtils.isEmpty(cardBackPath)) {
            Toast.makeText(mContext, "请上传身份证正反面", Toast.LENGTH_SHORT).show();
            return;
        }
        LoadingDialog.showDialogForLoading(this, "上传中...", false);
        UpLoadPicUtils.upOnePic(cardHeadPath, new UpLoadPicUtils.UpLoadPicListener() {
            @Override
            public void success(String qiNiuPath) {
                final String headQiniuPath = qiNiuPath;
                UpLoadPicUtils.upOnePic(cardBackPath, new UpLoadPicUtils.UpLoadPicListener() {
                    @Override
                    public void success(String qiNiuPath) {
                        submit(headQiniuPath, qiNiuPath, cardNum, name);
                    }

                    @Override
                    public void error() {

                    }
                });
            }

            @Override
            public void error() {

            }
        });
    }

    private void submit(String headQiniuPath, String backQiniuPath, String cardNum, String name) {
        new MyHttp().doPost(Api.getDefault().addShopContract(userInfo.getSj_login_token(), headQiniuPath, backQiniuPath,
                cardNum, name), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                LoadingDialog.cancelDialogForLoading();
                ContractSignActivity.actionStart(ElectronicContractActivity.this, shopEnterParameter);
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == REQUEST_CARD_HEAD) {
                ArrayList<String> lstPath = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                cardHeadPath = lstPath.get(0);
                GlideUtils.showMediumPic(this, ivCardHead, lstPath.get(0));
            }
            if (requestCode == REQUEST_CARD_BACK) {
                ArrayList<String> lstPath = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                cardBackPath = lstPath.get(0);
                GlideUtils.showMediumPic(this, ivCardBack, lstPath.get(0));
            }
        }
    }
}
