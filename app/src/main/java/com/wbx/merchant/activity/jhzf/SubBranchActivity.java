package com.wbx.merchant.activity.jhzf;

import android.app.Activity;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.wbx.merchant.R;
import com.wbx.merchant.adapter.BankAdapter;
import com.wbx.merchant.adapter.BankZHAdapter;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.bean.BanKZhbean;
import com.wbx.merchant.bean.BankListBean;
import com.wbx.merchant.common.LoginUtil;
import com.wbx.merchant.widget.LoadingDialog;

import butterknife.Bind;

//支行
public class SubBranchActivity extends BaseActivity {

    @Bind(R.id.title_name_tv)
    TextView titleNameTv;
    @Bind(R.id.search_edit_text)
    EditText searchEditText;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    String keyword = "支行";
    String bankCode = "";
    BankZHAdapter bankAdapter;
    public static  final int REQUESTCODE_ZH= 1325;//支行

    public static void actionStart(Activity context,int requestCode,String bankCode){
        Intent intent = new Intent(context, SubBranchActivity.class);
        intent.putExtra("bankCode",bankCode);
        context.startActivityForResult(intent,requestCode);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_bank_list;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    keyword =textView.getText().toString();
                    fillData();
                }
                return false;
            }
        });

        bankAdapter = new BankZHAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(bankAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        bankAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    Intent intent = new Intent();
                    intent.putExtra("zhName", bankAdapter.getItem(position));
                    setResult(Activity.RESULT_OK, intent);
                    finish();
            }
        });


        initdata();
    }

    private void initdata() {
        bankCode = getIntent().getStringExtra("bankCode");
    }

    @Override
    public void fillData() {
        LoadingDialog.showDialogForLoading(mActivity);
            //银行
            new MyHttp().doPost(Api.getDefault().list_bankSub(LoginUtil.getLoginToken(), keyword,bankCode), new HttpListener() {
                @Override
                public void onSuccess(JSONObject result) {
                    BanKZhbean bankListBean = new Gson().fromJson(result.toString(),BanKZhbean.class);
                    bankAdapter.setNewData(bankListBean.getData());
                }

                @Override
                public void onError(int code) {

                }
            });
    }

    @Override
    public void setListener() {

    }

}