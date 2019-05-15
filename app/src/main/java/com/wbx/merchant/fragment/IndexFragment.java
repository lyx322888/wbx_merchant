package com.wbx.merchant.fragment;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.uuzuche.lib_zxing.view.ViewfinderView;
import com.wbx.merchant.R;
import com.wbx.merchant.activity.AccreditationActivity;
import com.wbx.merchant.activity.ActivityManagerActivity;
import com.wbx.merchant.activity.AwardCashActivity;
import com.wbx.merchant.activity.BookSeatActivity;
import com.wbx.merchant.activity.CashActivity;
import com.wbx.merchant.activity.DaDaActivity;
import com.wbx.merchant.activity.GoodsAccreditationActivity;
import com.wbx.merchant.activity.GoodsManagerActivity;
import com.wbx.merchant.activity.InComeActivity;
import com.wbx.merchant.activity.IntelligentReceiveActivity;
import com.wbx.merchant.activity.IntelligentServiceActivity;
import com.wbx.merchant.activity.InventoryAnalyzeActivity;
import com.wbx.merchant.activity.InviteActivity;
import com.wbx.merchant.activity.LoginActivity;
import com.wbx.merchant.activity.MerchantSubsidiesActivity;
import com.wbx.merchant.activity.MessageListActivity;
import com.wbx.merchant.activity.MyMemberActivity;
import com.wbx.merchant.activity.NumberOrderActivity;
import com.wbx.merchant.activity.OrderActivity;
import com.wbx.merchant.activity.PublishBusinessCircleActivity;
import com.wbx.merchant.activity.RunAnalyzeActivity;
import com.wbx.merchant.activity.ScanOrderActivity;
import com.wbx.merchant.activity.SeatActivity;
import com.wbx.merchant.activity.StoreManagerActivity;
import com.wbx.merchant.activity.WebActivity;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseApplication;
import com.wbx.merchant.base.BaseFragment;
import com.wbx.merchant.baseapp.AppConfig;
import com.wbx.merchant.baseapp.AppManager;
import com.wbx.merchant.bean.ShopInfo;
import com.wbx.merchant.bean.UserInfo;
import com.wbx.merchant.common.LoginUtil;
import com.wbx.merchant.dialog.AlertUploadAccreditationDialog;
import com.wbx.merchant.dialog.DaDaCouponDialog;
import com.wbx.merchant.dialog.MiniProgramDialog;
import com.wbx.merchant.dialog.OperatingStateDialog;
import com.wbx.merchant.utils.GlideUtils;
import com.wbx.merchant.utils.SPUtils;
import com.wbx.merchant.utils.ToastUitl;
import com.wbx.merchant.widget.CircleImageView;
import com.wbx.merchant.widget.CustomizedProgressBar;
import com.wbx.merchant.widget.LoadingDialog;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPreview;

/**
 * Created by wushenghui on 2017/6/20.
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
    //    @Bind(R.id.ll_award)
//    LinearLayout ll_award;
//    @Bind(R.id.tv_customized)
//    TextView tv_customized;
//    @Bind(R.id.bt_cash_withdrawal)
//    Button bt_cash_withdrawal;
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

    @Override
    protected int getLayoutResource() {
        return (loginUser == null || loginUser.getGrade_id() == AppConfig.StoreType.FOOD_STREET) ? R.layout.fragment_index_food_street : R.layout.fragment_index;
    }

    @Override
    public void initPresenter() {
        IntentFilter filter = new IntentFilter(AppConfig.REFRESH_UI);
        myReceiver = new MyReceiver();
        getActivity().registerReceiver(myReceiver, filter);

        getShopInfo();
    }


    @Override
    protected void initView() {
        customized = rootView.findViewById(R.id.customizedbar);
        ll_award = rootView.findViewById(R.id.ll_award);
        tv_customized = rootView.findViewById(R.id.tv_customized);
        bt_cash_withdrawal = rootView.findViewById(R.id.bt_cash_withdrawal);
    }

    @Override
    public void onResume() {
        super.onResume();
//        EMChatManager emChatManager = EMClient.getInstance().chatManager();
//        if (null != emChatManager) {
//            int unreadMsgsCount = emChatManager.getUnreadMsgsCount();
//            if (unreadMsgsCount > 0) {
//                hasMessageTv.setVisibility(View.VISIBLE);
//            } else {
//                hasMessageTv.setVisibility(View.GONE);
//            }
//        }
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
                loginUser.setScan_order_type(shopInfo.getScan_order_type());
                BaseApplication.getInstance().saveUserInfo(loginUser);
                setData();

            }

            @Override
            public void onError(int code) {
            }
        });
    }

    //填充数据
    private void setData() {
        tvWaitSendNum.setText(String.valueOf(shopInfo.getNew_order_num()));
        tvWaitRefundNum.setText(String.valueOf(shopInfo.getDtk_order_num()));
        if (shopInfo.getNew_order_num() > 0) {
            tvSendOrderNum.setVisibility(View.VISIBLE);
            tvSendOrderNum.setText(String.valueOf(shopInfo.getNew_order_num()));
        } else {
            tvSendOrderNum.setVisibility(View.GONE);
        }
        if (shopInfo.getUnfinished_scan_order_num() > 0) {
            tvScanOrderNum.setVisibility(View.VISIBLE);
            tvScanOrderNum.setText(String.valueOf(shopInfo.getUnfinished_scan_order_num()));
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
        if (shopInfo.getIs_renzheng() == 1 && (!TextUtils.isEmpty(shopInfo.getHygiene_photo()) || shopInfo.getHas_hygiene_photo() == 0)) {
            //已认证并且有食品许可证或者该店不需要食品许可证
            attestationStateTv.setText("查看资质");
        } else if (shopInfo.getIs_add_audit() == 1 && (!TextUtils.isEmpty(shopInfo.getHygiene_photo()) || shopInfo.getHas_hygiene_photo() == 0) && shopInfo.getIs_renzheng() == 0) {
            //已经添加资料但是认证还未通过
            attestationStateTv.setText("审核中");
        } else {
            attestationStateTv.setText("未认证");
            Boolean isNoAskAgain = SPUtils.getSharedBooleanData(getActivity(), AppConfig.NO_ASK_AGAIN_ACCREDITATION);
            if (!isNoAskAgain) {
                if (shopInfo.getIs_add_audit() == 1) {
                    AlertUploadAccreditationDialog uploadAccreditationDialog = AlertUploadAccreditationDialog.newInstance(true);
                    uploadAccreditationDialog.show(getActivity().getSupportFragmentManager(), "");
                } else {
                    AlertUploadAccreditationDialog uploadAccreditationDialog = AlertUploadAccreditationDialog.newInstance(false);
                    uploadAccreditationDialog.show(getActivity().getSupportFragmentManager(), "");
                }
            }
        }

        if (shopInfo.getIs_dispatching_money_activity() == 0) {
            DaDaCouponDialog.newInstance().show(getFragmentManager(), "");
        }

        if (shopInfo.getShop_grade() == 6) {
            Boolean flag = SPUtils.getSharedBooleanData(getContext(), "flag");
            if (Awardflag == flag) {
                ll_award.setVisibility(View.VISIBLE);
                tv_customized.setText((int) shopInfo.getOrder_money() + "");
                customized.setMaxCount(progressMax);
                customized.setCurrentCount(shopInfo.getOrder_money());
                bt_cash_withdrawal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (shopInfo.getOrder_money() < 30000) {
                            ToastUitl.showShort("未到达活动金额,无法提现！");
                            return;
                        }
//                        Intent intent = new Intent(getContext(), AwardCashActivity.class);
//                        startActivity(intent);
                        startActivityForResult(AwardCashActivity.class, 0);
                    }
                });
            } else {
                ll_award.setVisibility(View.GONE);
            }

        } else {
            ll_award.setVisibility(View.GONE);
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
                new MyHttp().doPost(Api.getDefault().updateNotice(loginUser.getSj_login_token(), noticeStr), new HttpListener() {
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

    @OnClick({R.id.chat_list_im, R.id.index_head_im, R.id.attestation_state_tv, R.id.show_open_state_tv, R.id.iv_pub_bus_cir, R.id.ll_wait_send, R.id.ll_wait_refund, R.id.rl_send_order, R.id.rl_scan_order, R.id.rl_book_order, R.id.rl_number_order, R.id.rl_shop_manager, R.id.rl_goods_manager, R.id.rl_business_manager, R.id.rl_customer_manager, R.id.rl_notice_manager, R.id.rl_activity_manager, R.id.rl_inventory_manager, R.id.rl_business_analyse, R.id.rl_merchant_withdraw, R.id.rl_merchant_subsidy, R.id.rl_intelligent_receive, R.id.rl_dada, R.id.rl_make_money_by_share, R.id.rl_share_shop, R.id.rl_video_course, R.id.service_im, R.id.rl_seat_manager,R.id.ll_video_study})
    public void onViewClicked(View view) {
        if (shopInfo == null) {
            return;
        }
        switch (view.getId()) {
            case R.id.chat_list_im:
                MessageListActivity.actionStart(getContext());
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
                PublishBusinessCircleActivity.actionStart(getContext());
                break;
            case R.id.ll_wait_send:
                OrderActivity.actionStart(getContext(), 0);
                break;
            case R.id.ll_wait_refund:
                OrderActivity.actionStart(getContext(), 2);
                break;
            case R.id.rl_send_order:
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
                MerchantSubsidiesActivity.actionStart(getContext());
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
                IntelligentServiceActivity.actionStart(getContext());
                break;
            case R.id.rl_seat_manager:
                startActivity(new Intent(getActivity(), SeatActivity.class));
                break;
            case R.id.ll_video_study:
                WebActivity.actionStart(getContext(), "http://www.wbx365.com/Wbxwaphome/video");
                break;
        }
    }

    private void showCertification() {
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && requestCode == 0) {
            SPUtils.setSharedBooleanData(getContext(), "flag", true);
            if (shopInfo.getShop_grade() == 6 || shopInfo.getShop_grade() != 6) {
                ll_award.setVisibility(View.GONE);
            }
        }
    }
}
