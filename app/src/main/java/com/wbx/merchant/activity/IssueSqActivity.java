package com.wbx.merchant.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.flyco.roundview.RoundTextView;
import com.google.gson.Gson;
import com.wbx.merchant.R;
import com.wbx.merchant.adapter.AddBusinessCirclePhotoAdapter;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.bean.IssueSqIdBean;
import com.wbx.merchant.common.LoginUtil;
import com.wbx.merchant.dialog.ConfirmDialog;
import com.wbx.merchant.dialog.SelectVideoDialog;
import com.wbx.merchant.utils.ToastUitl;
import com.wbx.merchant.utils.UpLoadPicUtils;
import com.wbx.merchant.widget.LoadingDialog;
import com.wbx.merchant.widget.decoration.SpacesItemDecoration;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.api.widget.Widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;
import me.iwf.photopicker.PhotoPicker;

//新版商圈
public class IssueSqActivity extends BaseActivity {

    @Bind(R.id.title_name_tv)
    TextView titleNameTv;
    @Bind(R.id.et_sqwa)
    EditText etSqwa;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.ll_tp)
    LinearLayout llTp;
    @Bind(R.id.ll_sp)
    LinearLayout llSp;
    @Bind(R.id.iv_choose_video)
    ImageView ivChooseVideo;
    @Bind(R.id.video_view)
    JZVideoPlayerStandard videoView;
    @Bind(R.id.cb_qf)
    CheckBox cbQf;
    @Bind(R.id.ll_qfyl)
    LinearLayout llQfyl;
    @Bind(R.id.tv_submit)
    RoundTextView tvSubmit;
    @Bind(R.id.iv_video_gb)
    ImageView ivVideoGb;
    int type = 0; //0 为图文模式  1为视频模式
    public static final int MAX_PICTURE_NUM = 9;
    private static final int REQUEST_GET_BUSINESS_CIRCLE_PHOTO = 1000;
    private static final int REQUEST_CHOOSE_VIDEO = 1001;
    private List<String> lstPhoto = new ArrayList<>();
    private AddBusinessCirclePhotoAdapter adapter;
    private String videoUrl;

    public static void actionStart(Context context, int type) {
        Intent intent = new Intent(context, IssueSqActivity.class);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_issue_sq;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        type = getIntent().getIntExtra("type", 0);
        if (type == 0) {
            //图文
            llTp.setVisibility(View.VISIBLE);
            llSp.setVisibility(View.GONE);

            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
            recyclerView.setLayoutManager(gridLayoutManager);
            recyclerView.addItemDecoration(new SpacesItemDecoration(8));
            adapter = new AddBusinessCirclePhotoAdapter(this);
            recyclerView.setAdapter(adapter);

            lstPhoto.add("");
            adapter.update(lstPhoto);
        } else {
            //视频
            llTp.setVisibility(View.GONE);
            llSp.setVisibility(View.VISIBLE);
        }

        cbQf.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                tvSubmit.setText("下一步");
            }else {
                tvSubmit.setText("提交");
            }
        });

    }

    @Override
    public void fillData() {

    }

    public void addPhoto() {
        PhotoPicker.builder().setShowCamera(true).setPhotoCount(MAX_PICTURE_NUM - lstPhoto.size() + 1).setPreviewEnabled(true).start(this, REQUEST_GET_BUSINESS_CIRCLE_PHOTO);
    }

    private void psVideo() {
        startActivityForResult(new Intent(mContext, VideoRecordActivity.class), REQUEST_CHOOSE_VIDEO);
    }

    //选择视频
    private void chooseVideo() {
        Album.video(this) // Video selection.
                .singleChoice()
                .widget(Widget.newDarkBuilder(mActivity)
                        .title("选择视频") // Title.
                        .mediaItemCheckSelector(ContextCompat.getColor(mActivity, R.color.white), ContextCompat.getColor(mActivity, R.color.app_color))//按钮颜色
                        .statusBarColor(ContextCompat.getColor(mActivity, R.color.app_color))
                        .toolBarColor(ContextCompat.getColor(mActivity, R.color.app_color)).build())// Toolbar color..build())// StatusBar color.)
                .limitDuration(30)
                .camera(false)
                .columnCount(3)
                .onResult(result -> {
                    String path = result.get(0).getPath();
                    ivChooseVideo.setVisibility(View.GONE);
                    ivVideoGb.setVisibility(View.VISIBLE);
                    videoUrl = path;
                    videoView.setVisibility(View.VISIBLE);
                    videoView.setUp(videoUrl, JZVideoPlayer.SCREEN_WINDOW_NORMAL, "商家视频");
                    videoView.startVideo();
//                    ConfirmDialog confirmDialog = ConfirmDialog.newInstance("是否压缩视频？");
//                    confirmDialog.setDialogListener(() -> {
//
//                    });
//                    new Handler().postDelayed(() -> confirmDialog.show(getSupportFragmentManager(), ""), 500);
                })
                .start();
    }

    //上传视频到七牛
    public void upOnevideo(String videoUrl) {
        LoadingDialog.showDialogForLoading(mActivity, "上传中", false);
        UpLoadPicUtils.upOnePic(videoUrl, new UpLoadPicUtils.UpLoadPicListener() {
            @Override
            public void success(String qiNiuPath) {
                add_discove_video(qiNiuPath);
            }

            @Override
            public void error() {
                showShortToast("上传失败，请稍后重试");
                LoadingDialog.cancelDialogForLoading();
            }
        });
    }

    //上传视频动态
    private void add_discove_video(String qiNiuPath) {
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("sj_login_token", LoginUtil.getLoginToken());
        hashMap.put("text", etSqwa.getText());
        hashMap.put("video", qiNiuPath);
        hashMap.put("discover_type", 2);
        hashMap.put("is_draw_fans", cbQf.isChecked()?1:0);
        new MyHttp().doPost(Api.getDefault().add_discove_v2(hashMap), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                showShortToast("发布成功");
                finish();
                if (cbQf.isChecked()){
                    IssueSqIdBean issueSqIdBean = new Gson().fromJson(result.toString(),IssueSqIdBean.class);
                    skipPay(issueSqIdBean.getData().getDiscover_id());
                }
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    //圈粉支付
    private void skipPay(String id){
        FollowerDrainagePayActivity.actionStart(mContext,id);
    }

    //上传动态
    private void add_discove_pto( ) {
        LoadingDialog.showDialogForLoading(mActivity);
        ArrayList<String> lstGoodsPhoto = new ArrayList();
        lstGoodsPhoto.addAll(lstPhoto);
        if (lstGoodsPhoto.size() != 0 && TextUtils.isEmpty(lstGoodsPhoto.get(lstGoodsPhoto.size() - 1))) {
            lstGoodsPhoto.remove(lstGoodsPhoto.size() - 1);
        }
        UpLoadPicUtils.batchUpload(lstGoodsPhoto, new UpLoadPicUtils.BatchUpLoadPicListener() {
            @Override
            public void success(List<String> qiNiuPath) {
                HashMap<String,Object> hashMap = new HashMap<>();
                hashMap.put("sj_login_token", LoginUtil.getLoginToken());
                hashMap.put("text", etSqwa.getText());
                hashMap.put("discover_type", 1);
                hashMap.put("photos",  JSONArray.toJSONString(qiNiuPath));
                hashMap.put("is_draw_fans", cbQf.isChecked()?1:0);
                new MyHttp().doPost(Api.getDefault().add_discove_v2(hashMap), new HttpListener() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        showShortToast("发布成功");
                        finish();
                        if (cbQf.isChecked()){
                            IssueSqIdBean issueSqIdBean = new Gson().fromJson(result.toString(),IssueSqIdBean.class);
                            skipPay(issueSqIdBean.getData().getDiscover_id());
                        }
                    }

                    @Override
                    public void onError(int code) {

                    }
                });
            }

            @Override
            public void error() {
                ToastUitl.showShort("图片上传失败，请重试！");
                LoadingDialog.cancelDialogForLoading();
            }
        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && requestCode == REQUEST_GET_BUSINESS_CIRCLE_PHOTO) {
            ArrayList<String> pics = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
            for (String pic : pics) {
                if (!lstPhoto.contains(pic)) {
                    lstPhoto.add(lstPhoto.size() - 1, pic);
                }
            }
            if (lstPhoto.size() == MAX_PICTURE_NUM + 1) {
                lstPhoto.remove(MAX_PICTURE_NUM);
            }
            adapter.update(lstPhoto);
        } else if (data != null && requestCode == REQUEST_CHOOSE_VIDEO) {
            ivChooseVideo.setVisibility(View.GONE);
            ivVideoGb.setVisibility(View.VISIBLE);
            videoUrl = data.getStringExtra("URL");
            videoView.setVisibility(View.VISIBLE);
            videoView.setUp(videoUrl, JZVideoPlayer.SCREEN_WINDOW_NORMAL, "商家视频");
            videoView.startVideo();
        }
    }

    @Override
    public void setListener() {

    }

    @OnClick({R.id.iv_choose_video, R.id.tv_submit,R.id.iv_video_gb,R.id.ll_qfyl})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_choose_video:
                SelectVideoDialog dialog = new SelectVideoDialog(mActivity);
                dialog.setDialogListener(new SelectVideoDialog.DialogListener() {
                    @Override
                    public void dialogOneClickListener() {
                        psVideo();
                    }

                    @Override
                    public void dialogTowClickListener() {
                        chooseVideo();
                    }
                });
                dialog.show();
                break;
            case R.id.ll_qfyl:
                FollowersDrainageActivity.actionStart(mActivity);
                break;
            case R.id.iv_video_gb:
                videoUrl = null;
                ivChooseVideo.setVisibility(View.VISIBLE);
                ivVideoGb.setVisibility(View.GONE);
                videoView.setVisibility(View.GONE);
                videoView.onCompletion();
                videoView.setVisibility(View.GONE);
                break;
            case R.id.tv_submit:
                submit();
                break;
        }
    }

    //提交
    private void submit() {

        if (type==0){
            //图片
            if (lstPhoto.size()==0){
                showShortToast("请选择图片");
                return;
            }

            add_discove_pto();
        }else {
            //视频
            if (TextUtils.isEmpty(videoUrl)){
                showShortToast("请上传视频");
                return;
            }

            upOnevideo(videoUrl);
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