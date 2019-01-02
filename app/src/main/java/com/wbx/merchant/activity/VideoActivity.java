package com.wbx.merchant.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSONObject;
import com.wbx.merchant.R;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.utils.UpLoadPicUtils;
import com.wbx.merchant.widget.LoadingDialog;

import butterknife.Bind;
import butterknife.OnClick;
import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;

/**
 * Created by wushenghui on 2017/10/27.
 */

public class VideoActivity extends BaseActivity {
    private static final int REQUEST_CHOOSE_VIDEO = 1001;
    @Bind(R.id.video_view)
    JZVideoPlayerStandard bdVideoView;
    @Bind(R.id.up_load_btn)
    Button btnUpload;
    @Bind(R.id.btn_modify)
    Button btnModify;
    @Bind(R.id.rl_right)
    RelativeLayout rlRight;
    private String videoUrl;

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, VideoActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_video;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {

    }

    @Override
    public void fillData() {
        new MyHttp().doPost(Api.getDefault().getVideo(userInfo.getSj_login_token()), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                videoUrl = result.getJSONObject("data").getString("video");
                if (!TextUtils.isEmpty(videoUrl)) {
                    rlRight.setVisibility(View.VISIBLE);
                    btnModify.setVisibility(View.VISIBLE);
                    bdVideoView.setVisibility(View.VISIBLE);
                    bdVideoView.setUp(videoUrl, JZVideoPlayer.SCREEN_WINDOW_NORMAL, "商家视频");
                    bdVideoView.startVideo();
                } else {
                    rlRight.setVisibility(View.INVISIBLE);
                    btnUpload.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    @Override
    public void setListener() {
    }

    @OnClick({R.id.rl_right, R.id.iv_choose_video, R.id.up_load_btn, R.id.btn_modify})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_right:
                deleteVideo();
                break;
            case R.id.iv_choose_video:
            case R.id.btn_modify:
                chooseVideo();
                break;
            case R.id.up_load_btn:
                uploadVideo();
                break;
        }
    }

    private void uploadVideo() {
        if (TextUtils.isEmpty(videoUrl)) {
            showShortToast("请拍摄视频");
            return;
        }
        LoadingDialog.showDialogForLoading(VideoActivity.this, "视频上传中...", true);
        UpLoadPicUtils.upOnePic(videoUrl, new UpLoadPicUtils.UpLoadPicListener() {
            @Override
            public void success(String qiNiuPath) {
                new MyHttp().doPost(Api.getDefault().upLoadVideo(userInfo.getSj_login_token(), qiNiuPath), new HttpListener() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        showShortToast("上传成功！");
                        btnUpload.setVisibility(View.GONE);
                        btnModify.setVisibility(View.VISIBLE);
                        rlRight.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError(int code) {

                    }
                });
            }

            @Override
            public void error() {
                LoadingDialog.cancelDialogForLoading();
            }
        });
    }

    private void chooseVideo() {
        startActivityForResult(new Intent(mContext, VideoRecordActivity.class), REQUEST_CHOOSE_VIDEO);
    }

    private void deleteVideo() {
        LoadingDialog.showDialogForLoading(this, "删除中...", true);
        new MyHttp().doPost(Api.getDefault().upLoadVideo(userInfo.getSj_login_token(), ""), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                showShortToast("删除成功！");
                videoUrl = null;
                btnUpload.setVisibility(View.VISIBLE);
                btnModify.setVisibility(View.GONE);
                rlRight.setVisibility(View.INVISIBLE);
                bdVideoView.onCompletion();
                bdVideoView.setVisibility(View.GONE);
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (null == data) {
            return;
        }
        if (requestCode == REQUEST_CHOOSE_VIDEO) {
            btnModify.setVisibility(View.GONE);
            btnUpload.setVisibility(View.VISIBLE);
            videoUrl = data.getStringExtra("URL");
            bdVideoView.setVisibility(View.VISIBLE);
            bdVideoView.setUp(videoUrl, JZVideoPlayer.SCREEN_WINDOW_NORMAL, "商家视频");
            bdVideoView.startVideo();
        }
    }

    @Override
    public void onBackPressed() {
        if (JZVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JZVideoPlayer.releaseAllVideos();
    }
}
