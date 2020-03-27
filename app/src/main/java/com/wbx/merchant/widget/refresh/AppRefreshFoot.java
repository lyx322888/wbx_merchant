package com.wbx.merchant.widget.refresh;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshKernel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.wbx.merchant.R;

/**
 * @author Zero
 * @date 2018/11/9
 */
public class AppRefreshFoot extends FrameLayout implements RefreshFooter {

    private View arrow;
    private ProgressBar progress;
    private TextView tvHint;

    public AppRefreshFoot(@NonNull Context context) {
        this(context, null);
    }

    public AppRefreshFoot(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AppRefreshFoot(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View layout = LayoutInflater.from(context).inflate(R.layout.layout_refresh_foot, null);
        addView(layout);
        arrow = findViewById(R.id.arrow);
        progress = findViewById(R.id.progress);
        tvHint = findViewById(R.id.tv_hint);
    }

    @Override
    public boolean setNoMoreData(boolean noMoreData) {
        return false;
    }

    @NonNull
    @Override
    public View getView() {
        return this;
    }

    @NonNull
    @Override
    public SpinnerStyle getSpinnerStyle() {
        return SpinnerStyle.Translate;
    }

    @Override
    public void setPrimaryColors(int... colors) {

    }

    @Override
    public void onInitialized(@NonNull RefreshKernel kernel, int height, int maxDragHeight) {

    }

    @Override
    public void onMoving(boolean isDragging, float percent, int offset, int height, int maxDragHeight) {

    }

    @Override
    public void onReleased(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {

    }

    @Override
    public void onStartAnimator(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {
    }

    @Override
    public int onFinish(@NonNull RefreshLayout refreshLayout, boolean success) {
        return 100;
    }

    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {

    }

    @Override
    public boolean isSupportHorizontalDrag() {
        return false;
    }

    @Override
    public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {
        switch (newState) {
            case None:
            case PullUpToLoad:
                progress.setVisibility(GONE);
                arrow.animate().rotation(180);
                tvHint.setText("上拉加载更多");
                break;
            case ReleaseToLoad:
                progress.setVisibility(GONE);
                arrow.animate().rotation(0);
                tvHint.setText("释放加载更多");
                break;
            case Loading:
                progress.setVisibility(VISIBLE);
                arrow.setVisibility(GONE);
                tvHint.setText("正在加载...");
                break;
        }
    }
}
