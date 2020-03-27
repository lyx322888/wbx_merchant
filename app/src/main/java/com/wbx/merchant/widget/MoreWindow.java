package com.wbx.merchant.widget;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.wbx.merchant.R;
import com.wbx.merchant.activity.ProductActivity;
import com.wbx.merchant.activity.ReleaseActivity;
import com.wbx.merchant.activity.SpecialSupplyActivity;
import com.wbx.merchant.utils.KickBackAnimator;

import jp.wasabeef.glide.transformations.internal.FastBlur;

/**
 * Created by wushenghui on 2017/11/3.
 */

public class MoreWindow extends PopupWindow implements OnClickListener {

    private String TAG = MoreWindow.class.getSimpleName();
    Activity mContext;
    private int mWidth;
    private int mHeight;
    private int statusBarHeight;
    private Bitmap mBitmap = null;
    private Bitmap overlay = null;

    private Handler mHandler = new Handler();

    public MoreWindow(Activity context) {
        mContext = context;
    }

    public void init() {
        Rect frame = new Rect();
        mContext.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        statusBarHeight = frame.top;
        DisplayMetrics metrics = new DisplayMetrics();
        mContext.getWindowManager().getDefaultDisplay()
                .getMetrics(metrics);
        mWidth = metrics.widthPixels;
        mHeight = metrics.heightPixels;
        setWidth(mWidth);
        setHeight(mHeight);
    }

    private Bitmap blur() {
        if (null != overlay) {
            return overlay;
        }
        long startMs = System.currentTimeMillis();
        View view = mContext.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache(true);
        mBitmap = view.getDrawingCache();
        float scaleFactor = 8;//图片缩放比例；
        float radius = 10;//模糊程度
        int width = mBitmap.getWidth();
        int height = mBitmap.getHeight();

        overlay = Bitmap.createBitmap((int) (width / scaleFactor), (int) (height / scaleFactor), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        canvas.scale(1 / scaleFactor, 1 / scaleFactor);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(mBitmap, 0, 0, paint);
        overlay = FastBlur.blur(overlay, (int) radius, true);
        return overlay;
    }

    public void showMoreWindow(View anchor) {
        final ConstraintLayout layout = (ConstraintLayout) LayoutInflater.from(mContext).inflate(R.layout.center_more_window, null);
        setContentView(layout);
        layout.findViewById(R.id.close_im).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isShowing()) {
                    dismiss();
                }
            }

        });
        showAnimation(layout);
//        setBackgroundDrawable(new BitmapDrawable(mContext.getResources(), blur()));
        setOutsideTouchable(true);
        setFocusable(true);
        showAtLocation(anchor, Gravity.BOTTOM, 0, statusBarHeight);
    }

    private void showAnimation(ViewGroup layout) {
        for (int i = 0; i < layout.getChildCount(); i++) {
            final View child = layout.getChildAt(i);
            if (child.getId() == R.id.close_im) {
                continue;
            }
            child.setOnClickListener(this);
            child.setVisibility(View.INVISIBLE);
            mHandler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    child.setVisibility(View.VISIBLE);
                    ValueAnimator fadeAnim = ObjectAnimator.ofFloat(child, "translationY", 600, 0);
                    fadeAnim.setDuration(700);
                    KickBackAnimator kickAnimator = new KickBackAnimator();
                    kickAnimator.setDuration(150);
                    fadeAnim.setEvaluator(kickAnimator);
                    fadeAnim.start();
                }
            }, i * 50);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.release_btn:
                mContext.startActivity(new Intent(mContext, ReleaseActivity.class));
                dismiss();
                break;
            case R.id.product_library_btn:
                mContext.startActivity(new Intent(mContext, ProductActivity.class));
                dismiss();
                break;
//            case R.id.product:
//                mContext.startActivity(new Intent(mContext, SpecialSupplyActivity.class));
//                dismiss();
//                break;
            default:
                break;
        }
    }
}