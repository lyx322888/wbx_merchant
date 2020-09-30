package com.wbx.merchant.activity.jhzf;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.wbx.merchant.R;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.bean.AttachOneBean;
import com.wbx.merchant.common.LoginUtil;
import com.wbx.merchant.utils.ChoosePictureUtils;
import com.wbx.merchant.utils.FileUtils;
import com.wbx.merchant.utils.GlideUtils;
import com.wbx.merchant.utils.ImageUtil;
import com.wbx.merchant.widget.LoadingDialog;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import top.zibin.luban.Luban;

//图片上传
public class UploadPicturesActivity extends BaseActivity {

    String attachType = "";
    @Bind(R.id.rl_right)
    RelativeLayout rlRight;
    @Bind(R.id.iv_pic)
    ImageView ivPic;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    File file  ;
    String base64 = "";
    String attachNum = "";
    boolean isUpdata = false;
    boolean isUploading = false;//是否上传

    public static void actionStart(Activity context, int requestCode, String attachType, String title) {
        Intent intent = new Intent(context, UploadPicturesActivity.class);
        intent.putExtra("attachType", attachType);
        intent.putExtra("title", title);
        context.startActivityForResult(intent, requestCode);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_upload_pictures;
    }

    @Override
    public void initPresenter() {

    }


    @Override
    public void finish() {
        Intent intent = new Intent();
        intent.putExtra("isUploading", isUploading);
        setResult(Activity.RESULT_OK, intent);
        super.finish();
    }

    @Override
    public void initView() {
        attachType = getIntent().getStringExtra("attachType");
        tvTitle.setText(getIntent().getStringExtra("title"));

    }

    @Override
    public void fillData() {
        new MyHttp().doPost(Api.getDefault().get_attach_one(LoginUtil.getLoginToken(),attachType), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                AttachOneBean attachOneBean= new Gson().fromJson(result.toString(),AttachOneBean.class);
                if (attachOneBean.getData().getAttachList().size()>0){
                    isUploading = true;
                    isUpdata =true;
                    attachNum = attachOneBean.getData().getAttachList().get(0).getAttachNum();
                    GlideUtils.showMediumPic(mContext,ivPic,attachOneBean.getData().getAttachList().get(0).getUrl());
                }
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    //选择图片
    public void choosePicture() {
        ChoosePictureUtils.choosePictureCommon(mContext, 1, new ChoosePictureUtils.Action<ArrayList<String>>() {
            @Override
            public void onAction(@NonNull ArrayList<String> result)  {

                List<File> files= null;
                try {
                    files = Luban.with(mContext).load(result).setTargetDir(FileUtils.getCacheDir(mContext,"image").getAbsolutePath()).get();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                file =files.get(0);
                GlideUtils.showMediumPic(mActivity,ivPic, result.get(0));
            }
        });

    }


    @Override
    public void setListener() {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void sumbit(){
        LoadingDialog.showDialogForLoading(mActivity);
        new MyHttp().doPost(Api.getDefault().upload_attach(LoginUtil.getLoginToken(), attachType, ImageUtil.imageToBase64(file.getPath())), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                isUploading = true;
                showShortToast("上传成功");
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    //修改图片
    public void updata(){
        LoadingDialog.showDialogForLoading(mActivity);
        new MyHttp().doPost(Api.getDefault().update_attach(LoginUtil.getLoginToken(),attachType,ImageUtil.imageToBase64(file.getPath()),attachNum), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                isUploading = true;
                showShortToast("上传成功");
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    @OnClick({R.id.tv_bc, R.id.iv_pic})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_bc:
                if (file==null){
                    showShortToast("请先选择图片");
                }else {
                    if (isUpdata){
                        updata();
                    }else {
                        sumbit( );
                    }
                }
                break;
            case R.id.iv_pic:
                choosePicture();
                break;
        }
    }
}