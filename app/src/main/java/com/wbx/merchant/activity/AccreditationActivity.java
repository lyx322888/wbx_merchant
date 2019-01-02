package com.wbx.merchant.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bigkoo.pickerview.TimePickerView;
import com.wbx.merchant.R;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.baseapp.AppConfig;
import com.wbx.merchant.bean.AccreditationDetailBean;
import com.wbx.merchant.utils.GlideUtils;
import com.wbx.merchant.utils.UpLoadPicUtils;
import com.wbx.merchant.widget.LoadingDialog;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;

/**
 * Created by wushenghui on 2017/6/21.
 * 认证
 */

public class AccreditationActivity extends BaseActivity {
    @Bind(R.id.show_date_tv)
    TextView showDateTv;
    @Bind(R.id.store_signage_pic_im)
    ImageView storeSignageIm;
    @Bind(R.id.submit_authentication_btn)
    Button submitAuthenticationBtn;
    @Bind(R.id.tv_change_acc)
    TextView tvChangeAcc;
    private String mPhotoPath = "";
    @Bind(R.id.licence_name_edit)
    EditText licenceNameTv;
    @Bind(R.id.licence_num_edit)
    EditText licenceNumEdit;
    @Bind(R.id.licence_address_edit)
    EditText licenceAddressEdit;
    private HashMap<String, Object> mParams = new HashMap<>();
    private boolean isCheck = false;
    private AccreditationDetailBean bean;

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, AccreditationActivity.class);
        context.startActivity(intent);
    }

    public static void actionStart(Context context, boolean isCheck) {
        Intent intent = new Intent(context, AccreditationActivity.class);
        intent.putExtra("isCheck", isCheck);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_accreditation;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {

    }

    @Override
    public void fillData() {
        isCheck = getIntent().getBooleanExtra("isCheck", false);
        if (isCheck) {
            LoadingDialog.showDialogForLoading(this);
            submitAuthenticationBtn.setText("查看食品经营许可证");
            new MyHttp().doPost(Api.getDefault().getAccreditation(userInfo.getSj_login_token()), new HttpListener() {
                @Override
                public void onSuccess(JSONObject result) {
                    bean = JSONObject.parseObject(result.getString("data"), AccreditationDetailBean.class);
                    updateUI();
                }

                @Override
                public void onError(int code) {

                }
            });
        }
    }

    private void updateUI() {
        GlideUtils.showBigPic(this, storeSignageIm, bean.getPhoto());
        tvChangeAcc.setVisibility(View.GONE);
        licenceNameTv.setText(bean.getName());
        licenceNumEdit.setText(bean.getZhucehao());
        licenceAddressEdit.setText(bean.getAddr());
        licenceNameTv.setFocusable(false);
        licenceNumEdit.setFocusable(false);
        licenceAddressEdit.setFocusable(false);
        showDateTv.setText(bean.getEnd_date());
        submitAuthenticationBtn.setText("查看食品许可证");
    }

    @Override
    public void setListener() {

    }

    @OnClick({R.id.select_date_layout, R.id.store_signage_pic_layout, R.id.submit_authentication_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.select_date_layout:
                if (isCheck) {
                    return;
                }
                TimePickerView pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {//选中事件回调
                        showDateTv.setText(new SimpleDateFormat("yyyy-MM-dd").format(date));
                    }
                })
                        //年月日时分秒 的显示与否，不设置则默认全部显示
                        .setType(new boolean[]{true, true, true, false, false, false})
                        .setLabel("", "", "", "", "", "")
                        .isCenterLabel(false)
                        .setDividerColor(Color.GRAY)
                        .setContentSize(17)
                        .setDate(Calendar.getInstance())
                        .setBackgroundId(0x00FFFFFF) //设置外部遮罩颜色
                        .setDecorView(null)
                        .build();
                pvTime.show();
                break;
            case R.id.store_signage_pic_layout:
                if (isCheck) {
                    ArrayList<String> lstPhoto = new ArrayList<>();
                    lstPhoto.add(bean.getPhoto());
                    if (lstPhoto.size() > 0) {
                        PhotoPreview.builder().setPhotos(lstPhoto).setShowDeleteButton(false).start(this);
                    }
                } else {
                    PhotoPicker.builder()
                            .setPhotoCount(1)
                            .setShowCamera(true)
                            .setPreviewEnabled(false)
                            .start(mActivity, AppConfig.TAKE_PHOTO_CODE);
                }
                break;
            case R.id.submit_authentication_btn:
                if (isCheck) {
                    GoodsAccreditationActivity.actionStart(this, bean);
                    finish();
                    return;
                }
                if (!submitSuccess()) {
                    return;
                }
                LoadingDialog.showDialogForLoading(mActivity, "信息提交中...", true);
                submitAuthenticationBtn.setEnabled(false);
                upLoadPic();
                break;
        }
    }

    private void upLoadPic() {
        UpLoadPicUtils.upOnePic(mPhotoPath, new UpLoadPicUtils.UpLoadPicListener() {
            @Override
            public void success(String qiNiuPath) {
                submit(qiNiuPath);
            }

            @Override
            public void error() {
                submitAuthenticationBtn.setEnabled(true);
                showShortToast("图片上传失败，请重试。");
            }
        });
    }

    private void submit(String qiNiuPath) {
        String licenceName = licenceNameTv.getText().toString();
        String licenceNum = licenceNumEdit.getText().toString();
        String licenceAddress = licenceAddressEdit.getText().toString();
        String date = showDateTv.getText().toString();
        mParams.put("sj_login_token", userInfo.getSj_login_token());
        mParams.put("photo", qiNiuPath);
        mParams.put("name", licenceName);
        mParams.put("zhucehao", licenceNum);
        mParams.put("addr", licenceAddress);
        mParams.put("end_date", date);

        new MyHttp().doPost(Api.getDefault().addShopAudit(mParams), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                submitAuthenticationBtn.setEnabled(true);
                showShortToast(result.getString("msg"));
                GoodsAccreditationActivity.actionStart(AccreditationActivity.this);
                finish();
            }

            @Override
            public void onError(int code) {
                submitAuthenticationBtn.setEnabled(true);
            }
        });
    }

    private boolean submitSuccess() {
        if (TextUtils.isEmpty(mPhotoPath)) {
            showShortToast("请上传营业执照");
            return false;
        }
        if (TextUtils.isEmpty(licenceNameTv.getText().toString())) {
            showShortToast("请填写许可证名称");
            return false;
        }
        if (TextUtils.isEmpty(licenceNumEdit.getText().toString())) {
            showShortToast("请填写许可证注册号");
            return false;
        }
        if (TextUtils.isEmpty(licenceAddressEdit.getText().toString())) {
            showShortToast("请填写许可证所在地");
            return false;
        }
//        if(TextUtils.isEmpty(showDateTv.getText().toString())){
//            showShortToast("请选择许可证有效时间");
//            return false;
//        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == AppConfig.TAKE_PHOTO_CODE) {
            ArrayList<String> pics = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
            GlideUtils.displayUri(mContext, storeSignageIm, Uri.fromFile(new File(pics.get(0))));
            mPhotoPath = pics.get(0);
        }
    }
}
