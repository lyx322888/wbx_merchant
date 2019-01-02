package com.wbx.merchant.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wbx.merchant.R;
import com.wbx.merchant.adapter.AnalyzeAdapter;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.baseapp.AppConfig;
import com.wbx.merchant.bean.OrderInfo;
import com.wbx.merchant.utils.TimeUtil;
import com.wbx.merchant.widget.refresh.BaseRefreshListener;
import com.wbx.merchant.widget.refresh.PullToRefreshLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import cn.carbs.android.segmentcontrolview.library.SegmentControlView;
import lecho.lib.hellocharts.formatter.LineChartValueFormatter;
import lecho.lib.hellocharts.formatter.SimpleLineChartValueFormatter;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by wushenghui on 2017/9/28.
 * 经营分析
 */

public class RunAnalyzeActivity extends BaseActivity implements BaseRefreshListener {
    @Bind(R.id.tab_layout)
    TabLayout mTabLayout;
    @Bind(R.id.line_chart)
    LineChartView lineChart;
    @Bind(R.id.segment_view)
    SegmentControlView segmentControlView;
    @Bind(R.id.analyze_recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.refresh_layout)
    PullToRefreshLayout mRefreshLayout;
    private AnalyzeAdapter mAdapter;
    String[] date;
    Integer[] score;//图表的数据
    Integer[] score2;//图表的数据
    private List<PointValue> mPointValues = new ArrayList<PointValue>();
    private List<PointValue> mPointValues1 = new ArrayList<PointValue>();
    private List<AxisValue> mAxisXValues = new ArrayList<AxisValue>();
    private String[] segmentStr = {"收入详情", "退款详情"};
    private HashMap<String, Object> mParams = new HashMap<>();
    private List<OrderInfo> orderInfoList = new ArrayList<>();
    private int pageSize = AppConfig.pageSize;
    private boolean canLoadMore = true;
    @Bind(R.id.income_money_tv)
    TextView incomeMoneyTv;
    @Bind(R.id.refund_money_tv)
    TextView refundMoneyTv;
    private boolean isRefund = false;//是否是退款详情

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, RunAnalyzeActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_run_analyze;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        segmentControlView.setTexts(segmentStr);
        mParams.put("sum_day", 7);
        mParams.put("status", 1);
        mParams.put("order_id", 0);
        ArrayList<String> pastDateList = TimeUtil.getPastDateList(7);
        Collections.reverse(pastDateList);
        date = pastDateList.toArray(new String[pastDateList.size()]);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mTabLayout.addTab(mTabLayout.newTab().setText("7天"));
        mTabLayout.addTab(mTabLayout.newTab().setText("30天"));

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new AnalyzeAdapter(orderInfoList, mContext);
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public void fillData() {
        mParams.put("sj_login_token", userInfo.getSj_login_token());
        mParams.put("page", AppConfig.pageNum);
        mParams.put("num", pageSize);
        new MyHttp().doPost(Api.getDefault().runAnalyze(mParams), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                if (null != mRefreshLayout) {
                    mRefreshLayout.finishRefresh();
                    mRefreshLayout.finishLoadMore();
                }
                List<OrderInfo> dataList = JSONArray.parseArray(result.getJSONObject("data").getString("list"), OrderInfo.class);
                if ((int) mParams.get("order_id") == 0) {
                    orderInfoList.clear();
                }
                mAdapter.setIsRefund(isRefund);
                orderInfoList.addAll(dataList);
                if (dataList.size() < AppConfig.pageSize) {
                    //说明下次已经没有数据了
                    canLoadMore = false;
                }
                List<Integer> inComeDataList = JSONArray.parseArray(result.getJSONObject("data").getString("need_pay_arr"), Integer.class);
                score = inComeDataList.toArray(new Integer[inComeDataList.size()]);
                List<Integer> refundDataList = JSONArray.parseArray(result.getJSONObject("data").getString("need_refund_pay_arr"), Integer.class);
                score2 = refundDataList.toArray(new Integer[refundDataList.size()]);
                mAdapter.notifyDataSetChanged();
                incomeMoneyTv.setText(String.format("收入%.2f元", result.getJSONObject("data").getIntValue("sum_need_pay") / 100.00));
                refundMoneyTv.setText(String.format("退款%.2f元", result.getJSONObject("data").getIntValue("sum_refund_need_pay") / 100.00));
                getAxisXLables();//获取x轴的标注
                getAxisPoints();//获取坐标点
                getAxisPoints1();
                initLineChart();//初始化

            }

            @Override
            public void onError(int code) {

            }
        });
    }

    @Override
    public void setListener() {
        mRefreshLayout.setRefreshListener(this);
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mPointValues.clear();
                mAxisXValues.clear();
                mPointValues1.clear();
                switch (tab.getPosition()) {
                    case 0:
                        orderInfoList.clear();
                        mParams.put("sum_day", 7);
                        ArrayList<String> pastDateList = TimeUtil.getPastDateList(7);
                        Collections.reverse(pastDateList);
                        date = pastDateList.toArray(new String[pastDateList.size()]);
                        fillData();
                        break;
                    case 1:
                        mParams.put("sum_day", 30);
                        ArrayList<String> pastDateList30 = TimeUtil.getPastDateList(30);
                        Collections.reverse(pastDateList30);
                        date = pastDateList30.toArray(new String[pastDateList30.size()]);
                        orderInfoList.clear();
                        fillData();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        segmentControlView.setOnSegmentChangedListener(new SegmentControlView.OnSegmentChangedListener() {
            @Override
            public void onSegmentChanged(int newSelectedIndex) {
                switch (newSelectedIndex) {
                    case 0:
                        isRefund = false;
                        mParams.put("order_id", 0);
                        mParams.put("status", 1);
                        break;
                    case 1:
                        isRefund = true;
                        mParams.put("order_id", 0);
                        mParams.put("status", 2);
                        break;
                }
                fillData();
            }
        });
    }


    /**
     * 初始化LineChart的一些设置
     */
    private void initLineChart() {
        Line line = new Line(mPointValues).setColor(Color.parseColor("#06c1ae"));  //折线的颜色
        line.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.SQUARE）
        line.setCubic(false);//曲线是否平滑
        line.setStrokeWidth(1);//线条的粗细，默认是3
        line.setFilled(false);//是否填充曲线的面积
        line.setHasLabels(true);//曲线的数据坐标是否加上备注
        LineChartValueFormatter chartValueFormatter = new SimpleLineChartValueFormatter(2);
        line.setFormatter(chartValueFormatter);//显示小数点
//		line.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        line.setHasLines(true);//是否用直线显示。如果为false 则没有曲线只有点显示
        line.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示
        Line line2 = new Line(mPointValues1).setColor(Color.parseColor("#EEA468"));  //折线的颜色
        line2.setFormatter(chartValueFormatter);//显示小数点
        List<Line> lines = new ArrayList<Line>();
        LineChartData data = new LineChartData();
        data.setLines(lines);

        //坐标轴
        Axis axisX = new Axis(); //X轴
        axisX.setHasTiltedLabels(true);  //X轴下面坐标轴字体是斜的显示还是直的，true是斜的显示
//	    axisX.setTextColor(Color.WHITE);  //设置字体颜色
        axisX.setTextColor(Color.parseColor("#D6D6D9"));//灰色

//	    axisX.setName("收入");  //表格名称
        axisX.setTextSize(11);//设置字体大小
        axisX.setMaxLabelChars(7); //最多几个X轴坐标，意思就是你的缩放让X轴上数据的个数7<=x<=mAxisValues.length
        axisX.setValues(mAxisXValues);  //填充X轴的坐标名称
        data.setAxisXBottom(axisX); //x 轴在底部
//	    data.setAxisXTop(axisX);  //x 轴在顶部
        axisX.setHasLines(true); //x 轴分割线


        line2.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.SQUARE）
        line2.setCubic(false);//曲线是否平滑
        line2.setStrokeWidth(1);//线条的粗细，默认是3
        line2.setFilled(false);//是否填充曲线的面积
        line2.setHasLabels(true);//曲线的数据坐标是否加上备注
//		line.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        line2.setHasLines(true);//是否用直线显示。如果为false 则没有曲线只有点显示
        line2.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示
        lines.add(line);
        lines.add(line2);
        Axis axisY = new Axis();  //Y轴
        axisY.setName("");//y轴标注
        axisY.setTextSize(11);//设置字体大小
        data.setAxisYLeft(axisY);  //Y轴设置在左边
        //data.setAxisYRight(axisY);  //y轴设置在右边
        //设置行为属性，支持缩放、滑动以及平移
        lineChart.setInteractive(true);
        lineChart.setZoomType(ZoomType.HORIZONTAL);  //缩放类型，水平
        lineChart.setMaxZoom((float) 3);//缩放比例
        lineChart.setLineChartData(data);
        lineChart.setVisibility(View.VISIBLE);
        /**注：下面的7，10只是代表一个数字去类比而已
         * 尼玛搞的老子好辛苦！！！见（http://forum.xda-developers.com/tools/programming/library-hellocharts-charting-library-t2904456/page2）;
         * 下面几句可以设置X轴数据的显示个数（x轴0-7个数据），当数据点个数小于（29）的时候，缩小到极致hellochart默认的是所有显示。当数据点个数大于（29）的时候，
         * 若不设置axisX.setMaxLabelChars(int count)这句话,则会自动适配X轴所能显示的尽量合适的数据个数。
         * 若设置axisX.setMaxLabelChars(int count)这句话,
         * 33个数据点测试，若 axisX.setMaxLabelChars(10);里面的10大于v.right= 7; 里面的7，则
         刚开始X轴显示7条数据，然后缩放的时候X轴的个数会保证大于7小于10
         若小于v.right= 7;中的7,反正我感觉是这两句都好像失效了的样子 - -!
         * 并且Y轴是根据数据的大小自动设置Y轴上限
         * 若这儿不设置 v.right= 7; 这句话，则图表刚开始就会尽可能的显示所有数据，交互性太差
         */
        Viewport v = new Viewport(lineChart.getMaximumViewport());
        v.left = 0;
        v.right = 7;
        lineChart.setCurrentViewport(v);


    }


    /**
     * X 轴的显示
     */
    private void getAxisXLables() {
        mAxisXValues.clear();
        for (int i = 0; i < date.length; i++) {
            mAxisXValues.add(new AxisValue(i).setLabel(date[i]));
        }
    }

    /**
     * 图表的每个点的显示
     */
    private void getAxisPoints() {
        mPointValues.clear();
        for (int i = 0; i < score.length; i++) {
            mPointValues.add(new PointValue(i, score[i] / 100.00f));
        }
    }

    /**
     * 图表的每个点的显示
     */
    private void getAxisPoints1() {
        mPointValues1.clear();
        for (int i = 0; i < score2.length; i++) {
            mPointValues1.add(new PointValue(i, score2[i] / 100.00f));
        }
    }

    @Override
    public void refresh() {
        mParams.put("order_id", 0);
        canLoadMore = true;
        fillData();
    }

    @Override
    public void loadMore() {
        if (!canLoadMore) {
            mRefreshLayout.finishLoadMore();
            showShortToast("没有更多数据了");
            return;
        }
        mParams.put("order_id", orderInfoList.get(orderInfoList.size() - 1).getOrder_id());
        fillData();
    }
}
