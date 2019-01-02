package com.wbx.merchant.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wbx.merchant.R;
import com.wbx.merchant.bean.NoticeBean;
import com.wbx.merchant.utils.SPUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NoticeDialog extends DialogFragment {
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_content)
    TextView tvContent;
    private NoticeBean data;
    private String shopId;

    public static NoticeDialog newInstance(NoticeBean noticeBean, String shopId) {
        NoticeDialog noticeDialog = new NoticeDialog();
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", noticeBean);
        bundle.putString("shopId", shopId);
        noticeDialog.setArguments(bundle);
        return noticeDialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = inflater.inflate(R.layout.dialog_notice, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        data = (NoticeBean) getArguments().getSerializable("data");
        shopId = getArguments().getString("shopId");
        tvTitle.setText(data.getTitle());
        tvContent.setText(data.getContent());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.tv_know, R.id.tv_no_warn_again})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_know:
                dismiss();
                break;
            case R.id.tv_no_warn_again:
                SPUtils.setSharedStringData(getContext(), "NoticeId", shopId + ":" + data.getId());
                dismiss();
                break;
        }
    }
}
