package com.wbx.merchant.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.wbx.merchant.R;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.bean.FullDataInfo;
import com.wbx.merchant.widget.PriceEditText;

import butterknife.Bind;

/**
 * Created by Administrator on 2017/10/27 0027.
 */

public class AddFullActivity extends BaseActivity {
    private FullDataInfo fullDataInfo;
    @Bind(R.id.full_money_edit)
    PriceEditText fullMoneyEdit;
    @Bind(R.id.reduce_money_edit)
    PriceEditText reduceMoneyEdit;

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_full;
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
        findViewById(R.id.complete_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String money = fullMoneyEdit.getText().toString();
                String reduce = reduceMoneyEdit.getText().toString();
                if (TextUtils.isEmpty(money)) {
                    showShortToast("请输入消费金额");
                    return;
                }
                if (TextUtils.isEmpty(reduce)) {
                    showShortToast("请输入免减金额");
                    return;
                }
                if (Float.valueOf(reduce) >= Float.valueOf(money)) {
                    showShortToast("减免金额需小于消费金额");
                    return;
                }
                fullDataInfo = new FullDataInfo();
                fullDataInfo.setFull_money((int) (Float.valueOf(money) * 100));
                fullDataInfo.setReduce_money((int) (Double.valueOf(reduce) * 100));
                Intent intent = new Intent();
                intent.putExtra("fullData", fullDataInfo);//通过意图传值回到上个界面
                setResult(10045, intent);//发送意图
                finish();//关闭当前界面
            }
        });
    }
}
