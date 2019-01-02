package com.wbx.merchant.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class SignView extends View {

    private Paint paint;
    private Path path;

    public SignView(Context context) {
        super(context);
    }

    public SignView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SignView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs) {
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
        paint.setAntiAlias(true);
        path = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(path, paint);
    }

    private float lastX;
    private float lastY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = x;
                lastY = y;
                path.moveTo(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(x - lastX) > 10 || Math.abs(y - lastY) > 10) {
                    path.quadTo(lastX, lastY, (x + lastX) / 2, (y + lastY) / 2);
                    lastX = x;
                    lastY = y;
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                lastX = x;
                lastY = y;
                path.moveTo(x, y);
                break;
        }
        return true;
    }

    public void reDraw() {
        path.reset();
        invalidate();
    }

    public Bitmap save() {
        if (path.isEmpty()) {
            Toast.makeText(getContext(), "请先签名!", Toast.LENGTH_SHORT).show();
            return null;
        }
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        RectF rectF = new RectF();
        path.computeBounds(rectF, true);
        if (rectF.width() * rectF.height() < 10000) {
            Toast.makeText(getContext(), "字体太小，请点击重签后重新签名!", Toast.LENGTH_SHORT).show();
            return null;
        }
        canvas.drawPath(path, paint);
        return bitmap;
    }
}
