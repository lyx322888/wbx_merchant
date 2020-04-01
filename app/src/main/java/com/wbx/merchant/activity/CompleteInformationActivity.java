package com.wbx.merchant.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.wbx.merchant.R;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.bean.ShopIdentityBean;
import com.wbx.merchant.common.LoginUtil;
import com.wbx.merchant.utils.GlideUtils;
import com.wbx.merchant.utils.UpLoadPicUtils;
import com.wbx.merchant.widget.LoadingDialog;
import com.wbx.merchant.widget.SelectorButton;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumFile;
import com.yanzhenjie.album.api.widget.Widget;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;


public class CompleteInformationActivity extends BaseActivity {

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.iv_sfz_zm)
    ImageView ivSfzZm;
    @Bind(R.id.iv_sfz_fm)
    ImageView ivSfzFm;
    @Bind(R.id.iv_sfz_sc)
    ImageView ivSfzSc;
    @Bind(R.id.iv_zz)
    ImageView ivZz;
    @Bind(R.id.submit_btn)
    Button submitBtn;
    @Bind(R.id.sb_smrz)
    SelectorButton sbSmrz;
    @Bind(R.id.ll_zz)
    LinearLayout llZz;
    private String pathSfzZm ="";
    private String pathSfzFm ="";
    private String pathSfzSc ="";
    private String pathZz ="";

    @Override
    public int getLayoutId() {
        return R.layout.activity_complete_information;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        sbSmrz.setOnselectorListen(new SelectorButton.SelectorListen() {
            @Override
            public void onSelector(boolean isSelectionOne) {
                if (isSelectionOne){
                    llZz.setVisibility(View.GONE);
                }else {
                    llZz.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void fillData() {
        getShopIdentity();
    }

    @Override
    public void setListener() {

    }


    @OnClick({R.id.iv_sfz_zm, R.id.iv_sfz_fm, R.id.iv_sfz_sc, R.id.iv_zz, R.id.submit_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_sfz_zm:
            case R.id.iv_sfz_fm:
            case R.id.iv_sfz_sc:
            case R.id.iv_zz:
                showAlbum((ImageView) view);
                break;
            case R.id.submit_btn:
                detection();
                break;
        }
    }
    //检测信息完整
    private void detection() {
        if (TextUtils.isEmpty(pathSfzZm)){
            showShortToast("请上传身份证正面");
            return;
        }
        if (TextUtils.isEmpty(pathSfzFm)){
            showShortToast("请上传身份证反面");
            return;
        }
        if (TextUtils.isEmpty(pathSfzSc)){
            showShortToast("请上传手持身份证照片");
            return;
        }
        if (!sbSmrz.getSelectionOne()){
            if (TextUtils.isEmpty(pathZz)){
                showShortToast("请上传营业执照");
                return;
            }
        }
        submit();

    }

    //选择图片
    private void showAlbum(final ImageView iv) {
        Album.image(this)
                .multipleChoice()
                .widget(Widget.newDarkBuilder(this)
                        .title("选择图片") // Title.
                        .mediaItemCheckSelector(ContextCompat.getColor(mActivity, R.color.white), ContextCompat.getColor(mActivity, R.color.app_color))//按钮颜色
                        .statusBarColor(ContextCompat.getColor(mActivity, R.color.app_color))
                        .toolBarColor(ContextCompat.getColor(mActivity, R.color.app_color)).build())// Toolbar color..build())// StatusBar color.)
                .camera(true)
                .columnCount(4)
                .selectCount(1)
                .onResult(new Action<ArrayList<AlbumFile>>() {
                    @Override
                    public void onAction(@NonNull final ArrayList<AlbumFile> result) {
                        //上传到七牛
                        LoadingDialog.showDialogForLoading(mActivity, "上传中...", false);
                        UpLoadPicUtils.upOnePic(result.get(0).getPath(), new UpLoadPicUtils.UpLoadPicListener() {
                            @Override
                            public void success(String qiNiuPath) {
                                //提交
                                switch (iv.getId()) {
                                    case R.id.iv_sfz_zm:
                                        pathSfzZm = qiNiuPath;
                                        break;
                                    case R.id.iv_sfz_fm:
                                        pathSfzFm = qiNiuPath;
                                        break;
                                    case R.id.iv_sfz_sc:
                                        pathSfzSc = qiNiuPath;
                                        break;
                                    case R.id.iv_zz:
                                        pathZz = qiNiuPath;
                                        break;
                                }
                                GlideUtils.showMediumPic(mContext, iv, qiNiuPath);
                                LoadingDialog.cancelDialogForLoading();
                            }
                            @Override
                            public void error() {
                                showShortToast("图片上传失败，请重试！");
                                LoadingDialog.cancelDialogForLoading();
                            }
                        });

                    }
                }).start();
    }


    //获取页面信息
    private void getShopIdentity(){
        new MyHttp().doPost(Api.getDefault().getShopIdentity(LoginUtil.getLoginToken()), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                ShopIdentityBean shopIdentityBean = new Gson().fromJson(result.toString(),ShopIdentityBean.class);
                pathSfzZm = shopIdentityBean.getData().getIdentity_card_front();
                pathSfzFm = shopIdentityBean.getData().getIdentity_card_contrary();
                pathSfzSc = shopIdentityBean.getData().getIdentity_card_in_hand();
                pathZz = shopIdentityBean.getData().getBusiness_license();
                if (!TextUtils.isEmpty(pathSfzZm)){
                    GlideUtils.showMediumPic(mContext,ivSfzZm,pathSfzZm);
                }
                if (!TextUtils.isEmpty(pathSfzFm)){
                    GlideUtils.showMediumPic(mContext,ivSfzFm,pathSfzFm);
                }
                if (!TextUtils.isEmpty(pathSfzSc)){
                    GlideUtils.showMediumPic(mContext,ivSfzSc,pathSfzSc);
                }
                if (!TextUtils.isEmpty(pathZz)){
                    GlideUtils.showMediumPic(mContext,ivZz,pathZz);
                }
            }

            @Override
            public void onError(int code) {
            }
        });
    }

    //提交
    private void submit(){
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("sj_login_token",LoginUtil.getLoginToken());
        hashMap.put("identity_card_front",pathSfzZm);
        hashMap.put("identity_card_contrary",pathSfzFm);
        hashMap.put("identity_card_in_hand",pathSfzSc);
        if (!sbSmrz.getSelectionOne()){
            hashMap.put("business_license",pathZz);
        }
        LoadingDialog.showDialogForLoading(mActivity, "上传中...", false);
        new MyHttp().doPost(Api.getDefault().addShopIdentity(hashMap), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                showShortToast("上传成功，请等待审核");
            }

            @Override
            public void onError(int code) {

            }
        });
    }

}
