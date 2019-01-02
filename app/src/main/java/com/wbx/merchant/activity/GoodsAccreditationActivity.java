package com.wbx.merchant.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.wbx.merchant.R;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.baseapp.AppConfig;
import com.wbx.merchant.bean.AccreditationDetailBean;
import com.wbx.merchant.utils.GlideUtils;
import com.wbx.merchant.utils.ToastUitl;
import com.wbx.merchant.utils.UpLoadPicUtils;
import com.wbx.merchant.widget.LoadingDialog;

import java.io.File;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;

public class GoodsAccreditationActivity extends BaseActivity {

    @Bind(R.id.iv_goods_accreditation)
    ImageView ivGoodsAccreditation;
    @Bind(R.id.iv_dont_need)
    ImageView ivDontNeed;
    @Bind(R.id.tv_goods_acc)
    TextView tvGoodsAcc;
    private String mPhotoPath = "";
    private AccreditationDetailBean accreditationDetailBean;

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, GoodsAccreditationActivity.class);
        context.startActivity(intent);
    }

    public static void actionStart(Context context, AccreditationDetailBean detailBean) {
        Intent intent = new Intent(context, GoodsAccreditationActivity.class);
        intent.putExtra("AccreditationDetailBean", detailBean);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_goods_accreditation;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        accreditationDetailBean = (AccreditationDetailBean) getIntent().getSerializableExtra("AccreditationDetailBean");
        if (accreditationDetailBean != null) {
            GlideUtils.showBigPic(this, ivGoodsAccreditation, accreditationDetailBean.getHygiene_photo());
            tvGoodsAcc.setVisibility(View.GONE);
            ivDontNeed.setSelected(accreditationDetailBean.getHas_hygiene_photo() == 1 ? false : true);
            ivDontNeed.setEnabled(false);
        } else {
            ivDontNeed.setSelected(false);
        }
    }

    @Override
    public void fillData() {
    }

    @Override
    public void setListener() {

    }

    @OnClick({R.id.rl_goods_accreditation, R.id.ll_dont_need, R.id.btn_complete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_goods_accreditation:
                if (accreditationDetailBean != null) {
                    if (!TextUtils.isEmpty(accreditationDetailBean.getHygiene_photo())) {
                        ArrayList<String> lstPhoto = new ArrayList<>();
                        lstPhoto.add(accreditationDetailBean.getHygiene_photo());
                        PhotoPreview.builder().setPhotos(lstPhoto).start(this);
                    }
                } else {
                    PhotoPicker.builder()
                            .setPhotoCount(1)
                            .setShowCamera(true)
                            .setPreviewEnabled(false)
                            .start(mActivity, AppConfig.TAKE_PHOTO_CODE);
                }
                break;
            case R.id.ll_dont_need:
                if (accreditationDetailBean != null) {
                    return;
                }
                if (ivDontNeed.isSelected()) {
                    ivDontNeed.setSelected(false);
                } else {
                    ivDontNeed.setSelected(true);
                }
                break;
            case R.id.btn_complete:
                if (accreditationDetailBean != null) {
                    finish();
                } else {
                    complete();
                }
                break;
            default:
                break;
        }
    }

    private void complete() {
        if (!ivDontNeed.isSelected() && TextUtils.isEmpty(mPhotoPath)) {
            ToastUitl.showShort("请上传食品经营许可证!");
            return;
        }
        LoadingDialog.showDialogForLoading(this, "上传中", false);
        if (!ivDontNeed.isSelected()) {
            UpLoadPicUtils.upOnePic(mPhotoPath, new UpLoadPicUtils.UpLoadPicListener() {
                @Override
                public void success(String qiNiuPath) {
                    submit(qiNiuPath);
                }

                @Override
                public void error() {
                    showShortToast("图片上传失败，请重试。");
                }
            });
        } else {
            submit(null);
        }
    }

    private void submit(String photoPath) {
        new MyHttp().doPost(Api.getDefault().addGoodsAccreditation(userInfo.getSj_login_token(), photoPath, ivDontNeed.isSelected() ? 0 : 1), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                showShortToast(result.getString("msg"));
                finish();
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == AppConfig.TAKE_PHOTO_CODE) {
            ArrayList<String> pics = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
            GlideUtils.displayUri(mContext, ivGoodsAccreditation, Uri.fromFile(new File(pics.get(0))));
            mPhotoPath = pics.get(0);
        }
    }
}
