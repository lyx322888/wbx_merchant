package com.wbx.merchant.activity;

import android.content.Intent;
import android.widget.EditText;

import com.alibaba.fastjson.JSONObject;
import com.wbx.merchant.R;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.baseapp.AppManager;
import com.wbx.merchant.utils.MD5;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by wushenghui on 2017/6/21.
 * 修改密码
 */

public class ModifyPswActivity extends BaseActivity {
    @Bind(R.id.reset_psw_old_edit)
    EditText mOldPswEdit;
    @Bind(R.id.reset_psw_new_edit)
    EditText mNewPswEdit;
    @Bind(R.id.reset_psw_affirm_edit)
    EditText mAffirmEdit;

    @Override
    public int getLayoutId() {
        return R.layout.activity_modify_psw;
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

    @OnClick(R.id.reset_psw_submit_btn)
    public void submit() {
        String oldPsw = mOldPswEdit.getText().toString();
        String newPsw = mNewPswEdit.getText().toString();
        String newPswAgain = mAffirmEdit.getText().toString();
        if (!newPsw.equals(newPswAgain)) {
            showShortToast("两次输入的密码不同，请重新输入");
            return;
        }
        String pswMd5 = "";
        String newMd5 = "";
        try {
            pswMd5 = new MD5().EncoderByMd5(oldPsw);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            newMd5 = new MD5().EncoderByMd5(newPsw);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        new MyHttp().doPost(Api.getDefault().modifyPsw(userInfo.getSj_login_token(), pswMd5, newMd5), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                showShortToast(result.getString("msg"));
                AppManager.getAppManager().finishAllActivity();
                startActivity(new Intent(mContext, LoginActivity.class));
            }

            @Override
            public void onError(int code) {

            }
        });

    }
}
