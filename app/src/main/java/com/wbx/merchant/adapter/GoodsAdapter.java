package com.wbx.merchant.adapter;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.wbx.merchant.R;
import com.wbx.merchant.base.BaseAdapter;
import com.wbx.merchant.base.BaseApplication;
import com.wbx.merchant.base.BaseViewHolder;
import com.wbx.merchant.baseapp.AppConfig;
import com.wbx.merchant.bean.GoodsInfo;
import com.wbx.merchant.utils.FormatUtil;
import com.wbx.merchant.utils.GlideUtils;

import java.util.List;

/**
 * Created by wushenghui on 2017/6/26.
 */

public class GoodsAdapter extends BaseAdapter<GoodsInfo, Context> {
    private boolean mIsBath;

    public GoodsAdapter(List<GoodsInfo> dataList, Context context) {
        super(dataList, context);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_goods;
    }

    @Override
    public void convert(BaseViewHolder holder, final GoodsInfo goodsInfo, int position) {
        holder.getView(R.id.edit_check_im).setClickable(false);
        TextView priceTv = holder.getView(R.id.selling_price_tv);
        if (goodsInfo.getIs_attr() == 1) {
            //启用多规格
            if (goodsInfo.getMin_price() != 0) {
                priceTv.setText(String.format("%.2f", goodsInfo.getMin_price() / 100.00));
            } else {
                if (goodsInfo.getSales_promotion_is() == 1) {
                    //是否促销
                    priceTv.setText(String.format("%.2f", goodsInfo.getSales_promotion_price() / 100.00));
                } else {
                    priceTv.setText(String.format("%.2f", BaseApplication.getInstance().readLoginUser().getGrade_id() != AppConfig.StoreGrade.MARKET ? goodsInfo.getMall_price() / 100.00 : goodsInfo.getPrice() / 100.00));
                }
            }

        } else {
            if (goodsInfo.getSales_promotion_is() == 1) {
                //是否促销
                priceTv.setText(String.format("%.2f", goodsInfo.getSales_promotion_price() / 100.00));
            } else {
                priceTv.setText(String.format("%.2f", BaseApplication.getInstance().readLoginUser().getGrade_id() != AppConfig.StoreGrade.MARKET ? goodsInfo.getMall_price() / 100.00 : goodsInfo.getPrice() / 100.00));
            }
        }
        holder.setText(R.id.sold_num_tv, String.format("销量 %d", goodsInfo.getSold_num()));
        holder.setText(R.id.create_time_tv, String.format("上架时间 %s", FormatUtil.stampToDate(goodsInfo.getCreate_time() + "")));
        if (goodsInfo.getAudit() == 0) {
            holder.setText(R.id.audit_state_tv, "审核中");
        } else if (goodsInfo.getAudit() == 1) {
            holder.setText(R.id.audit_state_tv, "审核通过");
        } else {
            holder.setText(R.id.audit_state_tv, "已拒绝");
        }
        CheckBox checkCb = holder.getView(R.id.edit_check_im);
        if (mIsBath) {
            checkCb.setVisibility(View.VISIBLE);
        } else {
            checkCb.setVisibility(View.GONE);
        }
        checkCb.setChecked(goodsInfo.isSelect());
        ImageView goodsPicIm = holder.getView(R.id.goods_pic_im);
        GlideUtils.showRoundMediumPic(mContext, goodsPicIm, goodsInfo.getPhoto());
        holder.setText(R.id.goods_name_tv, goodsInfo.getProduct_name());
        setButtonShow(holder, goodsInfo);
    }

    private void setButtonShow(BaseViewHolder holder, GoodsInfo goodsInfo) {
        Button promotionBtn = holder.getView(R.id.promotion_btn);
        Button editBtn = holder.getView(R.id.edit_btn);
        Button soldOutInBtn = holder.getView(R.id.sold_out_in_btn);
        Button deleteBtn = holder.getView(R.id.delete_btn);
        ImageView isSalesProIm = holder.getView(R.id.is_sales_pro_im);
        if (goodsInfo.getClosed() == 0) {
            //已上架
//            if(BaseApplication.getInstance().readLoginUser().getGrade_id()!=AppConfig.StoreGrade.MARKET){
            //是否是促销中的商品
            if (goodsInfo.getSales_promotion_is() == 1) {
                isSalesProIm.setVisibility(View.VISIBLE);
                promotionBtn.setText("取消促销");
            } else {
                isSalesProIm.setVisibility(View.GONE);
                promotionBtn.setText("促销");
            }
//                promotionBtn.setVisibility(View.GONE);
//            }
            editBtn.setVisibility(View.VISIBLE);
            soldOutInBtn.setVisibility(View.VISIBLE);
            deleteBtn.setVisibility(View.VISIBLE);
        } else {
            //已下架
            promotionBtn.setVisibility(View.GONE);
            editBtn.setVisibility(View.VISIBLE);
            soldOutInBtn.setText("上架");
            soldOutInBtn.setVisibility(View.VISIBLE);
            deleteBtn.setVisibility(View.VISIBLE);
        }

    }

    public void setIsBath(boolean isBath) {
        this.mIsBath = isBath;
        notifyDataSetChanged();
    }
}
