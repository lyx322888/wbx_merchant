package com.wbx.merchant.dialog;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wbx.merchant.R;
import com.wbx.merchant.activity.AccreditationActivity;
import com.wbx.merchant.activity.GoodsAccreditationActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * 确认框
 */
public class ConfirmDialog extends DialogFragment {

    private DialogListener listener;
    private TextView tv_content;
    private String content;


    public static ConfirmDialog newInstance(String content ) {
        ConfirmDialog uploadAccreditationDialog = new ConfirmDialog();
        Bundle bundle = new Bundle();
        bundle.putString("content", content);
        uploadAccreditationDialog.setArguments(bundle);
        return uploadAccreditationDialog;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        content = getArguments().getString("content");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.confirm_dialog, container, false);
        ButterKnife.bind(this, view);
        tv_content = view.findViewById(R.id.tv_content);
        tv_content.setText(content);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.tv_upload, R.id.tv_no_warn_again})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_upload:
                if (listener!=null){
                    listener.dialogClickListener();
                }
                break;
            case R.id.tv_no_warn_again:
                break;
            default:
                break;
        }
        dismiss();
    }

    public void setDialogListener(DialogListener listener) {
        this.listener = listener;
    }




    public interface DialogListener {
        void dialogClickListener( );
    }
}
