package com.wbx.merchant.dialog;

import android.app.DialogFragment;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.wbx.merchant.R;
import com.wbx.merchant.utils.ToastUitl;
import com.wbx.merchant.widget.PriceEditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GiftbagDiolog extends DialogFragment {


    @Bind(R.id.user_nick_name_tv)
    TextView userNickNameTv;
    @Bind(R.id.selling_price_edit)
    PriceEditText sellingPriceEdit;
    @Bind(R.id.dialog_cancel_btn)
    Button dialogCancelBtn;
    @Bind(R.id.dialog_complete_btn)
    Button dialogCompleteBtn;
    private DialogListener listener;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.diolog_giftbag, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    @OnClick({R.id.dialog_cancel_btn, R.id.dialog_complete_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.dialog_cancel_btn:
                dismiss();
                break;
            case R.id.dialog_complete_btn:
                if (listener != null) {
                    if (!TextUtils.isEmpty(sellingPriceEdit.getText().toString())&&Float.valueOf(sellingPriceEdit.getText().toString())>0) {
                        listener.dialogClickListener(sellingPriceEdit.getText().toString());
                        dismiss();
                    } else {
                        ToastUitl.showShort("请输入正确面额");
                    }
                }
                break;
        }
    }

    public void setDialogListener(DialogListener listener) {
        this.listener = listener;
    }

    public interface DialogListener {
        void dialogClickListener(String content);
    }
}
