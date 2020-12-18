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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.wbx.merchant.R;
import com.wbx.merchant.adapter.AddDdtcAdapter;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.bean.StoreSetMealBean;
import com.wbx.merchant.common.LoginUtil;
import com.wbx.merchant.utils.AdapterUtilsNew;
import com.wbx.merchant.widget.refresh.BaseRefreshListener;
import com.wbx.merchant.widget.refresh.PullToRefreshLayout;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

//选择到店套餐
public class SelectDdtcDialog extends Dialog {

    int page = 1;
    @Bind(R.id.tv_qx)
    ImageView tvQx;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.tr_refresh)
    PullToRefreshLayout trRefresh;
    AddDdtcAdapter selectDdtcAdapter;
    private DialogListener listener;

    public SelectDdtcDialog(@NonNull Context context) {
        super(context, R.style.DialogTheme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View layout = LayoutInflater.from(getContext()).inflate(R.layout.dialog_select_ddtc, null);
        ButterKnife.bind(this, layout);
        setContentView(layout);
        initview();
        getData();
    }

    private void initview() {
        getWindow().setGravity(Gravity.BOTTOM);//设置显示在底部
        WindowManager windowManager = getWindow().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.width = display.getWidth();//设置Dialog的宽度为屏幕宽度
//        layoutParams.height = DensityUtil.dip2px(mContext, 250);//设置Dialog的高度
        getWindow().setAttributes(layoutParams);
        getWindow().setWindowAnimations(R.style.main_menu_animStyle);


        trRefresh.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                getData();
            }

            @Override
            public void loadMore() {
            }
        });
        trRefresh.setCanLoadMore(false);

        selectDdtcAdapter = new AddDdtcAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(selectDdtcAdapter);

        selectDdtcAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (listener!=null&& selectDdtcAdapter.getData().size()>0){
                    listener.dialogBeanClickListener(selectDdtcAdapter.getItem(position));
                }
                dismiss();
            }
        });
    }

    private void getData() {
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("sj_login_token", LoginUtil.getLoginToken());
        hashMap.put("is_video_select", "1");
        new MyHttp().doPost(Api.getDefault().list_shop_set_meal(hashMap), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                StoreSetMealBean storeSetMealBean  = new Gson().fromJson(result.toString(),StoreSetMealBean.class);
                selectDdtcAdapter.setNewData(storeSetMealBean.getData());
            }

            @Override
            public void onError(int code) {

            }
        });
    }


    @OnClick({R.id.tv_qx})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_qx:
                dismiss();
                break;
        }
    }

    public void setDialogListener(DialogListener listener) {
        this.listener = listener;
    }

    public static abstract class DialogListener {
        public abstract void dialogBeanClickListener(StoreSetMealBean.DataBean dataBean);

    }

    //刷新列表
    public void refresh(){
        page = 1;
        getData();
    }
}
