package com.wbx.merchant.adapter;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.wbx.merchant.R;
import com.wbx.merchant.base.BaseAdapter;
import com.wbx.merchant.base.BaseViewHolder;
import com.wbx.merchant.bean.BookSeatInfo;
import com.wbx.merchant.utils.FormatUtil;

import java.util.List;

/**
 * Created by wushenghui on 2017/12/26.
 */

public class BookSeatOrderAdapter extends BaseAdapter<BookSeatInfo,Context> {
    public BookSeatOrderAdapter(List<BookSeatInfo> dataList, Context context) {
        super(dataList, context);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_book_seat_order;
    }

    @Override
    public void convert(BaseViewHolder holder, BookSeatInfo bookSeatInfo, int position) {
       holder.setText(R.id.constants_tv,"联系人:"+bookSeatInfo.getName())
       .setText(R.id.book_time_tv, FormatUtil.myStampToDate1(bookSeatInfo.getReserve_time()+"","yyyy-MM-dd HH:mm"))
       .setText(R.id.use_num_tv,String.format("%d人",bookSeatInfo.getNumber()))
       .setText(R.id.remark_tv,String.format("备注:%s",bookSeatInfo.getNote()))
       .setText(R.id.is_ordering_tv,bookSeatInfo.getType()==2?"已点餐":"未点餐");
        ImageView orderStateIm = holder.getView(R.id.order_state_im);
        Button receiveBtn = holder.getView(R.id.receive_order_btn);
        Button callCustomBtn = holder.getView(R.id.call_custom_btn);
        Button refuseBtn = holder.getView(R.id.refuse_order_btn);
        switch (bookSeatInfo.getStatus()){
            case 2:
                //已接单
                refuseBtn.setVisibility(View.GONE);
                callCustomBtn.setVisibility(View.VISIBLE);
                receiveBtn.setVisibility(View.GONE);
                orderStateIm.setImageResource(R.drawable.orderview_icon_yes);
                break;
            case 9:
                //已拒单
                refuseBtn.setVisibility(View.GONE);
                callCustomBtn.setVisibility(View.VISIBLE);
                receiveBtn.setVisibility(View.GONE);
                orderStateIm.setImageResource(R.drawable.reserve_refuse);
                break;
            case 4:
                //待退款
                refuseBtn.setVisibility(View.GONE);
                callCustomBtn.setVisibility(View.VISIBLE);
                receiveBtn.setVisibility(View.GONE);
                orderStateIm.setImageResource(R.drawable.reserve_no_refund);
                break;
            case 5:
                //已退款
                refuseBtn.setVisibility(View.GONE);
                callCustomBtn.setVisibility(View.VISIBLE);
                receiveBtn.setVisibility(View.GONE);
                orderStateIm.setImageResource(R.drawable.reserve_yes_refund);
                break;
            case 8:
                //已完成
                refuseBtn.setVisibility(View.GONE);
                callCustomBtn.setVisibility(View.VISIBLE);
                receiveBtn.setVisibility(View.GONE);
                orderStateIm.setImageResource(R.drawable.reserve_complete);
                break;
            default:
                //未接单
                callCustomBtn.setVisibility(View.VISIBLE);
                receiveBtn.setVisibility(View.VISIBLE);
                refuseBtn.setVisibility(View.VISIBLE);
                orderStateIm.setImageResource(R.drawable.orderview_icon_no);
                break;
        }
    }
}
