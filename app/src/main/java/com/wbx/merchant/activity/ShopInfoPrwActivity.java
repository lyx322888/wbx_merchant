package com.wbx.merchant.activity;


import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.bigkoo.pickerview.OptionsPickerView;
import com.google.gson.Gson;
import com.wbx.merchant.MainActivity;
import com.wbx.merchant.R;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.base.BaseApplication;
import com.wbx.merchant.baseapp.AppConfig;
import com.wbx.merchant.baseapp.AppManager;
import com.wbx.merchant.bean.JoinAddressInfo;
import com.wbx.merchant.bean.ShopGradeBean;
import com.wbx.merchant.common.LoginUtil;
import com.wbx.merchant.utils.FormatUtil;
import com.wbx.merchant.utils.PermissionsChecker;
import com.wbx.merchant.utils.SPUtils;
import com.wbx.merchant.widget.AddressBottomDialog;
import com.wbx.merchant.widget.LoadingDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import chihane.jdaddressselector.OnAddressSelectedListener;
import chihane.jdaddressselector.model.City;
import chihane.jdaddressselector.model.County;
import chihane.jdaddressselector.model.Province;
import chihane.jdaddressselector.model.Street;

public class ShopInfoPrwActivity extends BaseActivity implements OnAddressSelectedListener {

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.shop_name_edit)
    EditText shopNameEdit;
    @Bind(R.id.show_shop_address_tv)
    TextView showShopAddressTv;
    @Bind(R.id.choose_shop_address_layout)
    LinearLayout chooseShopAddressLayout;
    @Bind(R.id.address_edit)
    EditText addressEdit;
    @Bind(R.id.get_location_btn)
    ImageView getLocationBtn;
    @Bind(R.id.tv_shop_type)
    TextView tvShopType;
    @Bind(R.id.choose_shop_type)
    LinearLayout chooseShopType;
    @Bind(R.id.shop_info_next_btn)
    Button shopInfoNextBtn;
    public AMapLocationClient mLocationClient = null;
    @Bind(R.id.iv_r)
    ImageView ivR;
    @Bind(R.id.agency_account_edit)
    EditText agencyAccountEdit;
    @Bind(R.id.tv_grade_type)
    TextView tvGradeType;
    private List<JoinAddressInfo> addressInfos;

    private static final String[] PERMISSIONS_CONTACT = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION};
    private static final int REQUEST_CONTACTS = 1000;
    private boolean hasLocation = false;
    private String selectCity = "";
    private String cityName = "";
    private int shopEdition = 2;//1个人版 2实体店
    private int cityId;
    private int countyId;
    private String grade_id = "";

    private AddressBottomDialog addressBottomDialog;
    private OptionsPickerView pvOptions;
    private OptionsPickerView pvOptionsHylx;


    @Override
    public int getLayoutId() {
        FormatUtil.setStatubarColor(mActivity, R.color.app_color);
        return R.layout.activity_shop_info_prw;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        initPopCommunity();

    }

    @Override
    public void fillData() {
        getHylx();
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

    private double lat;
    private double lng;
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
            lat = aMapLocation.getLatitude();
            lng = aMapLocation.getLongitude();
            hasLocation = true;
        }
    };


    private boolean canNext() {

        if (TextUtils.isEmpty(shopNameEdit.getText().toString())) {
            showShortToast("请填写店铺名称");
            return false;
        }

        if (TextUtils.isEmpty(showShopAddressTv.getText().toString())) {
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

        if (TextUtils.isEmpty(grade_id)) {
            showShortToast("请选择行业类型");
            return false;
        }

        return true;
    }

    @OnClick({R.id.choose_grade_type, R.id.choose_shop_address_layout, R.id.get_location_btn, R.id.choose_shop_type, R.id.shop_info_next_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.choose_shop_address_layout:
                //选择城市
                //获取城市及商圈等数据
                if (addressBottomDialog == null) {
                    LoadingDialog.showDialogForLoading(mActivity, "加载中...", true);
                    getCityList();
                } else {
                    addressBottomDialog.show();
                }
                break;
            case R.id.get_location_btn:
                //点位
                initLocation();
                break;
            case R.id.choose_shop_type:
                //店铺类型
                if (!BaseApplication.isxqwdb) {
                    pvOptions.show();
                }
                break;
            case R.id.choose_grade_type:
                //行业类型
                if (pvOptionsHylx!=null){
                 pvOptionsHylx.show();
                }else {
                    getHylx();
                }
                break;
            case R.id.shop_info_next_btn:
                //下一步
                if (canNext()) {
                    addShopInfo();
                }
                break;
        }
    }


    private void initPopCommunity() {
        //数据源
        final List<String> strings = new ArrayList<>();
        strings.add("个人版");
        strings.add("实体店版");
        strings.add("餐饮店版");
        pvOptions = new OptionsPickerView(new OptionsPickerView.Builder(mContext, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                shopEdition = options1 + 1;
                tvShopType.setText(strings.get(options1));
            }
        }).setSubmitText("确定")//确定按钮文字
                .setCancelText("取消")//取消按钮文字
                .setSubCalSize(18)//确定和取消文字大小
                .setTitleSize(20)//标题文字大小
                .setTitleColor(ContextCompat.getColor(mContext, R.color.black))//标题文字颜色
                .setSubmitColor(ContextCompat.getColor(mContext, R.color.app_color))//确定按钮文字颜色
                .setCancelColor(ContextCompat.getColor(mContext, R.color.black))//取消按钮文字颜色
//                .setTitleBgColor(0xFF333333)//标题背景颜色 Night mode
//                .setBgColor(0xFF000000)//滚轮背景颜色 Night mode
                .setContentTextSize(18)//滚轮文字大小
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setCyclic(false, false, false)//循环与否
                .setSelectOptions(1)  //设置默认选中项
                .setOutSideCancelable(true)//点击外部dismiss default true
        );
        pvOptions.setPicker(strings);//添加数据源


    }

    private void initHylx(final List<ShopGradeBean.DataBean> data) {
        //数据源
        final List<String> strings = new ArrayList<>();

        for (ShopGradeBean.DataBean bean : data) {
            strings.add(bean.getGrade_name());

        }
        pvOptionsHylx = new OptionsPickerView(new OptionsPickerView.Builder(mContext, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                grade_id = data.get(options1).getGrade_id();
                tvGradeType.setText(strings.get(options1));
            }
        }).setSubmitText("确定")//确定按钮文字
                .setCancelText("取消")//取消按钮文字
                .setSubCalSize(18)//确定和取消文字大小
                .setTitleSize(20)//标题文字大小
                .setTitleColor(ContextCompat.getColor(mContext, R.color.black))//标题文字颜色
                .setSubmitColor(ContextCompat.getColor(mContext, R.color.app_color))//确定按钮文字颜色
                .setCancelColor(ContextCompat.getColor(mContext, R.color.black))//取消按钮文字颜色
//                .setTitleBgColor(0xFF333333)//标题背景颜色 Night mode
//                .setBgColor(0xFF000000)//滚轮背景颜色 Night mode
                .setContentTextSize(18)//滚轮文字大小
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setCyclic(false, false, false)//循环与否
                .setSelectOptions(1)  //设置默认选中项
                .setOutSideCancelable(true)//点击外部dismiss default true
        );
        pvOptionsHylx.setPicker(strings);//添加数据源


    }

    //获取城市及商圈等数据
    private void getCityList() {
        new MyHttp().doPost(Api.getDefault().getJoinSelectAddress(), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                addressInfos = JSONArray.parseArray(result.getString("data"), JoinAddressInfo.class);
                addressBottomDialog = new AddressBottomDialog(ShopInfoPrwActivity.this);
                addressBottomDialog.addAddressData(addressInfos);
                addressBottomDialog.setOnAddressSelectedListener(ShopInfoPrwActivity.this);
                addressBottomDialog.show();
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    private void getHylx() {
        new MyHttp().doPost(Api.getDefault().get_shop_grade(LoginUtil.getLoginToken()), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                ShopGradeBean bean = new Gson().fromJson(result.toString(), ShopGradeBean.class);
                initHylx(bean.getData());
            }

            @Override
            public void onError(int code) {

            }
        });
    }


    //提交店铺信息
    private void addShopInfo() {
        HashMap<String, Object> mParams;
        mParams = new HashMap<>();
        mParams.put("sj_login_token", userInfo.getSj_login_token());
//        mParams.put("logo", qiNiuPath);//logo
        mParams.put("shop_name", shopNameEdit.getText().toString());//店铺名称
        mParams.put("addr", addressEdit.getText().toString());//详细地址
        mParams.put("lat", lat);
        mParams.put("city_id", cityId);
        if (!TextUtils.isEmpty(agencyAccountEdit.getText().toString())) {
            mParams.put("yewuyuan_id", agencyAccountEdit.getText().toString());//代理账号
        }
        mParams.put("area_id", countyId);
        mParams.put("grade_id", grade_id);
//        mParams.put("is_douyin", );
        mParams.put("shop_edition", shopEdition);
        mParams.put("lng", lng);
        new MyHttp().doPost(Api.getDefault().add_apply_info(mParams), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                JSONObject data = JSONObject.parseObject(result.getString("data"));
                userInfo.setShop_id(data.getIntValue("shop_id"));
                userInfo.setGrade_id(data.getIntValue("grade_id"));
                showShortToast(result.getString("msg"));
                BaseApplication.getInstance().saveUserInfo(userInfo);
                if (1 == userInfo.getTry_shop()) {
                    //判断是否付过费 = 1没有付费
                    startActivity(new Intent(mContext, ChooseShopVersionsPrwActivity.class));
                } else {
                    SPUtils.setSharedBooleanData(mContext, AppConfig.LOGIN_STATE, true);
                    AppManager.getAppManager().finishAllActivity();
                    startActivity(new Intent(mContext, MainActivity.class));
                }
                finish();
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    @Override
    public void onAddressSelected(Province province, City city, County county, Street street) {
        StringBuffer cityBuf = new StringBuffer();
        selectCity = "";
        cityName = "";
        if (null != province) {
            cityBuf.append(province.name);
            selectCity += province.name;
        }
        if (null != city) {
            cityBuf.append("-" + city.name);
            cityId = city.id;
            selectCity += city.name;
            cityName = city.name;
        }
        if (null != county) {
            cityBuf.append("-" + county.name);
            countyId = county.id;
            selectCity += county.name;
        }
        showShopAddressTv.setText(cityBuf);
        addressBottomDialog.dismiss();
    }

}
