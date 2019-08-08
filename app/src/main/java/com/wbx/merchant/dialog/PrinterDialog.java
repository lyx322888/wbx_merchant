package com.wbx.merchant.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wbx.merchant.R;
import com.wbx.merchant.bean.PrinterBrandBean;
import com.wbx.merchant.utils.GlideUtils;

import java.util.List;

/**
 * 店铺领取优惠券弹窗
 */
public class PrinterDialog extends Dialog {
    private Context mContext;
    private View layout;
    private List<PrinterBrandBean.PrinterBean> mPrinterList;
    private PrintBrandAdapter adapter;
    private DialogListener listener;

    public PrinterDialog(Context context, List<PrinterBrandBean.PrinterBean> list, DialogListener listener) {
        super(context, R.style.DialogTheme);
        this.mPrinterList = list;
        this.mContext = context;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layout = LayoutInflater.from(mContext).inflate(R.layout.dialog_print_add, null);
        setContentView(layout);
        init();
        initView();
    }

    private void initView() {
        View btnNext = layout.findViewById(R.id.btn_next);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.dialogClickListener(mPrinterList.get(checkIndex));
                }
                dismiss();
            }
        });
        RecyclerView recyclerView = layout.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        adapter = new PrintBrandAdapter(mPrinterList);
        adapter.bindToRecyclerView(recyclerView);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (checkIndex != position) {
                    checkIndex = position;
                    adapter.notifyDataSetChanged();
                }
            }
        });
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

    public interface DialogListener {
        void dialogClickListener(PrinterBrandBean.PrinterBean bean);
    }

    private int checkIndex;

    private class PrintBrandAdapter extends BaseQuickAdapter<PrinterBrandBean.PrinterBean, BaseViewHolder> {
        PrintBrandAdapter(@Nullable List<PrinterBrandBean.PrinterBean> data) {
            super(R.layout.item_printer_brand, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, PrinterBrandBean.PrinterBean printerBean) {
            ImageView ivLogo = holder.getView(R.id.iv_logo);
            GlideUtils.showRoundMediumPic(mContext, ivLogo, printerBean.getShop_brand_logo());
            ImageView ivCheck = holder.getView(R.id.iv_check);
            ImageView clLayout = holder.getView(R.id.cl_layout);
            if (checkIndex == holder.getAdapterPosition()) {
                ivCheck.setBackgroundResource(R.drawable.ic_ok);
                clLayout.setBackgroundResource(R.drawable.ova_line_green);
            } else {
                ivCheck.setBackgroundResource(R.drawable.ic_round);
                clLayout.setBackgroundResource(R.drawable.ova_line_white);
            }
        }
    }
}