package com.wbx.merchant.activity;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.wbx.merchant.R;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.bean.CanFreeGoodsBean;
import com.wbx.merchant.bean.IntegralFreeGoodsBean;
import com.wbx.merchant.common.LoginUtil;
import com.wbx.merchant.utils.GlideUtils;
import com.wbx.merchant.utils.ToastUitl;
import com.wbx.merchant.widget.LoadingDialog;

import butterknife.Bind;
import butterknife.OnClick;

public class AddIntegralFreeActivity extends BaseActivity {
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
    @Bind(R.id.et_consume_num)
    EditText etConsumeNum;
    @Bind(R.id.et_free_num)
    EditText etFreeNum;
    private IntegralFreeGoodsBean data;
    private boolean isEdit = false;

    public static void actionStart(Activity context, CanFreeGoodsBean data) {
        Intent intent = new Intent(context, AddIntegralFreeActivity.class);
        intent.putExtra("data", data);
        context.startActivityForResult(intent, SelectFreeActivityGoodsListActivity.REQUEST_ADD);
    }

    public static void actionStart(Activity context, IntegralFreeGoodsBean data) {
        Intent intent = new Intent(context, AddIntegralFreeActivity.class);
        intent.putExtra("integralFreeGoods", data);
        intent.putExtra("isEdit", true);
        context.startActivityForResult(intent, REQUEST_EDIT);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_integral_free;
    }

    @Override
    public void initPresenter() {
        isEdit = getIntent().getBooleanExtra("isEdit", false);
        if (isEdit) {
            data = (IntegralFreeGoodsBean) getIntent().getSerializableExtra("integralFreeGoods");
        } else {
            CanFreeGoodsBean canFreeGoodsBean = (CanFreeGoodsBean) getIntent().getSerializableExtra("data");
            data = new IntegralFreeGoodsBean();
            data.setGoods_id(canFreeGoodsBean.getGoods_id());
            data.setTitle(canFreeGoodsBean.getTitle());
            data.setPhoto(canFreeGoodsBean.getPhoto());
            data.setPrice(canFreeGoodsBean.getPrice());
            data.setNum(canFreeGoodsBean.getNum());
            data.setSold_num(canFreeGoodsBean.getSold_num());
        }
    }

    @Override
    public void initView() {
        GlideUtils.showMediumPic(this, ivGoods, data.getPhoto());
        tvName.setText(data.getTitle());
        tvPrice.setText(String.format("¥%.2f", data.getPrice() / 100.00));
        tvSoldNum.setText(String.format("销量%d", data.getSold_num()));
        tvStock.setText(String.format("库存%d", data.getNum()));
        if (isEdit) {
            etConsumeNum.setText(String.valueOf(data.getAccumulate_free_need_num()));
            etFreeNum.setText(String.valueOf(data.getAccumulate_free_get_num()));
        }
    }

    @Override
    public void fillData() {

    }

    @Override
    public void setListener() {

    }

    @OnClick({R.id.view_play_rule, R.id.tv_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.view_play_rule:
                break;
            case R.id.tv_save:
                save();
                break;
        }
    }

    private void save() {
        if (TextUtils.isEmpty(etConsumeNum.getText().toString())) {
            ToastUitl.showShort("请输入消费数量");
            return;
        }
        if (TextUtils.isEmpty(etFreeNum.getText().toString())) {
            ToastUitl.showShort("请输入免费数量");
            return;
        }
        final Integer consumeNum = Integer.valueOf(etConsumeNum.getText().toString());
        final Integer freeNum = Integer.valueOf(etFreeNum.getText().toString());
        if (consumeNum < 1 || freeNum < 1) {
            ToastUitl.showShort("请输入正确的数量");
            return;
        }
        LoadingDialog.showDialogForLoading(this);
        new MyHttp().doPost(Api.getDefault().addIntegralFreeGoods(LoginUtil.getLoginToken(), consumeNum,
                freeNum, data.getGoods_id()), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                ToastUitl.showShort("保存成功");
                data.setAccumulate_free_need_num(consumeNum);
                data.setAccumulate_free_get_num(freeNum);
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
}