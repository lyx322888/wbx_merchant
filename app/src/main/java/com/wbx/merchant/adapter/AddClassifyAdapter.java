package com.wbx.merchant.adapter;

import android.content.Context;

import com.wbx.merchant.R;
import com.wbx.merchant.base.BaseApplication;
import com.wbx.merchant.base.BaseAdapter;
import com.wbx.merchant.base.BaseViewHolder;
import com.wbx.merchant.baseapp.AppConfig;
import com.wbx.merchant.bean.CateInfo;

import java.util.List;

/**
 * Created by wushenghui on 2017/6/26.
 */

public class AddClassifyAdapter extends BaseAdapter<CateInfo,Context> {

    public AddClassifyAdapter(List<CateInfo> dataList, Context context) {
        super(dataList, context);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_classify;
    }

    @Override
    public void convert(BaseViewHolder holder, CateInfo cateInfo, int position) {
           holder.setText(R.id.classify_name_tv,cateInfo.getCate_name()).setText(R.id.classify_order_tv, BaseApplication.getInstance().readLoginUser().getGrade_id()== AppConfig.StoreGrade.MARKET?cateInfo.getOrderby_cate()+"":cateInfo.getOrderby()+"");
    }

}
