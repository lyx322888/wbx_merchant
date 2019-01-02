package com.wbx.merchant.activity;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.wbx.merchant.R;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.widget.LoadingDialog;

import butterknife.Bind;
import butterknife.OnClick;

public class AddNatureAttrActivity extends BaseActivity {
    public static final int REQUEST_ADD = 1000;
    @Bind(R.id.et_name)
    EditText etName;
    private String itemId;

    public static void actionStart(Activity activity, String itemId) {
        Intent intent = new Intent(activity, AddNatureAttrActivity.class);
        intent.putExtra("itemId", itemId);
        activity.startActivityForResult(intent, REQUEST_ADD);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_nature_attr;
    }

    @Override
    public void initPresenter() {
        itemId = getIntent().getStringExtra("itemId");
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

    @OnClick(R.id.rl_right)
    public void onViewClicked() {
        final String name = this.etName.getText().toString();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(mContext, "请输入规格属性名称", Toast.LENGTH_SHORT).show();
            return;
        }
        LoadingDialog.showDialogForLoading(this);
        new MyHttp().doPost(Api.getDefault().addNature(userInfo.getSj_login_token(), 2, name, itemId), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                Toast.makeText(mContext, result.getString("msg"), Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onError(int code) {

            }
        });
    }
}
