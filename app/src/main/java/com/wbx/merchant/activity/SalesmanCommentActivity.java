package com.wbx.merchant.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.hedgehog.ratingbar.RatingBar;
import com.wbx.merchant.R;
import com.wbx.merchant.adapter.CommentAddImgAdaptter;
import com.wbx.merchant.adapter.SalemanCommentTmAdapter;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.base.BaseApplication;
import com.wbx.merchant.bean.QuestionAnswerInfo;
import com.wbx.merchant.bean.SalesmanCommentInfo;
import com.wbx.merchant.common.LoginUtil;
import com.wbx.merchant.utils.GlideUtils;
import com.wbx.merchant.utils.UpLoadPicUtils;
import com.wbx.merchant.widget.CircleImageView;
import com.wbx.merchant.widget.LoadingDialog;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumFile;
import com.yanzhenjie.album.api.widget.Widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
/*销售代理评论*/
public class SalesmanCommentActivity extends BaseActivity {

    @Bind(R.id.civ_selesman_head_p)
    CircleImageView civSelesmanHeadP;
    @Bind(R.id.rb_score)
    RatingBar rbScore;
    @Bind(R.id.iv_zjpm)
    ImageView ivZjpm;
    @Bind(R.id.iv_phone)
    ImageView ivPhone;
    @Bind(R.id.rv_comment_tm)
    RecyclerView rvCommentTm;
    @Bind(R.id.et_comment)
    EditText etComment;
    @Bind(R.id.iv_add_img)
    ImageView ivAddImg;
    @Bind(R.id.rv_comment_img)
    RecyclerView rvCommentImg;
    @Bind(R.id.et_comment_zj)
    EditText etCommentZj;
    @Bind(R.id.iv_add_img_zj)
    ImageView ivAddImgZj;
    @Bind(R.id.rv_comment_zj_img)
    RecyclerView rvCommentZjImg;
    @Bind(R.id.ll_zj_comment)
    LinearLayout llZjComment;
    @Bind(R.id.tv_submit)
    TextView tvSubmit;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.ll_comment)
    LinearLayout llComment;
    private String firstCommentType = "1";//1为添加第一次或已添加过一次时获取  2为追加时获取
    private SalemanCommentTmAdapter commentTmAdapter;
    private CommentAddImgAdaptter commentAddImgAdaptter;
    private CommentAddImgAdaptter commentAddImgZJAdaptter;
    private ArrayList<AlbumFile> albumFiles = new ArrayList<>();
    private ArrayList<AlbumFile> albumFilesZJ = new ArrayList<>();
    private float comment_rank;
    private String comment_text = "";

    private String activityType = "zc";//zj  为评论第一次后 又点击追加评论进入 此时可编辑 zc为首页进入根据返回数据判断是否可编辑
    private ArrayList<String> imglist;
    private ArrayList<String> imglistZj;
    private String phone = "";//电话

    //供外部跳转  activityType =  zj  为评论第一次后 又点击追加评论进入 此时可编辑 zc为首页进入根据返回数据判断是否可编辑
    public static void actionStart(Context context, String activityType) {
        Intent intent = new Intent(context, SalesmanCommentActivity.class);
        intent.putExtra("activityType", activityType);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_salesman_comment;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        //获取进入状态 zj  为评论第一次后 又点击追加评论进入 此时可编辑 zc为首页进入根据返回数据判断是否可编辑
        activityType = getIntent().getStringExtra("activityType");
        if (TextUtils.equals(activityType,"zj")){
            firstCommentType = "2";
        }
        initCommentTmAdapter();
        initaddimgAdapter();
        //星星数监听
        rbScore.setOnRatingChangeListener(new RatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(float RatingCount) {
                comment_rank = RatingCount;
            }
        });
    }

    //评论图片适配器
    private void initaddimgAdapter() {
        commentAddImgAdaptter = new CommentAddImgAdaptter(R.layout.item_comment_add_img, albumFiles);
        rvCommentImg.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        rvCommentImg.setAdapter(commentAddImgAdaptter);
        commentAddImgAdaptter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.iv_comment_delete:
                        //删除图片
                        albumFiles.remove(position);
                        commentAddImgAdaptter.setNewData(albumFiles);
                        break;
                }
            }
        });
        //查看大图
        commentAddImgAdaptter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                showBigImg(imglist,position);
            }
        });
        //追加图片
        commentAddImgZJAdaptter = new CommentAddImgAdaptter(R.layout.item_comment_add_img, albumFilesZJ);
        rvCommentZjImg.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        rvCommentZjImg.setAdapter(commentAddImgZJAdaptter);
        commentAddImgZJAdaptter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.iv_comment_delete:
                        //删除图片
                        albumFilesZJ.remove(position);
                        commentAddImgZJAdaptter.setNewData(albumFilesZJ);
                        break;
                }
            }
        });
        //查看大图
        commentAddImgZJAdaptter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                showBigImg(imglistZj,position);
            }
        });

    }
    //显示大图
    private void showBigImg(ArrayList<String> imageList,int position){

        Album.gallery(this)
                .widget(Widget.newDarkBuilder(this)
                        .title("查看大图") // Title.
                       .build())// Toolbar color..build())// StatusBar color.)
                .checkedList(imageList) // List of image to view: ArrayList<String>.
                .checkable(false) // Whether there is a selection function.
                .currentPosition(position)
                .onResult(new Action<ArrayList<String>>() { // If checkable(false), action not required.
                    @Override
                    public void onAction(@NonNull ArrayList<String> result) {
                    }
                })
                .onCancel(new Action<String>() {
                    @Override
                    public void onAction(@NonNull String result) {
                    }
                })
                .start();
    }

    //初始化题目列表
    private void initCommentTmAdapter() {
        commentTmAdapter = new SalemanCommentTmAdapter(R.layout.item_comment_tm, new ArrayList<SalesmanCommentInfo.DataBean.QuestionBean>());
        rvCommentTm.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        rvCommentTm.setAdapter(commentTmAdapter);
    }

    @Override
    public void fillData() {
        new MyHttp().doPost(Api.getDefault().getSalesmanCommentInfo(BaseApplication.getInstance().readLoginUser().getSj_login_token(), firstCommentType), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                SalesmanCommentInfo info = new Gson().fromJson(result.toString(), SalesmanCommentInfo.class);
                GlideUtils.showMediumPic(SalesmanCommentActivity.this, civSelesmanHeadP, info.getData().getSalesman_headimg());
                commentTmAdapter.setNewData(info.getData().getQuestion());
                tvName.setText(info.getData().getSalesman_nickname());
                phone = info.getData().getSalesman_phone();
                //图片数组
                imglist = info.getData().getComment_photo();
                imglistZj = info.getData().getSuperaddition_comment_photo();

                //如果是点击追加评论进入的 则进入可编辑状态
                if (TextUtils.equals(activityType, "zj")) {
                    ivZjpm.setVisibility(View.INVISIBLE);
                    rbScore.setmClickable(true);
                    tvSubmit.setVisibility(View.VISIBLE);
                    ivAddImg.setVisibility(View.VISIBLE);
                    llZjComment.setVisibility(View.VISIBLE);
                    llComment.setVisibility(View.GONE);
                    commentTmAdapter.setEnabled(true);
                    commentAddImgAdaptter.setEditable(true);
                    commentAddImgZJAdaptter.setEditable(true);
                } else {
                    //常规进入 先判断是否已经评论过 有评论过直接显示数据  在判断是否是追加评论
                    //update_num没添加0 添加了1 修改了2
                    switch (info.getData().getUpdate_num()) {
                        case 0:
                            //第一次进
                            ivZjpm.setVisibility(View.INVISIBLE);
                            rbScore.setmClickable(true);
                            tvSubmit.setVisibility(View.VISIBLE);
                            ivAddImg.setVisibility(View.VISIBLE);
                            ivAddImgZj.setVisibility(View.VISIBLE);
                            llZjComment.setVisibility(View.GONE);
                            commentTmAdapter.setEnabled(true);
                            llComment.setVisibility(View.VISIBLE);
                            //可删除图片
                            commentAddImgAdaptter.setEditable(true);
                            commentAddImgZJAdaptter.setEditable(true);
                            break;
                        case 1:
                            //评论过一次
                            ivZjpm.setVisibility(View.VISIBLE);
                            rbScore.setmClickable(false);
                            rbScore.setStar( info.getData().getComment_rank());
                            tvSubmit.setVisibility(View.GONE);
                            ivAddImg.setVisibility(View.GONE);
                            commentTmAdapter.setEnabled(false);
                            llZjComment.setVisibility(View.GONE);
                            //可删除图片
                            commentAddImgAdaptter.setEditable(false);
                            commentAddImgZJAdaptter.setEditable(false);
                            //是否有过评论语或图片
                            if (TextUtils.isEmpty(info.getData().getComment_text())&&info.getData().getComment_photo().size()>0){
                                llComment.setVisibility(View.GONE);
                            }else {
                                llComment.setVisibility(View.VISIBLE);
                                etComment.setText(info.getData().getComment_text());
                                //让输入框不可编辑
                                etComment.setEnabled(false);
                                etComment.setFocusable(false);
                                //展示评论图片
                                albumFiles.clear();
                                for (int i = 0; i < info.getData().getComment_photo().size(); i++) {
                                    AlbumFile albumFile = new AlbumFile();
                                    albumFile.setPath(info.getData().getComment_photo().get(i));
                                    albumFiles.add(albumFile);
                                }
                                commentAddImgAdaptter.setNewData(albumFiles);

                            }
                            break;
                        case 2:
                            //追加过
                            ivZjpm.setVisibility(View.INVISIBLE);
                            rbScore.setmClickable(false);
                            rbScore.setStar((float)info.getData().getComment_rank());
                            tvSubmit.setVisibility(View.GONE);
                            ivAddImg.setVisibility(View.GONE);
                            ivAddImgZj.setVisibility(View.GONE);
                            commentTmAdapter.setEnabled(false);
                            //可删除图片
                            commentAddImgAdaptter.setEditable(false);
                            commentAddImgZJAdaptter.setEditable(false);
                            //是否有过评论语或图片
                            if (TextUtils.isEmpty(info.getData().getComment_text())&&info.getData().getComment_photo().size()==0){
                                llComment.setVisibility(View.GONE);
                            }else {
                                llComment.setVisibility(View.VISIBLE);
                                etComment.setText(info.getData().getComment_text());
                                //让输入框不可编辑
                                etComment.setEnabled(false);
                                etComment.setFocusable(false);
                                //展示评论图片
                                albumFiles.clear();
                                for (int i = 0; i < info.getData().getComment_photo().size(); i++) {
                                    AlbumFile albumFile = new AlbumFile();
                                    albumFile.setPath(info.getData().getComment_photo().get(i));
                                    albumFiles.add(albumFile);
                                }
                                commentAddImgAdaptter.setNewData(albumFiles);
                            }
                            //判断追加评论框
                            if (TextUtils.isEmpty(info.getData().getSuperaddition_comment_text())&&info.getData().getSuperaddition_comment_photo().size()==0){
                                llZjComment.setVisibility(View.GONE);
                            }else {
                                llZjComment.setVisibility(View.VISIBLE);
                                etCommentZj.setText(info.getData().getSuperaddition_comment_text());
                                //让输入框不可编辑
                                etCommentZj.setEnabled(false);
                                etCommentZj.setFocusable(false);
                                //展示评论图片
                                albumFilesZJ.clear();
                                for (int i = 0; i < info.getData().getSuperaddition_comment_photo().size(); i++) {
                                    AlbumFile albumFile = new AlbumFile();
                                    albumFile.setPath(info.getData().getSuperaddition_comment_photo().get(i));
                                    albumFilesZJ.add(albumFile);
                                }
                                commentAddImgZJAdaptter.setNewData(albumFilesZJ);
                            }
                            break;
                            default:break;
                    }
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


    @OnClick({R.id.iv_zjpm, R.id.iv_phone, R.id.iv_add_img, R.id.iv_add_img_zj, R.id.tv_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_zjpm:
                //追加评论
                SalesmanCommentActivity.actionStart(this,"zj");
                finish();
                break;
            case R.id.iv_phone:
                //电话
                if (!TextUtils.isEmpty(phone)){
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + phone));
                    startActivity(intent);
                }
                break;
            case R.id.iv_add_img:
                //添加评论图片
                showAlbum();
                break;
            case R.id.iv_add_img_zj:
                //追加评论图片
                showAlbumZJ();
                break;
            case R.id.tv_submit:
                //提交评论
                if (comment_rank == 0) {
                    showShortToast("请在头像右边进行星级评价");
                    return;
                }
                //检测是否答完所有题目
                if (!isCorrectFeedback()) {
                    showShortToast("请完成的所有评价题目");
                    return;
                }
                //提交
                upLoadPicUtils();
                break;
        }
    }

    //检测是否答完所有题目
    private boolean isCorrectFeedback() {
        //检测是否答完所有题目
        boolean isCorrectFeedback = true;
        for (int i = 0; i < commentTmAdapter.getData().size(); i++) {
            if (commentTmAdapter.getData().get(i).getQuestion_answer() == -1) {
                isCorrectFeedback = false;
            }
        }
        return isCorrectFeedback;
    }
    //选择图片
    private void showAlbum() {
        Album.image(this)
                .multipleChoice()
                .widget(Widget.newDarkBuilder(this)
                        .title("选择图片") // Title.
                        .mediaItemCheckSelector(ContextCompat.getColor(mActivity, R.color.white), ContextCompat.getColor(mActivity, R.color.app_color))//按钮颜色
                        .statusBarColor(ContextCompat.getColor(mActivity, R.color.app_color))
                        .toolBarColor(ContextCompat.getColor(mActivity, R.color.app_color)).build())// Toolbar color..build())// StatusBar color.)
                .camera(false)
                .columnCount(4)
                .selectCount(5)
                .checkedList(albumFiles)
                .onResult(new Action<ArrayList<AlbumFile>>() {
                    @Override
                    public void onAction(@NonNull ArrayList<AlbumFile> result) {
                        albumFiles = result;
                        imglist.clear();
                        for (int i = 0; i < result.size(); i++) {
                            imglist.add(result.get(i).getPath());
                        }
                        commentAddImgAdaptter.setNewData(albumFiles);
                    }
                }).start();
    }
    //选择追加图片
    private void showAlbumZJ() {
        Album.image(this)
                .multipleChoice()
                .widget(Widget.newDarkBuilder(this)
                        .title("选择图片") // Title.
                        .mediaItemCheckSelector(ContextCompat.getColor(mActivity, R.color.white), ContextCompat.getColor(mActivity, R.color.app_color))//按钮颜色
                        .statusBarColor(ContextCompat.getColor(mActivity, R.color.app_color))
                        .toolBarColor(ContextCompat.getColor(mActivity, R.color.app_color)).build())// Toolbar color..build())// StatusBar color.)
                .camera(false)
                .columnCount(4)
                .selectCount(5)
                .checkedList(albumFilesZJ)
                .onResult(new Action<ArrayList<AlbumFile>>() {
                    @Override
                    public void onAction(@NonNull ArrayList<AlbumFile> result) {
                        imglistZj.clear();
                        for (int i = 0; i < result.size(); i++) {
                            imglistZj.add(result.get(i).getPath());
                        }
                        albumFilesZJ = result;
                        commentAddImgZJAdaptter.setNewData(albumFilesZJ);
                    }
                }).start();
    }

    //提交评论
    private void submit(List<String> lstGoodsPhoto) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("sj_login_token", LoginUtil.getLoginToken());
        map.put("comment_rank", comment_rank);
        //答案
        ArrayList<QuestionAnswerInfo> answerInfos = new ArrayList<>();
        for (int i = 0; i < commentTmAdapter.getData().size(); i++) {
            QuestionAnswerInfo info = new QuestionAnswerInfo(commentTmAdapter.getData().get(i).getId(),commentTmAdapter.getData().get(i).getQuestion_answer());
            answerInfos.add(info);
        }
        map.put("question_answer", JSONArray.toJSONString(answerInfos));
        //评论语
        if (TextUtils.equals(activityType,"zc")){
            comment_text = etComment.getText().toString();
            map.put("comment_text", comment_text);
            //评论图片
            String comment_photo = "";
            for (int i = 0; i < lstGoodsPhoto.size(); i++) {
                comment_photo +=lstGoodsPhoto.get(i)+",";
            }
            //去掉多余的 ，
            if (lstGoodsPhoto.size()>0){
                comment_photo = comment_photo.substring(0,comment_photo.length()-1);
            }
            map.put("comment_photo",comment_photo );
        }else {
            comment_text = etCommentZj.getText().toString();
            map.put("comment_text", comment_text);
            //评论图片
            String superaddition_comment_photo = "";
            for (int i = 0; i < lstGoodsPhoto.size(); i++) {
                superaddition_comment_photo +=lstGoodsPhoto.get(i)+",";
            }
            //去掉多余的 ，
            if (lstGoodsPhoto.size()>0){
                superaddition_comment_photo = superaddition_comment_photo.substring(0,superaddition_comment_photo.length()-1);
            }
            map.put("comment_photo",superaddition_comment_photo );
        }
        

        new MyHttp().doPost(Api.getDefault().addSalesmanComment(map), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                activityType = "zc";
                firstCommentType = "1";
                fillData();
            }

            @Override
            public void onError(int code) {
                
            }
        });
    }
    //将图片上传到
    private void upLoadPicUtils(){
        LoadingDialog.showDialogForLoading(mActivity, "上传中...", false);
        final ArrayList<String> lstGoodsPhoto = new ArrayList<>();
        if (TextUtils.equals(activityType,"zc")){
            for (int i = 0; i < albumFiles.size(); i++) {
                lstGoodsPhoto.add(albumFiles.get(i).getPath());
            }
        }
        else {
            for (int i = 0; i < albumFilesZJ.size(); i++) {
                lstGoodsPhoto.add(albumFilesZJ.get(i).getPath());
            }
        }
        //有图片就先上传到七牛
        if (lstGoodsPhoto.size()>0){
            //上传到七牛
            UpLoadPicUtils.batchUpload(lstGoodsPhoto, new UpLoadPicUtils.BatchUpLoadPicListener() {
                @Override
                public void success(List<String> qiNiuPath) {
                    //提交
                    submit(qiNiuPath);
                }

                @Override
                public void error() {
                    showShortToast("图片上传失败，请重试！");
                    LoadingDialog.cancelDialogForLoading();
                }
            });
        }else {
            submit(lstGoodsPhoto);
        }

    }

}
