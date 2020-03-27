package com.wbx.merchant.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.kyleduo.switchbutton.SwitchButton;
import com.orhanobut.logger.Logger;
import com.wbx.merchant.R;
import com.wbx.merchant.adapter.PhotoAdapter;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.base.BaseAdapter;
import com.wbx.merchant.baseapp.AppConfig;
import com.wbx.merchant.bean.ShopDetailInfo;
import com.wbx.merchant.utils.GlideUtils;
import com.wbx.merchant.utils.UpLoadPicUtils;
import com.wbx.merchant.widget.LoadingDialog;
import com.wbx.merchant.widget.decoration.SpacesItemDecoration;
import com.wbx.merchant.widget.iosdialog.AlertDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;

/**
 * Created by wushenghui on 2017/6/20.
 * 店铺管理
 */

public class StoreManagerActivity extends BaseActivity implements AMapLocationListener {
    public static final int REQUEST_UPDATE_BUSINESS_TIME = 1001;//更新营业时间
    public static final int REQUEST_UPDATE_SEND_SETTING = 1002;//更新配送设置
    @Bind(R.id.pic_recycler_view)
    RecyclerView picRecyclerView;
    @Bind(R.id.store_signage_pic_im)
    ImageView storeSignageIm;
    @Bind(R.id.shop_name_edit)
    TextView shopNameEdit;
    @Bind(R.id.location_shop_address_tv)
    TextView locationShopAddressTv;
    @Bind(R.id.shop_contact_edit)
    EditText shopContactEdit;
    @Bind(R.id.shop_tel_edit)
    EditText shopTelEdit;
    @Bind(R.id.shop_open_time)
    TextView shopOpenTime;
    @Bind(R.id.detail_edit)
    EditText detailEdit;
    @Bind(R.id.get_location_btn)
    Button getLocationBtn;
    @Bind(R.id.shop_address_edit)
    EditText shopAddressEdit;
    @Bind(R.id.sb_auto_print)
    SwitchButton sbAutoPrint;
    @Bind(R.id.sb_auto_send_by_dada)
    SwitchButton sbAutoSendByDaDa;
    @Bind(R.id.sb_auto_send_by_seller)
    SwitchButton sbAutoSendBySeller;
    private ArrayList<String> picList = new ArrayList<>();
    private PhotoAdapter mAdapter;
    private ShopDetailInfo data;
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    private String selectPhoto = "";
    private ArrayList<String> selectPicList = new ArrayList<>();
    private ArrayList<String> qiniuPathList = new ArrayList<>();
    private int selectPicNum;
    HashMap<String, Object> params = new HashMap<>();

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, StoreManagerActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_store_manager;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        initLocation();
        picList.add("");
        picRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        picRecyclerView.addItemDecoration(new SpacesItemDecoration(8));
        mAdapter = new PhotoAdapter(picList, mContext);
        picRecyclerView.setAdapter(mAdapter);
    }

    private void initLocation() {
        //初始化AMapLocationClientOption对象
        AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        mLocationOption.setOnceLocationLatest(true);
        mLocationOption.setOnceLocation(true);
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //初始化定位
        mLocationClient = new AMapLocationClient(mContext.getApplicationContext());
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //设置定位回调监听
        mLocationClient.setLocationListener(this);
    }

    @Override
    public void fillData() {
        LoadingDialog.showDialogForLoading(mActivity, "加载中...", true);
        new MyHttp().doPost(Api.getDefault().getShopDetail(userInfo.getSj_login_token()), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                data = JSONObject.parseObject(result.getString("data"), ShopDetailInfo.class);
                setData();
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    private void setData() {
        GlideUtils.showBigPic(mContext, storeSignageIm, data.getLogo());
        shopAddressEdit.setText(data.getAddr());
        shopContactEdit.setText(data.getContact());
        shopTelEdit.setText(data.getTel());
        shopOpenTime.setText(data.getBusiness_time());
        shopNameEdit.setText(data.getShop_name());
        sbAutoSendBySeller.setChecked(data.getIs_print_deliver() != 0);
        sbAutoSendByDaDa.setChecked(data.getIs_auto_dada() != 0);
        if (userInfo.getGrade_id() == AppConfig.StoreGrade.MARKET) {   //菜市场
            sbAutoPrint.setChecked(data.getIs_ele_print() != 0);
        } else {
            sbAutoPrint.setChecked(data.getIs_goods_print() != 0);
        }
        detailEdit.setText(data.getDetails());
        //必须传的参数
        params.put("sj_login_token", userInfo.getSj_login_token());
        params.put("logo", data.getLogo());
        params.put("addr", data.getAddr());
        locationShopAddressTv.setText(TextUtils.isEmpty(data.getAddr()) ? "请定位" : "已定位");
        params.put("tel", data.getTel());
        params.put("is_print_deliver", data.getIs_print_deliver());
        params.put("is_ele_print", data.getIs_goods_print());
        params.put("lat", data.getLat());
        params.put("lng", data.getLng());
        params.put("is_daofu_pay", 0);
        if (userInfo.getGrade_id() == AppConfig.StoreGrade.MARKET) {
            params.put("is_ele_print", data.getIs_ele_print());
        } else {
            params.put("is_goods_print", data.getIs_goods_print());
        }
        if (data.getPhotos() != null) {
            selectPicNum = data.getPhotos().size();
            String join = TextUtils.join(",", data.getPhotos());
            params.put("photos", join);
            picList.addAll(data.getPhotos());
            qiniuPathList.addAll(data.getPhotos());
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void setListener() {
        mAdapter.setOnItemClickListener(R.id.iv_delete, new BaseAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(View view, final int position) {
                new AlertDialog(mContext).builder()
                        .setTitle("提示")
                        .setMsg("删除此照片？")
                        .setNegativeButton("再想想", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                            }
                        })
                        .setPositiveButton("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                selectPicNum--;
                                qiniuPathList.remove(mAdapter.getItem(position));
                                selectPicList.remove(mAdapter.getItem(position));
                                picList.remove(position);
                                mAdapter.notifyDataSetChanged();
                                params.put("photos", TextUtils.join(",", qiniuPathList));
                            }
                        }).show();
            }
        });
        //自动打印
        sbAutoPrint.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (userInfo.getGrade_id() == AppConfig.StoreGrade.MARKET) {
                    params.put("is_ele_print", isChecked ? 1 : 0);
                } else {
                    params.put("is_goods_print", isChecked ? 1 : 0);
                }
            }
        });
        //商家自动发货
        sbAutoSendBySeller.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                params.put("is_print_deliver", isChecked ? 1 : 0);
                if (isChecked) {
                    sbAutoSendByDaDa.setChecked(false);
                }
            }
        });
        //达达自动发货
        sbAutoSendByDaDa.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                params.put("is_auto_dada", isChecked ? 1 : 0);
                if (isChecked) {
                    sbAutoSendBySeller.setChecked(false);
                }
            }
        });
        getLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLocationBtn.setText("定位中...");
                getLocationBtn.setClickable(false);
                getLocationBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.ova_bg_gray));
                mLocationClient.startLocation();
            }
        });
        mAdapter.setOnItemClickListener(R.id.root_view, new BaseAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(View view, int position) {
                if (position == 0) {
                    if (selectPicList.size() == AppConfig.MAX_COUNT) {
                        showShortToast("最多只能选6张哦！");
                        return;
                    }
                    PhotoPicker.builder()
                            .setPhotoCount(AppConfig.MAX_COUNT - selectPicList.size())
                            .setShowCamera(true)
                            .setPreviewEnabled(false)
                            .start(mActivity, PhotoPicker.REQUEST_CODE);
                } else {
                    if (picList.size() > 0) {
                        PhotoPreview.builder()
                                .setPhotos(picList)
                                .setCurrentItem(position)
                                .setShowDeleteButton(false)
                                .start(mActivity);
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK && requestCode == PhotoPicker.REQUEST_CODE) {
            if (intent != null) {
                ArrayList<String> stringArrayListExtra = intent.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                selectPicNum += stringArrayListExtra.size();
                selectPicList.addAll(stringArrayListExtra);
                picList.addAll(selectPicList);
                mAdapter.notifyDataSetChanged();
            }
        } else if (resultCode == RESULT_OK && requestCode == AppConfig.TAKE_PHOTO_CODE) {
            if (intent != null) {
                ArrayList<String> pics = intent.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                GlideUtils.displayUri(mContext, storeSignageIm, Uri.fromFile(new File(pics.get(0))));
                selectPhoto = pics.get(0);
            }

        } else if (resultCode == RESULT_OK && requestCode == REQUEST_UPDATE_BUSINESS_TIME) {
            if (intent != null) {
                String time = intent.getStringExtra("businessTime");
                shopOpenTime.setText(time);
                if (data != null) {
                    data.setBusiness_time(time);
                }
            }
        } else if (resultCode == RESULT_OK && requestCode == REQUEST_UPDATE_SEND_SETTING) {
            if (intent != null) {
                data = (ShopDetailInfo) intent.getSerializableExtra("shopDetailInfo");
            }
        }
    }

    @OnClick({R.id.store_signage_pic_layout, R.id.choose_business_time_layout, R.id.ll_send_setting, R.id.save_btn, R.id.add_video_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.store_signage_pic_layout:
                addStoreSignagePic();
                break;
            case R.id.choose_business_time_layout:
                if (data != null) {
                    BusinessTimeActivity.actionStart(mActivity, data.getBusiness_time());
                }
                break;
            case R.id.ll_send_setting:
                if (data != null) {
                    SendSettingActivity.actionStart(this, data);
                }
                break;
            case R.id.save_btn:
                //保存
                if (!TextUtils.isEmpty(selectPhoto)) {
                    //把图片先传到七牛
                    upLoadPic();
                    return;
                }
                if (selectPicList.size() != 0) {
                    //有添加环境图
                    batchUpLoadPic();
                    return;
                }
                save();
                break;
            case R.id.add_video_layout:
                VideoActivity.actionStart(mContext);
                break;
        }
    }

    private void batchUpLoadPic() {
        //批量上传环境图
        LoadingDialog.showDialogForLoading(mActivity, "信息提交中...", true);
        for (int i = 0; i < selectPicList.size(); i++) {
            UpLoadPicUtils.upOnePic(selectPicList.get(i), new UpLoadPicUtils.UpLoadPicListener() {
                @Override
                public void success(String qiNiuPath) {
                    Logger.d("七牛:" + qiNiuPath);
                    qiniuPathList.add(qiNiuPath);
                    if (qiniuPathList.size() == selectPicNum) {
                        //所有图片上传完了
                        String join = TextUtils.join(",", qiniuPathList);
                        params.put("photos", join);
                        if (!TextUtils.isEmpty(selectPhoto)) {
                            //判断有没有更换封面图 如果有
                            UpLoadPicUtils.upOnePic(selectPhoto, new UpLoadPicUtils.UpLoadPicListener() {
                                @Override
                                public void success(String qiNiuPath) {
                                    params.put("logo", qiNiuPath);
                                    save();
                                }

                                @Override
                                public void error() {
                                    showShortToast("图片上传失败，请重试。");
                                }
                            });
                        } else {
                            save();
                        }
                    }
                }

                @Override
                public void error() {
                    showShortToast("图片上传失败，请重试。");
                }
            });
        }
    }

    private void upLoadPic() {
        LoadingDialog.showDialogForLoading(mActivity, "信息提交中...", true);
        UpLoadPicUtils.upOnePic(selectPhoto, new UpLoadPicUtils.UpLoadPicListener() {
            @Override
            public void success(String qiNiuPath) {
                Logger.d("七牛:" + qiNiuPath);
                params.put("logo", qiNiuPath);
                if (selectPicList.size() != 0) {
                    //批量上传环境图
                    for (int i = 0; i < selectPicList.size(); i++) {
                        UpLoadPicUtils.upOnePic(selectPicList.get(i), new UpLoadPicUtils.UpLoadPicListener() {
                            @Override
                            public void success(String qiNiuPath) {
                                qiniuPathList.add(qiNiuPath);
                                if (qiniuPathList.size() == selectPicList.size()) {
                                    //所有图片上传完了
                                    String join = TextUtils.join(",", qiniuPathList);
                                    params.put("photos", join);
                                    save();
                                }
                            }

                            @Override
                            public void error() {
                                showShortToast("图片上传失败，请重试。");
                            }
                        });
                    }
                } else {
                    save();
                }
            }

            @Override
            public void error() {
                showShortToast("图片上传失败，请重试。");
            }
        });
    }

    private void save() {
        params.put("addr", shopAddressEdit.getText().toString());
        if (!TextUtils.isEmpty(shopContactEdit.getText().toString())) {
            params.put("contact", shopContactEdit.getText().toString());
        }
        params.put("tel", shopTelEdit.getText().toString());
        if (!TextUtils.isEmpty(detailEdit.getText().toString())) {
            params.put("details", detailEdit.getText().toString());
        }
        new MyHttp().doPost(Api.getDefault().updateShopDetail(params), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                showShortToast(result.getString("msg"));
                finish();
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    //选择店铺招牌图
    private void addStoreSignagePic() {
        PhotoPicker.builder()
                .setPhotoCount(1)
                .setShowCamera(true)
                .setPreviewEnabled(false)
                .start(mActivity, AppConfig.TAKE_PHOTO_CODE);
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        getLocationBtn.setText("重新定位");
        getLocationBtn.setClickable(true);
        getLocationBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.ova_bg_app_color));
        params.put("lat", aMapLocation.getLatitude());
        params.put("lng", aMapLocation.getLongitude());
        shopAddressEdit.setText(aMapLocation.getRoad() + aMapLocation.getStreetNum() + aMapLocation.getPoiName());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLocationClient != null) {
            mLocationClient.onDestroy();
            mLocationClient = null;
        }
    }
}
