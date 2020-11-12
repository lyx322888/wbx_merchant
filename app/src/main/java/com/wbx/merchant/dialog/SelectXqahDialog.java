package com.wbx.merchant.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.flyco.roundview.RoundTextView;
import com.wbx.merchant.R;
import com.wbx.merchant.adapter.FollowersDrainageAdapter;
import com.wbx.merchant.bean.FollowersDrainageBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectXqahDialog extends Dialog {

    @Bind(R.id.tv_qx)
    ImageView tvQx;
    @Bind(R.id.tv_qr)
    RoundTextView tvQr;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    private DialogListener dialogListener;
    FollowersDrainageAdapter followersDrainageAdapter;

    public SelectXqahDialog(@NonNull Context context) {
        super(context, R.style.DialogTheme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View layout = LayoutInflater.from(getContext()).inflate(R.layout.pop_xqah, null);
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


        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
        followersDrainageAdapter = new FollowersDrainageAdapter();
        recyclerView.setAdapter(followersDrainageAdapter);

        followersDrainageAdapter.setOnItemClickListener((adapter, view, position) -> {
            followersDrainageAdapter.getItem(position).setIs_select( followersDrainageAdapter.getItem(position).getIs_select()==0?1:0);
            followersDrainageAdapter.notifyItemChanged(position);
        });
    }

    public void setList(List<FollowersDrainageBean.DataBean.HobbyBean> list) {
        followersDrainageAdapter.setNewData(list);

    }

    @OnClick({R.id.tv_qx, R.id.tv_qr})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_qx:
                dismiss();
                break;
            case R.id.tv_qr:
                if (dialogListener!=null){
                    dialogListener.dialogClickListener(followersDrainageAdapter.getData());
                }
                dismiss();
                break;
        }
    }

    public void setDialogListener(DialogListener listener) {
        this.dialogListener = listener;
    }

    public interface DialogListener {
        void dialogClickListener(List<FollowersDrainageBean.DataBean.HobbyBean> list);
    }
}
