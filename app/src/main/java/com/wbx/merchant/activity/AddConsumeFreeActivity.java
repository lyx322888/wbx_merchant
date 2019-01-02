package com.wbx.merchant.activity;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bigkoo.pickerview.OptionsPickerView;
import com.wbx.merchant.R;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.bean.CanFreeGoodsBean;
import com.wbx.merchant.bean.ConsumeFreeGoodsBean;
import com.wbx.merchant.common.LoginUtil;
import com.wbx.merchant.utils.GlideUtils;
import com.wbx.merchant.utils.ToastUitl;
import com.wbx.merchant.widget.LoadingDialog;
import com.wbx.merchant.widget.PriceEditText;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class AddConsumeFreeActivity extends BaseActivity {
    public static final int REQUEST_EDIT = 1001;
    @Bind(R.id.iv_goods)
    ImageView ivGoods;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_price)
    TextView tvPrice;
    @Bind(R.id.tv_sold_num)
    TextView tvSoldNum;
    @Bind(R.id.tv_stock)
    TextView tvStock;
    @Bind(R.id.et_activity_stock)
    EditText etActivityStock;
    @Bind(R.id.et_need_num)
    EditText etNeedNum;
    @Bind(R.id.tv_mall_price)
    TextView tvMallPrice;
    @Bind(R.id.et_free_start_price)
    PriceEditText etFreeStartPrice;
    @Bind(R.id.tv_continue_time)
    TextView tvContinueTime;
    private List<String> lstContinueTime = new ArrayList<>();
    private long selectTime = 999999999;
    private ConsumeFreeGoodsBean data;
    private boolean isEdit = false;

    public static void actionStart(Activity context, CanFreeGoodsBean data) {
        Intent intent = new Intent(context, AddConsumeFreeActivity.class);
        intent.putExtra("data", data);
        context.startActivityForResult(intent, SelectFreeActivityGoodsListActivity.REQUEST_ADD);
    }

    public static void actionStart(Activity context, ConsumeFreeGoodsBean data) {
        Intent intent = new Intent(context, AddConsumeFreeActivity.class);
        intent.putExtra("consumeFreeGoods", data);
        intent.putExtra("isEdit", true);
        context.startActivityForResult(intent, REQUEST_EDIT);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_consume_free;
    }

    @Override
    public void initPresenter() {
        isEdit = getIntent().getBooleanExtra("isEdit", false);
        if (isEdit) {
            data = (ConsumeFreeGoodsBean) getIntent().getSerializableExtra("consumeFreeGoods");
        } else {
            CanFreeGoodsBean canFreeGoodsBean = (CanFreeGoodsBean) getIntent().getSerializableExtra("data");
            data = new ConsumeFreeGoodsBean();
            data.setGoods_id(canFreeGoodsBean.getGoods_id());
            data.setTitle(canFreeGoodsBean.getTitle());
            data.setPhoto(canFreeGoodsBean.getPhoto());
            data.setPrice(canFreeGoodsBean.getPrice());
            data.setNum(canFreeGoodsBean.getNum());
            data.setSold_num(canFreeGoodsBean.getSold_num());
        }
        for (int i = 1; i < 25; i++) {
            lstContinueTime.add(i + "小时");
        }
        lstContinueTime.add("不限时");
    }

    @Override
    public void initView() {
        GlideUtils.showMediumPic(this, ivGoods, data.getPhoto());
        tvName.setText(data.getTitle());
        tvPrice.setText(String.format("¥%.2f", data.getPrice() / 100.00));
        tvMallPrice.setText(String.format("¥%.2f", data.getPrice() / 100.00));
        tvSoldNum.setText(String.format("销量%d", data.getSold_num()));
        tvStock.setText(String.format("库存%d", data.getNum()));
        if (isEdit) {
            etActivityStock.setText(String.valueOf(data.getConsume_free_num()));
            etFreeStartPrice.setText(String.format("%.2f", data.getConsume_free_price() / 100.00));
            etNeedNum.setText(String.valueOf(data.getConsume_free_amount()));
            tvContinueTime.setText(data.getConsume_free_duration() == 999999999 ? "不限时" : (data.getConsume_free_duration() + "小时"));
            selectTime = data.getConsume_free_duration();
        }
    }

    @Override
    public void fillData() {

    }

    @Override
    public void setListener() {

    }

    @OnClick({R.id.view_play_rule, R.id.view_continue_time, R.id.tv_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.view_play_rule:
                break;
            case R.id.view_continue_time:
                showTimePicker();
                break;
            case R.id.tv_save:
                save();
                break;
        }
    }

    private void save() {
        if (TextUtils.isEmpty(etActivityStock.getText().toString())) {
            ToastUitl.showShort("请输入活动库存");
            return;
        }
        if (TextUtils.isEmpty(etFreeStartPrice.getText().toString())) {
            ToastUitl.showShort("请输入免单底价");
            return;
        }
        if (TextUtils.isEmpty(etNeedNum.getText().toString())) {
            ToastUitl.showShort("请输入需要人数");
            return;
        }
        final Integer stock = Integer.valueOf(etActivityStock.getText().toString());
        final Float price = Float.valueOf(etFreeStartPrice.getText().toString());
        final Integer needNum = Integer.valueOf(etNeedNum.getText().toString());
        LoadingDialog.showDialogForLoading(this);
        new MyHttp().doPost(Api.getDefault().addConsumeFreeGoods(LoginUtil.getLoginToken(), stock,
                needNum, price, selectTime, data.getGoods_id()), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                ToastUitl.showShort("保存成功");
                data.setConsume_free_num(stock);
                data.setConsume_free_price((int) (price * 100));
                data.setConsume_free_amount(needNum);
                data.setConsume_free_duration(selectTime);
                Intent intent = new Intent();
                intent.putExtra("data", data);
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    private void showTimePicker() {
        OptionsPickerView pickerView = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                if (options1 != lstContinueTime.size() - 1) {
                    selectTime = options1 + 1;
                    tvContinueTime.setText((options1 + 1) + "小时");
                } else {
                    //不限时
                    tvContinueTime.setText("不限时");
                    selectTime = 999999999;
                }
            }
        }).build();
        pickerView.setPicker(lstContinueTime);
        pickerView.show(true);
    }
}
