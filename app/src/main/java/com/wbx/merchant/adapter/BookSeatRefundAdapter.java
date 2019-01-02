package com.wbx.merchant.adapter;

import android.content.Context;
import android.text.Html;

import com.wbx.merchant.R;
import com.wbx.merchant.base.BaseAdapter;
import com.wbx.merchant.base.BaseViewHolder;
import com.wbx.merchant.bean.BookSeatInfo;
import com.wbx.merchant.utils.FormatUtil;

import java.util.List;

/**
 * Created by wushenghui on 2017/12/28.
 * 预定退款
 */

public class BookSeatRefundAdapter extends BaseAdapter<BookSeatInfo,Context> {

    public BookSeatRefundAdapter(List<BookSeatInfo> dataList, Context context) {
        super(dataList, context);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_book_seat_refund;
    }

    @Override
    public void convert(BaseViewHolder holder, BookSeatInfo bookSeatInfo, int position) {
        holder.setText(R.id.order_num_tv,String.format("订单号: %d",bookSeatInfo.getReserve_table_id()))
                .setText(R.id.constants_name_tv,bookSeatInfo.getName())
                .setText(R.id.book_time_tv, FormatUtil.myStampToDate1(bookSeatInfo.getReserve_time()+"","yyyy.MM.dd HH:mm"))
                .setText(R.id.refund_money_tv, Html.fromHtml("退款金额  :  <font color=#ff0000>"+(bookSeatInfo.getSubscribe_money()/100.00)+"</font>"));
    }
}
