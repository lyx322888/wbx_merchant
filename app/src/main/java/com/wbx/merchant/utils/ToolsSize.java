package com.wbx.merchant.utils;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.wbx.merchant.base.BaseApplication;


public class ToolsSize {

	private static Context context = BaseApplication.getInstance().getApplicationContext();

    /** 获取屏幕宽度 */
	public static int getScreenWidth() {
        WindowManager windowMgr = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowMgr.getDefaultDisplay();
        if (android.os.Build.VERSION.SDK_INT >= 17) {
            Point outSize = new Point();
            display.getRealSize(outSize);
            return outSize.x;
        } else {
            return display.getWidth();
        }
    }
	/** 获取屏幕高度 */
	public static int getScreenHeight() {
        WindowManager windowMgr = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = windowMgr.getDefaultDisplay();
        if (android.os.Build.VERSION.SDK_INT >= 17) {
            Point outSize = new Point();
            display.getRealSize(outSize);
            return outSize.y;
        } else {
            return display.getHeight();
        }
	}




	/** 获取状态栏高度 */
	public static int getStatusHeight(Window window) {
		Rect frame = new Rect();
		window.getDecorView().getWindowVisibleDisplayFrame(frame);
		return frame.top;
	}
	/** 将控件的高度设置为状态栏高度 */
	public static void setStatusHeight(final Window window , final View v) {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				LinearLayout.LayoutParams par = (LinearLayout.LayoutParams) v.getLayoutParams();
				par.height = getStatusHeight(window);
				v.setLayoutParams(par);
			}
		}, 50);
	}
	/**将dp转为px*/
	public static int getSize(float dp){
		final float scale = context.getResources().getDisplayMetrics().density;
		return Math.round(dp * scale);
	}
	/**将sp转为px*/
	public static int getSizeBySp(float sp){
		final float scale = context.getResources().getDisplayMetrics().scaledDensity;
		return Math.round(sp * scale );
	}

	
}
