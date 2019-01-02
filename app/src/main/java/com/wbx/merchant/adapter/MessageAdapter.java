package com.wbx.merchant.adapter;

import android.content.Context;

import com.wbx.merchant.R;
import com.wbx.merchant.base.BaseAdapter;
import com.wbx.merchant.base.BaseViewHolder;
import com.wbx.merchant.bean.MessageInfo;

import java.util.List;

/**
 * Created by wushenghui on 2017/8/1.
 */

public class MessageAdapter extends BaseAdapter<MessageInfo,Context> {

    public MessageAdapter(List<MessageInfo> dataList, Context context) {
        super(dataList, context);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_message_list;
    }

    @Override
    public void convert(BaseViewHolder holder, MessageInfo messageInfo, int position) {

    }
}
