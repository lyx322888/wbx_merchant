package com.wbx.merchant.activity;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.wbx.merchant.bean.SalesmanCommentInfo;
import com.wbx.merchant.common.LoginUtil;
import com.wbx.merchant.utils.GlideUtils;
import com.wbx.merchant.widget.CircleImageView;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumFile;
import com.yanzhenjie.album.api.widget.Widget;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
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
    private String firstCommentType = "1";//1为添加第一次或已添加过一次时获取  2为追加时获取
    private SalemanCommentTmAdapter commentTmAdapter;
    private CommentAddImgAdaptter commentAddImgAdaptter;
    private ArrayList<AlbumFile> albumFiles =new ArrayList<>();
    private float comment_rank  ;
    private String comment_text  = "" ;

    @Override
    public int getLayoutId() {
        return R.layout.activity_salesman_comment;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
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
        commentAddImgAdaptter = new CommentAddImgAdaptter(R.layout.item_comment_add_img,albumFiles);
        rvCommentImg.setLayoutManager(new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false));
        rvCommentImg.setAdapter(commentAddImgAdaptter);
        commentAddImgAdaptter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()){
                    case R.id.iv_comment_delete:
                        //删除图片
                        albumFiles.remove(position);
                        commentAddImgAdaptter.setNewData(albumFiles);
                        break;
                }
            }
        });
    }

    //初始化题目列表
    private void initCommentTmAdapter() {
        commentTmAdapter = new SalemanCommentTmAdapter(R.layout.item_comment_tm,new ArrayList<SalesmanCommentInfo.DataBean.QuestionBean>());
        rvCommentTm.setLayoutManager(new LinearLayoutManager(this){
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
                tvName.setText(info.getData().getSalesman_nickname());
                if (TextUtils.equals(firstCommentType,"2")){
                    //如果是已追加评论的 直接显示
                    rbScore.setmClickable(false);
                    rbScore.setStar(info.getData().getComment_rank());
                    tvSubmit.setVisibility(View.GONE);
                }
                commentTmAdapter.setNewData(info.getData().getQuestion());
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
                break;
            case R.id.iv_phone:
                //电话
                break;
            case R.id.iv_add_img:
                //添加评论图片
                showAlbum();
                break;
            case R.id.iv_add_img_zj:
                //追加评论图片
                break;
            case R.id.tv_submit:
                //提交评论
                if (comment_rank==0){
                    showShortToast("请在头像右边进行星级评价");
                    return;
                }
                //检测是否答完所有题目
                if (!isCorrectFeedback ()){
                    showShortToast("请完成的所有评价题目");
                    return;
                }
                //提交
                submit();
                break;
        }
    }
    //检测是否答完所有题目
    private boolean isCorrectFeedback (){
        //检测是否答完所有题目
        boolean isCorrectFeedback = true;
        for (int i = 0; i < commentTmAdapter.getData().size(); i++) {
            if (commentTmAdapter.getData().get(i).getQuestion_answer()==0){
                isCorrectFeedback = false;
            }
        }
        return isCorrectFeedback;
    }
    //选择图片
    private void showAlbum() {
        Album.image(this)
                .multipleChoice()
                .widget( Widget.newDarkBuilder(this)
                        .title("选择图片") // Title.
                        .mediaItemCheckSelector(ContextCompat.getColor(mActivity,R.color.white), ContextCompat.getColor(mActivity,R.color.app_color))//按钮颜色
                        .statusBarColor(ContextCompat.getColor(mActivity,R.color.app_color))
                        .toolBarColor(ContextCompat.getColor(mActivity,R.color.app_color)).build())// Toolbar color..build())// StatusBar color.)
                .camera(false)
                .columnCount(4)
                .selectCount(5)
                .checkedList(albumFiles)
                .onResult(new Action<ArrayList<AlbumFile>>() {
                    @Override
                    public void onAction(@NonNull ArrayList<AlbumFile> result) {
                        albumFiles = result;
                        commentAddImgAdaptter.setNewData(albumFiles);
                    }
                }).start();
    }
    //提交评论
    private void submit(){
        HashMap<String,Object> map = new HashMap<>();
        map.put("sj_login_token", LoginUtil.getLoginToken());
        map.put("comment_rank", comment_rank);
        //评论语
        comment_text = etComment.getText().toString();
        map.put("comment_text", comment_text);
        //评论图片

        new MyHttp().doPost(Api.getDefault().addSalesmanComment(map), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {

            }

            @Override
            public void onError(int code) {

            }
        });
    }

}
