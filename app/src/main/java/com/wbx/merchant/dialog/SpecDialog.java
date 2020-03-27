package com.wbx.merchant.dialog;


import android.app.Fragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wbx.merchant.R;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseApplication;
import com.wbx.merchant.bean.GoodsInfo;
import com.wbx.merchant.bean.SpecInfo;
import com.wbx.merchant.bean.UploadMemberGoodsBean;
import com.wbx.merchant.utils.ToastUitl;
import com.wbx.merchant.widget.PriceEditText;
import com.wbx.merchant.widget.iosdialog.AlertDialog;

import java.text.DecimalFormat;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SpecDialog#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SpecDialog extends DialogFragment {
    private static final String GOODS_INFO = "GOODS_INFO";
    @Bind(R.id.ll_container)
    LinearLayout llContainer;
    private GoodsInfo goodsInfo;
    private DecimalFormat decimalFormat = new DecimalFormat("0.00");

    public SpecDialog() {
    }

    public static SpecDialog newInstance(GoodsInfo goodsInfo) {
        SpecDialog fragment = new SpecDialog();
        Bundle args = new Bundle();
        args.putSerializable(GOODS_INFO, goodsInfo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        if (getArguments() != null) {
            goodsInfo = (GoodsInfo) getArguments().getSerializable(GOODS_INFO);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_spec, container, false);
        ButterKnife.bind(this, view);
        updateSpec();
        return view;
    }

    private void updateSpec() {
        llContainer.removeAllViews();
        for (final SpecInfo specInfo : goodsInfo.getGoods_attr()) {
            View inflate = LayoutInflater.from(getContext()).inflate(R.layout.item_member_goods_spec, null);
            TextView tvSpecName = inflate.findViewById(R.id.tv_spec_name);
            PriceEditText etPrice = inflate.findViewById(R.id.et_member_price);
            ImageView ivDelete = inflate.findViewById(R.id.iv_delete);
            tvSpecName.setText(specInfo.getAttr_name());
            if (specInfo.getShop_member_price() != 0) {
                etPrice.setText(decimalFormat.format(specInfo.getShop_member_price() / 100.00));
            }
            ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (specInfo.getShop_member_price() > 0) {
                        new AlertDialog(getContext()).builder().setTitle("提示").setMsg("确定删除？")
                                .setNegativeButton("取消", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                    }
                                }).setPositiveButton("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ArrayList<UploadMemberGoodsBean> lstData = new ArrayList<>();
                                UploadMemberGoodsBean uploadMemberGoodsBean = new UploadMemberGoodsBean();
                                uploadMemberGoodsBean.setGoods_id(goodsInfo.getGoods_id());
                                uploadMemberGoodsBean.setAttr_id(specInfo.getAttr_id());
                                lstData.add(uploadMemberGoodsBean);
                                new MyHttp().doPost(Api.getDefault().deleteMemberGoods(BaseApplication.getInstance().readLoginUser().getSj_login_token(), JSONArray.toJSONString(lstData)), new HttpListener() {
                                    @Override
                                    public void onSuccess(JSONObject result) {
                                        ToastUitl.showShort(result.getString("msg"));
                                        specInfo.setShop_member_price(0);
                                        goodsInfo.getGoods_attr().remove(specInfo);
                                        updateSpec();
                                    }

                                    @Override
                                    public void onError(int code) {

                                    }
                                });
                            }
                        }).show();
                    } else {
                        goodsInfo.getGoods_attr().remove(specInfo);
                        updateSpec();
                    }
                }
            });
            etPrice.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() > 0) {
                        specInfo.setShop_member_price(Float.valueOf(s.toString()) * 100);
                    }
                }
            });
            llContainer.addView(inflate);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.iv_close, R.id.tv_complete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                dismiss();
                break;
            case R.id.tv_complete:
                dismiss();
                break;
        }
    }
}
