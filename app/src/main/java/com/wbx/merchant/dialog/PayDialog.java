package com.wbx.merchant.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.flyco.roundview.RoundTextView;
import com.wbx.merchant.R;
import com.wbx.merchant.baseapp.AppConfig;
import com.wbx.merchant.bean.CricePayBaen;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 *
 */
public class PayDialog extends DialogFragment {


    @Bind(R.id.tv_price)
    TextView tvPrice;
    @Bind(R.id.ali_pay_im)
    ImageView aliPayIm;
    @Bind(R.id.ali_pay_layout)
    LinearLayout aliPayLayout;
    @Bind(R.id.wx_pay_im)
    ImageView wxPayIm;
    @Bind(R.id.wx_pay_layout)
    LinearLayout wxPayLayout;
    @Bind(R.id.tv_pay)
    RoundTextView tvPay;
    private DialogListener listener;
    private String payPrice ="100";
    private String payMode = AppConfig.PayMode.alipay;;
    public static PayDialog newInstance(String payPrice) {
        PayDialog uploadAccreditationDialog = new PayDialog();
        Bundle bundle = new Bundle();
        bundle.putString("payPrice",payPrice);
        uploadAccreditationDialog.setArguments(bundle);
        return uploadAccreditationDialog;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        payPrice = getArguments().getString("payPrice");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_pay, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        tvPrice.setText("¥"+payPrice);
        tvPay.setText(String.format("确定支付%s元",payPrice));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    public void setDialogListener(DialogListener listener) {
        this.listener = listener;
    }

    @Override
    public void show(@NonNull FragmentManager manager, @Nullable String tag) {
        super.show(manager, tag);
    }

    @OnClick({R.id.ali_pay_layout, R.id.wx_pay_layout, R.id.tv_pay})
    public void onClick(View view) {
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
            case R.id.tv_pay:
                if (listener!=null){
                    listener.dialogClickListener(payPrice,payMode);
                }
                break;
        }
    }

    public interface DialogListener {
        void dialogClickListener(String price,String payMode);
    }
}
