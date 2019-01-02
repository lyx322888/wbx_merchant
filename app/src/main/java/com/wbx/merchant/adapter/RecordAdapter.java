package com.wbx.merchant.adapter;

import android.content.Context;

import com.wbx.merchant.R;
import com.wbx.merchant.base.BaseAdapter;
import com.wbx.merchant.base.BaseViewHolder;
import com.wbx.merchant.bean.RecordInfo;
import com.wbx.merchant.utils.FormatUtil;

import java.util.List;

/**
 * Created by wushenghui on 2017/6/30.
 */

public class RecordAdapter extends BaseAdapter<RecordInfo, Context> {

    public RecordAdapter(List<RecordInfo> dataList, Context context) {
        super(dataList, context);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_record;
    }

    @Override
    public void convert(BaseViewHolder holder, RecordInfo recordInfo, int position) {
        holder.setText(R.id.cash_info_tv, "提现-到" + recordInfo.getBank_name() + "(" + recordInfo.getBank_num() + ")")
                .setText(R.id.tv_state, "(" + recordInfo.getType() + ")-" + recordInfo.getStatus())
                .setText(R.id.cash_time_tv, FormatUtil.stampToDate(recordInfo.getAddtime() + ""))
                .setText(R.id.cash_money_tv, String.format("¥%.2f", recordInfo.getGold() / 100.00));
    }
}
