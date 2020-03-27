package com.wbx.merchant.widget;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by wushenghui on 2017/11/14.
 */

public class MyMaxHeightRecyclerView extends RecyclerView {
    private int maxHeight = 450;
    public MyMaxHeightRecyclerView(Context context) {
        super(context);
    }

    public MyMaxHeightRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyMaxHeightRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        if (maxHeight > -1) {
            heightSpec = MeasureSpec.makeMeasureSpec(maxHeight,
                    MeasureSpec.AT_MOST);
        }
        super.onMeasure(widthSpec, heightSpec);
    }
}
