package com.wbx.merchant.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.wbx.merchant.R;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.base.BaseApplication;
import com.wbx.merchant.baseapp.AppConfig;
import com.wbx.merchant.fragment.BookSeatOrderFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by wushenghui on 2017/12/26.
 */

public class BookSeatActivity extends BaseActivity {
    @Bind(R.id.tab_layout)
    TabLayout mTabLayout;
    @Bind(R.id.view_pager)
    ViewPager mOrderViewPager;
    @Bind(R.id.tv_right)
    TextView tvRight;
    @Bind(R.id.tv_date)
    TextView tvDate;
    private MyFragmentStateAdapter mPagerAdapter;
    private String[] mTitles = new String[]{"", "", "", "", "", "", ""};
    private List<Calendar> calendarList = new ArrayList<>();
    private List<BookSeatOrderFragment> mFragmentList = new ArrayList<>();
    private int mPosition = 0;
    private String showDateStr = "";
    private boolean isHide;
    @Bind(R.id.show_book_seat_num_tv)
    TextView showBookSeatNumTv;
    private HashMap<String, Object> mParams = new HashMap<>();
    private Dialog dialog;
    private View inflate;
    private EditText inputPriceEdit;

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, BookSeatActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_book_seat;
    }

    @Override
    public void initPresenter() {
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
    }

    @Override
    public void initView() {
    }

    @Override
    public void fillData() {
        tvRight.setText(userInfo.getIsSubscribe() == 0 ? "开启预定" : "关闭预定");
        showDateStr = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        tvDate.setText(showDateStr);
        mPagerAdapter = new MyFragmentStateAdapter(getSupportFragmentManager());
        mOrderViewPager.setAdapter(mPagerAdapter);
        mTabLayout.setupWithViewPager(mOrderViewPager);
        for (int i = 0; i < mTitles.length; i++) {
            Calendar instance = Calendar.getInstance();
            instance.add(Calendar.DATE, i);
            calendarList.add(instance);
            mTabLayout.getTabAt(i).setCustomView(getTabView(i));
        }
        mParams.put("day_time", Calendar.getInstance().getTimeInMillis() / 1000L);
        getServiceData();

    }

    private void getServiceData() {
        mParams.put("sj_login_token", userInfo.getSj_login_token());
        mParams.put("page", AppConfig.pageNum);
        mParams.put("num", AppConfig.pageSize);
        new MyHttp().doPost(Api.getDefault().getBookSeatList(mParams), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                showBookSeatNumTv.setText(TextUtils.isEmpty(result.getJSONObject("data").getString("count_subscribe")) ? "0" : result.getJSONObject("data").getString("count_subscribe"));
            }

            @Override
            public void onError(int code) {
                showBookSeatNumTv.setText("0");
            }
        });
    }

    @Override
    public void setListener() {
        findViewById(R.id.book_seat_refund_im).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, BookSeatRefundActivity.class));
            }
        });
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                showDateStr = new SimpleDateFormat("yyyy-MM-dd").format(calendarList.get(position).getTime());
                tvDate.setText(showDateStr);
                mParams.put("day_time", calendarList.get(position).getTimeInMillis() / 1000L);
                getServiceData();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        mOrderViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mPosition = position;

            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    /**
     * 输入开启预定价格
     */
    private void showInputPriceDialog() {
        if (null == dialog) {
            inflate = getLayoutInflater().inflate(R.layout.open_book_input_price_dialog, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setView(inflate);
            dialog = builder.create();

        }
        inputPriceEdit = (EditText) inflate.findViewById(R.id.dialog_input_nick_name_edit);
        inflate.findViewById(R.id.dialog_cancel_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        inflate.findViewById(R.id.dialog_complete_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String price = inputPriceEdit.getText().toString().trim();
                if (TextUtils.isEmpty(price)) {
                    showShortToast("请输入预定金额");
                    return;
                }
                openOrCloseBook(price);
            }
        });

        if (!dialog.isShowing()) dialog.show();
    }

    private void openOrCloseBook(String price) {
        new MyHttp().doPost(Api.getDefault().openOrCloseBook(userInfo.getSj_login_token(), userInfo.getIsSubscribe() == 0 ? 1 : 0, price), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                if (null != dialog && dialog.isShowing()) dialog.dismiss();
                tvRight.setText(userInfo.getIsSubscribe() == 0 ? "关闭预定" : "开启预定");
                showShortToast(result.getString("msg"));
                userInfo.setIsSubscribe(userInfo.getIsSubscribe() == 0 ? 1 : 0);
                BaseApplication.getInstance().saveUserInfo(userInfo);
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    public View getTabView(int position) {
        View view = LayoutInflater.from(this).inflate(R.layout.book_seat_tab_layout, null);
        TextView monthDayTv = (TextView) view.findViewById(R.id.month_day_tv);
        TextView weekDayTv = (TextView) view.findViewById(R.id.week_day_tv);
        monthDayTv.setText(calendarList.get(position).get(Calendar.DAY_OF_MONTH) + "");
        weekDayTv.setText(AppConfig.getWeekStr(calendarList.get(position).get(Calendar.DAY_OF_WEEK) - 1));
        return view;
    }

    @OnClick({R.id.iv_close, R.id.tv_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                finish();
                break;
            case R.id.tv_right:
                if (userInfo.getIsSubscribe() == 0) {
                    showInputPriceDialog();
                } else {
                    new com.wbx.merchant.widget.iosdialog.AlertDialog(mContext).builder()
                            .setTitle("提示")
                            .setMsg("确认关闭预定？")
                            .setNegativeButton("再想想", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                }
                            })
                            .setPositiveButton("确定", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    openOrCloseBook("0");
                                }
                            }).show();

                }
                break;
        }
    }

    class MyFragmentStateAdapter extends FragmentStatePagerAdapter {

        public MyFragmentStateAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            BookSeatOrderFragment bookSeatOrderFragment = BookSeatOrderFragment.newInstance(calendarList.get(position).getTimeInMillis() / 1000L);
            mFragmentList.add(bookSeatOrderFragment);
            return bookSeatOrderFragment;
        }

        @Override
        public int getCount() {
            return mTitles.length;
        }
    }
}
