package com.wbx.merchant.dialog;

import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.wbx.merchant.R;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseApplication;
import com.wbx.merchant.utils.ToastUitl;
import com.wbx.merchant.widget.LoadingDialog;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 */
public class DaDaCouponDialog extends DialogFragment {
    @Bind(R.id.tv_receive)
    TextView tvReceive;

    public static DaDaCouponDialog newInstance() {
        DaDaCouponDialog daDaCouponDialog = new DaDaCouponDialog();
        return daDaCouponDialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feng_niao_coupon_dialog, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.tv_receive)
    public void onViewClicked() {
        tvReceive.setEnabled(false);
        LoadingDialog.showDialogForLoading(getActivity(), "领取中...", true);
        new MyHttp().doPost(Api.getDefault().gainDaDaMoney(BaseApplication.getInstance().readLoginUser().getSj_login_token()), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                ToastUitl.showLong(result.getString("msg"));
                dismiss();
            }

            @Override
            public void onError(int code) {
                tvReceive.setEnabled(true);
            }
        });
    }
}
