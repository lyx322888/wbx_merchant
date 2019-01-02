package com.wbx.merchant.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.wbx.merchant.R;

/**
 * Created by wushenghui on 2017/6/20.
 * 按宽高比例设置Layout
 */

public class RatioLayout extends RelativeLayout {
    private float ratio;

    public RatioLayout(Context context) {
        super(context);
    }

    public RatioLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public RatioLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        // 获取属性值
        // attrs.getAttributeFloatValue("tcl", "ratio", 1);

        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.RatioLayout);
        ratio = typedArray.getFloat(R.styleable.RatioLayout_ratio, -1);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        // 1.获取宽度
        // 2.根据宽度和比例ratio,计算控件高度
        // 3.重新测量控件
        int width = MeasureSpec.getSize(widthMeasureSpec);// 获取宽度值
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);// 获取宽度模式

        int height = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        // 宽度确定，高度不确定，ratio合法，才计算高度值
        if (widthMode == MeasureSpec.EXACTLY
                && heightMode != MeasureSpec.EXACTLY && ratio > 0) {

            int imageWidth = width - getPaddingLeft() - getPaddingRight();// 图片真实宽度要减去内边距

            // 高度 = 宽度/比例
            int imageHeight = (int) (imageWidth / ratio);

            height = imageHeight + getPaddingBottom() + getPaddingTop();// 控件高度=图片高度+内边距

            // 根据最新的高度来重新生成(高度是确定模式)
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height,
                    MeasureSpec.EXACTLY);
        }

        // MeasureSpec.AT_MOST;至多模式，控件有多大就显示多大 wrap_content
        // MeasureSpec.EXACTLY;确定模式模式，类似宽高写死 match_parent
        // MeasureSpec.UNSPECIFIED;未知模式

        // 根据最新的高度来测量控件(高度是确定模式)
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
