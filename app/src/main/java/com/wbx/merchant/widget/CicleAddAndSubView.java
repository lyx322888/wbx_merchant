package com.wbx.merchant.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wbx.merchant.R;

public class CicleAddAndSubView extends LinearLayout implements View.OnClickListener {
    public static final String TYPE_SUBTRACT = "minus";//减
    public static final String TYPE_ADD = "plus";//加
    private static final int DEFAULT_NUM = 0;//默认num值

    private View mLayoutView;
    private Context mContext;
    private ImageView mBtnAdd;//加按钮
    private ImageView mBtnSub;//减按钮
    private TextView mTvCount;//数量显示
    private int mNum;
    private OnNumChangeListener mOnNumChangeListener;

    /**
     * 设置监听回调
     *
     * @param onNumChangeListener
     */
    public void setOnNumChangeListener(OnNumChangeListener onNumChangeListener) {
        this.mOnNumChangeListener = onNumChangeListener;
    }

    public CicleAddAndSubView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mLayoutView = LayoutInflater.from(context).inflate(R.layout.add_sub_view, this);
        this.mContext = context;

        initView();
        initData();
        setListener();
    }

    private void initView() {
        mBtnAdd = mLayoutView.findViewById(R.id.btn_add);
        mBtnSub = mLayoutView.findViewById(R.id.btn_sub);
        mTvCount = mLayoutView.findViewById(R.id.tv_count);
        setPadding(1, 1, 1, 1);
        //重新设置mBtnAdd，mBtnSub宽高，用来保证显示正方形
        setViewSize(mBtnAdd);
        setViewSize(mBtnSub);
    }

    private void initData() {
        setAddBtnImageResource(R.drawable.add);
        setSubBtnImageResource(R.drawable.sub);
        mNum = DEFAULT_NUM;
        setNum(mNum);//设置默认数量
    }

    private void setListener() {
        mBtnAdd.setOnClickListener(this);
        mBtnSub.setOnClickListener(this);
    }

    private void setViewSize(final View view) {
        view.post(new Runnable() {
            @Override
            public void run() {//这里获取宽高
                int width = view.getWidth();
                int height = view.getHeight();
                LinearLayout.LayoutParams params = (LayoutParams) view.getLayoutParams();
                if (width < height) {
                    params.height = width;
                } else if (width > height) {
                    params.width = height;
                }
                view.setLayoutParams(params);
            }
        });
    }

    @Override
    public void onClick(View v) {
        String countText = mTvCount.getText().toString();
        if (TextUtils.isEmpty(countText)) {
            mNum = DEFAULT_NUM;
            mTvCount.setText(String.valueOf(mNum));
            return;
        }
        switch (v.getId()) {
            case R.id.btn_add://加号
                mNum++;
                setNum(mNum);
                if (mOnNumChangeListener != null) {
                    mOnNumChangeListener.onNumChange(mLayoutView, TYPE_ADD, getNum());
                }
                break;
            case R.id.btn_sub://减号
                mNum--;
                setNum(mNum);
                if (mOnNumChangeListener != null) {
                    mOnNumChangeListener.onNumChange(mLayoutView, TYPE_SUBTRACT, getNum());
                }
                break;
            default:
                break;
        }
    }


    /**
     * 设置中间的距离
     *
     * @param distance
     */
    public void setMiddleDistance(int distance) {
        mTvCount.setPadding(distance, 0, distance, 0);
    }

    /**
     * 设置数量
     *
     * @param num
     */
    public void setNum(int num) {
        this.mNum = num;
        if (mNum > 0) {
            mBtnSub.setVisibility(View.VISIBLE);
            mTvCount.setVisibility(View.VISIBLE);
        } else {
            mBtnSub.setVisibility(View.INVISIBLE);
            mTvCount.setVisibility(View.INVISIBLE);
        }
        mTvCount.setText(String.valueOf(mNum));
    }

    /**
     * 获取值
     *
     * @return
     */
    public int getNum() {
        String countText = mTvCount.getText().toString().trim();
        if (!TextUtils.isEmpty(countText)) {
            return Integer.parseInt(countText);
        } else {
            return DEFAULT_NUM;
        }
    }


    /**
     * 设置加号图片
     *
     * @param addBtnDrawable
     */
    public void setAddBtnImageResource(int addBtnDrawable) {
        mBtnAdd.setImageResource(addBtnDrawable);
    }

    /**
     * 设置减法图片
     *
     * @param subBtnDrawable
     */
    public void setSubBtnImageResource(int subBtnDrawable) {
        mBtnSub.setImageResource(subBtnDrawable);
    }

    /**
     * 设置加法减法的背景色
     *
     * @param addBtnColor
     * @param subBtnColor
     */
    public void setButtonBgColor(int addBtnColor, int subBtnColor) {
        mBtnAdd.setBackgroundColor(addBtnColor);
        mBtnSub.setBackgroundColor(subBtnColor);
    }

    public interface OnNumChangeListener {
        /**
         * 监听方法
         *
         * @param view
         * @param stype 点击按钮的类型
         * @param num   返回的数值
         */
        void onNumChange(View view, String stype, int num);
    }
}
