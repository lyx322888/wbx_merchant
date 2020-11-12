package com.wbx.merchant.activity;

import android.app.Dialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wbx.merchant.R;
import com.wbx.merchant.adapter.AddClassifyAdapter;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.base.BaseAdapter;
import com.wbx.merchant.baseapp.AppConfig;
import com.wbx.merchant.bean.CateInfo;
import com.wbx.merchant.widget.LoadingDialog;
import com.wbx.merchant.widget.decoration.DividerItemDecoration;
import com.wbx.merchant.widget.iosdialog.AlertDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by wushenghui on 2017/6/26.
 * 添加分类
 */

public class GoodsClassifyActivity extends BaseActivity {
    @Bind(R.id.goods_cate_recycler_view)
    RecyclerView mRecyclerView;
    private List<CateInfo> cateInfoList = new ArrayList<>();
    private AddClassifyAdapter mAdapter;
    private Dialog dialog;

    //    private List<CateInfo> newsAddCateList = new ArrayList<>();
    @Override
    public int getLayoutId() {
        return R.layout.activity_goods_classify;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
        mAdapter = new AddClassifyAdapter(cateInfoList, mContext);
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public void fillData() {
        getServiceData();
    }

    //获取分类数据
    private void getServiceData() {
        LoadingDialog.showDialogForLoading(mActivity, "加载中...", true);
        new MyHttp().doPost(Api.getDefault().getCateList(userInfo.getSj_login_token()), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                List<CateInfo> dataList = JSONArray.parseArray(result.getString("data"), CateInfo.class);
                if (null == dataList) {
                    return;
                }
                cateInfoList.clear();
                cateInfoList.addAll(dataList);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    @Override
    public void setListener() {
        mAdapter.setOnItemClickListener(R.id.swipe_edit, new BaseAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(View view, final int position) {
                if (null == dialog) {
                    dialog = new Dialog(mContext);
                }
                View inflate = LayoutInflater.from(mContext).inflate(R.layout.add_classify_dialog_view, null);
                final EditText orderByEdit = (EditText) inflate.findViewById(R.id.classify_orderby_edit);
                final EditText nameEdit = (EditText) inflate.findViewById(R.id.classify_name_edit);
                nameEdit.setText(cateInfoList.get(position).getCate_name());
                orderByEdit.setText(userInfo.getGrade_id() != AppConfig.StoreGrade.MARKET ? cateInfoList.get(position).getOrderby() : cateInfoList.get(position).getOrderby_cate());
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                dialog.setContentView(inflate);
                dialog.show();
                inflate.findViewById(R.id.dialog_complete_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String orderByStr = orderByEdit.getText().toString();
                        String nameStr = nameEdit.getText().toString();
                        if (TextUtils.isEmpty(orderByStr)) {
                            showShortToast("请输入分类排序");
                            return;
                        }
                        if (TextUtils.isEmpty(nameStr)) {
                            showShortToast("请输入分类名称");
                            return;
                        }
                        cateDoEdit(position, nameStr, orderByStr);
                    }
                });
                inflate.findViewById(R.id.dialog_cancel_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

            }
        });
        mAdapter.setOnItemClickListener(R.id.swipe_delete, new BaseAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(View view, final int position) {
                new AlertDialog(mContext).builder()
                        .setTitle("提示")
                        .setMsg("删除分类？")
                        .setNegativeButton("再想想", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        })
                        .setPositiveButton("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                deleteCate(position);

                            }
                        }).show();
            }
        });
    }

    //删除分类
    private void deleteCate(final int position) {
        LoadingDialog.showDialogForLoading(mActivity, "删除中...", true);
        new MyHttp().doPost(Api.getDefault().deleteCate(userInfo.getSj_login_token(), cateInfoList.get(position).getCate_id()), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                cateInfoList.remove(position);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    //编辑分类
    private void cateDoEdit(final int position, final String nameStr, final String orderByStr) {
        LoadingDialog.showDialogForLoading(mActivity, "信息提交中...", true);
        HashMap<String, Object> params = new HashMap<>();
        params.put("sj_login_token", userInfo.getSj_login_token());
        params.put("cate_name", nameStr);
        params.put(userInfo.getGrade_id() == AppConfig.StoreGrade.MARKET ? "orderby_cate" : "orderby", orderByStr);
        params.put("cate_id", cateInfoList.get(position).getCate_id());
        new MyHttp().doPost(Api.getDefault().addCate(params), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                if (null != dialog) {
                    dialog.dismiss();
                }
                cateInfoList.get(position).setCate_name(nameStr);
                if (userInfo.getGrade_id() == AppConfig.StoreGrade.MARKET) {
                    cateInfoList.get(position).setOrderby_cate(orderByStr);
                } else {
                    cateInfoList.get(position).setOrderby(orderByStr);
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    @OnClick({R.id.add_classify_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_classify_btn:
                showAddClassifyDialog();
                break;

        }

    }

    private void showAddClassifyDialog() {
        if (null == dialog) {
            dialog = new Dialog(mContext);
        }
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.add_classify_dialog_view, null);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        dialog.setContentView(inflate);
        dialog.show();
        final EditText orderByEdit = (EditText) inflate.findViewById(R.id.classify_orderby_edit);
        final EditText nameEdit = (EditText) inflate.findViewById(R.id.classify_name_edit);
        inflate.findViewById(R.id.dialog_complete_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String orderByStr = orderByEdit.getText().toString();
                String nameStr = nameEdit.getText().toString();
                if (TextUtils.isEmpty(orderByStr)) {
                    showShortToast("请输入分类排序");
                    return;
                }
                if (TextUtils.isEmpty(nameStr)) {
                    showShortToast("请输入分类名称");
                    return;
                }
                doAddClassify(nameStr, orderByStr);
            }
        });
        inflate.findViewById(R.id.dialog_cancel_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    //添加分类
    private void doAddClassify(final String nameStr, final String orderByStr) {
        LoadingDialog.showDialogForLoading(mActivity, "信息提交中...", true);
        HashMap<String, Object> params = new HashMap<>();
        params.put("sj_login_token", userInfo.getSj_login_token());
        params.put("cate_name", nameStr);
        params.put(userInfo.getGrade_id() == AppConfig.StoreGrade.MARKET ? "orderby_cate" : "orderby", orderByStr);
        new MyHttp().doPost(Api.getDefault().addCate(params), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                if (null != dialog) dialog.dismiss();
                getServiceData();
            }

            @Override
            public void onError(int code) {

            }
        });
    }

}
