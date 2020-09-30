package com.wbx.merchant.activity.jhzf;

import android.view.View;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.google.gson.Gson;
import com.wbx.merchant.R;
import com.wbx.merchant.adapter.JdDetailAdapter;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.baseapp.AppConfig;
import com.wbx.merchant.bean.JDBean;
import com.wbx.merchant.common.LoginUtil;
import com.wbx.merchant.utils.AdapterUtilsNew;
import com.wbx.merchant.widget.refresh.BaseRefreshListener;
import com.wbx.merchant.widget.refresh.PullToRefreshLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

//设备保证金返还明细
public class JingdongDetailActivity extends BaseActivity {

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.refresh_layout)
    PullToRefreshLayout refreshLayout;
    @Bind(R.id.tv_status)
    TextView tvStatus;
    @Bind(R.id.tv_startTime)
    TextView tvStartTime;
    @Bind(R.id.tv_endTime)
    TextView tvEndTime;
    private int pageNum = 1;
    private JdDetailAdapter jdDetailAdapter;
    private OptionsPickerView pvOptions;
    private String status = "SUCCESS";

    @Override
    public int getLayoutId() {
        return R.layout.activity_jingdong_detail;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        initStatus();
        jdDetailAdapter = new JdDetailAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(jdDetailAdapter);
        jdDetailAdapter.setEmptyView(R.layout.layout_empty, recyclerView);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        refreshLayout.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                pageNum = AppConfig.pageNum;
                fillData();
            }

            @Override
            public void loadMore() {
                pageNum += 1;
                fillData();
            }
        });
    }

    @Override
    public void fillData() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sj_login_token", LoginUtil.getLoginToken());
        hashMap.put("status", status);
        hashMap.put("startTime", tvStartTime.getText().toString());
        hashMap.put("endTime", tvEndTime.getText().toString());
        hashMap.put("pageNum ", pageNum);
        new MyHttp().doPost(Api.getDefault().list_order(hashMap), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                JDBean jdBean = new Gson().fromJson(result.toString(),JDBean.class);
                AdapterUtilsNew.setData(jdDetailAdapter,jdBean.getData().orderList,pageNum,10);
                refreshLayout.finishRefresh();
                refreshLayout.finishLoadMore();
            }

            @Override
            public void onError(int code) {
                refreshLayout.finishRefresh();
                refreshLayout.finishLoadMore();
            }
        });
    }

    @Override
    public void setListener() {

    }

    //刷新数据
    private void updata(){
        pageNum = AppConfig.pageNum;
        fillData();
    }

    //选择时间
    private void chooseTime(final TextView textView) {
        TimePickerView pvTime = new TimePickerView.Builder(mContext, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
                textView.setText(time);
                updata();

            }
        }).setType(new boolean[]{true, true, true, true, true, true})//年月日时分秒 的显示与否，不设置则默认全部显示
                .setLabel("", "", "", "", "", "")
                .isCenterLabel(false)
                .setTitleColor(ContextCompat.getColor(mContext, R.color.black))//标题文字颜色
                .setSubmitColor(ContextCompat.getColor(mContext, R.color.app_color))//确定按钮文字颜色
                .setCancelColor(ContextCompat.getColor(mContext, R.color.black))//取消按钮文字颜色
                .setDate(Calendar.getInstance())
                .setDecorView(null)
                .build();
        pvTime.show();
    }

    @OnClick({R.id.tv_status, R.id.tv_startTime, R.id.tv_endTime})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_status:
                if (pvOptions!=null){
                    pvOptions.show();
                }
                break;
            case R.id.tv_startTime:
                chooseTime((TextView) view);
                break;
            case R.id.tv_endTime:
                chooseTime((TextView) view);
                break;
        }
    }

    //选择商户类
    private void initStatus() {
        //数据源
        final List<String> strings = new ArrayList<>();
        strings.add("待⽀付");
        strings.add("成功");
        strings.add("已取消");
        strings.add("已退款");
        strings.add("退款中");
        strings.add("退款失败");
        pvOptions = new OptionsPickerView(new OptionsPickerView.Builder(mContext, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                tvStatus.setText("状态："+strings.get(options1));
                switch (options1){
                    case 0:
                        status = "INIT";
                        break;
                    case 1:
                        status = "SUCCESS";
                        break;
                    case 2:
                        status = "CANCEL";
                        break;
                    case 3:
                        status = "REFUND";
                        break;
                    case 4:
                        status = "REFUNDING";
                        break;
                    case 5:
                        status = "REFUNDFAIL";
                        break;
                }
                updata();
            }
        }).setSubmitText("确定")//确定按钮文字
                .setCancelText("取消")//取消按钮文字
                .setSubCalSize(18)//确定和取消文字大小
                .setTitleSize(20)//标题文字大小
                .setTitleColor(ContextCompat.getColor(mContext, R.color.black))//标题文字颜色
                .setSubmitColor(ContextCompat.getColor(mContext, R.color.app_color))//确定按钮文字颜色
                .setCancelColor(ContextCompat.getColor(mContext, R.color.black))//取消按钮文字颜色
//                .setTitleBgColor(0xFF333333)//标题背景颜色 Night mode
//                .setBgColor(0xFF000000)//滚轮背景颜色 Night mode
                .setContentTextSize(18)//滚轮文字大小
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setCyclic(false, false, false)//循环与否
                .setSelectOptions(0)  //设置默认选中项
                .setOutSideCancelable(true)//点击外部dismiss default true
        );
        pvOptions.setPicker(strings);//添加数据源


    }
}