package com.wbx.merchant.dialog;

import android.app.DialogFragment;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wbx.merchant.R;
import com.wbx.merchant.activity.WebActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PromptPopDiolog extends DialogFragment {
    @Bind(R.id.tv_content)
    TextView tvContent;
    @Bind(R.id.tv_zdl)
    TextView tvZdl;
    private String content;

    public static PromptPopDiolog newInstance(String content) {
        PromptPopDiolog promptPopDiolog = new PromptPopDiolog();
        Bundle bundle = new Bundle();
        bundle.putString("content", content);
        promptPopDiolog.setArguments(bundle);
        return promptPopDiolog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        content = getArguments().getString("content");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.diolog_prompt, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        tvContent.setText(content);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @OnClick({R.id.tv_zdl,R.id.dialog_js_btn})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.tv_zdl:
                dismiss();
                break;
            case R.id.dialog_js_btn:
                // 手续费介绍
                WebActivity.actionStart(getContext(), "http://www.wbx365.com/Wbxwaphome/protocol/payment.html","支付收费标准");
                dismiss();
                break;

        }
    }
}
