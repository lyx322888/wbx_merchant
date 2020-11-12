package com.wbx.merchant.activity.jhzf;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.alibaba.fastjson.JSONObject;
import com.bigkoo.pickerview.OptionsPickerView;
import com.flyco.roundview.RoundTextView;
import com.google.gson.Gson;
import com.wbx.merchant.R;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.bean.JdShopInfo;
import com.wbx.merchant.common.LoginUtil;
import com.wbx.merchant.dialog.ConfirmDialog;
import com.wbx.merchant.widget.LoadingDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

//聚合支付店铺信息
public class JdShopInfoActivity extends BaseActivity {

    @Bind(R.id.title_name_tv)
    TextView titleNameTv;
    @Bind(R.id.tv_jylx)
    TextView tvJylx;
    @Bind(R.id.ll_jylx)
    LinearLayout llJylx;
    @Bind(R.id.tv_jypl)
    TextView tvJypl;
    @Bind(R.id.ll_jypl)
    LinearLayout llJypl;
    @Bind(R.id.tv_dp_phone)
    EditText tvDpPhone;
    @Bind(R.id.tv_submit)
    RoundTextView tvSubmit;
    private String oneIndustry = "";
    private String twoIndustry = "";
    private String microBizType = "";
    private OptionsPickerView pvOptions;
    //是否修改
    boolean isUpdata = false;
    @Override
    public int getLayoutId() {
        return R.layout.activity_jd_shop_info;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        initJylx();
    }

    @Override
    public void fillData() {
        new MyHttp().doPost(Api.getDefault().list_shopinfo(LoginUtil.getLoginToken()), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                JdShopInfo jdShopInfo = new Gson().fromJson(result.toString(),JdShopInfo.class);
                if (jdShopInfo.getData().getShopList()!=null&&jdShopInfo.getData().getShopList().size()>0){
                    setData(jdShopInfo.getData().getShopList().get(0));
                }
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    private void setData(JdShopInfo.DataBean.ShopListBean shopListBean) {
        isUpdata = true;
        microBizType = shopListBean.getMicroBizType();
        switch (microBizType){
            case "MICRO_TYPE_STORE":
                tvJylx.setText("⻔店场所");
                break;
            case "MICRO_TYPE_MOBILE":
                tvJylx.setText("流动经营/便⺠服务");
                break;
            case "MICRO_TYPE_ONLINE":
                tvJylx.setText("线上商品/服务交易");
                break;
        }
        oneIndustry = shopListBean.getOneIndustry();
        twoIndustry = shopListBean.getTwoIndustry();
        tvJypl.setText(oneIndustry +"-"+twoIndustry);
        tvDpPhone.setText(shopListBean.getMobilePhone());
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
        if (data!=null&&requestCode==IndustrySecondActivity.REQUESTCODE_JYLX){
            oneIndustry = data.getStringExtra("oneIndustry");
            twoIndustry = data.getStringExtra("twoIndustry ");
            tvJypl.setText(oneIndustry +"-"+twoIndustry);
        }
    }

    //选择商户类
    private void initJylx() {
        //数据源
        final List<String> strings = new ArrayList<>();
        strings.add("⻔店场所");
        strings.add("流动经营/便⺠服务");
        strings.add("线上商品/服务交易");
        pvOptions = new OptionsPickerView(new OptionsPickerView.Builder(mContext, new OptionsPickerView.OnOptionsSelectListener() {

            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                tvJylx.setText(strings.get(options1));
                switch (options1) {
                    case 0:
                        microBizType = "MICRO_TYPE_STORE";
                        break;
                    case 1:
                        microBizType = "MICRO_TYPE_MOBILE";
                        break;
                    case 2:
                        microBizType = "MICRO_TYPE_ONLINE";
                        break;
                }
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
        pvOptions.setPicker(strings);//添加数据源


    }

    @OnClick({R.id.ll_jylx, R.id.ll_jypl, R.id.tv_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_jylx:
                if (pvOptions!=null){
                    pvOptions.show();
                }
                break;
            case R.id.ll_jypl:
                IndustrySecondActivity.actionStart(mActivity);
                break;
            case R.id.tv_submit:
                if (!isUpdata){
                    submit();
                }else {
                    updata();
                }
                break;
        }
    }

    private void submit() {
        LoadingDialog.showDialogForLoading(mActivity);
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("sj_login_token", LoginUtil.getLoginToken());
        hashMap.put("oneIndustry",oneIndustry);
        hashMap.put("twoIndustry",twoIndustry);
        hashMap.put("mobilePhone",tvDpPhone.getText().toString());
        hashMap.put("microBizType",microBizType);
        new MyHttp().doPost(Api.getDefault().create(hashMap), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                showShortToast("设置成功");
                startActivity(new Intent(mContext,CredentialsActivity.class));
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    private void updata() {
        LoadingDialog.showDialogForLoading(mActivity);
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("sj_login_token", LoginUtil.getLoginToken());
        hashMap.put("oneIndustry",oneIndustry);
        hashMap.put("twoIndustry",twoIndustry);
        hashMap.put("mobilePhone",tvDpPhone.getText().toString());
        hashMap.put("microBizType",microBizType);
        new MyHttp().doPost(Api.getDefault().update_declare_shopinfo(hashMap), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                showShortToast("设置成功");
                startActivity(new Intent(mContext,CredentialsActivity.class));
            }

            @Override
            public void onError(int code) {

            }
        });
    }
}