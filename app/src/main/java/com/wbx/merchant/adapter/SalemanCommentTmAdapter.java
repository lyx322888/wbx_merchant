package com.wbx.merchant.adapter;

import android.support.annotation.Nullable;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wbx.merchant.R;
import com.wbx.merchant.bean.SalesmanCommentInfo;
import com.wbx.merchant.widget.SelectorCommentButton;

import java.util.List;
//评论题目
public class SalemanCommentTmAdapter extends BaseQuickAdapter<SalesmanCommentInfo.DataBean.QuestionBean, BaseViewHolder> {
    public SalemanCommentTmAdapter(int layoutResId, @Nullable List<SalesmanCommentInfo.DataBean.QuestionBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SalesmanCommentInfo.DataBean.QuestionBean item) {
        helper.setText(R.id.tv_comment_title,helper.getAdapterPosition()+1+"."+item.getTitle());
        SelectorCommentButton selectorCommentButton = helper.getView(R.id.scb_comment);
        switch (item.getQuestion_answer()){
            //问题答案 -1未选择 0否 1是
            case -1:
                selectorCommentButton.setSelection(SelectorCommentButton.NO_CHOICE);
                break;
            case 0:
                selectorCommentButton.setSelection(SelectorCommentButton.CHOICE_TOW);
                break;
            case 1:
                selectorCommentButton.setSelection(SelectorCommentButton.CHOICE_ONE);
                break;
        }
    }
}
