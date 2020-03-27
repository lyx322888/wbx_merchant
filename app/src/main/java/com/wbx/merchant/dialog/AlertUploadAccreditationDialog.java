package com.wbx.merchant.dialog;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wbx.merchant.R;
import com.wbx.merchant.activity.AccreditationActivity;
import com.wbx.merchant.activity.GoodsAccreditationActivity;
import com.wbx.merchant.baseapp.AppConfig;
import com.wbx.merchant.utils.SPUtils;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 */
public class AlertUploadAccreditationDialog extends DialogFragment {

    private boolean isAlreadyUploadLicence;

    public static AlertUploadAccreditationDialog newInstance(boolean isAlreadyUploadLicence) {
        AlertUploadAccreditationDialog uploadAccreditationDialog = new AlertUploadAccreditationDialog();
        Bundle bundle = new Bundle();
        bundle.putBoolean("isAlreadyUploadLicence", isAlreadyUploadLicence);
        uploadAccreditationDialog.setArguments(bundle);
        return uploadAccreditationDialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isAlreadyUploadLicence = getArguments().getBoolean("isAlreadyUploadLicence");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alert_upload_accreditation_dialog, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.tv_upload, R.id.tv_no_warn_again, R.id.iv_close})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_upload:
                if (isAlreadyUploadLicence) {
                    GoodsAccreditationActivity.actionStart(getActivity());
                } else {
                    AccreditationActivity.actionStart(getActivity());
                }
                break;
            case R.id.tv_no_warn_again:
                SPUtils.setSharedBooleanData(getContext(), AppConfig.NO_ASK_AGAIN_ACCREDITATION, true);
                break;
            case R.id.iv_close:
                break;
            default:
                break;
        }
        dismiss();
    }
}
