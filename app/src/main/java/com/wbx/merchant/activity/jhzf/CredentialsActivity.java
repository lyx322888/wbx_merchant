package com.wbx.merchant.activity.jhzf;


import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSONObject;
import com.flyco.roundview.RoundTextView;
import com.wbx.merchant.MainActivity;
import com.wbx.merchant.R;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.baseapp.AppManager;
import com.wbx.merchant.common.LoginUtil;
import com.wbx.merchant.dialog.ConfirmDialog;
import com.wbx.merchant.widget.LoadingDialog;

import butterknife.Bind;
import butterknife.OnClick;

//证件上传
public class CredentialsActivity extends BaseActivity {

    @Bind(R.id.title_name_tv)
    TextView titleNameTv;
    @Bind(R.id.tv_shxy)
    TextView tvShxy;
    @Bind(R.id.ll_shxy)
    LinearLayout llShxy;
    @Bind(R.id.tv_yyzz)
    TextView tvYyzz;
    @Bind(R.id.ll_yyzz)
    LinearLayout llYyzz;
    @Bind(R.id.tv_sqs)
    TextView tvSqs;
    @Bind(R.id.ll_sqs)
    LinearLayout llSqs;
    @Bind(R.id.tv_zzjgdm)
    TextView tvZzjgdm;
    @Bind(R.id.ll_zzjgdm)
    LinearLayout llZzjgdm;
    @Bind(R.id.tv_frsfzzp)
    TextView tvFrsfzzp;
    @Bind(R.id.ll_frsfzzp)
    LinearLayout llFrsfzzp;
    @Bind(R.id.tv_jsrsfzzp)
    TextView tvJsrsfzzp;
    @Bind(R.id.ll_jsrsfzzp)
    LinearLayout llJsrsfzzp;
    @Bind(R.id.tv_yhk)
    TextView tvYhk;
    @Bind(R.id.ll_yhk)
    LinearLayout llYhk;
    @Bind(R.id.tv_mtz)
    TextView tvMtz;
    @Bind(R.id.ll_mtz)
    LinearLayout llMtz;
    @Bind(R.id.tv_dnz)
    TextView tvDnz;
    @Bind(R.id.ll_dnz)
    LinearLayout llDnz;
    @Bind(R.id.tv_syt)
    TextView tvSyt;
    @Bind(R.id.ll_syt)
    LinearLayout llSyt;
    @Bind(R.id.tv_jszds)
    TextView tvJszds;
    @Bind(R.id.ll_jszds)
    LinearLayout llJszds;
    @Bind(R.id.tv_jsrscjsk)
    TextView tvJsrscjsk;
    @Bind(R.id.ll_jsrscjsk)
    LinearLayout llJsrscjsk;
    @Bind(R.id.tv_jsrscsfz)
    TextView tvJsrscsfz;
    @Bind(R.id.ll_jsrscsfz)
    LinearLayout llJsrscsfz;
    @Bind(R.id.tv_submit)
    RoundTextView tvSubmit;
    @Bind(R.id.tv_frsfzzp_f)
    TextView tvFrsfzzpF;
    @Bind(R.id.ll_frsfzzp_f)
    LinearLayout llFrsfzzpF;
    @Bind(R.id.tv_jsrsfzzp_f)
    TextView tvJsrsfzzpF;
    @Bind(R.id.ll_jsrsfzzp_f)
    LinearLayout llJsrsfzzpF;

    final int REQUESTCODE_SGXY = 1111;
    //营业执照
    final int REQUESTCODE_YYZZ = 1112;
    //授权书
    final int REQUESTCODE_SQS = 1113;
    final int REQUESTCODE_ZZJGDM = 1114;
    //法人身份证照片正
    final int REQUESTCODE_SFZZM = 1115;
    //法人身份证照片反
    final int REQUESTCODE_SFZFM = 1116;
    //结算人身份证正面
    final int REQUESTCODE_JSRZM = 1117;
    //结算人身份证fan面
    final int REQUESTCODE_JSRFM = 1118;
    //银行卡
    final int REQUESTCODE_YHK = 1119;
    //门头照
    final int REQUESTCODE_MTZ = 1122;
    //店内找
    final int REQUESTCODE_DNZ = 1123;
    //收银台
    final int REQUESTCODE_SYT = 1124;
    //账户指定书
    final int REQUESTCODE_ZDS = 1125;
    //手持结算书
    final int REQUESTCODE_JSS = 1126;
    //手持身份证
    final int REQUESTCODE_SCSFZ = 1127;
    @Override
    public int getLayoutId() {
        return R.layout.activity_credentials;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {

    }

    @Override
    public void fillData() {

    }

    @Override
    public void setListener() {

    }

    @Override
    public void onBackPressed() {
        ConfirmDialog confirmDialog = ConfirmDialog.newInstance("返回后这个页面的信息将不保留，请确保您已经提交信息！");
        confirmDialog.setDialogListener(new ConfirmDialog.DialogListener() {
            @Override
            public void dialogClickListener() {
                finish();
            }
        });
        confirmDialog.show(getSupportFragmentManager(),"");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data!=null){
            if (data.getBooleanExtra("isUploading",false)){
                switch (requestCode){
                    case REQUESTCODE_SGXY:
                        tvShxy.setText("已上传");
                        break;
                    case REQUESTCODE_YYZZ:
                        tvYyzz.setText("已上传");
                        break;
                    case REQUESTCODE_SQS:
                        tvSqs.setText("已上传");
                        break;
                    case REQUESTCODE_ZZJGDM:
                        tvZzjgdm.setText("已上传");
                        break;
                    case REQUESTCODE_SFZZM:
                        tvFrsfzzp.setText("已上传");
                        break;
                    case REQUESTCODE_SFZFM:
                        tvFrsfzzpF.setText("已上传");
                        break;
                    case REQUESTCODE_JSRZM:
                        tvJsrsfzzp.setText("已上传");
                        break;
                    case REQUESTCODE_JSRFM:
                        tvJsrsfzzpF.setText("已上传");
                        break;
                    case REQUESTCODE_YHK:
                        tvYhk.setText("已上传");
                        break;
                    case REQUESTCODE_MTZ:
                        tvMtz.setText("已上传");
                        break;
                    case REQUESTCODE_DNZ:
                        tvDnz.setText("已上传");
                        break;
                    case REQUESTCODE_SYT:
                        tvSyt.setText("已上传");
                        break;
                    case REQUESTCODE_ZDS:
                        tvJszds.setText("已上传");
                        break;
                    case REQUESTCODE_JSS:
                        tvJsrscjsk.setText("已上传");
                        break;
                    case REQUESTCODE_SCSFZ:
                        tvJsrscsfz.setText("已上传");
                        break;
                }
            }
        }
    }

    @OnClick({R.id.ll_jsrsfzzp_f,R.id.ll_frsfzzp_f,R.id.ll_shxy, R.id.ll_yyzz, R.id.ll_sqs, R.id.ll_zzjgdm, R.id.ll_frsfzzp, R.id.ll_jsrsfzzp, R.id.ll_yhk, R.id.ll_mtz, R.id.ll_dnz, R.id.ll_syt, R.id.ll_jszds, R.id.ll_jsrscjsk, R.id.ll_jsrscsfz, R.id.tv_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_shxy:
                //商户协议
                UploadPicturesActivity.actionStart(mActivity, REQUESTCODE_SGXY, "PROTOCAL", "商户协议");
                break;
            case R.id.ll_yyzz:
                //营业执照
                UploadPicturesActivity.actionStart(mActivity, REQUESTCODE_YYZZ, "LICENCE", "营业执照");
                break;
            case R.id.ll_sqs:
                //授权书
                UploadPicturesActivity.actionStart(mActivity, REQUESTCODE_SQS, "AUTHORIZATION", "授权书");
                break;
            case R.id.ll_zzjgdm:
                //组织机构代码
                UploadPicturesActivity.actionStart(mActivity, REQUESTCODE_ZZJGDM, "ORGANIZATIONCODE", "统⼀社会信⽤代码证书");
                break;
            case R.id.ll_frsfzzp:
                //法人身份证照片正
                UploadPicturesActivity.actionStart(mActivity, REQUESTCODE_SFZZM, "IDENTITYFRONT", "法人身份证照片正");
                break;
            case R.id.ll_frsfzzp_f:
                //法人身份证照片反
                UploadPicturesActivity.actionStart(mActivity, REQUESTCODE_SFZFM, "IDENTITYOPPOSITE", "法人身份证照片反");
                break;
            case R.id.ll_jsrsfzzp:
                //结算人身份证正面
                UploadPicturesActivity.actionStart(mActivity, REQUESTCODE_JSRZM, "SETTLEIDENTITYFRONT", "结算人身份证正面");
                break;
            case R.id.ll_jsrsfzzp_f:
                //结算人身份证反面
                UploadPicturesActivity.actionStart(mActivity, REQUESTCODE_JSRFM, "SETTLEIDENTITYOPPOSITE", "结算人身份证反面");
                break;
            case R.id.ll_yhk:
                //银行卡
                UploadPicturesActivity.actionStart(mActivity, REQUESTCODE_YHK, "BANKFRONT", "银行卡");
                break;
            case R.id.ll_mtz:
                //门头照
                UploadPicturesActivity.actionStart(mActivity, REQUESTCODE_MTZ, "IDENTITYDOOR", "门头照");
                break;
            case R.id.ll_dnz:
                //店内找
                UploadPicturesActivity.actionStart(mActivity, REQUESTCODE_DNZ, "SHOP", "店内照");
                break;
            case R.id.ll_syt:
                //收银台
                UploadPicturesActivity.actionStart(mActivity, REQUESTCODE_SYT, "CASHIERDESK", "收银台");
                break;
            case R.id.ll_jszds:
                //账户指定书
                UploadPicturesActivity.actionStart(mActivity, REQUESTCODE_ZDS, "AUTHORIZATIONFORSETTLEMENT", "账户指定书");
                break;
            case R.id.ll_jsrscjsk:
                //手持结算书
                UploadPicturesActivity.actionStart(mActivity, REQUESTCODE_JSS, "BANKFRONTHOLD", "手持结算书");
                break;
            case R.id.ll_jsrscsfz:
                //手持身份证
                UploadPicturesActivity.actionStart(mActivity, REQUESTCODE_SCSFZ, "HANDHELD_OF_ID_CARD", "手持身份证");
                break;
            case R.id.tv_submit:
                submit();
                break;
        }
    }

    public void submit( ){
        LoadingDialog.showDialogForLoading(mActivity);
        new MyHttp().doPost(Api.getDefault().declare_complete(LoginUtil.getLoginToken()), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                ConfirmDialog confirmDialog = ConfirmDialog.newInstance("再次确认提交报单");
                confirmDialog.setDialogListener(new ConfirmDialog.DialogListener() {
                    @Override
                    public void dialogClickListener() {
                        new MyHttp().doPost(Api.getDefault().declare_complete_confirm(LoginUtil.getLoginToken()), new HttpListener() {
                            @Override
                            public void onSuccess(JSONObject result) {
                                ConfirmDialog dialog = ConfirmDialog.newInstance("已经提交成功，请等待审核");
                                dialog.setDialogListener(new ConfirmDialog.DialogListener() {
                                    @Override
                                    public void dialogClickListener() {
                                        AppManager.getAppManager().finishAllExpectSpecifiedActivity(MainActivity.class);
                                    }
                                });
                                dialog.show(getSupportFragmentManager(),"");
                            }

                            @Override
                            public void onError(int code) {

                            }
                        });
                    }
                });
                confirmDialog.show(getSupportFragmentManager(),"");
            }

            @Override
            public void onError(int code) {

            }
        });
    }

}