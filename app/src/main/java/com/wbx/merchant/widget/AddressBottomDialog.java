package com.wbx.merchant.widget;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import com.wbx.merchant.bean.JoinAddressInfo;
import java.util.ArrayList;
import java.util.List;
import chihane.jdaddressselector.AddressProvider;
import chihane.jdaddressselector.AddressSelector;
import chihane.jdaddressselector.OnAddressSelectedListener;
import chihane.jdaddressselector.model.City;
import chihane.jdaddressselector.model.County;
import chihane.jdaddressselector.model.Province;
import chihane.jdaddressselector.model.Street;
import mlxy.utils.Dev;

public class AddressBottomDialog extends Dialog {
    private AddressSelector selector;
    private List<JoinAddressInfo> mDataList = new ArrayList<>();

    public AddressBottomDialog(Context context) {
        super(context, chihane.jdaddressselector.R.style.bottom_dialog);
        init(context);
    }

    public AddressBottomDialog(Context context, int themeResId) {
        super(context, themeResId);
        init(context);
    }

    public AddressBottomDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init(context);
    }

    private void init(Context context) {
        selector = new AddressSelector(context);
        setContentView(selector.getView());
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = Dev.dp2px(context, 256);
        window.setAttributes(params);
        window.setGravity(Gravity.BOTTOM);
    }

    public void setOnAddressSelectedListener(OnAddressSelectedListener listener) {
        this.selector.setOnAddressSelectedListener(listener);
    }

    public void addAddressData(List<JoinAddressInfo> addressInfos){
        this.mDataList = addressInfos;
        selector.setAddressProvider(new AddressProvider() {
            @Override
            public void provideProvinces(AddressReceiver<Province> addressReceiver) {
                List<Province> provinces = new ArrayList<Province>();
                for(JoinAddressInfo addressInfo : mDataList){
                    Province province = new Province();
                    province.id = addressInfo.getProvince_id();
                    province.name = addressInfo.getName();
                    provinces.add(province);
                }
                addressReceiver.send(provinces);
            }
            @Override
            public void provideCitiesWith(int i, AddressReceiver<City> addressReceiver) {
                List<City> cityList = new ArrayList<City>();
                for(JoinAddressInfo addressInfo : mDataList){
                 if(addressInfo.getProvince_id()==i){
                         for(JoinAddressInfo.City cityInfo : addressInfo.getCity()){
                             City city = new City();
                             city.name = cityInfo.getName();
                             city.province_id = i;
                             city.id = cityInfo.getCity_id();
                             cityList.add(city);
                     }
                 }
                }
                addressReceiver.send(cityList);
            }
            @Override
            public void provideCountiesWith(int i, AddressReceiver<County> addressReceiver) {
                List<County> countyList = new ArrayList<County>();
                  for(JoinAddressInfo addressInfo : mDataList){
                      for(JoinAddressInfo.City city : addressInfo.getCity()){
                          if(city.getCity_id()==i){
                                  for(JoinAddressInfo.Area area : city.getArea()){
                                      County county = new County();
                                      county.name = area.getArea_name();
                                      county.id = area.getArea_id();
                                      county.city_id = i;
                                      countyList.add(county);
                              }
                          }
                      }
                  }
                addressReceiver.send(countyList);
            }
            @Override
            public void provideStreetsWith(int i, AddressReceiver<Street> addressReceiver) {
                addressReceiver.send(null);
            }
        });
    }
    public static AddressBottomDialog show(Context context) {
        return show(context, null);
    }
    public static AddressBottomDialog show(Context context, OnAddressSelectedListener listener) {
        AddressBottomDialog dialog = new AddressBottomDialog(context, chihane.jdaddressselector.R.style.bottom_dialog);
        dialog.selector.setOnAddressSelectedListener(listener);
        dialog.show();

        return dialog;
    }
}
