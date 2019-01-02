package com.wbx.merchant.activity;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.ScrollView;

import com.alibaba.fastjson.JSONObject;
import com.kyleduo.switchbutton.SwitchButton;
import com.wbx.merchant.R;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.widget.LoadingDialog;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

/**
 * Created by wushenghui on 2017/11/29.
 * 活动设置
 */

public class ActivitySettingsActivity extends BaseActivity {
    @Bind(R.id.scroll_view)
    ScrollView mScrollView;

    @Bind({R.id.full_reduction_is_seckill_sb, R.id.full_reduction_is_sales_sb, R.id.coupon_is_seckill_sb, R.id.coupon_is_sales_sb, R.id.coupon_is_full_reduction_sb})
    List<SwitchButton> switchButtonList;

    private HashMap<String, Object> mParams = new HashMap<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_settings_activity;
    }

    @Override
    public void initPresenter() {
        OverScrollDecoratorHelper.setUpOverScroll(mScrollView);
    }

    @Override
    public void initView() {
        mParams.put("sj_login_token", userInfo.getSj_login_token());
    }

    @Override
    public void fillData() {
        LoadingDialog.showDialogForLoading(mActivity, "信息获取中...", true);
        new MyHttp().doPost(Api.getDefault().getActivitySettingsInfo(userInfo.getSj_login_token()), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                JSONObject data = result.getJSONObject("data");
                switchButtonList.get(0).setChecked(data.getIntValue("full_reduction_is_seckill") == 1);//秒杀是否支持满减
                switchButtonList.get(1).setChecked(data.getIntValue("full_reduction_is_sales") == 1);//促销是否支持满减
                switchButtonList.get(2).setChecked(data.getIntValue("coupon_is_seckill") == 1);//秒杀商品是否支持优惠券
                switchButtonList.get(3).setChecked(data.getIntValue("coupon_is_sales") == 1);//促销商品是否支持优惠券
                switchButtonList.get(4).setChecked(data.getIntValue("coupon_full_reduction_all_use") == 1);//优惠券与满减活动是否同时使用
                mParams.put("full_reduction_is_seckill", data.getIntValue("full_reduction_is_seckill"));
                mParams.put("full_reduction_is_sales", data.getIntValue("full_reduction_is_sales"));
                mParams.put("coupon_is_seckill", data.getIntValue("coupon_is_seckill"));
                mParams.put("coupon_is_sales", data.getIntValue("coupon_is_sales"));
                mParams.put("coupon_full_reduction_all_use", data.getIntValue("coupon_full_reduction_all_use"));
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    @Override
    public void setListener() {
        switchButtonList.get(0).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mParams.put("full_reduction_is_seckill", b ? 1 : 0);
            }
        });
        switchButtonList.get(1).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mParams.put("full_reduction_is_sales", b ? 1 : 0);
            }
        });
        switchButtonList.get(2).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mParams.put("coupon_is_seckill", b ? 1 : 0);
            }
        });
        switchButtonList.get(3).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mParams.put("coupon_is_sales", b ? 1 : 0);
            }
        });
        switchButtonList.get(4).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mParams.put("coupon_full_reduction_all_use", b ? 1 : 0);
            }
        });

        findViewById(R.id.settings_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MyHttp().doPost(Api.getDefault().updateActivityInfo(mParams), new HttpListener() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        showShortToast("设置成功");
                        finish();
                    }

                    @Override
                    public void onError(int code) {

                    }
                });
            }
        });
    }
}
