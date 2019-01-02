package com.wbx.merchant.dialog;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.wbx.merchant.R;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseApplication;
import com.wbx.merchant.bean.ShopInfo;
import com.wbx.merchant.utils.ToastUitl;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class OperatingStateDialog extends DialogFragment {
    private static final int REST = 0;//休息中
    private static final int BUSINESS = 1;//营业中
    private static final int PREPARE = 2;//筹备中
    @Bind(R.id.tv_business)
    TextView tvBusiness;
    @Bind(R.id.iv_business)
    ImageView ivBusiness;
    @Bind(R.id.tv_prepare)
    TextView tvPrepare;
    @Bind(R.id.iv_prepare)
    ImageView ivPrepare;
    @Bind(R.id.tv_rest)
    TextView tvRest;
    @Bind(R.id.iv_rest)
    ImageView ivRest;
    private ShopInfo shopInfo;
    private OnStateChangeListener listener;

    public static OperatingStateDialog getInstance(ShopInfo shopInfo) {
        OperatingStateDialog dialog = new OperatingStateDialog();
        Bundle bundle = new Bundle();
        bundle.putSerializable("shopInfo", shopInfo);
        dialog.setArguments(bundle);
        return dialog;
    }

    public void setOnStateChangeListener(OnStateChangeListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    }

    private void loadData() {
        Bundle arguments = getArguments();
        shopInfo = (ShopInfo) arguments.getSerializable("shopInfo");
        resetState();
        switch (shopInfo.getIs_open()) {
            case 0:
                //休息中
                tvRest.setSelected(true);
                ivRest.setVisibility(View.VISIBLE);
                break;
            case 1:
                //营业中
                tvBusiness.setSelected(true);
                ivBusiness.setVisibility(View.VISIBLE);
                break;
            case 2:
                //筹备中
                tvPrepare.setSelected(true);
                ivPrepare.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void resetState() {
        tvRest.setSelected(false);
        tvBusiness.setSelected(false);
        tvPrepare.setSelected(false);
        ivBusiness.setVisibility(View.INVISIBLE);
        ivRest.setVisibility(View.INVISIBLE);
        ivPrepare.setVisibility(View.INVISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = inflater.inflate(R.layout.fragment_operating_state, container, false);
        ButterKnife.bind(this, view);
        loadData();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.cover, R.id.ll_business, R.id.ll_prepare, R.id.ll_rest, R.id.tv_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cover:
                dismiss();
                break;
            case R.id.ll_business:
                changeState(BUSINESS);
                break;
            case R.id.ll_prepare:
                changeState(PREPARE);
                break;
            case R.id.ll_rest:
                changeState(REST);
                break;
            case R.id.tv_cancel:
                dismiss();
                break;
        }
    }

    private void changeState(final int business) {
        if (shopInfo.getIs_open() == business) {
            return;
        }
        resetState();
        switch (business) {
            case REST:
                tvRest.setSelected(true);
                ivRest.setVisibility(View.VISIBLE);
                break;
            case BUSINESS:
                tvBusiness.setSelected(true);
                ivBusiness.setVisibility(View.VISIBLE);
                break;
            case PREPARE:
                tvPrepare.setSelected(true);
                ivPrepare.setVisibility(View.VISIBLE);
                break;
        }
        new MyHttp().doPost(Api.getDefault().updateOperatingState(BaseApplication.getInstance().readLoginUser().getSj_login_token(), business), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                ToastUitl.showShort(result.getString("msg"));
                shopInfo.setIs_open(business);
                if (listener != null) {
                    listener.onChange(business);
                }
                dismiss();
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    public interface OnStateChangeListener {
        void onChange(int state);
    }
}
