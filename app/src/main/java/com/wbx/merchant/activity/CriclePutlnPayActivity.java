package com.wbx.merchant.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.flyco.roundview.RoundTextView;
import com.wbx.merchant.R;
import com.wbx.merchant.adapter.CriclePayAdapter;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.baseapp.AppConfig;
import com.wbx.merchant.bean.CricePayBaen;
import com.wbx.merchant.bean.WxPayInfo;
import com.wbx.merchant.common.LoginUtil;
import com.wbx.merchant.utils.PayUtils;
import com.wbx.merchant.widget.LoadingDialog;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;

//圈粉充值
public class CriclePutlnPayActivity extends BaseActivity {

    @Bind(R.id.title_name_tv)
    TextView titleNameTv;

    @Bind(R.id.ali_pay_im)
    ImageView aliPayIm;
    @Bind(R.id.ali_pay_layout)
    LinearLayout aliPayLayout;
    @Bind(R.id.wx_pay_im)
    ImageView wxPayIm;
    @Bind(R.id.wx_pay_layout)
    LinearLayout wxPayLayout;
    @Bind(R.id.tv_zf)
    RoundTextView tvZf;
    @Bind(R.id.rv_price)
    RecyclerView rvPrice;
    private String payPrice ="100";
    private String payMode  ;
    CriclePayAdapter criclePayAdapter;
    private PayUtils payUtils;

    @Override
    public int getLayoutId() {
        return R.layout.activity_cricle_putln_pay;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        rvPrice.setLayoutManager(new GridLayoutManager(mContext,4));
        criclePayAdapter = new CriclePayAdapter();
        rvPrice.setAdapter(criclePayAdapter);
        criclePayAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                criclePayAdapter.Select(position);
                payPrice = criclePayAdapter.getData().get(position).price;
                tvZf.setText("确认支付（"+payPrice+"元）");
            }
        });

        ArrayList<CricePayBaen> arrayList = new ArrayList<>();
        arrayList.add(new CricePayBaen("100",true));
        arrayList.add(new CricePayBaen("200",false));
        arrayList.add(new CricePayBaen("300",false));
        arrayList.add(new CricePayBaen("500",false));
        criclePayAdapter.setNewData(arrayList);

        payUtils = new PayUtils(mActivity, new PayUtils.OnAlipayListener() {
            @Override
            public void onSuccess() {
                showShortToast("充值成功！");
            }
        });
    }

    @Override
    public void fillData() {

    }

    @Override
    public void setListener() {

    }

    @OnClick({R.id.ali_pay_layout, R.id.wx_pay_layout, R.id.tv_zf})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ali_pay_layout:
                payMode = AppConfig.PayMode.alipay;
                aliPayIm.setImageResource(R.drawable.ic_ok);
                wxPayIm.setImageResource(R.drawable.ic_round);
                break;
            case R.id.wx_pay_layout:
                payMode = AppConfig.PayMode.wxpay;
                aliPayIm.setImageResource(R.drawable.ic_round);
                wxPayIm.setImageResource(R.drawable.ic_ok);
                break;
            case R.id.tv_zf:
                if (TextUtils.isEmpty(payMode)){
                    showShortToast("请选择支付方式");
                    return;
                }
                pay();
                break;
        }
    }

    private void pay() {
        LoadingDialog.showDialogForLoading(mActivity);
        new MyHttp().doPost(Api.getDefault().recharge(LoginUtil.getLoginToken(), payMode, payPrice), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                if (payMode.equals(AppConfig.PayMode.alipay)) {
                    payUtils.startAliPay(result.getString("data"));
                } else {
                    WxPayInfo wxPayInfo = result.getObject("data", WxPayInfo.class);
                    payUtils.startWxPay(wxPayInfo);
                }
            }

            @Override
            public void onError(int code) {

            }
        });
    }

}