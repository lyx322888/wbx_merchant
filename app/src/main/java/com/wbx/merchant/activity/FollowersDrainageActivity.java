package com.wbx.merchant.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.flyco.roundview.RoundTextView;
import com.google.gson.Gson;
import com.wbx.merchant.R;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.bean.FollowersDrainageBean;
import com.wbx.merchant.common.LoginUtil;
import com.wbx.merchant.dialog.SelectXqahDialog;
import com.wbx.merchant.widget.LoadingDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

//圈粉爱好
public class FollowersDrainageActivity extends BaseActivity {

    @Bind(R.id.title_name_tv)
    TextView titleNameTv;
    @Bind(R.id.rg_xb)
    RadioGroup rgXb;
    @Bind(R.id.rg_tffw)
    RadioGroup rgTffw;
    @Bind(R.id.tv_city)
    TextView tvCity;
    @Bind(R.id.et_gls)
    EditText etGls;
    @Bind(R.id.rg_nl)
    RadioGroup rgNl;
    @Bind(R.id.ll_xqah)
    LinearLayout llXqah;
    @Bind(R.id.tv_submit)
    RoundTextView tvSubmit;
    @Bind(R.id.rb_xb_bx)
    RadioButton rbXbBx;
    @Bind(R.id.rb_xb_nan)
    RadioButton rbXbNan;
    @Bind(R.id.rb_xb_nv)
    RadioButton rbXbNv;
    @Bind(R.id.rb_tffw_city)
    RadioButton rbTffwCity;
    @Bind(R.id.rb_tffw_fj)
    RadioButton rbTffwFj;
    @Bind(R.id.rb_nl_bx)
    RadioButton rbNlBx;
    @Bind(R.id.rb_nl_00)
    RadioButton rbNl00;
    @Bind(R.id.rb_nl_90)
    RadioButton rbNl90;
    @Bind(R.id.rb_nl_80)
    RadioButton rbNl80;
    private SelectXqahDialog dialog;
    int sex = 0;
    int scope_type = 1;
    int age_group_type = 0;
    List<FollowersDrainageBean.DataBean.HobbyBean> list = new ArrayList<>();
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, FollowersDrainageActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_followers_drainage;
    }

    @Override
    public void initPresenter() {
        rgXb.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId){
                case R.id.rb_xb_bx:
                    sex = 0;
                    break;
                case R.id.rb_xb_nan:
                    sex = 1;
                    break;
                case R.id.rb_xb_nv:
                    sex = 2;
                    break;
            }
        });

        rgTffw.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId){
                case R.id.rb_tffw_city:
                    scope_type = 1;
                    break;
                case R.id.rb_tffw_fj:
                    scope_type = 2;
                    break;
            }
        });

        rgNl.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId){
                case R.id.rb_nl_bx:
                    age_group_type = 0;
                    break;
                case R.id.rb_nl_00:
                    age_group_type = 1;
                    break;
                case R.id.rb_nl_90:
                    age_group_type = 2;
                    break;
                case R.id.rb_nl_80:
                    age_group_type = 3;
                    break;
            }
        });
    }

    @Override
    public void initView() {
        dialog = new SelectXqahDialog(mContext);
        dialog.setDialogListener(arr -> {
            list = arr;
        });

    }

    @Override
    public void fillData() {
        new MyHttp().doPost(Api.getDefault().get_draw_fans_info(LoginUtil.getLoginToken()), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                FollowersDrainageBean bean = new Gson().fromJson(result.toString(), FollowersDrainageBean.class);
                setData(bean.getData());
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    //shuju
    private void setData(FollowersDrainageBean.DataBean data) {
        tvCity.setText(data.getCity_name());
        etGls.setText(data.getDistance());
        switch (data.getSex()){
            case 0:
                rbXbBx.setChecked(true);
                break;
            case 1:
                rbXbNan.setChecked(true);
                break;
            case 2:
                rbXbNv.setChecked(true);
                break;
        }

        //年龄
        switch (data.getAge_group_type()){
            case 0:
                rbNlBx.setChecked(true);
                break;
            case 1:
                rbNl00.setChecked(true);
                break;
            case 2:
                rbNl90.setChecked(true);
                break;
            case 3:
                rbNl80.setChecked(true);
                break;
        }

        switch (data.getScope_type()){
            case 1:
                rbTffwCity.setChecked(true);
                break;
            case 2:
                rbTffwFj.setChecked(true);
                break;

        }

        list = data.getHobby();
    }

    @Override
    public void setListener() {

    }


    @OnClick({R.id.ll_xqah, R.id.tv_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_xqah:
                //兴趣爱好
                if (dialog != null)
                dialog.show();
                dialog.setList(list);
                break;
            case R.id.tv_submit:
                //确定
                if (scope_type==2&& TextUtils.isEmpty(etGls.getText())){
                    showShortToast("请填写距店铺公里数");
                    return;
                }
                submit();
                break;
        }
    }

    private void submit() {
        LoadingDialog.showDialogForLoading(mActivity);
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("sj_login_token",LoginUtil.getLoginToken());
        hashMap.put("sex",sex);
        hashMap.put("scope_type",scope_type);
        hashMap.put("age_group_type",age_group_type);
        hashMap.put("distance",etGls.getText());
        hashMap.put("user_hobby_ids",getUserHobbyIds());
        new MyHttp().doPost(Api.getDefault().update_discover_draw_fans_settings(hashMap), new HttpListener() {
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

    private String getUserHobbyIds(){
        StringBuilder stringBuilder = new StringBuilder();
        for( FollowersDrainageBean.DataBean.HobbyBean hobbyBean:list){
            if (hobbyBean.getIs_select()==1){
                stringBuilder.append(hobbyBean.getHobby_id()).append(",");
            }
        }
        if (stringBuilder.length()>0){
            stringBuilder.deleteCharAt(stringBuilder.length()-1);
        }
        return stringBuilder.toString();
    }



}