package com.wbx.merchant.activity;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.wbx.merchant.R;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.bean.GoodsInfo;
import com.wbx.merchant.widget.LoadingDialog;

import butterknife.Bind;
import butterknife.OnClick;

public class EditNatureActivity extends BaseActivity {
    public static final int REQUEST_ADD = 1000;
    public static final int REQUEST_EDIT = 1001;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.et_name)
    EditText etName;
    private GoodsInfo.Nature spec;

    public static void actionStart(Activity activity) {
        actionStart(activity, null);
    }

    public static void actionStart(Activity activity, GoodsInfo.Nature spec) {
        Intent intent = new Intent(activity, EditNatureActivity.class);
        intent.putExtra("spec", spec);
        if (spec == null) {
            activity.startActivityForResult(intent, REQUEST_ADD);
        } else {
            activity.startActivityForResult(intent, REQUEST_EDIT);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_edit_nature;
    }

    @Override
    public void initPresenter() {
        spec = (GoodsInfo.Nature) getIntent().getSerializableExtra("spec");
    }

    @Override
    public void initView() {
        if (spec == null) {
            tvTitle.setText("新增规格项目");
        } else {
            tvTitle.setText("编辑规格项目");
            etName.setText(spec.getItem_name());
            etName.setSelection(spec.getItem_name().length());
        }
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
            Toast.makeText(mContext, "请输入规格项目名称", Toast.LENGTH_SHORT).show();
            return;
        }
        LoadingDialog.showDialogForLoading(this);
        if (spec == null) {
            new MyHttp().doPost(Api.getDefault().addNature(userInfo.getSj_login_token(), 1, name, null), new HttpListener() {
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
        } else {
            new MyHttp().doPost(Api.getDefault().updateNature(userInfo.getSj_login_token(), name, spec.getItem_id()), new HttpListener() {
                @Override
                public void onSuccess(JSONObject result) {
                    spec.setItem_name(name);
                    Intent intent = new Intent();
                    intent.putExtra("spec", spec);
                    setResult(RESULT_OK, intent);
                    finish();
                }

                @Override
                public void onError(int code) {

                }
            });
        }
    }
}
