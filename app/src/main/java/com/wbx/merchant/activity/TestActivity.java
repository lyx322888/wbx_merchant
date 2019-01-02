package com.wbx.merchant.activity;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.PopupWindow;

import com.wbx.merchant.R;
import com.wbx.merchant.base.BaseActivity;

/**
 * Created by wushenghui on 2017/7/4.
 */

public class TestActivity extends BaseActivity {
   PopupWindow mPopWindow;

    @Override
    public int getLayoutId() {
        return R.layout.activity_test;
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
       findViewById(R.id.start_pop_btn).setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:18206062707"));
               startActivity(intent);
           }
       });
    }


}
