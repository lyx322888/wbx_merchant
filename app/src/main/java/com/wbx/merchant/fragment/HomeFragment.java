package com.wbx.merchant.fragment;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.popwindowutils.CustomPopWindow;
import com.flyco.roundview.RoundTextView;
import com.google.gson.Gson;
import com.huawei.hms.support.api.entity.auth.AbstractResp;
import com.kyleduo.switchbutton.SwitchButton;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wbx.merchant.R;
import com.wbx.merchant.activity.ActivityManagerActivity;
import com.wbx.merchant.activity.jhzf.AggregatePayActivity;
import com.wbx.merchant.activity.BookSeatActivity;
import com.wbx.merchant.activity.BusinessMustActivity;
import com.wbx.merchant.activity.CompleteInformationActivity;
import com.wbx.merchant.activity.DaDaActivity;
import com.wbx.merchant.activity.GoodsManagerActivity;
import com.wbx.merchant.activity.InComeActivity;
import com.wbx.merchant.activity.IntelligentReceiveActivity;
import com.wbx.merchant.activity.InventoryAnalyzeActivity;
import com.wbx.merchant.activity.jhzf.CloseAnAccountActivity;
import com.wbx.merchant.activity.jhzf.CredentialsActivity;
import com.wbx.merchant.activity.jhzf.JdShopInfoActivity;
import com.wbx.merchant.activity.jhzf.JingdongDetailActivity;
import com.wbx.merchant.activity.LoginActivity;
import com.wbx.merchant.activity.MIneActivity;
import com.wbx.merchant.activity.NumberOrderActivity;
import com.wbx.merchant.activity.OrderActivity;
import com.wbx.merchant.activity.ReceiverListActivity;
import com.wbx.merchant.activity.RunAnalyzeActivity;
import com.wbx.merchant.activity.ScanOrderActivity;
import com.wbx.merchant.activity.SeatActivity;
import com.wbx.merchant.activity.SendSettingActivity;
import com.wbx.merchant.activity.StoreManagerActivity;
import com.wbx.merchant.activity.WebActivity;
import com.wbx.merchant.activity.jhzf.RedEnvelopeCodeActivity;
import com.wbx.merchant.adapter.ShareShopGoodsAdapter;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseApplication;
import com.wbx.merchant.base.BaseFragment;
import com.wbx.merchant.baseapp.AppConfig;
import com.wbx.merchant.baseapp.AppManager;
import com.wbx.merchant.bean.AuditResult;
import com.wbx.merchant.bean.ShopDetailInfo;
import com.wbx.merchant.bean.ShopFxinfo;
import com.wbx.merchant.bean.ShopIdentityBean;
import com.wbx.merchant.bean.ShopInfo;
import com.wbx.merchant.common.LoginUtil;
import com.wbx.merchant.dialog.DaDaCouponDialog;
import com.wbx.merchant.dialog.MiniProgramDialog;
import com.wbx.merchant.utils.FormatUtil;
import com.wbx.merchant.utils.GlideImageLoader;
import com.wbx.merchant.utils.GlideUtils;
import com.wbx.merchant.utils.SPUtils;
import com.wbx.merchant.utils.ScannerUtils;
import com.wbx.merchant.utils.ShareUtils;
import com.wbx.merchant.widget.CircleImageView;
import com.wbx.merchant.widget.LoadingDialog;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPreview;

/**
 * 首页
 */
public class HomeFragment extends BaseFragment {

    @Bind(R.id.iv_msg)
    ImageView ivMsg;
    @Bind(R.id.ic_yr)
    ImageView icYr;
    @Bind(R.id.ic_fx)
    ImageView icFx;
    @Bind(R.id.iv_dyj)
    ImageView ivDyj;
    @Bind(R.id.iv_set)
    ImageView ivSet;
    @Bind(R.id.civ_head)
    CircleImageView civHead;
    @Bind(R.id.tv_shop_name)
    TextView tvShopName;
    @Bind(R.id.btn_switch)
    SwitchButton btnSwitch;
    @Bind(R.id.fl_ewm)
    FrameLayout flEwm;
    @Bind(R.id.tv_ye_click)
    TextView tvYeClick;
    @Bind(R.id.tv_ye)
    TextView tvYe;
    @Bind(R.id.tv_jrsr)
    TextView tvJrsr;
    @Bind(R.id.tv_zrsr)
    TextView tvZrsr;
    @Bind(R.id.tv_dps)
    TextView tvDps;
    @Bind(R.id.tv_dtk)
    TextView tvDtk;
    @Bind(R.id.fl_info)
    FrameLayout flInfo;
    @Bind(R.id.xbanner_view)
    Banner xbannerView;

    @Bind(R.id.tv_msg)
    RoundTextView tvMsg;
    @Bind(R.id.ll_ddgl)
    LinearLayout llDdgl;
    private ShopInfo shopInfo;

    private Dialog dialog;
    private EditText inputEdit;
    private Button cancelBtn;
    private Button completeBtn;
    private MyReceiver myReceiver;
    private ShopDetailInfo shopDetailInfo;
    private AuditResult auditResult ;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_home;
    }

    @Override
    public void initPresenter() {
        IntentFilter filter = new IntentFilter(AppConfig.REFRESH_UI);
        myReceiver = new MyReceiver();
        getActivity().registerReceiver(myReceiver, filter);
    }

    @Override
    protected void initView() {
//        btnSwitch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (shopInfo != null) {
//                    if (shopInfo.getIs_open() == 1) {
//                        postSw(0);
//                    } else {
//                        postSw(1);
//                    }
//                }
//            }
//        });

        btnSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Log.e("dfdf", "initView: "+isChecked );
            if (isChecked){
                postSw(1);
            }else {
                postSw(0);
            }
        });

        if (loginUser == null || loginUser.getGrade_id() == AppConfig.StoreType.FOOD_STREET) {
            llDdgl.setVisibility(View.VISIBLE);
        }else {
            llDdgl.setVisibility(View.GONE);
        }
    }

    private void postSw(final int is_open) {
        new MyHttp().doPost(Api.getDefault().updateIsOpen(LoginUtil.getLoginTokenRegister(), is_open), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                if (shopInfo!=null){
                    shopInfo.setIs_open(is_open);
                }
                updataShopState();
            }

            @Override
            public void onError(int code) {
                if (shopInfo!=null){
                    shopInfo.setIs_open(is_open == 1 ? 0 : 1);
                }
                updataShopState();
                getShopIdentity();
            }
        });
    }

    //完善信息弹窗
    private void getShopIdentity() {

        new MyHttp().doPost(Api.getDefault().getShopIdentity(LoginUtil.getLoginTokenRegister()), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                ShopIdentityBean shopIdentityBean = new Gson().fromJson(result.toString(), ShopIdentityBean.class);
                switch (shopIdentityBean.getData().getAudit()) {
                    //0未核审 1已核审 2核审未通过
                    case 0:
//                    "请等待审核"
                        break;
                    case 2:
//                     您的审核未通过请重新上传身份证
                        popWsxs();
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onError(int code) {
                popWsxs();
            }
        });
    }

    //完善信息弹窗
    private void popWsxs() {
        final View shareInflate = LayoutInflater.from(getContext()).inflate(R.layout.pop_wsxx, null);
        ImageView imageView = shareInflate.findViewById(R.id.iv_wxss);
        //dialog
        final CustomPopWindow shareDialog = new CustomPopWindow.PopupWindowBuilder(getContext())
                .setView(shareInflate)
                .size(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .setAnimationStyle(R.style.AlphaEnterExitAnimation)
                .enableBackgroundDark(true)
                .create()
                .showAtLocation(shareInflate, Gravity.CENTER, 0, 0);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), CompleteInformationActivity.class));
                shareDialog.dissmiss();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getShopInfo();
    }

    @Override
    public void onStart() {
        super.onStart();
        xbannerView.startAutoPlay();
    }

    @Override
    public void onStop() {
        super.onStop();
        xbannerView.stopAutoPlay();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(myReceiver);
    }

    @Override
    protected void fillData() {
        if (null == loginUser) {
            showShortToast("账号信息不存在，请重新登录！");
            AppManager.getAppManager().finishAllActivity();
            SPUtils.setSharedBooleanData(getActivity(), AppConfig.LOGIN_STATE, false);
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }

        List<Drawable> imgurl = new ArrayList<>();
        imgurl.add(ContextCompat.getDrawable(getContext(), R.mipmap.ic_jhzf));
        imgurl.add(ContextCompat.getDrawable(getContext(), R.mipmap.ic_hbm));
        imgurl.add(ContextCompat.getDrawable(getContext(), R.drawable.vidio_study));
        xbannerView.setBannerStyle(BannerConfig.NOT_INDICATOR);
        xbannerView.setImages(imgurl).setDelayTime(10000).setImageLoader(new GlideImageLoader()).start();
        xbannerView.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                switch (position) {
                    case 0://聚合支付
                        if (auditResult!=null){
                            switch (auditResult.getData().getStatus()){
                                case "PASS"://已通过
                                case "WAITCHANNELAUDIT"://审核中
                                    showShortToast("您的报单已提交审核，请注意查看短信");
                                    break;
                                default:
                                    Intent intent = new Intent(getContext(), AggregatePayActivity.class);
                                    intent.putExtra("auditOpinion",auditResult.getData().getAuditOpinion());
                                    startActivity(intent);
                                    break;
                            }
                        }else {
                            if (shopDetailInfo!=null){
                                //入驻流程
                                switch (shopDetailInfo.getDuolabao_step()){
                                    case 2:
                                        //结算信息
                                        startActivity(new Intent(getContext(), CloseAnAccountActivity.class));
                                        break;
                                    case 3:
                                        //店铺信息
                                        startActivity(new Intent(getContext(), JdShopInfoActivity.class));
                                        break;
                                    case 4:
                                        //证件信息
                                        startActivity(new Intent(getContext(),CredentialsActivity.class));
                                        break;
                                    case 5:
                                        //报单提交
                                        startActivity(new Intent(getContext(),CredentialsActivity.class));
                                        break;
                                    case 6:
                                        //报单确认提交
                                        startActivity(new Intent(getContext(),CredentialsActivity.class));
                                        break;
                                    case 7:
                                        showShortToast("您的报单已提交审核，请注意查看短信");
                                        break;
                                    default:
                                        startActivity(new Intent(getContext(), AggregatePayActivity.class));
                                        break;
                                }
                            }
                        }
                        break;
                    case 1:
                        //红包码
                        startActivity(new Intent(getContext(), RedEnvelopeCodeActivity.class));
                        break;
                    case 2:
                        WebActivity.actionStart(getContext(), "http://www.wbx365.com/Wbxwaphome/video");
                        break;
                }
            }
        });

    }

    //获取首页 店铺的信息
    private void getShopInfo() {
        if (null == loginUser) {
            showShortToast("账号信息不存在，请重新登录！");
            AppManager.getAppManager().finishAllActivity();
            SPUtils.setSharedBooleanData(getActivity(), AppConfig.LOGIN_STATE, false);
            startActivity(new Intent(getActivity(), LoginActivity.class));
            return;
        }
        LoadingDialog.showDialogForLoading(getActivity(), "数据获取中...", false);
        new MyHttp().doPost(Api.getDefault().getShopInfo(loginUser.getSj_login_token()), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                shopInfo = JSONObject.parseObject(result.getString("data"), ShopInfo.class);
                loginUser.setIsSubscribe(shopInfo.getIs_subscribe());
                loginUser.setEnd_date(shopInfo.getEnd_date());
                loginUser.setShopPhoto(shopInfo.getPhoto());
                //保存用户头像及昵称
                SPUtils.setSharedStringData(getContext(), AppConfig.LOGIN_PHOTO, shopInfo.getPhoto());
                SPUtils.setSharedStringData(getContext(), AppConfig.LOGIN_NAME, shopInfo.getShop_name());
                SPUtils.setSharedIntData(getContext(), AppConfig.TRY_SHOP, shopInfo.getTry_shop());
                loginUser.setScan_order_type(shopInfo.getScan_order_type());
                loginUser.setGrade_id(shopInfo.getGrade_id());
                BaseApplication.getInstance().saveUserInfo(loginUser);
                setData();
            }

            @Override
            public void onError(int code) {

            }

        });

        //店铺详细信息
        new MyHttp().doPost(Api.getDefault().getShopDetail(loginUser.getSj_login_token()), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                shopDetailInfo = JSONObject.parseObject(result.getString("data"), ShopDetailInfo.class);
            }

            @Override
            public void onError(int code) {

            }
        });

        //获取聚合支付报单结果
        new MyHttp().doPost(Api.getDefault().get_audit_result(LoginUtil.getLoginToken()), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                 auditResult = new Gson().fromJson(result.toString(),AuditResult.class);
            }

            @Override
            public void onError(int code) {

            }
        });

    }

    //填充数据
    private void setData() {
        updataShopState();
        tvDps.setText(String.valueOf(shopInfo.getNew_order_num()));
        tvDtk.setText(String.valueOf(shopInfo.getDtk_order_num()));

        //扫码订单
//        if (shopInfo.getScan_order_no_pay_count() > 0) {
//            tvScanOrderNum.setVisibility(View.VISIBLE);
//            tvScanOrderNum.setText(String.valueOf(shopInfo.getScan_order_no_pay_count()));
//        } else {
//            tvScanOrderNum.setVisibility(View.GONE);
//        }
//        if (shopInfo.getLater_subscribe_order_num() > 0) {
//            tvBookOrderNum.setVisibility(View.VISIBLE);
//            tvBookOrderNum.setText(String.valueOf(shopInfo.getLater_subscribe_order_num()));
//        } else {
//            tvBookOrderNum.setVisibility(View.GONE);
//        }
//        if (shopInfo.getTake_number_no_read_num() > 0) {
//            tvNumberOrderNum.setVisibility(View.VISIBLE);
//            tvNumberOrderNum.setText(String.valueOf(shopInfo.getTake_number_no_read_num()));
//        } else {
//            tvNumberOrderNum.setVisibility(View.GONE);
//        }
//        if (shopInfo.getGoods_num() > 0) {
//            tvGoodsManagerNum.setVisibility(View.VISIBLE);
//            tvGoodsManagerNum.setText(String.valueOf(shopInfo.getGoods_num()));
//        } else {
//            tvGoodsManagerNum.setVisibility(View.GONE);
//        }
        int money_yesterday = shopInfo.getMoney_yesterday();
        int money_day = shopInfo.getMoney_day();
        int gold = shopInfo.getGold();
        tvZrsr.setText(money_yesterday == 0 ? "¥0.00" : String.format("¥%.2f", money_yesterday / 100.00));
        tvJrsr.setText(money_day == 0 ? "¥0.00" : String.format("¥%.2f", money_day / 100.00));
        tvYe.setText(String.format(gold == 0 ? "¥0.00" : "¥%.2f", gold / 100.00));
        GlideUtils.showMediumPic(getActivity(), civHead, shopInfo.getPhoto());
        tvShopName.setText(shopInfo.getShop_name());

        if (shopInfo.getIs_dispatching_money_activity() == 0) {
            DaDaCouponDialog.newInstance().show(getFragmentManager(), "");
        }
    }

    //更新店铺状态
    private void updataShopState() {
        //判断店铺状态
        if (shopInfo != null) {
            if (shopInfo.getIs_open() == 1) {
                btnSwitch.setCheckedNoEvent(true);
            } else {
                btnSwitch.setCheckedNoEvent(false);
            }
        }
    }

    @Override
    protected void bindEven() {

    }

    private void showNoticeDialog() {
        View inflate = getActivity().getLayoutInflater().inflate(R.layout.dialog_notice_view, null);
        if (null == dialog) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setView(inflate);
            dialog = builder.create();
            inputEdit = inflate.findViewById(R.id.dialog_input_edit);
            cancelBtn = inflate.findViewById(R.id.dialog_cancel_btn);
            completeBtn = inflate.findViewById(R.id.dialog_complete_btn);
        }
        inputEdit.setText(shopInfo.getNotice());
        dialog.show();
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        completeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String noticeStr = inputEdit.getText().toString();
                if (TextUtils.isEmpty(noticeStr)) {
                    showShortToast("公告内容不能为空");
                    return;
                }
                new MyHttp().doPost(Api.getDefault().updateNotice(LoginUtil.getLoginTokenRegister(), noticeStr), new HttpListener() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        showShortToast(result.getString("msg"));
                        shopInfo.setNotice(inputEdit.getText().toString());
                        dialog.dismiss();
                    }

                    @Override
                    public void onError(int code) {

                    }
                });
            }
        });
    }

    //跳转小程序
    private void jumpXcx() {
        if (shopInfo!=null){
            IWXAPI api = WXAPIFactory.createWXAPI(getContext(), AppConfig.WX_APP_ID);
            WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
            req.userName = "gh_c90fe5b5ba40"; // 填小程序原始id
            req.path = "/pages/home/shopDetails/shopDetails?shopID=" + shopInfo.getShop_id() + "&gradeid=" + shopInfo.getGrade_id();        //拉起小程序页面的可带参路径，不填默认拉起小程序首页
            req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;// 可选打开 开发MINIPROGRAM_TYPE_PREVIEW，体验版MINIPROGRAM_TYPE_TEST,和正式版MINIPTOGRAM_TYPE_RELEASE。
            api.sendReq(req);
        }
    }

    private void showPhoto() {
        if (shopInfo!=null&&!TextUtils.isEmpty(shopInfo.getPhoto())) {
            ArrayList<String> lstPhoto = new ArrayList<>();
            lstPhoto.add(shopInfo.getPhoto());
            PhotoPreview.builder()
                    .setPhotos(lstPhoto)
                    .setCurrentItem(0)
                    .setShowDeleteButton(false)
                    .start(getActivity());
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.ll_dps, R.id.ll_dtk, R.id.fl_ewm, R.id.cl_ddps, R.id.tv_tx, R.id.iv_msg, R.id.ic_yr, R.id.ic_fx, R.id.iv_dyj, R.id.iv_set, R.id.civ_head, R.id.btn_switch, R.id.tv_ye_click, R.id.cl_wmdd, R.id.cl_tsdd, R.id.cl_dfdd, R.id.cl_dzdd, R.id.cl_qcdd, R.id.cl_ddzw, R.id.cl_pdgl, R.id.cl_dzgl, R.id.cl_jdzfmx, R.id.cl_spgl, R.id.cl_hdgl, R.id.cl_sjfx, R.id.cl_dpzl, R.id.cl_psgl, R.id.cl_gggl})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_msg:
                //
                break;
            case R.id.ll_dps:
                //待配送
                OrderActivity.actionStart(getContext(), 0);
                break;
            case R.id.ll_dtk:
                //待退款
                OrderActivity.actionStart(getContext(), 3);
                break;
            case R.id.fl_ewm:
                //二维吗
                if (shopInfo!=null){
                    MiniProgramDialog.getInstance(shopInfo).show(getFragmentManager(), "");
                }
                break;
            case R.id.ic_yr:
                // 预览小程序
                jumpXcx();
                break;
            case R.id.ic_fx:
                //分享店铺
                popfxdp();
                break;
            case R.id.iv_dyj:
                //接单器
                startActivity(new Intent(getActivity(), ReceiverListActivity.class));
                break;
            case R.id.iv_set:
                //我的
                startActivity(new Intent(getContext(), MIneActivity.class));
                break;
            case R.id.civ_head:
                showPhoto();
                break;
            case R.id.tv_ye_click:
            case R.id.tv_tx:
                InComeActivity.actionStart(getContext());
                break;
            case R.id.cl_wmdd:
                //订单
                OrderActivity.actionStart(getContext());
                break;
            case R.id.cl_tsdd:
                //tsdd
                ScanOrderActivity.actionStart(getContext());
                break;
            case R.id.cl_dfdd:
                //到店付款
                IntelligentReceiveActivity.actionStart(getContext());
                break;
            case R.id.cl_dzdd:
                //订座订单
                BookSeatActivity.actionStart(getContext());
                break;
            case R.id.cl_qcdd:
                //取餐订单
                NumberOrderActivity.actionStart(getContext());
                break;
            case R.id.cl_ddzw:
                //堂点座位
                startActivity(new Intent(getActivity(), SeatActivity.class));
                break;
            case R.id.cl_pdgl:
                //库存管理
                InventoryAnalyzeActivity.actionStart(getContext());
                break;
            case R.id.cl_dzgl:
                //商家物料
                startActivity(new Intent(getActivity(), BusinessMustActivity.class));
                break;
            case R.id.cl_jdzfmx:
                //京东支付明细
                startActivity(new Intent(getContext(), JingdongDetailActivity.class));
                break;
            case R.id.cl_spgl:
                //商品管理
                GoodsManagerActivity.actionStart(getContext());
                break;
            case R.id.cl_hdgl:
                //活动管理
                ActivityManagerActivity.actionStart(getContext());
                break;
            case R.id.cl_sjfx:
                //数据分析
                RunAnalyzeActivity.actionStart(getContext());
                break;
            case R.id.cl_dpzl:
                //店铺资料
                StoreManagerActivity.actionStart(getContext());
                break;
            case R.id.cl_psgl:
                //配送设置
                if (shopDetailInfo != null) {
                    SendSettingActivity.actionStart(getActivity(), shopDetailInfo);
                } else {
                    showLongToast("请刷新一下界面");
                }
                break;
            case R.id.cl_gggl:
                //公告管理
                showNoticeDialog();
                break;
            case R.id.cl_ddps:
                //达达配送
                DaDaActivity.actionStart(getContext());
                break;
        }
    }

    class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    tvMsg.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    //分享
    private void popfxdp() {

        final View shareInflate = LayoutInflater.from(getContext()).inflate(R.layout.share_pop_view, null);
        final CircleImageView shopHeard = shareInflate.findViewById(R.id.civ_head);
        final CircleImageView shopEwm = shareInflate.findViewById(R.id.civ_ewm);
        final ConstraintLayout linearLayout = shareInflate.findViewById(R.id.ctl_hb);
        final TextView shopName = shareInflate.findViewById(R.id.tv_shop_name);
        //按钮
        final TextView share_wechat_friends_tv = shareInflate.findViewById(R.id.share_wechat_friends_tv);
        final TextView share_pyq = shareInflate.findViewById(R.id.share_pyq);
        final TextView share_bctp = shareInflate.findViewById(R.id.share_bctp);
        final TextView poop_share_cancel_btn = shareInflate.findViewById(R.id.poop_share_cancel_btn);
        //dialog
        final CustomPopWindow shareDialog = new CustomPopWindow.PopupWindowBuilder(getContext())
                .setView(shareInflate)
                .size(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                .setAnimationStyle(R.style.AlphaEnterExitAnimation)
                .enableBackgroundDark(true)
                .create()
                .showAtLocation(shareInflate, Gravity.CENTER, 0, 0);

        //获取当前Activity所在的窗体
        poop_share_cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //取消
                shareDialog.dissmiss();
            }
        });
        new MyHttp().doPost(Api.getDefault().shareShop(loginUser.getSj_login_token()), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                final ShopFxinfo shopFxinfo = new Gson().fromJson(result.toString(), ShopFxinfo.class);
                GlideUtils.showMediumPic(getContext(), shopHeard, shopFxinfo.getData().getPhoto());
                GlideUtils.showMediumPic(getContext(), shopEwm, shopFxinfo.getData().getSmall_routine_photo());
                share_wechat_friends_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //微信好友
                        String path = "pages/home/store/store?shopID=" + loginUser.getShop_id() + "&gradeid=" + loginUser.getGrade_id();
                        ShareUtils.getInstance().shareMiniProgram(getContext(), shopFxinfo.getData().getShop_name(), "", shopFxinfo.getData().getPhoto(), path, "www.wbx365.com");
                        shareDialog.dissmiss();
                    }
                });
                share_pyq.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //微信朋友圈
                        ShareUtils.getInstance().shareToTimeLine(FormatUtil.viewToBitmap(linearLayout), getContext());
                        shareDialog.dissmiss();
                    }
                });
                share_bctp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //保存图片
                        linearLayout.post(new Runnable() {
                            @Override
                            public void run() {
                                ScannerUtils.saveImageToGallery(getContext(), FormatUtil.viewToBitmap(linearLayout), ScannerUtils.ScannerType.RECEIVER);
                            }
                        });
                        shareDialog.dissmiss();
                    }
                });

                shopName.setText(shopFxinfo.getData().getShop_name());
                //Adapter
                final RecyclerView recyclerView = shareInflate.findViewById(R.id.rv_goods);
                ShareShopGoodsAdapter shareShopGoodsAdapter = new ShareShopGoodsAdapter() {
                    @Override
                    public void onBindViewHolder(BaseViewHolder holder, int position) {
                        GridLayoutManager.LayoutParams layoutparams = new GridLayoutManager.LayoutParams(recyclerView.getWidth() / 2, recyclerView.getHeight());
                        holder.itemView.setLayoutParams(layoutparams);
                        super.onBindViewHolder(holder, position);
                    }
                };
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2) {
                    @Override
                    public boolean canScrollVertically() {
                        return true;
                    }
                });
                recyclerView.setAdapter(shareShopGoodsAdapter);
                shareShopGoodsAdapter.setNewData(shopFxinfo.getData().getGoods());
            }

            @Override
            public void onError(int code) {

            }
        });

    }
}
