package com.wbx.merchant.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.widget.EditText;

import com.alibaba.fastjson.JSONObject;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.wbx.merchant.R;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.bean.ShopDetailInfo;
import com.wbx.merchant.utils.ToastUitl;
import com.wbx.merchant.widget.PriceEditText;

import butterknife.Bind;
import butterknife.OnClick;

public class SendSettingActivity extends BaseActivity {
    @Bind(R.id.map_view)
    MapView mapView;
    @Bind(R.id.et_start_send_price)
    PriceEditText etStartSendPrice;
    @Bind(R.id.et_send_price)
    PriceEditText etSendPrice;
    @Bind(R.id.et_kilometre)
    EditText etKilometre;
    @Bind(R.id.et_per_send_price)
    PriceEditText etPerSendPrice;
    @Bind(R.id.et_send_range)
    EditText etSendRange;
    private AMap map;
    private ShopDetailInfo shopDetailInfo;

    public static void actionStart(Activity context, ShopDetailInfo shopDetailInfo) {
        Intent intent = new Intent(context, SendSettingActivity.class);
        intent.putExtra("shopDetailInfo", shopDetailInfo);
        context.startActivityForResult(intent, StoreManagerActivity.REQUEST_UPDATE_SEND_SETTING);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_send_setting;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        mapView.onCreate(msavedInstanceState);
        if (map == null) {
            map = mapView.getMap();
        }
        UiSettings uiSettings = map.getUiSettings();
        uiSettings.setZoomControlsEnabled(false);
    }

    @Override
    public void fillData() {
        shopDetailInfo = (ShopDetailInfo) getIntent().getSerializableExtra("shopDetailInfo");
        if (shopDetailInfo == null) {
            ToastUitl.showShort("店铺数据异常");
            finish();
            return;
        }
        if (TextUtils.isEmpty(shopDetailInfo.getLat()) || TextUtils.isEmpty(shopDetailInfo.getLat())) {
            ToastUitl.showShort("店铺定位异常");
        } else {
            LatLng latLng = new LatLng(Double.valueOf(shopDetailInfo.getLat()), Double.valueOf(shopDetailInfo.getLng()));
            CameraUpdate mCameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(latLng, (float) 11.5, 0, 0));
            map.moveCamera(mCameraUpdate);
            map.addCircle(new CircleOptions().center(latLng).radius(6000).fillColor(Color.parseColor("#993388FF")).strokeColor(Color.parseColor("#3388FF")).strokeWidth(10));
            map.addMarker(new MarkerOptions().anchor(0.5f, 0.5f).position(latLng).draggable(false).icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.icon_location))));
        }
        etSendRange.setText(shopDetailInfo.getPeisong_fanwei());
        etStartSendPrice.setText(String.format("%.2f", shopDetailInfo.getSince_money() / 100.00));
        etSendPrice.setText(String.format("%.2f", shopDetailInfo.getLogistics() / 100.00));
        if (shopDetailInfo.getEvery_exceed_kilometre() != 0) {
            etKilometre.setText(String.valueOf(shopDetailInfo.getEvery_exceed_kilometre()));
        }
        if (shopDetailInfo.getExceed_every_kilometre_logistics() != 0) {
            etPerSendPrice.setText(String.format("%.2f", shopDetailInfo.getExceed_every_kilometre_logistics() / 100.00));
        }
    }

    @Override
    public void setListener() {

    }

    @OnClick(R.id.rl_right)
    public void onViewClicked() {
        float startSendPrice = 0;
        if (!TextUtils.isEmpty(etStartSendPrice.getText().toString())) {
            startSendPrice = Float.valueOf(etStartSendPrice.getText().toString());
        }
        float sendPrice = 0;
        if (!TextUtils.isEmpty(etSendPrice.getText().toString())) {
            sendPrice = Float.valueOf(etSendPrice.getText().toString());
        }
        int kilometre = 0;
        if (!TextUtils.isEmpty(etKilometre.getText().toString())) {
            kilometre = Integer.valueOf(etKilometre.getText().toString());
        }
        float perSendPrice = 0;
        if (!TextUtils.isEmpty(etPerSendPrice.getText().toString())) {
            perSendPrice = Float.valueOf(etPerSendPrice.getText().toString());
        }

        final float finalStartSendPrice = startSendPrice;
        final float finalSendPrice = sendPrice;
        final int finalKilometre = kilometre;
        final float finalPerSendPrice = perSendPrice;
        new MyHttp().doPost(Api.getDefault().updateDistanceSendFee(userInfo.getSj_login_token(), etSendRange.getText().toString(), startSendPrice, sendPrice, kilometre, perSendPrice), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                showShortToast(result.getString("msg"));
                shopDetailInfo.setPeisong_fanwei(etSendRange.getText().toString());
                shopDetailInfo.setSince_money((int) (finalStartSendPrice * 100));
                shopDetailInfo.setLogistics((int) (finalSendPrice * 100));
                shopDetailInfo.setEvery_exceed_kilometre(finalKilometre);
                shopDetailInfo.setExceed_every_kilometre_logistics((int) (finalPerSendPrice * 100));
                Intent intent = new Intent();
                intent.putExtra("shopDetailInfo", shopDetailInfo);
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        mapView.onSaveInstanceState(outState);
    }
}
