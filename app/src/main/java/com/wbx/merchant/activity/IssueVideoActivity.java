package com.wbx.merchant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.bigkoo.pickerview.OptionsPickerView;
import com.flyco.roundview.RoundTextView;
import com.google.gson.Gson;
import com.wbx.merchant.R;
import com.wbx.merchant.adapter.AddDdtcAdapter;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.baseapp.AppConfig;
import com.wbx.merchant.bean.CricePayBaen;
import com.wbx.merchant.bean.StoreSetMealBean;
import com.wbx.merchant.bean.VideoCalssifyBean;
import com.wbx.merchant.bean.WxPayInfo;
import com.wbx.merchant.common.LoginUtil;
import com.wbx.merchant.dialog.PayDialog;
import com.wbx.merchant.dialog.SelectDdtcDialog;
import com.wbx.merchant.dialog.SelectVideoDialog;
import com.wbx.merchant.dialog.TkfwDialog;
import com.wbx.merchant.utils.PayUtils;
import com.wbx.merchant.utils.UpLoadPicUtils;
import com.wbx.merchant.widget.LoadingDialog;
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

//发布视频抵用券
public class IssueVideoActivity extends BaseActivity {

    @Bind(R.id.title_name_tv)
    TextView titleNameTv;
    @Bind(R.id.tv_tc)
    TextView tvTc;
    @Bind(R.id.tv_video_type)
    TextView tvVideoType;
    @Bind(R.id.ll_select_video_type)
    LinearLayout llSelectVideoType;
    @Bind(R.id.iv_choose_video)
    ImageView ivChooseVideo;
    @Bind(R.id.video_view)
    JZVideoPlayerStandard videoView;
    @Bind(R.id.iv_video_gb)
    ImageView ivVideoGb;
    @Bind(R.id.iv_add_ddtc)
    ImageView ivAddDdtc;
    @Bind(R.id.rv_ddtc)
    RecyclerView rvDdtc;
    @Bind(R.id.tv_cxxz)
    RoundTextView tvCxxz;
    @Bind(R.id.tv_delete)
    RoundTextView tvDelete;
    @Bind(R.id.ll_ddtc)
    LinearLayout llDdtc;
    @Bind(R.id.ll_sp)
    LinearLayout llSp;
    @Bind(R.id.cb_tkfw)
    CheckBox cbTkfw;
    @Bind(R.id.ll_tkfy)
    LinearLayout llTkfy;
    @Bind(R.id.tv_submit)
    RoundTextView tvSubmit;

    private String videoUrl;
    AddDdtcAdapter addDdtcAdapter;
    private static final int REQUEST_CHOOSE_VIDEO = 1001;
    private OptionsPickerView pv;
    String video_promotion_classify_id ="";
    String shop_set_meal_id ="";//套餐id
    private TkfwDialog tkfwDialog;
    private String price = "";
    private PayUtils payUtils;

    @Override
    public int getLayoutId() {
        return R.layout.activity_issue_video;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        addDdtcAdapter = new AddDdtcAdapter();
        rvDdtc.setLayoutManager(new LinearLayoutManager(mContext){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        rvDdtc.setAdapter(addDdtcAdapter);

        cbTkfw.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                if (TextUtils.isEmpty(price)){
                    showDialog();
                }else {
                    tvSubmit.setText(String.format("发布(支付%s元)",price));
                }
            }else {
                tvSubmit.setText("发布");
            }
        });

        payUtils = new PayUtils(mActivity, new PayUtils.OnAlipayListener() {
            @Override
            public void onSuccess() {
                showShortToast("添加成功！");
            }
        });
    }

    @Override
    public void fillData() {

    }

    //获取视频类型
    private void getVideoTypeData(){
        new MyHttp().doPost(Api.getDefault().get_video_promotion_classify(LoginUtil.getLoginToken()), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                VideoCalssifyBean videoCalssifyBean = new Gson().fromJson(result.toString(),VideoCalssifyBean.class);
                initVidioType(videoCalssifyBean.getData());
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    private void initVidioType(List<VideoCalssifyBean.DataBean> data) {
        //数据源
        final List<String> strings = new ArrayList<>();
        final List<List<String>> string2 = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            strings.add(data.get(i).getName());
            final List<String> items = new ArrayList<>();
            for (int j = 0; j < data.get(i).getJunior().size(); j++) {
                items.add(data.get(i).getJunior().get(j).getName());
            }
            string2.add(items);
        }

        pv = new OptionsPickerView(new OptionsPickerView.Builder(mContext, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                tvVideoType.setText(data.get(options1).getName()+"-"+data.get(options1).getJunior().get(options2).getName());
                video_promotion_classify_id = data.get(options1).getJunior().get(options2).getVideo_promotion_classify_id();
            }
        }).setSubmitText("确定")//确定按钮文字
                .setCancelText("取消")//取消按钮文字
                .setSubCalSize(18)//确定和取消文字大小
                .setTitleSize(20)//标题文字大小
                .setTitleColor(ContextCompat.getColor(mContext, R.color.black))//标题文字颜色
                .setSubmitColor(ContextCompat.getColor(mContext, R.color.app_color))//确定按钮文字颜色
                .setCancelColor(ContextCompat.getColor(mContext, R.color.black))//取消按钮文字颜色
//                .setTitleBgColor(0xFF333333)//标题背景颜色 Night mode
//                .setBgColor(0xFF000000)//滚轮背景颜色 Night mode
                .setContentTextSize(18)//滚轮文字大小
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setCyclic(false, false, false)//循环与否
                .setSelectOptions(0)  //设置默认选中项
                .setOutSideCancelable(true)//点击外部dismiss default true
        );
        pv.setPicker(strings,string2);//添加数据源

        pv.show();

    }

    @Override
    public void setListener() {

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
                    videoView.setUp(videoUrl, JZVideoPlayer.SCREEN_WINDOW_LIST, "视频");
//                    videoView.startVideo();
//                    ConfirmDialog confirmDialog = ConfirmDialog.newInstance("是否压缩视频？");
//                    confirmDialog.setDialogListener(() -> {
//
//                    });
//                    new Handler().postDelayed(() -> confirmDialog.show(getSupportFragmentManager(), ""), 500);
                })
                .start();
    }

    @OnClick({R.id.tv_cxxz,R.id.tv_delete,R.id.ll_select_video_type, R.id.iv_add_ddtc, R.id.ll_tkfy, R.id.tv_submit, R.id.iv_choose_video, R.id.iv_video_gb})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_select_video_type:
                if (pv!=null){
                    pv.show();
                }else {
                    getVideoTypeData();
                }
                break;
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
            case R.id.iv_video_gb:
                videoUrl = null;
                JZVideoPlayer.releaseAllVideos();
                ivChooseVideo.setVisibility(View.VISIBLE);
                ivVideoGb.setVisibility(View.GONE);
                videoView.setVisibility(View.GONE);
                videoView.onCompletion();
                videoView.setVisibility(View.GONE);
                break;
            case R.id.iv_add_ddtc:
                //添加到店套餐
               addDDtc();
                break;
            case R.id.tv_cxxz:
                //重新选择
                addDDtc();
                break;
            case R.id.tv_delete:
                addDdtcAdapter.setNewData(new ArrayList<>());
                ivAddDdtc.setVisibility(View.VISIBLE);
                llDdtc.setVisibility(View.GONE);
                shop_set_meal_id = "";
                break;
            case R.id.ll_tkfy:
                //拓客服务
                showDialog();
                break;
            case R.id.tv_submit:
                //发布
                if (TextUtils.isEmpty(video_promotion_classify_id)){
                    showShortToast("请选择视频类型");
                    return;
                }
                if (TextUtils.isEmpty(videoUrl)){
                    showShortToast("请上传视频");
                    return;
                }
                if (TextUtils.isEmpty(shop_set_meal_id)){
                    showShortToast("请添加到店套餐");
                    return;
                }
                //是否选择拓客服务
                if (cbTkfw.isChecked()){
                    //拓客服务发布
                    if (TextUtils.isEmpty(price)){
                        showShortToast("请选择拓客服务套餐");
                        return;
                    }
                    PayDialog payDialog =PayDialog.newInstance(price);
                    payDialog.setDialogListener(new PayDialog.DialogListener() {
                        @Override
                        public void dialogClickListener(String price, String payMode) {
                            payDialog.dismiss();
                            upOnevideo(price,payMode);
                        }
                    });
                    payDialog.show(getSupportFragmentManager(),"");
                }else {
                    //直接发布
                    upOnevideo("","");
                }
                break;
        }
    }

    //提交
    private void add_video_promotion(String pay_money,String code){
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("sj_login_token",LoginUtil.getLoginToken());
        hashMap.put("video_promotion_classify_id",video_promotion_classify_id);
        hashMap.put("video",videoUrl);
        hashMap.put("discounts_type","2");
        hashMap.put("shop_set_meal_id",shop_set_meal_id);
        hashMap.put("is_talk",cbTkfw.isChecked()?"1":"0");
        hashMap.put("pay_money",pay_money);
        hashMap.put("code",code);
        LoadingDialog.showDialogForLoading(mActivity);
        new MyHttp().doPost(Api.getDefault().add_video_deduction_coupon(hashMap), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                if (cbTkfw.isChecked()){
                    if (code.equals(AppConfig.PayMode.alipay)) {
                        payUtils.startAliPay(result.getString("data"));
                    } else {
                        WxPayInfo wxPayInfo = result.getObject("data", WxPayInfo.class);
                        payUtils.startWxPay(wxPayInfo);
                    }
                }else {
                    showShortToast("添加成功");
                }
            }

            @Override
            public void onError(int code) {

            }
        });
    }


    //上传视频到七牛
    public void upOnevideo(String pay_money,String code) {
        //是否上传本地视频
        if (videoUrl.contains("http")){
            add_video_promotion(pay_money,code);
        }else {
            LoadingDialog.showDialogForLoading(mActivity, "上传中", false);
            UpLoadPicUtils.upOnePic(videoUrl, new UpLoadPicUtils.UpLoadPicListener() {
                @Override
                public void success(String qiNiuPath) {
                    videoUrl  = qiNiuPath;
                    add_video_promotion(pay_money,code);
                    LoadingDialog.cancelDialogForLoading();
                }
                @Override
                public void error() {
                    showShortToast("上传失败，请稍后重试");
                    LoadingDialog.cancelDialogForLoading();
                }
            });
        }

    }

    //拓客服务
    private void showDialog(){
        if (tkfwDialog==null){
            tkfwDialog = TkfwDialog.newInstance();
            tkfwDialog.setDialogListener(new TkfwDialog.DialogListener() {
                @Override
                public void dialogClickListener(CricePayBaen cricePayBaen) {
                    price = cricePayBaen.price;
                    tvTc.setText(price+"元套餐");
                    if (cbTkfw.isChecked()){
                        tvSubmit.setText(String.format("发布(支付%s元)",price));
                    }
                }
            });
        }

        tkfwDialog.show(getSupportFragmentManager(),"");
    }

    //添加到店套餐
    private void addDDtc(){
        SelectDdtcDialog selectDdtcDialog =new SelectDdtcDialog(mContext);
        selectDdtcDialog.show();
        selectDdtcDialog.setDialogListener(new SelectDdtcDialog.DialogListener() {
            @Override
            public void dialogBeanClickListener(StoreSetMealBean.DataBean dataBean) {
                List<StoreSetMealBean.DataBean> dataBeans = new ArrayList<>();
                dataBeans.add(dataBean);
                addDdtcAdapter.setNewData(dataBeans);
                ivAddDdtc.setVisibility(View.GONE);
                llDdtc.setVisibility(View.VISIBLE);
                shop_set_meal_id= dataBean.getShop_set_meal_id();
            }
        });
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
         if (data != null && requestCode == REQUEST_CHOOSE_VIDEO) {
            ivChooseVideo.setVisibility(View.GONE);
            ivVideoGb.setVisibility(View.VISIBLE);
            videoUrl = data.getStringExtra("URL");
            videoView.setVisibility(View.VISIBLE);
            videoView.setUp(videoUrl, JZVideoPlayer.SCREEN_WINDOW_NORMAL, "视频");
            videoView.startVideo();
        }
    }
}