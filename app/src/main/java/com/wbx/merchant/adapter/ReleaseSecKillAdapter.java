package com.wbx.merchant.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.wbx.merchant.R;
import com.wbx.merchant.base.BaseAdapter;
import com.wbx.merchant.base.BaseViewHolder;
import com.wbx.merchant.bean.GoodsInfo;
import com.wbx.merchant.utils.GlideUtils;
import com.wbx.merchant.utils.PriceUtil;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by wushenghui on 2017/12/12.
 */

public class ReleaseSecKillAdapter extends BaseAdapter<GoodsInfo, Context> {
    private boolean mIsShow;
    private DecimalFormat decimalFormat = new DecimalFormat("0.00");

    public ReleaseSecKillAdapter(List<GoodsInfo> dataList, Context context) {
        super(dataList, context);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_release_seckill;
    }

    @Override
    public void convert(BaseViewHolder holder, final GoodsInfo goodsInfo, int position) {
        ImageView picIm = holder.getView(R.id.goods_pic_im);
        GlideUtils.showRoundMediumPic(mContext, picIm, goodsInfo.getPhoto());
        ImageView selectIm = holder.getView(R.id.edit_check_im);
        if (goodsInfo.getIs_attr() == 1) {
            holder.getView(R.id.is_attr_im).setVisibility(View.VISIBLE);
            holder.getView(R.id.is_attr_tv).setVisibility(View.VISIBLE);
            holder.getView(R.id.un_attr_layout).setVisibility(View.GONE);
        } else {
            holder.getView(R.id.un_attr_layout).setVisibility(View.VISIBLE);
            holder.getView(R.id.is_attr_im).setVisibility(View.GONE);
            holder.getView(R.id.is_attr_tv).setVisibility(View.GONE);
        }
        if (goodsInfo.isSelect()) {
            selectIm.setImageResource(R.drawable.ic_ok);
        } else {
            selectIm.setImageResource(R.drawable.ic_round);
        }
        if (mIsShow) {
            holder.getView(R.id.edit_check_im).setVisibility(View.VISIBLE);
            holder.getView(R.id.delete_im).setVisibility(View.GONE);
        } else {
            holder.getView(R.id.edit_check_im).setVisibility(View.GONE);
            holder.getView(R.id.delete_im).setVisibility(View.VISIBLE);
        }

        holder.setText(R.id.goods_name_tv, goodsInfo.getProduct_name()).setText(R.id.selling_price_tv, goodsInfo.getMall_price() == 0 ? String.format("¥%.2f", goodsInfo.getPrice() / 100.00) : String.format("¥%.2f", goodsInfo.getMall_price() / 100.00))
                .setText(R.id.goods_num_tv, String.format("库存:%d", goodsInfo.getNum()));
        final EditText secKillPriceEdit = holder.getView(R.id.seckill_price_edit);
        secKillPriceEdit.setText(decimalFormat.format(goodsInfo.getSeckill_price()));
        secKillPriceEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                return false;
            }
        });
        secKillPriceEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!PriceUtil.isCorrectPrice(secKillPriceEdit, charSequence)) {
                    return;
                }
                if (charSequence.length() == 0) {
                    goodsInfo.setFloatSeckill_price(0);
                } else {
                    goodsInfo.setFloatSeckill_price(Float.valueOf(charSequence.toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        EditText secKillNumEdit = holder.getView(R.id.seckill_num_edit);
        secKillNumEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 0) {
                    goodsInfo.setSeckill_num("");
                } else {
                    goodsInfo.setSeckill_num(charSequence.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        //
    }

    public void setIsShow(boolean isShow) {
        mIsShow = isShow;
        notifyDataSetChanged();
    }
}
