package com.wbx.merchant.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.IBinder;
import androidx.fragment.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.umeng.analytics.MobclickAgent;
import com.wbx.merchant.activity.SplashActivity;
import com.wbx.merchant.baseapp.AppManager;
import com.wbx.merchant.baserx.RxManager;
import com.wbx.merchant.bean.UserInfo;
import com.wbx.merchant.utils.StatusBarUtil;
import com.wbx.merchant.utils.TUtil;
import com.wbx.merchant.utils.ToastUitl;
import com.wbx.merchant.widget.LoadingDialog;

import butterknife.ButterKnife;

/**
 * Created by wushenghui on 2017/4/23.
 * <p>
 * <p>
 * mvp
 * <p>
 * public class MainActivity extends BaseActivity<TestPresenter,TestModel> implements TestContract.View{
 *
 * @Override public int getLayoutId() {
 * return R.layout.activity_main;
 * }
 * @Override public void initPresenter() {
 * mPresenter.setVM(this,mModel);
 * }
 * @Override public void initView() {
 * findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
 * @Override public void onClick(View view) {
 * mPresenter.getAppIdRequest("1dc2f0ac6f94c439560d0fe6246ca4ad");
 * }
 * });
 * <p>
 * <p>
 * }
 * @Override public void dataSuccess(DeviceInfo result) {
 * showShortToast("appId::::::"+result.getAppId());
 * }
 */

public abstract class BaseActivity<T extends BasePresenter, E extends BaseModel> extends FragmentActivity {
    public T mPresenter;
    public E mModel;
    public Context mContext;
    public RxManager mRxManager;
    protected Activity mActivity;
    protected UserInfo userInfo;
    public Bundle msavedInstanceState;

    //    /** 判断是否是快速点击 */
//    private static long lastClickTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userInfo = BaseApplication.getInstance().readLoginUser();
        msavedInstanceState = savedInstanceState;
        mActivity = this;
        mRxManager = new RxManager();
        doBeforeSetcontentView();
        setContentView(getLayoutId());
        if (this instanceof SplashActivity && avoidRenewLaunchActivity()) {
            return;
        }
        ButterKnife.bind(this);
        mContext = this;
        mPresenter = TUtil.getT(this, 0);
        mModel = TUtil.getT(this, 1);
        if (mPresenter != null) {
            mPresenter.mContext = this;
        }
        this.initPresenter();
        this.initView();
        this.fillData();
        this.setListener();
        initPush();
    }

    private void initPush() {
    }

    @Override
    protected void onResume() {
        super.onResume();
        userInfo = BaseApplication.getInstance().readLoginUser();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    /**
     * 设置layout前配置
     */
    private void doBeforeSetcontentView() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        // 把actvity放到application栈中管理
        AppManager.getAppManager().addActivity(this);
        // 无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 设置竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // 默认着色状态栏
        StatusBarUtil.StatusBarLightMode(this);
    }


    /*********************子类实现*****************************/
    //获取布局文件
    public abstract int getLayoutId();




    //简单页面无需mvp就不用管此方法即可,完美兼容各种实际场景的变通
    public abstract void initPresenter();

    //初始化view
    public abstract void initView();

    //加载网络数据
    public abstract void fillData();

    //事件
    public abstract void setListener();


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     *
     * @param token
     */
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 短暂显示Toast提示(来自String)
     **/
    public void showShortToast(String text) {
        ToastUitl.showShort(text);
    }

    public void back(View view) {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LoadingDialog.cancelDialogForLoading();
        AppManager.getAppManager().removeActivity(this);
    }

    /**
     * 避免首次安装点击打开启动程序后，按home键返回桌面然后点击桌面图标会重新实例化入口类的activity，原因参考http://www.cnblogs.com/net168/p/5722752.html
     */
    private boolean avoidRenewLaunchActivity() {
        if (!this.isTaskRoot()) {
            Intent intent = getIntent();
            if (intent != null) {
                String action = intent.getAction();
                if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && Intent.ACTION_MAIN.equals(action)) {
                    finish();
                    return true;
                }
            }
        }
        return false;
    }
}
