package com.wbx.merchant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.flyco.roundview.RoundTextView;
import com.google.gson.Gson;
import com.wbx.merchant.R;
import com.wbx.merchant.adapter.VideoVoucherAdapter;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.bean.VideoVVoucherListBean;
import com.wbx.merchant.common.LoginUtil;
import com.wbx.merchant.dialog.ConfirmDialog;
import com.wbx.merchant.widget.LoadingDialog;
import com.wbx.merchant.widget.refresh.BaseRefreshListener;
import com.wbx.merchant.widget.refresh.PullToRefreshLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;

//视频抵用券列表
public class VideoVoucherListActivity extends BaseActivity {

    @Bind(R.id.title_name_tv)
    TextView titleNameTv;
    @Bind(R.id.tv_right)
    TextView tvRight;
    @Bind(R.id.order_recycler_view)
    RecyclerView rv;
    @Bind(R.id.refresh_layout)
    PullToRefreshLayout refreshLayout;
    @Bind(R.id.tv_add_ddtc)
    RoundTextView tvAddDdtc;
    VideoVoucherAdapter videoVoucherAdapter;
    @Override
    public int getLayoutId() {
        return R.layout.activity_video_voucher_list;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        refreshLayout.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                getList();
            }

            @Override
            public void loadMore() {

            }
        });
        refreshLayout.setCanLoadMore(false);

        videoVoucherAdapter = new VideoVoucherAdapter();
        rv.setLayoutManager(new LinearLayoutManager(mContext));
        rv.setAdapter(videoVoucherAdapter);

        rv.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(@NonNull View view) {

            }

            @Override
            public void onChildViewDetachedFromWindow(@NonNull View view) {
                JZVideoPlayerStandard jzvd = view.findViewById(R.id.jz_video);

                        JZVideoPlayerStandard.releaseAllVideos();
            }
        });

        videoVoucherAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()){
                    case R.id.tv_bj:
                        //编辑
                        IssueVideoActivity.actionStart(mContext,"1",videoVoucherAdapter.getItem(position).getVideo_promotion_id());
                        break;
                    case R.id.tv_sc:
                        //删除
                        ConfirmDialog confirmDialog =ConfirmDialog.newInstance("删除后，相关统计数据将消失 您确认要删除？");
                        confirmDialog.setDialogListener(new ConfirmDialog.DialogListener() {
                            @Override
                            public void dialogClickListener() {
                                LoadingDialog.showDialogForLoading(mActivity);
                                delete_video_promotion(videoVoucherAdapter.getItem(position).getVideo_promotion_id());
                            }
                        });
                        confirmDialog.show(getSupportFragmentManager(),"");
                        break;
                    case R.id.tv_sjtj:
                        //数据统计
                        ReportFormsSetMealActivity.actionStart(mActivity,videoVoucherAdapter.getItem(position).getShop_set_meal_id());
                        break;
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getList();
    }

    private void getList(){
        new MyHttp().doPost(Api.getDefault().list_video_promotion(LoginUtil.getLoginToken()), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                VideoVVoucherListBean videoVVoucherListBean = new Gson().fromJson(result.toString(),VideoVVoucherListBean.class);
                videoVoucherAdapter.setNewData(videoVVoucherListBean.getData());
                refreshLayout.finishRefresh();
            }

            @Override
            public void onError(int code) {
                refreshLayout.finishRefresh();
            }
        });
    }

    @Override
    public void fillData() {

    }

    @Override
    public void setListener() {

    }

    private void delete_video_promotion(String video_promotion_id){
        LoadingDialog.showDialogForLoading(mActivity);
        new MyHttp().doPost(Api.getDefault().delete_video_promotion(LoginUtil.getLoginToken(), video_promotion_id), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                getList();
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    @OnClick({R.id.tv_right, R.id.tv_add_ddtc})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_right:
                startActivity(new Intent(mContext,VideoGzsmActivity.class));
                break;
            case R.id.tv_add_ddtc:
                IssueVideoActivity.actionStart(mContext,"0","");
                break;
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