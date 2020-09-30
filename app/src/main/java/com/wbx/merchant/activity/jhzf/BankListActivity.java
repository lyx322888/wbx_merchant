package com.wbx.merchant.activity.jhzf;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
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
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.bean.BankListBean;
import com.wbx.merchant.common.LoginUtil;
import com.wbx.merchant.widget.LoadingDialog;

import butterknife.Bind;

//银行
public class BankListActivity extends BaseActivity {

    @Bind(R.id.title_name_tv)
    TextView titleNameTv;
    @Bind(R.id.search_edit_text)
    EditText searchEditText;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    String keyword = "";
    String bankCode = "";
    BankAdapter bankAdapter;
    public static  final int REQUESTCODE_YH= 1324;//银行

    public static void actionStart(Activity context,int requestCode){
        Intent intent = new Intent(context, BankListActivity.class);
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

        bankAdapter = new BankAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(bankAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        bankAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    Intent intent = new Intent();
                    intent.putExtra("bankCode",  bankAdapter.getItem(position).getBankCode());
                    intent.putExtra("yhName", bankAdapter.getItem(position).getBankName());
                    setResult(Activity.RESULT_OK, intent);
                    finish();

            }
        });


        initdata();
    }

    private void initdata() {
    }

    @Override
    public void fillData() {
        LoadingDialog.showDialogForLoading(mActivity);
            //银行
            new MyHttp().doPost(Api.getDefault().list_bank(LoginUtil.getLoginToken(), keyword), new HttpListener() {
                @Override
                public void onSuccess(JSONObject result) {
                    BankListBean bankListBean = new Gson().fromJson(result.toString(),BankListBean.class);
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