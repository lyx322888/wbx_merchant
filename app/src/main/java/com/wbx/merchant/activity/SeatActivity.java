package com.wbx.merchant.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.wbx.merchant.R;
import com.wbx.merchant.adapter.SeatAdapter;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.base.BaseAdapter;
import com.wbx.merchant.baseapp.AppConfig;
import com.wbx.merchant.bean.SeatInfo;
import com.wbx.merchant.utils.ScannerUtils;
import com.wbx.merchant.utils.ToastUitl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by wushenghui on 2017/12/5.
 * 座位
 */

public class SeatActivity extends BaseActivity {
    @Bind(R.id.seat_recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.title_right_tv)
    TextView titleRightTv;
    @Bind(R.id.ll_empty)
    LinearLayout llEmpty;
    private List<SeatInfo> seatInfoList = new ArrayList<>();
    private SeatAdapter mAdapter;
    private Dialog dialog;
    private EditText seatNameEdit;
    private HashMap<String, Object> mParams = new HashMap<>();
    private Dialog qrCodeDialog;
    private ImageView showQrCodeIm;
    private TextView showName;
    private View inflate;
    private Bitmap mBitmap;
    private boolean isEdit = false;
    private MyHttp myHttp;

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, SeatActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_seat;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        mAdapter = new SeatAdapter(seatInfoList, mContext);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void fillData() {
        myHttp = new MyHttp();
        myHttp.doPost(Api.getDefault().getSeatInfo(userInfo.getSj_login_token()), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                List<SeatInfo> dataList = JSONArray.parseArray(result.getString("data"), SeatInfo.class);
                seatInfoList.addAll(dataList);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(int code) {
                if (code == AppConfig.ERROR_STATE.NULLDATA) {
                    isEmpty();
                }
            }
        });
    }

    private void isEmpty() {
        if (seatInfoList.size() == 0) {
            mRecyclerView.setVisibility(View.GONE);
            llEmpty.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            llEmpty.setVisibility(View.GONE);
        }
    }

    @Override
    public void setListener() {
        titleRightTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEdit) {
                    isEdit = false;
                    titleRightTv.setText("编辑");
                } else {
                    isEdit = true;
                    titleRightTv.setText("退出编辑");
                }
                mAdapter.setEdit(isEdit);
            }
        });
        mAdapter.setOnItemClickListener(R.id.show_qrcode_tv, new BaseAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(View view, int position) {
                if (null == qrCodeDialog) {
                    inflate = getLayoutInflater().inflate(R.layout.dialog_qrcode_seat, null);
                    showName = inflate.findViewById(R.id.shop_name_tv);
                    showQrCodeIm = inflate.findViewById(R.id.show_qrcode_im);
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setView(inflate);
                    qrCodeDialog = builder.create();
                    qrCodeDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            showQrCodeIm.setImageBitmap(null);
                        }
                    });
                }
                mBitmap = null;
                Glide.with(SeatActivity.this).load(mAdapter.getItem(position).getSmall_routine_photo()).asBitmap().centerCrop().into(new SimpleTarget<Bitmap>(800, 800) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        showQrCodeIm.setImageBitmap(resource);
                        mBitmap = resource;
                    }
                });
                if (!qrCodeDialog.isShowing()) {
                    qrCodeDialog.show();
                }
                inflate.findViewById(R.id.sure_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mBitmap == null) {
                            ToastUitl.showShort("图片异常，保存失败");
                            return;
                        }
                        ScannerUtils.saveImageToGallery(mContext, mBitmap, ScannerUtils.ScannerType.RECEIVER);
                        qrCodeDialog.dismiss();
                    }
                });

            }
        });
        mAdapter.setOnItemClickListener(R.id.iv_close, new BaseAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(View view, final int position) {
                new com.wbx.merchant.widget.iosdialog.AlertDialog(mContext).builder()
                        .setTitle("提示")
                        .setMsg("确定删除该座位吗")
                        .setNegativeButton("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        })
                        .setPositiveButton("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                myHttp.doPost(Api.getDefault().deleteSeat(userInfo.getSj_login_token(), "" + mAdapter.getItem(position).getId()), new HttpListener() {
                                    @Override
                                    public void onSuccess(JSONObject result) {
                                        seatInfoList.remove(position);
                                        mAdapter.notifyDataSetChanged();
                                        isEmpty();
                                    }

                                    @Override
                                    public void onError(int code) {

                                    }
                                });
                            }
                        }).show();
            }
        });

    }

    @OnClick({R.id.add_seat_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_seat_btn:
                showAddSeatDialog();
                break;
        }
    }

    private void showAddSeatDialog() {
        if (null == dialog) {
            View inflate = getLayoutInflater().inflate(R.layout.dialog_add_seat, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setView(inflate);
            dialog = builder.create();
            final EditText etMinNum = inflate.findViewById(R.id.et_min_num);
            final EditText etMaxNum = inflate.findViewById(R.id.et_max_num);
            seatNameEdit = inflate.findViewById(R.id.seat_name_edit);
            inflate.findViewById(R.id.sure_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (TextUtils.isEmpty(seatNameEdit.getText().toString())) {
                        showShortToast("请输入座位名称");
                        return;
                    }
                    if (TextUtils.isEmpty(etMinNum.getText().toString())) {
                        showShortToast("请选择用餐人数最小数");
                        return;
                    }
                    if (TextUtils.isEmpty(etMaxNum.getText().toString())) {
                        showShortToast("请选择用餐人数最大数");
                        return;
                    }
                    mParams.put("dinner_people_min", etMinNum.getText().toString());
                    mParams.put("dinner_people_max", etMaxNum.getText().toString());
                    addSeat();
                }
            });
            inflate.findViewById(R.id.cancel_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
        }
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    private void addSeat() {
        mParams.put("sj_login_token", userInfo.getSj_login_token());
        mParams.put("table_number", seatNameEdit.getText().toString());
        new MyHttp().doPost(Api.getDefault().addSeat(mParams), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                SeatInfo data = result.getObject("data", SeatInfo.class);
                seatInfoList.add(data);
                mAdapter.notifyDataSetChanged();
                showShortToast("添加成功");
                isEmpty();
                dialog.dismiss();
                dialog = null;
            }

            @Override
            public void onError(int code) {

            }
        });
    }
}
