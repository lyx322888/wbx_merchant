package com.wbx.merchant.activity;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.EditText;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wbx.merchant.R;
import com.wbx.merchant.adapter.AddBusinessCirclePhotoAdapter;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.utils.ToastUitl;
import com.wbx.merchant.utils.UpLoadPicUtils;
import com.wbx.merchant.widget.LoadingDialog;
import com.wbx.merchant.widget.decoration.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPicker;

public class PublishBusinessCircleActivity extends BaseActivity {
    private static final int REQUEST_GET_BUSINESS_CIRCLE_PHOTO = 1000;
    public static final int MAX_PICTURE_NUM = 9;
    @Bind(R.id.et_introduce)
    EditText etIntroduce;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    private List<String> lstPhoto = new ArrayList<>();
    private AddBusinessCirclePhotoAdapter adapter;

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, PublishBusinessCircleActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_publish_business_circle;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addItemDecoration(new SpacesItemDecoration(8));
        adapter = new AddBusinessCirclePhotoAdapter(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void fillData() {
        lstPhoto.add("");
        adapter.update(lstPhoto);
    }

    @Override
    public void setListener() {

    }

    @OnClick(R.id.rl_right)
    public void onViewClicked() {
        publishDiscovery();
    }

    private void publishDiscovery() {
        if (TextUtils.isEmpty(etIntroduce.getText().toString())) {
            ToastUitl.showShort("请简单介绍您的产品...");
            return;
        }
        ArrayList<String> lstGoodsPhoto = new ArrayList();
        lstGoodsPhoto.addAll(lstPhoto);
        if (lstGoodsPhoto.size() != 0 && TextUtils.isEmpty(lstGoodsPhoto.get(lstGoodsPhoto.size() - 1))) {
            lstGoodsPhoto.remove(lstGoodsPhoto.size() - 1);
        }
        LoadingDialog.showDialogForLoading(this, "上传中...", false);
        if (lstGoodsPhoto.size() == 0) {
            submit(null);
        } else {
            UpLoadPicUtils.batchUpload(lstGoodsPhoto, new UpLoadPicUtils.BatchUpLoadPicListener() {
                @Override
                public void success(List<String> qiNiuPath) {
                    submit(qiNiuPath);
                }

                @Override
                public void error() {
                    ToastUitl.showShort("图片上传失败，请重试！");
                    LoadingDialog.cancelDialogForLoading();
                }
            });
        }
    }

    private void submit(List<String> qiNiuPath) {
        if (mActivity == null) {
            return;
        }
        LoadingDialog.showDialogForLoading(mActivity, "上传中...", false);
        new MyHttp().doPost(Api.getDefault().addDiscovery(userInfo.getSj_login_token(), etIntroduce.getText().toString(), qiNiuPath == null ? null : JSONArray.toJSONString(qiNiuPath)), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                ToastUitl.showShort(result.getString("msg"));
                MyBusinessCircleActivity.actionStart(PublishBusinessCircleActivity.this);
                finish();
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    public void addPhoto() {
        PhotoPicker.builder().setShowCamera(true).setPhotoCount(MAX_PICTURE_NUM - lstPhoto.size() + 1).setPreviewEnabled(true).start(this, REQUEST_GET_BUSINESS_CIRCLE_PHOTO);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && requestCode == REQUEST_GET_BUSINESS_CIRCLE_PHOTO) {
            ArrayList<String> pics = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
            for (String pic : pics) {
                if (!lstPhoto.contains(pic)) {
                    lstPhoto.add(lstPhoto.size() - 1, pic);
                }
            }
            if (lstPhoto.size() == MAX_PICTURE_NUM + 1) {
                lstPhoto.remove(MAX_PICTURE_NUM);
            }
            adapter.update(lstPhoto);
        }
    }
}
