package com.wbx.merchant;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.flyco.roundview.RoundTextView;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.common.LoginUtil;
import com.wbx.merchant.utils.ChoosePictureUtils;
import com.wbx.merchant.utils.FileUtils;
import com.wbx.merchant.utils.GlideUtils;
import com.wbx.merchant.utils.UpLoadPicUtils;
import com.wbx.merchant.widget.LoadingDialog;
import com.wbx.merchant.widget.MyImagView;
import com.wbx.merchant.widget.PriceEditText;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import top.zibin.luban.Luban;

import static com.wbx.merchant.activity.AddStoreSetMealActivity.REQUEST__SECRE_GOODS;

//添加隐私商品
public class AddSecreGoodsActivity extends BaseActivity {

    @Bind(R.id.title_name_tv)
    TextView titleNameTv;
    @Bind(R.id.iv_fmt)
    MyImagView ivFmt;
    @Bind(R.id.tv_submit)
    RoundTextView tvSubmit;
    @Bind(R.id.et_shop_name)
    EditText etShopName;
    @Bind(R.id.et_sc_price)
    PriceEditText etScPrice;
    @Bind(R.id.et_pt_price)
    PriceEditText etPtPrice;

    String photo;

    //跳转
    public static void actionStart(Activity activity) {
        Intent intent = new Intent(activity, AddSecreGoodsActivity.class);
        activity.startActivityForResult(intent,REQUEST__SECRE_GOODS);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_secre_goods;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {

    }

    @Override
    public void fillData() {

    }


    @Override
    public void setListener() {

    }

    //选择图片
    private void chooseImage(int maxSelectable) {
        ChoosePictureUtils.choosePictureCommon(mContext, maxSelectable, result -> {
            //图片压缩
            LoadingDialog.showDialogForLoading(mActivity);
            Observable.just(result)
                    .observeOn(Schedulers.io())
                    .map(list -> {
                        // 同步方法直接返回压缩后的文件
                        ArrayList<String> arrayList = new ArrayList<>();
                        List<File> files = null;
                        try {
                            files = Luban.with(mContext).load(list).setTargetDir(FileUtils.getCacheDir(mContext, "image").getAbsolutePath()).get();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        for (int i = 0; i < files.size(); i++) {
                            arrayList.add(files.get(i).getPath());
                        }
                        return arrayList;
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(arrayList -> upLoadPic(arrayList));
        });
    }

    //上传图片七牛
    private void upLoadPic(ArrayList<String> arrayList) {
        UpLoadPicUtils.batchUpload(arrayList, new UpLoadPicUtils.BatchUpLoadPicListener() {
            @Override
            public void success(List<String> qiNiuPath) {
                photo = qiNiuPath.get(0);
                GlideUtils.showBigPic(mContext, ivFmt,photo);
                LoadingDialog.cancelDialogForLoading();
            }

            @Override
            public void error() {
                showShortToast("上传失败，请稍后重试");
                LoadingDialog.cancelDialogForLoading();
            }
        });
    }

    @OnClick({R.id.iv_fmt, R.id.tv_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_fmt:
                chooseImage(1);
                break;
            case R.id.tv_submit:
                if (TextUtils.isEmpty(etShopName.getText())){
                    showShortToast("请输入商品名称");
                    return;
                }
                if (TextUtils.isEmpty(etScPrice.getText())){
                    showShortToast("请输入市场价");
                    return;
                }
                if (TextUtils.isEmpty(etPtPrice.getText())){
                    showShortToast("请输入平台价");
                    return;
                }
                if (TextUtils.isEmpty(photo)){
                    showShortToast("请上传商品图片");
                    return;
                }
                submit();
                    break;
        }
    }

    private void submit() {
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("sj_login_token", LoginUtil.getLoginToken());
        hashMap.put("title",etShopName.getText());
        hashMap.put("price",etScPrice.getText());
        hashMap.put("mall_price",etPtPrice.getText());
        hashMap.put("photo",photo);
        LoadingDialog.showDialogForLoading(mActivity);
        new MyHttp().doPost(Api.getDefault().add_secret_goods(hashMap), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void onError(int code) {

            }
        });
    }

}