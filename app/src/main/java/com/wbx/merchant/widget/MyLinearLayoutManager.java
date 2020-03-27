package com.wbx.merchant.widget;

import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

/**
 * Created by wushenghui on 2017/10/9.
 */

public class MyLinearLayoutManager extends LinearLayoutManager {

    private RecyclerView.Recycler mRecycler;

    public MyLinearLayoutManager(Context context) {
        super(context);
    }

    @Override
    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
        super.onMeasure(recycler, state, widthSpec, heightSpec);
        mRecycler = recycler;
    }

    public int getScrollY() {
        int scrollY = getPaddingTop();
        int firstVisibleItemPosition = findFirstVisibleItemPosition();

        if (firstVisibleItemPosition >= 0 && firstVisibleItemPosition < getItemCount()) {
            for (int i = 0; i < firstVisibleItemPosition; i++) {
                View view = mRecycler.getViewForPosition(i);
                if (view == null) {
                    continue;
                }
                if (view.getMeasuredHeight() <= 0) {
                    measureChildWithMargins(view, 0, 0);
                }
                RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) view.getLayoutParams();
                scrollY += lp.topMargin;
                scrollY += getDecoratedMeasuredHeight(view);
                scrollY += lp.bottomMargin;
                mRecycler.recycleView(view);
            }

            View firstVisibleItem = findViewByPosition(firstVisibleItemPosition);
            RecyclerView.LayoutParams firstVisibleItemLayoutParams = (RecyclerView.LayoutParams) firstVisibleItem.getLayoutParams();
            scrollY += firstVisibleItemLayoutParams.topMargin;
            scrollY -= getDecoratedTop(firstVisibleItem);
        }

        return scrollY;
    }
}
