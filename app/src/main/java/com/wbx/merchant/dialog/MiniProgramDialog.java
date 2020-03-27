package com.wbx.merchant.dialog;


import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.wbx.merchant.R;
import com.wbx.merchant.baseapp.AppConfig;
import com.wbx.merchant.bean.ShopInfo;
import com.wbx.merchant.utils.ImageUtil;
import com.wbx.merchant.utils.ShareUtils;
import com.wbx.merchant.utils.ToastUitl;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class MiniProgramDialog extends DialogFragment {
    @Bind(R.id.iv_mini_program)
    ImageView ivMiniProgram;
    private ShopInfo shopInfo;
    private Bitmap miniProBitmap;

    public static MiniProgramDialog getInstance(ShopInfo shopInfo) {
        MiniProgramDialog miniProgramDialog = new MiniProgramDialog();
        Bundle bundle = new Bundle();
        bundle.putSerializable("shopInfo", shopInfo);
        miniProgramDialog.setArguments(bundle);
        return miniProgramDialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadData();
    }

    private void loadData() {
        Bundle arguments = getArguments();
        shopInfo = (ShopInfo) arguments.getSerializable("shopInfo");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = inflater.inflate(R.layout.fragment_mini_program_dialog, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        Glide.with(getActivity()).load(shopInfo.getSmall_routine_photo()).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                if (ivMiniProgram != null) {
                    ivMiniProgram.setImageBitmap(resource);
                }
                miniProBitmap = resource;
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.iv_close, R.id.btn_share, R.id.tv_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                dismiss();
                break;
            case R.id.btn_share:
                ShareUtils.getInstance().shareMiniProgram(getContext(), shopInfo.getShop_name(), "我在微百姓开了一家店，赶快来看看吧", shopInfo.getPhoto(), "pages/home/store/store?shopID=" + shopInfo.getShop_id() + "&gradeid=" + shopInfo.getGrade_id(), shopInfo.getGrade_id() == AppConfig.StoreGrade.MARKET ? "http://www.wbx365.com/wap/ele/shop/shop_id/" + shopInfo.getShop_id() + ".html" : "http://www.wbx365.com/wap/shop/goods/shop_id/" + shopInfo.getShop_id() + ".html");
                dismiss();
                break;
            case R.id.tv_save:
                save();
                dismiss();
                break;
        }
    }

    private void save() {
        if (miniProBitmap == null) {
            ToastUitl.showShort("抱歉，二维码错误，无法保存");
            return;
        }
        boolean isSuccess = ImageUtil.saveImageToGallery(getActivity(), miniProBitmap);
        if (isSuccess) {
            ToastUitl.showLong("保存成功");
        }
    }
}
