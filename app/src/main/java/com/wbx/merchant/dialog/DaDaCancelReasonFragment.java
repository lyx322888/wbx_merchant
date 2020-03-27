package com.wbx.merchant.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.wbx.merchant.R;
import com.wbx.merchant.adapter.DaDaCancelReasonAdapter;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.bean.DaDaCancelReasonBean;
import com.wbx.merchant.common.LoginUtil;
import com.wbx.merchant.widget.LoadingDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DaDaCancelReasonFragment extends DialogFragment {
    private static final String ORDER_ID = "ORDER_ID";
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    private OnResultListener mListener;
    private String order_id;
    private ArrayList<DaDaCancelReasonBean> lstData = new ArrayList<>();
    private DaDaCancelReasonAdapter adapter;

    public DaDaCancelReasonFragment() {
    }

    public static DaDaCancelReasonFragment newInstance(String order_id) {
        DaDaCancelReasonFragment fragment = new DaDaCancelReasonFragment();
        Bundle args = new Bundle();
        args.putString(ORDER_ID, order_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            order_id = getArguments().getString(ORDER_ID);
        }
        loadData();
    }

    private void loadData() {
        LoadingDialog.showDialogForLoading(getActivity());
        new MyHttp().doPost(Api.getDefault().getDaDaCancelReasons(LoginUtil.getLoginToken()), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                List<DaDaCancelReasonBean> data = JSONArray.parseArray(result.getString("data"), DaDaCancelReasonBean.class);
                lstData.addAll(data);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_da_da_cancel_reason, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new DaDaCancelReasonAdapter(lstData);
        adapter.bindToRecyclerView(recyclerView);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.ll_root) {
                    if (!lstData.get(position).isSelect()) {
                        for (DaDaCancelReasonBean lstDatum : lstData) {
                            lstDatum.setSelect(false);
                        }
                        lstData.get(position).setSelect(true);
                        if ("10000".endsWith(lstData.get(position).getId()) && adapter.getFooterLayout().getChildCount() > 0) {
                            //选中其他
                            adapter.getFooterLayout().getChildAt(0).setVisibility(View.VISIBLE);
                        } else {
                            adapter.getFooterLayout().getChildAt(0).setVisibility(View.INVISIBLE);
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });
        adapter.addFooterView(LayoutInflater.from(getContext()).inflate(R.layout.layout_foot_dada_cancel_reason, null));
        adapter.getFooterLayout().getChildAt(0).setVisibility(View.INVISIBLE);
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.BOTTOM;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(layoutParams);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public void setOnResultListener(OnResultListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.iv_close, R.id.tv_ensure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                dismiss();
                break;
            case R.id.tv_ensure:
                ensure();
                break;
        }
    }

    private void ensure() {
        String reasonId = "";
        for (DaDaCancelReasonBean lstDatum : lstData) {
            if (lstDatum.isSelect()) {
                reasonId = lstDatum.getId();
                break;
            }
        }
        if (TextUtils.isEmpty(reasonId)) {
            Toast.makeText(getContext(), "请选择取消原因", Toast.LENGTH_SHORT).show();
            return;
        }
        String otherReason = "";
        if (reasonId.endsWith("10000")) {
            otherReason = ((EditText) adapter.getFooterLayout().getChildAt(0)).getText().toString();
            if (TextUtils.isEmpty(otherReason)) {
                Toast.makeText(getContext(), "请输入具体原因", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        LoadingDialog.showDialogForLoading(getActivity());
        new MyHttp().doPost(Api.getDefault().cancelDaDaOrder(LoginUtil.getLoginToken(), order_id, reasonId, otherReason), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                Toast.makeText(getContext(), result.getString("msg"), Toast.LENGTH_SHORT).show();
                if (mListener != null) {
                    mListener.onSuccess();
                }
                dismiss();
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    public interface OnResultListener {
        void onSuccess();
    }
}