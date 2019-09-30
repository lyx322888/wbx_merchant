package com.wbx.merchant.dialog;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wbx.merchant.R;

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

    @OnClick(R.id.tv_zdl)
    public void onViewClicked() {
        dismiss();
    }
}
