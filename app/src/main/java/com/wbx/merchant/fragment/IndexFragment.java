package com.wbx.merchant.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.popwindowutils.CustomPopWindow;
import com.google.gson.Gson;
import com.hedgehog.ratingbar.RatingBar;
import com.kyleduo.switchbutton.SwitchButton;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wbx.merchant.R;
import com.wbx.merchant.activity.AccreditationActivity;
import com.wbx.merchant.activity.ActivityManagerActivity;
import com.wbx.merchant.activity.BookSeatActivity;
import com.wbx.merchant.activity.BusinessMustActivity;
import com.wbx.merchant.activity.ChooseShopVersionsPrwActivity;
import com.wbx.merchant.activity.CompleteInformationActivity;
import com.wbx.merchant.activity.DaDaActivity;
import com.wbx.merchant.activity.GoodsAccreditationActivity;
import com.wbx.merchant.activity.GoodsManagerActivity;
import com.wbx.merchant.activity.InComeActivity;
import com.wbx.merchant.activity.IntelligentReceiveActivity;
import com.wbx.merchant.activity.InventoryAnalyzeActivity;
import com.wbx.merchant.activity.InviteActivity;
import com.wbx.merchant.activity.LoginActivity;
import com.wbx.merchant.activity.MIneActivity;
import com.wbx.merchant.activity.MyBusinessCircleActivity;
import com.wbx.merchant.activity.MyGylActivity;
import com.wbx.merchant.activity.MyMemberActivity;
import com.wbx.merchant.activity.NumberOrderActivity;
import com.wbx.merchant.activity.OrderActivity;
import com.wbx.merchant.activity.PublishBusinessCircleActivity;
import com.wbx.merchant.activity.ReceiverListActivity;
import com.wbx.merchant.activity.RunAnalyzeActivity;
import com.wbx.merchant.activity.SalesmanCommentActivity;
import com.wbx.merchant.activity.ScanOrderActivity;
import com.wbx.merchant.activity.SeatActivity;
import com.wbx.merchant.activity.SendSettingActivity;
import com.wbx.merchant.activity.StoreManagerActivity;
import com.wbx.merchant.activity.WebActivity;
import com.wbx.merchant.activity.WebSetUpShopActivity;
import com.wbx.merchant.adapter.ShareShopGoodsAdapter;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseApplication;
import com.wbx.merchant.base.BaseFragment;
import com.wbx.merchant.baseapp.AppConfig;
import com.wbx.merchant.baseapp.AppManager;
import com.wbx.merchant.bean.ShopDetailInfo;
import com.wbx.merchant.bean.ShopFxinfo;
import com.wbx.merchant.bean.ShopIdentityBean;
import com.wbx.merchant.bean.ShopInfo;
import com.wbx.merchant.common.LoginUtil;
import com.wbx.merchant.dialog.DaDaCouponDialog;
import com.wbx.merchant.dialog.MiniProgramDialog;
import com.wbx.merchant.dialog.OperatingStateDialog;
import com.wbx.merchant.utils.FormatUtil;
import com.wbx.merchant.utils.GlideImageLoader;
import com.wbx.merchant.utils.GlideUtils;
import com.wbx.merchant.utils.SPUtils;
import com.wbx.merchant.utils.ScannerUtils;
import com.wbx.merchant.utils.ShareUtils;
import com.wbx.merchant.widget.CircleImageView;
import com.wbx.merchant.widget.CustomizedProgressBar;
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
 * Created by wushenghui on 2017/6/20.
 * 首页
 */
public class IndexFragment extends BaseFragment {
    @Bind(R.id.has_message_tv)
    TextView hasMessageTv;
    @Bind(R.id.index_shop_name_tv)
    TextView indexShopNameTv;
    @Bind(R.id.attestation_state_tv)
    TextView attestationStateTv;
    @Bind(R.id.show_open_state_tv)
    TextView openStateTv;
    @Bind(R.id.balance_tv)
    TextView balanceTv;
    @Bind(R.id.today_income_tv)
    TextView todayIncomeTv;
    @Bind(R.id.xbanner_view)
    Banner xbannerView;
    @Bind(R.id.yesterday_income_tv)
    TextView yesterdayIncomeTv;
    @Bind(R.id.index_head_im)
    CircleImageView indexHeadIm;
    @Bind(R.id.tv_wait_send_num)
    TextView tvWaitSendNum;
    @Bind(R.id.tv_wait_refund_num)
    TextView tvWaitRefundNum;
    @Bind(R.id.tv_send_order_num)
    TextView tvSendOrderNum;
    @Bind(R.id.tv_scan_order_num)
    TextView tvScanOrderNum;
    @Bind(R.id.tv_book_order_num)
    TextView tvBookOrderNum;
    @Bind(R.id.tv_number_order_num)
    TextView tvNumberOrderNum;
    @Bind(R.id.tv_goods_manager_num)
    TextView tvGoodsManagerNum;
    @Bind(R.id.civ_xsdl_headimg)
    CircleImageView civXsdlHeadimg;
    @Bind(R.id.tv_lxxsdl)
    TextView tvLxxsdl;
    @Bind(R.id.rb_score)
    RatingBar rbScore;
    @Bind(R.id.tv_wypj)
    TextView tvWypj;
    @Bind(R.id.ll_xsdl_pj)
    LinearLayout llXsdlPj;
    @Bind(R.id.chat_list_im)
    ImageView chatListIm;
    @Bind(R.id.iv_yrdp)
    ImageView ivYrdp;
    @Bind(R.id.bt_cash_withdrawal)
    Button btCashWithdrawal;
    @Bind(R.id.customizedbar)
    CustomizedProgressBar customizedbar;
    @Bind(R.id.tv_customized)
    TextView tvCustomized;
    @Bind(R.id.ll_award)
    LinearLayout llAward;
    @Bind(R.id.iv_pub_bus_cir)
    ImageView ivPubBusCir;
    @Bind(R.id.ctl_ktqjd)
    ConstraintLayout ctlKtqjd;
    @Bind(R.id.ll_wait_send)
    LinearLayout llWaitSend;
    @Bind(R.id.ll_wait_refund)
    LinearLayout llWaitRefund;
    @Bind(R.id.tv_send_order)
    TextView tvSendOrder;
    @Bind(R.id.rl_send_order)
    RelativeLayout rlSendOrder;
    @Bind(R.id.tv_scan_order)
    TextView tvScanOrder;
    @Bind(R.id.rl_scan_order)
    RelativeLayout rlScanOrder;
    @Bind(R.id.tv_book_order)
    TextView tvBookOrder;
    @Bind(R.id.rl_book_order)
    RelativeLayout rlBookOrder;
    @Bind(R.id.tv_number_order)
    TextView tvNumberOrder;
    @Bind(R.id.rl_number_order)
    RelativeLayout rlNumberOrder;
    @Bind(R.id.rl_shop_manager)
    RelativeLayout rlShopManager;
    @Bind(R.id.tv_goods_manager)
    TextView tvGoodsManager;
    @Bind(R.id.rl_goods_manager)
    RelativeLayout rlGoodsManager;
    @Bind(R.id.rl_business_manager)
    RelativeLayout rlBusinessManager;
    @Bind(R.id.rl_customer_manager)
    RelativeLayout rlCustomerManager;
    @Bind(R.id.rl_notice_manager)
    RelativeLayout rlNoticeManager;
    @Bind(R.id.rl_activity_manager)
    RelativeLayout rlActivityManager;
    @Bind(R.id.rl_inventory_manager)
    RelativeLayout rlInventoryManager;
    @Bind(R.id.rl_seat_manager)
    RelativeLayout rlSeatManager;
    @Bind(R.id.rl_business_analyse)
    RelativeLayout rlBusinessAnalyse;
    @Bind(R.id.rl_merchant_withdraw)
    RelativeLayout rlMerchantWithdraw;
    @Bind(R.id.rl_merchant_subsidy)
    RelativeLayout rlMerchantSubsidy;
    @Bind(R.id.rl_intelligent_receive)
    RelativeLayout rlIntelligentReceive;
    @Bind(R.id.rl_mysq)
    RelativeLayout rlMysq;
    @Bind(R.id.rl_business_must)
    RelativeLayout rlBusinessMust;
    @Bind(R.id.rl_dada)
    RelativeLayout rlDada;
    @Bind(R.id.rl_share_shop)
    RelativeLayout rlShareShop;
    @Bind(R.id.rl_video_course)
    RelativeLayout rlVideoCourse;
    @Bind(R.id.rl_jdq)
    RelativeLayout rlJdq;
    @Bind(R.id.rl_make_money_by_share)
    RelativeLayout rlMakeMoneyByShare;
    @Bind(R.id.rl_pssz)
    RelativeLayout rlPssz;
    @Bind(R.id.rl_gyl)
    RelativeLayout rlGyl;
    @Bind(R.id.rl_hy)
    RelativeLayout rlHy;
    @Bind(R.id.service_im)
    ImageView serviceIm;
    @Bind(R.id.iv_in_operation)
    ImageView ivInOperation;
    @Bind(R.id.tv_state)
    TextView tvState;
    @Bind(R.id.btn_switch)
    SwitchButton btnSwitch;
    private ShopInfo shopInfo;
    private Dialog dialog;
    private EditText inputEdit;
    private Button cancelBtn;
    private Button completeBtn;
    private MyReceiver myReceiver;
    private CustomizedProgressBar customized;
    private float progressMax = 30000;
    private boolean Awardflag = false;
    private LinearLayout ll_award;
    private TextView tv_customized;
    private Button bt_cash_withdrawal;
    private ShopDetailInfo shopDetailInfo;

    @Override
    protected int getLayoutResource() {
        return (loginUser == null || loginUser.getGrade_id() == AppConfig.StoreType.FOOD_STREET) ? R.layout.fragment_index_food_street : R.layout.fragment_index;
    }

    @Override
    public void initPresenter() {
        IntentFilter filter = new IntentFilter(AppConfig.REFRESH_UI);
        myReceiver = new MyReceiver();
        getActivity().registerReceiver(myReceiver, filter);
    }


    @Override
    protected void initView() {

        customized = rootView.findViewById(R.id.customizedbar);
        ll_award = rootView.findViewById(R.id.ll_award);
        tv_customized = rootView.findViewById(R.id.tv_customized);
        bt_cash_withdrawal = rootView.findViewById(R.id.bt_cash_withdrawal);
        btnSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shopInfo!=null){
                    if (shopInfo.getIs_open()==1){
                        postSw(0);
                    }else {
                        postSw(1);
                    }
                }
            }
        });

    }


    private void postSw(final int is_open){
        new MyHttp().doPost(Api.getDefault().updateIsOpen(LoginUtil.getLoginTokenRegister(),is_open), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                shopInfo.setIs_open(is_open);
                updataShopState();
            }

            @Override
            public void onError(int code) {
                shopInfo.setIs_open(is_open==1?0:1);
                updataShopState();
                getShopIdentity();
            }
        });
    }

    //完善信息弹窗
    private void getShopIdentity(){

        new MyHttp().doPost(Api.getDefault().getShopIdentity(LoginUtil.getLoginTokenRegister()), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                ShopIdentityBean shopIdentityBean = new Gson().fromJson(result.toString(),ShopIdentityBean.class);
                switch (shopIdentityBean.getData().getAudit()){
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
    private void popWsxs(){
        final View shareInflate = LayoutInflater.from(getContext()).inflate(R.layout.pop_wsxx, null);
        ImageView imageView =shareInflate.findViewById(R.id.iv_wxss);
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
//        imgurl.add(ContextCompat.getDrawable(getContext(), R.drawable.prw));
//        imgurl.add(ContextCompat.getDrawable(getContext(), R.drawable.cwdz));
        imgurl.add(ContextCompat.getDrawable(getContext(), R.drawable.vidio_study));
        xbannerView.setBannerStyle(BannerConfig.NOT_INDICATOR);
        xbannerView.setImages(imgurl).setDelayTime(10000).setImageLoader(new GlideImageLoader()).start();
        xbannerView.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                switch (position) {
                    case 0:
//                        WebSetUpShopActivity.actionStart(getContext(), String.format("http://www.wbx365.com/Wbxwaphome/openstore#/record?uid=%s", loginUser.getSj_login_token()));
                        break;
                    case 1:
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
//        //获取销售代理信息 评价
//        new MyHttp().doPost(Api.getDefault().getSalesmanCommentInfo(loginUser.getSj_login_token()), new HttpListener() {
//            @Override
//            public void onSuccess(JSONObject result) {
//                SalesmanCommentInfo info = new Gson().fromJson(result.toString(), SalesmanCommentInfo.class);
//                //头像
//
//
//            }
//
//            @Override
//            public void onError(int code) {
//
//            }
//        });
    }

    //填充数据
    private void setData() {
        //销售代理头像
        GlideUtils.showMediumPic(getActivity(), civXsdlHeadimg, shopInfo.getSalesman_headimg());
        //星评
        rbScore.setStar(shopInfo.getComment_rank());
        //判断是否试用店 ==1
        if (shopInfo.getTry_shop() == 1) {
            ctlKtqjd.setVisibility(View.VISIBLE);
        } else {
            ctlKtqjd.setVisibility(View.GONE);
        }
       updataShopState();
        tvWaitSendNum.setText(String.valueOf(shopInfo.getNew_order_num()));
        tvWaitRefundNum.setText(String.valueOf(shopInfo.getDtk_order_num()));
        if (shopInfo.getNew_order_num() > 0) {
            tvSendOrderNum.setVisibility(View.VISIBLE);
            tvSendOrderNum.setText(String.valueOf(shopInfo.getNew_order_num()));
        } else {
            tvSendOrderNum.setVisibility(View.GONE);
        }
        if (shopInfo.getScan_order_no_pay_count() > 0) {
            tvScanOrderNum.setVisibility(View.VISIBLE);
            tvScanOrderNum.setText(String.valueOf(shopInfo.getScan_order_no_pay_count()));
        } else {
            tvScanOrderNum.setVisibility(View.GONE);
        }
        if (shopInfo.getLater_subscribe_order_num() > 0) {
            tvBookOrderNum.setVisibility(View.VISIBLE);
            tvBookOrderNum.setText(String.valueOf(shopInfo.getLater_subscribe_order_num()));
        } else {
            tvBookOrderNum.setVisibility(View.GONE);
        }
        if (shopInfo.getTake_number_no_read_num() > 0) {
            tvNumberOrderNum.setVisibility(View.VISIBLE);
            tvNumberOrderNum.setText(String.valueOf(shopInfo.getTake_number_no_read_num()));
        } else {
            tvNumberOrderNum.setVisibility(View.GONE);
        }
        if (shopInfo.getGoods_num() > 0) {
            tvGoodsManagerNum.setVisibility(View.VISIBLE);
            tvGoodsManagerNum.setText(String.valueOf(shopInfo.getGoods_num()));
        } else {
            tvGoodsManagerNum.setVisibility(View.GONE);
        }
        openStateTv.setText(shopInfo.getIs_open() == 0 ? "休息中" : (shopInfo.getIs_open() == 1 ? "营业中" : "筹备中"));
        int money_yesterday = shopInfo.getMoney_yesterday();
        int money_day = shopInfo.getMoney_day();
        int gold = shopInfo.getGold();
        yesterdayIncomeTv.setText(money_yesterday == 0 ? "¥0.00" : String.format("¥%.2f", money_yesterday / 100.00));
        todayIncomeTv.setText(money_day == 0 ? "¥0.00" : String.format("¥%.2f", money_day / 100.00));
        balanceTv.setText(String.format(gold == 0 ? "¥0.00" : "¥%.2f", gold / 100.00));
        GlideUtils.showMediumPic(getActivity(), indexHeadIm, shopInfo.getPhoto());
        indexShopNameTv.setText(shopInfo.getShop_name());
//        //认证判断
//        if (!TextUtils.isEmpty(shopInfo.getReturn_reason())) {
//            //被驳回 Return_reason为空
//            attestationStateTv.setText("被驳回");
//            //returned_type 1 食品证的问题  returned_type 2 资质的问题
//            AlertNextDialog alertNextDialog = AlertNextDialog.newInstance(shopInfo.getReturn_reason(), shopInfo.getReturned_type());
//            alertNextDialog.show(getActivity().getSupportFragmentManager(), "");
//        } else {
//            if (shopInfo.getIs_renzheng() == 1 && (!TextUtils.isEmpty(shopInfo.getHygiene_photo()) || shopInfo.getHas_hygiene_photo() == 0)) {
//                //已认证并且有食品许可证或者该店不需要食品许可证
//                attestationStateTv.setText("查看资质");
//            } else if (shopInfo.getIs_add_audit() == 1 && (!TextUtils.isEmpty(shopInfo.getHygiene_photo()) || shopInfo.getHas_hygiene_photo() == 0) && shopInfo.getIs_renzheng() == 0) {
//                //已经添加资料但是认证还未通过
//                attestationStateTv.setText("审核中");
//            } else {
//                attestationStateTv.setText("未认证");
//                Boolean isNoAskAgain = SPUtils.getSharedBooleanData(getActivity(), AppConfig.NO_ASK_AGAIN_ACCREDITATION);
//                if (!isNoAskAgain) {
//                    if (shopInfo.getIs_add_audit() == 1) {
//                        AlertUploadAccreditationDialog uploadAccreditationDialog = AlertUploadAccreditationDialog.newInstance(true);
//                        uploadAccreditationDialog.show(getActivity().getSupportFragmentManager(), "");
//                    } else {
//                        AlertUploadAccreditationDialog uploadAccreditationDialog = AlertUploadAccreditationDialog.newInstance(false);
//                        uploadAccreditationDialog.show(getActivity().getSupportFragmentManager(), "");
//                    }
//                }
//            }
//        }


        if (shopInfo.getIs_dispatching_money_activity() == 0) {
            DaDaCouponDialog.newInstance().show(getFragmentManager(), "");
        }
//        //奖励活动  is_view_withdraw_commission 1显示  0不显示
//        if (shopInfo.getShop_grade() == 6 && SPUtils.getSharedIntData(getContext(), "is_view_withdraw_commission") == 1) {
//            Boolean flag = SPUtils.getSharedBooleanData(getContext(), "flag");
//            if (Awardflag == flag) {
//                ll_award.setVisibility(View.VISIBLE);
//                tv_customized.setText((int) shopInfo.getOrder_money() / 100 + "");
//                customized.setMaxCount(progressMax);
//                customized.setCurrentCount(shopInfo.getOrder_money());
//                bt_cash_withdrawal.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (shopInfo.getOrder_money() / 100 < 30000) {
//                            ToastUitl.showShort("未到达活动金额,无法提现！");
//                            return;
//                        }
//                        startActivityForResult(AwardCashActivity.class, 0);
//                    }
//                });
//            } else {
//                ll_award.setVisibility(View.GONE);
//            }
//        } else {
//            ll_award.setVisibility(View.GONE);
//        }
    }

    //更新店铺状态
    private void updataShopState() {
        //判断店铺状态
        if (shopInfo!=null){
            if (shopInfo.getIs_open() == 1) {
                ivInOperation.setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.yingyezhong));
                tvState.setText("店铺营业中");
                btnSwitch.setChecked(true);
            } else {
                ivInOperation.setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.xiuxizhong));
                tvState.setText("店铺正在休息中");
                btnSwitch.setChecked(false);
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

    @OnClick({R.id.rl_seting,R.id.ctl_ktqjd, R.id.rl_pssz, R.id.iv_yrdp, R.id.rl_gyl, R.id.rl_hy, R.id.rl_mysq, R.id.rl_jdq, R.id.ll_xsdl_pj, R.id.chat_list_im, R.id.index_head_im, R.id.attestation_state_tv, R.id.show_open_state_tv, R.id.iv_pub_bus_cir, R.id.ll_wait_send, R.id.ll_wait_refund, R.id.rl_send_order, R.id.rl_scan_order, R.id.rl_book_order, R.id.rl_number_order, R.id.rl_shop_manager, R.id.rl_goods_manager, R.id.rl_business_manager, R.id.rl_customer_manager, R.id.rl_notice_manager, R.id.rl_activity_manager, R.id.rl_inventory_manager, R.id.rl_business_analyse, R.id.rl_merchant_withdraw, R.id.rl_merchant_subsidy, R.id.rl_intelligent_receive, R.id.rl_dada, R.id.rl_make_money_by_share, R.id.rl_share_shop, R.id.rl_video_course, R.id.service_im, R.id.rl_seat_manager, R.id.rl_business_must})
    public void onViewClicked(View view) {
        if (shopInfo == null) {
            return;
        }
        switch (view.getId()) {
            case R.id.rl_seting:
                //我的
                startActivity(new Intent(getContext(), MIneActivity.class));

                break;
            case R.id.ctl_ktqjd:
                //开通旗舰店
                startActivity(new Intent(getContext(), ChooseShopVersionsPrwActivity.class));
                break;
            case R.id.rl_gyl:
                MyGylActivity.actionStart(getContext(), "供应链金融");
                break;
            case R.id.rl_pssz:
                //配送设置
                if (shopDetailInfo != null) {
                    SendSettingActivity.actionStart(getActivity(), shopDetailInfo);
                } else {
                    showLongToast("请刷新一下界面");
                }
                break;
            case R.id.rl_hy:
                //供应链
                MyGylActivity.actionStart(getContext(), "货源");
                break;
            case R.id.rl_mysq:
                //我的商圈
                MyBusinessCircleActivity.actionStart(getActivity());
                break;
            case R.id.rl_jdq:
                //接单器
                startActivity(new Intent(getActivity(), ReceiverListActivity.class));
                break;
            case R.id.chat_list_im:
                break;
            case R.id.index_head_im:
                showPhoto();
                break;
            case R.id.attestation_state_tv:
                showCertification();
                break;
            case R.id.show_open_state_tv:
                changeOpenState();
                break;
            case R.id.iv_pub_bus_cir:
                PublishBusinessCircleActivity.actionStart(getContext(),shopInfo.getDiscover_num());
                break;
            case R.id.ll_wait_send:
                OrderActivity.actionStart(getContext(), 0);
                break;
            case R.id.ll_wait_refund:
                //待退款
                OrderActivity.actionStart(getContext(), 3);
                break;
            case R.id.rl_send_order:
                //订单
                OrderActivity.actionStart(getContext());
                break;
            case R.id.rl_scan_order:
                ScanOrderActivity.actionStart(getContext());
                break;
            case R.id.rl_book_order:
                BookSeatActivity.actionStart(getContext());
                break;
            case R.id.rl_number_order:
                NumberOrderActivity.actionStart(getContext());
                break;
            case R.id.rl_shop_manager:
                StoreManagerActivity.actionStart(getContext());
                break;
            case R.id.rl_goods_manager:
                //商品管理
                GoodsManagerActivity.actionStart(getContext());
                break;
            case R.id.rl_business_manager:
                changeOpenState();
                break;
            case R.id.rl_customer_manager:
                MyMemberActivity.actionStart(getContext());
                break;
            case R.id.rl_notice_manager:
                showNoticeDialog();
                break;
            case R.id.rl_activity_manager:
                ActivityManagerActivity.actionStart(getContext());
                break;
            case R.id.rl_inventory_manager:
                InventoryAnalyzeActivity.actionStart(getContext());
                break;
            case R.id.rl_business_analyse:
                RunAnalyzeActivity.actionStart(getContext());
                break;
            case R.id.rl_merchant_withdraw:
                InComeActivity.actionStart(getContext());
                break;
            case R.id.rl_merchant_subsidy:
//                MerchantSubsidiesActivity.actionStart(getContext());
                break;
            case R.id.rl_intelligent_receive:
                IntelligentReceiveActivity.actionStart(getContext());
                break;
            case R.id.rl_dada:
                DaDaActivity.actionStart(getContext());
                break;
            case R.id.rl_make_money_by_share:
                InviteActivity.actionStart(getContext(), "https://www.wbx365.com/Wbxwaphome/apply/activity.html?type=1", shopInfo);
                break;
            case R.id.rl_share_shop:
                MiniProgramDialog.getInstance(shopInfo).show(getFragmentManager(), "");
                break;
            case R.id.rl_video_course:
                WebActivity.actionStart(getContext(), "http://www.wbx365.com/Wbxwaphome/video");
                break;
            case R.id.service_im:
                //分享店铺
                popfxdp();
                break;
            case R.id.iv_yrdp:
                // 预览小程序
                jumpXcx();
                break;
            case R.id.rl_seat_manager:
                startActivity(new Intent(getActivity(), SeatActivity.class));
                break;
            case R.id.rl_business_must:
                startActivity(new Intent(getActivity(), BusinessMustActivity.class));
                break;
            case R.id.ll_xsdl_pj:
                //对销售代理进行评价
                SalesmanCommentActivity.actionStart(getContext(), "zc");
                break;
        }
    }

    //跳转小程序
    private void jumpXcx() {
        IWXAPI api = WXAPIFactory.createWXAPI(getContext(), AppConfig.WX_APP_ID);
        WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
        req.userName = "gh_c90fe5b5ba40"; // 填小程序原始id
        req.path = "/pages/home/shopDetails/shopDetails?shopID=" + shopInfo.getShop_id() + "&gradeid=" + shopInfo.getGrade_id();        //拉起小程序页面的可带参路径，不填默认拉起小程序首页
        req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;// 可选打开 开发MINIPROGRAM_TYPE_PREVIEW，体验版MINIPROGRAM_TYPE_TEST,和正式版MINIPTOGRAM_TYPE_RELEASE。
        api.sendReq(req);
    }

    //查看或上传证件
    private void showCertification() {
        //不为空 被驳回
        if (!TextUtils.isEmpty(shopInfo.getReturn_reason())) {
            //returned_type 1 食品证的问题  returned_type 2 资质的问题     returned_type 3 食品证与资质都有问题
            switch (shopInfo.getReturned_type()) {
                case 1:
                    GoodsAccreditationActivity.actionStart(getActivity());
                    break;
                case 2:
                    AccreditationActivity.actionStart(getActivity());
                    break;
                case 3:
                    AccreditationActivity.actionStart(getActivity());
                    break;
            }
        } else {
            if (shopInfo.getIs_renzheng() == 1 && (!TextUtils.isEmpty(shopInfo.getHygiene_photo()) || shopInfo.getHas_hygiene_photo() == 0)) {
                //已认证并且有食品许可证或者该店不需要食品许可证
                AccreditationActivity.actionStart(getActivity(), true);
            } else if (shopInfo.getIs_add_audit() == 1 && (!TextUtils.isEmpty(shopInfo.getHygiene_photo()) || shopInfo.getHas_hygiene_photo() == 0) && shopInfo.getIs_renzheng() == 0) {
                //已经添加资料但是认证还未通过
                AccreditationActivity.actionStart(getActivity(), true);
            } else {
                //未认证
                if (shopInfo.getIs_add_audit() != 1 || (TextUtils.isEmpty(shopInfo.getHygiene_photo()) && shopInfo.getHas_hygiene_photo() != 0)) {
                    if (shopInfo.getIs_add_audit() == 1) {
                        GoodsAccreditationActivity.actionStart(getActivity());
                    } else {
                        AccreditationActivity.actionStart(getActivity());
                    }
                }
            }
        }

    }

    private void showPhoto() {
        if (!TextUtils.isEmpty(shopInfo.getPhoto())) {
            ArrayList<String> lstPhoto = new ArrayList<>();
            lstPhoto.add(shopInfo.getPhoto());
            PhotoPreview.builder()
                    .setPhotos(lstPhoto)
                    .setCurrentItem(0)
                    .setShowDeleteButton(false)
                    .start(getActivity());
        }
    }

    private void changeOpenState() {
        OperatingStateDialog instance = OperatingStateDialog.getInstance(shopInfo);
        instance.show(getFragmentManager(), "");
        instance.setOnStateChangeListener(new OperatingStateDialog.OnStateChangeListener() {
            @Override
            public void onChange(int state) {
                openStateTv.setText(state == 0 ? "休息中" : (state == 1 ? "营业中" : "筹备中"));
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    hasMessageTv.setVisibility(View.VISIBLE);
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            SPUtils.setSharedBooleanData(getContext(), "flag", true);
            if (shopInfo.getShop_grade() == 6 || shopInfo.getShop_grade() != 6) {
                ll_award.setVisibility(View.GONE);
            }
        }
    }
}
