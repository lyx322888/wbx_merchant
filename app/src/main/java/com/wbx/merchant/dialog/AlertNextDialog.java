package com.wbx.merchant.dialog;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
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
 */
public class AlertNextDialog extends DialogFragment {


    TextView tv_content;
    private String content;
    private int returned_type;

    public static AlertNextDialog newInstance(String content, int returned_type) {
        AlertNextDialog uploadAccreditationDialog = new AlertNextDialog();
        Bundle bundle = new Bundle();
        bundle.putString("content", content);
        bundle.putInt("returned_type", returned_type);
        uploadAccreditationDialog.setArguments(bundle);
        return uploadAccreditationDialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        content = getArguments().getString("content");
        returned_type = getArguments().getInt("returned_type");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_next_dialog, container, false);
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
                //returned_type 1 食品证的问题  returned_type 2 资质的问题     returned_type 3 食品证与资质都有问题
                switch (returned_type){
                    case 1:
                        GoodsAccreditationActivity.actionStart(getActivity());
                        break;
                    case 2:
                        AccreditationActivity.actionStart(getActivity());
                        break;
                    case 3:
                        AccreditationActivity.actionStart(getActivity());
                        break;
                }
                break;
            case R.id.tv_no_warn_again:
                break;
            default:
                break;
        }
        dismiss();
    }

}
