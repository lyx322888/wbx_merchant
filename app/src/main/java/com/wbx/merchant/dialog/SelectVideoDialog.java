package com.wbx.merchant.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.flyco.roundview.RoundTextView;
import com.wbx.merchant.R;
import com.wbx.merchant.activity.IssueSqActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectVideoDialog extends Dialog {

    @Bind(R.id.rtv_one)
    RoundTextView rtvOne;
    @Bind(R.id.rtv_tow)
    RoundTextView rtvTow;
    @Bind(R.id.rtv_qx)
    RoundTextView rtvQx;
    DialogListener dialogListener;
    public SelectVideoDialog(@NonNull Context context) {
        super(context, R.style.DialogTheme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View layout = LayoutInflater.from(getContext()).inflate(R.layout.pop_select_video, null);
        ButterKnife.bind(this, layout);
        setContentView(layout);
        init();
    }

    private void init() {
        getWindow().setGravity(Gravity.BOTTOM);//设置显示在底部
        WindowManager windowManager = getWindow().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.width = display.getWidth();//设置Dialog的宽度为屏幕宽度
//        layoutParams.height = DensityUtil.dip2px(mContext, 250);//设置Dialog的高度
        getWindow().setAttributes(layoutParams);
        getWindow().setWindowAnimations(R.style.main_menu_animStyle);
    }


    @OnClick({R.id.rtv_one, R.id.rtv_tow, R.id.rtv_qx})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rtv_one:
                if (dialogListener!=null){
                    dialogListener.dialogOneClickListener();
                }
                dismiss();
                break;
            case R.id.rtv_tow:
                if (dialogListener!=null){
                    dialogListener.dialogTowClickListener();
                }
                dismiss();
                break;
            case R.id.rtv_qx:
                dismiss();
                break;
        }
    }

    public void setDialogListener(DialogListener listener) {
        this.dialogListener = listener;
    }

    public interface DialogListener {
        void dialogOneClickListener();
        void dialogTowClickListener();
    }
}
