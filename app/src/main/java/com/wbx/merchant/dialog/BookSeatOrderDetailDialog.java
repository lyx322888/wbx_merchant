package com.wbx.merchant.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wbx.merchant.R;
import com.wbx.merchant.bean.BookSeatInfo;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 */
public class BookSeatOrderDetailDialog extends DialogFragment {

    @Bind(R.id.ll_container)
    LinearLayout llContainer;
    @Bind(R.id.tv_total_fee)
    TextView tvTotalFee;

    public static BookSeatOrderDetailDialog newInstance(BookSeatInfo data) {
        BookSeatOrderDetailDialog bookSeatOrderDetailDialog = new BookSeatOrderDetailDialog();
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", data);
        bookSeatOrderDetailDialog.setArguments(bundle);
        return bookSeatOrderDetailDialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = inflater.inflate(R.layout.fragment_book_seat_order_detail_dialog, container, false);
        ButterKnife.bind(this, view);
        updateUI();
        return view;
    }

    private void updateUI() {
        BookSeatInfo bookSeatInfo = (BookSeatInfo) getArguments().getSerializable("data");
        List<BookSeatInfo.OrderGoodsBean> lstData = bookSeatInfo.getOrder_goods();
        if (lstData != null && lstData.size() > 0) {
            llContainer.removeAllViews();
            for (int i = 0; i < lstData.size(); i++) {
                BookSeatInfo.OrderGoodsBean data = lstData.get(i);
                View layout = LayoutInflater.from(getActivity()).inflate(R.layout.item_book_seat_order_detail, null);
                TextView tvName = layout.findViewById(R.id.tv_name);
                TextView tvNum = layout.findViewById(R.id.tv_num);
                TextView tvPrice = layout.findViewById(R.id.tv_price);
                StringBuilder sb = new StringBuilder();
                if (!TextUtils.isEmpty(data.getAttr_name()) || !TextUtils.isEmpty(data.getNature_name())) {
                    sb.append("(");
                    if (!TextUtils.isEmpty(data.getAttr_name())) {
                        sb.append(data.getAttr_name());
                    }
                    if (!TextUtils.isEmpty(data.getAttr_name()) && !TextUtils.isEmpty(data.getNature_name())) {
                        sb.append("+");
                    }
                    if (!TextUtils.isEmpty(data.getNature_name())) {
                        sb.append(data.getNature_name());
                    }
                    sb.append(")");
                }
                tvName.setText(data.getTitle() + sb.toString());
                tvNum.setText("x" + data.getNum());
                tvPrice.setText(String.format("¥%.2f", data.getPrice() / 100.00));
                llContainer.addView(layout);
            }
            tvTotalFee.setText(String.format("¥%.2f", bookSeatInfo.getSubscribe_money() / 100.00));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.iv_close)
    public void onViewClicked() {
        dismiss();
    }
}
