package com.wbx.merchant.activity;

import android.content.Intent;
import android.graphics.Paint;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.wbx.merchant.R;

import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.bean.GoodsDetailsInfo;
import com.wbx.merchant.bean.OrderBean;
import com.wbx.merchant.common.LoginUtil;
import com.wbx.merchant.presenter.GoodsDetailsPresenterImp;
import com.wbx.merchant.utils.GlideUtils;
import com.wbx.merchant.utils.RetrofitUtils;
import com.wbx.merchant.utils.ToastUitl;
import com.wbx.merchant.view.GoodsDetailsView;


import org.jaaksi.pickerview.picker.MixedTimePicker;

import butterknife.Bind;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class GoodsDetailsActivity extends BaseActivity implements GoodsDetailsView {
    @Bind(R.id.goods_photo)
    ImageView ivGoods;
    @Bind(R.id.goods_name)
    TextView tvName;
    @Bind(R.id.goods_ps)
    TextView tvPs;
    @Bind(R.id.goods_price)
    TextView tvPrice;
    @Bind(R.id.goods_original)
    TextView tvOriginal;
    @Bind(R.id.goods_volume)
    TextView tvVolume;
    @Bind(R.id.goods_shop_num)
    TextView tvShopNum;
    @Bind(R.id.button_buy)
    TextView tvBuy;
    private int goods_id;

    @Override
    public int getLayoutId() {
        return R.layout.activity_goods_details;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        goods_id = getIntent().getIntExtra("details_goods_id", 1);
        GoodsDetailsPresenterImp presenterImp = new GoodsDetailsPresenterImp(this);
        presenterImp.getGoodsDetails(LoginUtil.getLoginToken(), goods_id);
    }

    @Override
    public void fillData() {

    }

    @Override
    public void setListener() {

    }

    @Override
    public void getGoodsDetails(final GoodsDetailsInfo goodInfo) {
        GlideUtils.showRoundBigPic(mContext, ivGoods, goodInfo.getData().getPhoto());
        tvName.setText(goodInfo.getData().getTitle());
        tvPs.setText(goodInfo.getData().getSubhead());
        tvPrice.setText("¥" + goodInfo.getData().getPrice() / 100 + "");
        tvOriginal.setText("¥" + goodInfo.getData().getOriginal_price() / 100 + "");
        tvOriginal.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        tvVolume.setText("销量 " + goodInfo.getData().getSales_volume() + "");
        tvShopNum.setText(goodInfo.getData().getBuy_shop_num() + "");
        tvBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (goodInfo.getData().getHas_printing() == 1 && goodInfo.getData().getCart_num() > 0) {
                    order();
                } else if (goodInfo.getData().getHas_printing() == 0 && goodInfo.getData().getCart_num() > 0) {
                    order();
                } else {
                    ToastUitl.showShort("请返回选择商品");
                    return;
                }
            }
        });

    }

    private void order() {
        RetrofitUtils.getInstance().server().getOeder(LoginUtil.getLoginToken())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<OrderBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(OrderBean orderBean) {
                        Intent intent = new Intent(GoodsDetailsActivity.this, ShopCartActivity.class);
                        intent.putExtra(ShopCartActivity.order, orderBean);
                        startActivity(intent);
                        finish();
                    }
                });
    }
}
