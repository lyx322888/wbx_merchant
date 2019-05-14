package com.wbx.merchant.activity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.bigkoo.pickerview.OptionsPickerView;
import com.wbx.merchant.R;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.base.BaseApplication;
import com.wbx.merchant.baseapp.AppConfig;
import com.wbx.merchant.bean.AddressInfo;
import com.wbx.merchant.bean.JoinAddressInfo;
import com.wbx.merchant.bean.ShopGradeInfo;
import com.wbx.merchant.utils.GlideUtils;
import com.wbx.merchant.utils.PermissionsChecker;
import com.wbx.merchant.utils.SPUtils;
import com.wbx.merchant.utils.UpLoadPicUtils;
import com.wbx.merchant.widget.AddressBottomDialog;
import com.wbx.merchant.widget.LoadingDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.OnClick;
import chihane.jdaddressselector.OnAddressSelectedListener;
import chihane.jdaddressselector.model.City;
import chihane.jdaddressselector.model.County;
import chihane.jdaddressselector.model.Province;
import chihane.jdaddressselector.model.Street;
import me.iwf.photopicker.PhotoPicker;

/**
 * Created by wushenghui on 2017/6/22.
 * 输入店铺信息
 */

public class InputShopInfoActivity extends BaseActivity implements OnAddressSelectedListener {
    /**
     * 定位相关
     **/
    //所需要申请的权限数组
    private static final String[] PERMISSIONS_CONTACT = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION};
    private static final int REQUEST_CONTACTS = 1000;
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    /**
     * end
     **/

    @Bind(R.id.address_edit)
    EditText addressEdit;
    @Bind(R.id.shop_name_edit)
    EditText shopNameEdit;//商家名称
    @Bind(R.id.shop_phone_edit)
    EditText shopPhoneEdit; //商家电话
    @Bind(R.id.agency_account_edit)
    EditText agencyAccountEdit;//代理账号
    @Bind(R.id.show_shop_grade_tv)
    TextView shopShopGradeTv;
    @Bind(R.id.store_signage_pic_im)
    ImageView storeSignageIm;
    @Bind(R.id.show_shop_address_tv)
    TextView shopShopAddressTv;
    private List<ShopGradeInfo> shopGradeInfos;
    private ArrayList<ArrayList<AddressInfo.AreaBean>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<AddressInfo.AreaBean.BusinessBean>>> options3Items = new ArrayList<>();
    private OptionsPickerView addressPickerView;//地址的pickerview
    private OptionsPickerView gradePickerView;//店铺等级/商家分类
    private List<JoinAddressInfo> addressInfos;
    private HashMap<String, Object> mParams;
    private String mPhotoPath = "";
    private String selectCity = "";
    private AddressBottomDialog addressBottomDialog;
    private boolean hasLocation = false;

    @Override
    public int getLayoutId() {
        return R.layout.activity_input_shop_info;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        addressPickerView = new OptionsPickerView.Builder(mContext, new MyPickerSelectListener(0)).build();
//        gradePickerView = new OptionsPickerView.Builder(mContext, new MyPickerSelectListener(1)).build();
    }

    @Override
    public void fillData() {
        mParams = new HashMap<>();
    }

    @Override
    public void setListener() {
        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                String speChat = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#¥%……&*（）——+|{}【】‘；：”“’。，、？ ]";
                Pattern pattern = Pattern.compile(speChat);
                Matcher matcher = pattern.matcher(source.toString());
                if (matcher.matches()) {
                    return "";
                } else {
                    return source;
                }
            }
        };
        shopNameEdit.setFilters(new InputFilter[]{filter});
    }

    @OnClick({R.id.shop_info_next_btn, R.id.get_location_btn, R.id.store_signage_pic_layout, R.id.choose_shop_address_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.shop_info_next_btn:
                if (canNext()) {
                    upLoadPic();
                }
                break;
            case R.id.get_location_btn:
                initLocation();
                break;
//            case R.id.get_shop_cate_layout:
//                getShopCate();
//                break;
            case R.id.store_signage_pic_layout:
                //选择封面图
                PhotoPicker.builder()
                        .setPhotoCount(1)
                        .setShowCamera(true)
                        .setPreviewEnabled(false)
                        .start(mActivity, AppConfig.TAKE_PHOTO_CODE);
                break;
            case R.id.choose_shop_address_layout:
                //获取城市及商圈等数据
                if (addressBottomDialog == null) {
                    LoadingDialog.showDialogForLoading(mActivity, "加载中...", true);
                    getCityList();
                } else {
                    addressBottomDialog.show();
                }

                break;
        }
    }

    //点击下一步 先把图片上传到七牛 获得图片的网络地址
    private void upLoadPic() {
        LoadingDialog.showDialogForLoading(this);
        UpLoadPicUtils.upOnePic(mPhotoPath, new UpLoadPicUtils.UpLoadPicListener() {
            @Override
            public void success(String qiNiuPath) {
                addShopInfo(qiNiuPath);
            }

            @Override
            public void error() {
                LoadingDialog.cancelDialogForLoading();
                showShortToast("图片上传失败，请重试。");
            }
        });
    }

    //提交店铺信息
    private void addShopInfo(String qiNiuPath) {
        mParams.put("sj_login_token", userInfo.getSj_login_token());
        mParams.put("logo", qiNiuPath);//logo
        mParams.put("shop_name", shopNameEdit.getText().toString());//店铺名称
        mParams.put("yewuyuan_id", agencyAccountEdit.getText().toString());//代理账号
        mParams.put("addr", addressEdit.getText().toString());//详细地址
        mParams.put("tel", shopPhoneEdit.getText().toString());//商家电话
        new MyHttp().doPost(Api.getDefault().addShopInfo(mParams), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                JSONObject data = JSONObject.parseObject(result.getString("data"));
                userInfo.setShop_id(data.getIntValue("shop_id"));
//                userInfo.setGrade_id((int) mParams.get("grade_id"));
                showShortToast(result.getString("msg"));
                startActivity(new Intent(mContext, ChooseShopTypeActivity.class));
                BaseApplication.getInstance().saveUserInfo(userInfo);
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    //获取城市及商圈等数据
    private void getCityList() {
        new MyHttp().doPost(Api.getDefault().getJoinSelectAddress(), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                addressInfos = JSONArray.parseArray(result.getString("data"), JoinAddressInfo.class);
                addressBottomDialog = new AddressBottomDialog(InputShopInfoActivity.this);
                addressBottomDialog.addAddressData(addressInfos);
                addressBottomDialog.setOnAddressSelectedListener(InputShopInfoActivity.this);
                addressBottomDialog.show();
            }

            @Override
            public void onError(int code) {

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == AppConfig.TAKE_PHOTO_CODE) {
            if (null == data) {
                return;
            }
            ArrayList<String> pics = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
            mPhotoPath = pics.get(0);
            GlideUtils.displayUri(mContext, storeSignageIm, Uri.fromFile(new File(pics.get(0))));
        }
    }

//    private void getShopCate() {
//        LoadingDialog.showDialogForLoading(mActivity, "获取中...", false);
//        new MyHttp().doPost(Api.getDefault().getShopGrade(), new HttpListener() {
//            @Override
//            public void onSuccess(JSONObject result) {
//                shopGradeInfos = JSONArray.parseArray(result.getString("data"), ShopGradeInfo.class);
//                if (!gradePickerView.isShowing()) {
//                    showGradePickerView();
//                }
//            }
//
//            @Override
//            public void onError(int code) {
//
//            }
//        });
//    }

//    //弹出商家等级/商家分类的pickerview
//    private void showGradePickerView() {
//        gradePickerView.setPicker(shopGradeInfos);//添加数据
//        gradePickerView.show();
//    }

    private void initLocation() {
        //初始化AMapLocationClientOption对象
        AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
        //获取一次定位结果：
        //该方法默认为false。
        mLocationOption.setOnceLocation(true);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //初始化定位
        mLocationClient = new AMapLocationClient(mContext.getApplicationContext());
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        getPersimmions();
    }

    private void getPersimmions() {
        if (Build.VERSION.SDK_INT >= 23) {
            showSetPermission();
        } else {
            mLocationClient.startLocation();
        }
    }

    private void showSetPermission() {
        PermissionsChecker permissionsChecker = new PermissionsChecker(mContext);
        if (permissionsChecker.lacksPermissions(PERMISSIONS_CONTACT)) {
            if (Build.VERSION.SDK_INT >= 23) {
                requestPermissions(PERMISSIONS_CONTACT, REQUEST_CONTACTS);
            }
        } else {
            mLocationClient.startLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CONTACTS:
                mLocationClient.startLocation();
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private boolean canNext() {
        if (TextUtils.isEmpty(mPhotoPath)) {
            showShortToast("请选择店面招牌图");
            return false;
        }
        if (TextUtils.isEmpty(shopNameEdit.getText().toString())) {
            showShortToast("请填写店铺名称");
            return false;
        }
        if (TextUtils.isEmpty(shopPhoneEdit.getText().toString())) {
            showShortToast("请输入商户电话");
            return false;
        }
        if (TextUtils.isEmpty(agencyAccountEdit.getText().toString())) {
            showShortToast("请输入代理账号");
            return false;
        }
//        if (TextUtils.isEmpty(shopShopGradeTv.getText().toString())) {
//            showShortToast("请选择店铺分类");
//            return false;
//        }
        if (TextUtils.isEmpty(shopShopAddressTv.getText().toString())) {
            showShortToast("请选择店铺地址");
            return false;
        }
        if (TextUtils.isEmpty(addressEdit.getText().toString())) {
            showShortToast("请填写店铺详细地址");
            return false;
        }
        if (!hasLocation) {
            showShortToast("请点击获取定位地址");
            return false;
        }
        return true;
    }

    AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            LoadingDialog.cancelDialogForLoading();
            StringBuilder sb = new StringBuilder();
            if (!TextUtils.isEmpty(aMapLocation.getStreet())) {
                sb.append(aMapLocation.getStreet());
            }
            if (!TextUtils.isEmpty(aMapLocation.getStreetNum())) {
                sb.append(aMapLocation.getStreetNum());
            }
            if (!TextUtils.isEmpty(aMapLocation.getPoiName())) {
                sb.append(aMapLocation.getPoiName());
            }
            addressEdit.setText(sb.toString());
            mParams.put("lat", aMapLocation.getLatitude());
            mParams.put("lng", aMapLocation.getLongitude());
            hasLocation = true;
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLocationClient != null) {
            mLocationClient.onDestroy();
            mLocationClient = null;
        }
    }

    @Override
    public void onAddressSelected(Province province, City city, County county, Street street) {
        StringBuffer cityBuf = new StringBuffer();
        selectCity = "";
        if (null != province) {
            cityBuf.append(province.name);
            selectCity += province.name;
        }
        if (null != city) {
            cityBuf.append("-" + city.name);
            mParams.put("city_id", city.id);
            selectCity += city.name;
        }
        if (null != county) {
            cityBuf.append("-" + county.name);
            mParams.put("area_id", county.id);
            selectCity += county.name;
        }
        shopShopAddressTv.setText(cityBuf);
        addressBottomDialog.dismiss();
    }

    class MyPickerSelectListener implements OptionsPickerView.OnOptionsSelectListener {
        int mPickerViewType;

        public MyPickerSelectListener(int pickerView) {
            this.mPickerViewType = pickerView;
        }

        @Override
        public void onOptionsSelect(int options1, int options2, int options3, View v) {
            mParams.put("grade_id", shopGradeInfos.get(options1).getGrade_id());
            shopShopGradeTv.setText(shopGradeInfos.get(options1).getGrade_name());
        }
    }
}
