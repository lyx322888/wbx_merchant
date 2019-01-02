package com.wbx.merchant.chat;
import android.content.Context;

import com.wbx.merchant.R;
import com.wbx.merchant.base.BaseAdapter;
import com.wbx.merchant.base.BaseViewHolder;

import java.util.List;

/**
 * Created by wushenghui on 2018/1/3.
 */

public class QuestionAdapter extends BaseAdapter<QuestionInfo,Context> {

    public QuestionAdapter(List<QuestionInfo> dataList, Context context) {
        super(dataList, context);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_text_view;
    }

    @Override
    public void convert(BaseViewHolder holder, QuestionInfo question, int position) {
        holder.setText(R.id.title_tv,question.getTitle());
//        holder.getView(R.id.title_tv).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
    }
}
