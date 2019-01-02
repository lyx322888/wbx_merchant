package com.wbx.merchant.activity;

import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.wbx.merchant.R;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.utils.FormatUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by wushenghui on 2017/12/12.
 */

public class AddSecKillActivity extends BaseActivity {
    @Bind(R.id.seckill_start_time_tv)
    TextView startTimeTv;
    @Bind(R.id.seckill_end_time_tv)
    TextView endTimeTv;
    @Bind(R.id.limitations_num_edit)
    EditText limitationsEdit;

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_seckill;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {

    }

    @Override
    public void fillData() {

    }

    @Override
    public void setListener() {

    }

    @OnClick({R.id.settings_start_time_layout, R.id.settings_end_time_layout, R.id.next_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.next_btn:
                int startTime = 0;
                int endTime = 0;
                try {
                    startTime = Integer.parseInt(FormatUtil.mydateToStamp2(startTimeTv.getText().toString(), "yyyy-MM-dd HH:mm"));
                    endTime = Integer.parseInt(FormatUtil.mydateToStamp2(endTimeTv.getText().toString(), "yyyy-MM-dd HH:mm"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (startTime > endTime) {
                    showShortToast("开始时间不能大于结束时间");
                    return;
                }

                if (TextUtils.isEmpty(startTimeTv.getText().toString())) {
                    showShortToast("请选择活动开始时间");
                    return;
                }
                if (TextUtils.isEmpty(endTimeTv.getText().toString())) {
                    showShortToast("请选择活动结束时间");
                    return;
                }
                if (TextUtils.isEmpty(limitationsEdit.getText().toString())) {
                    showShortToast("请输入活动限购数量");
                    return;
                }
                Intent intent = new Intent(mContext, SeckillChooseGoodsActivity.class);
                intent.putExtra("seckill_start_time", startTimeTv.getText().toString());
                intent.putExtra("seckill_end_time", endTimeTv.getText().toString());
                intent.putExtra("limitations_num", limitationsEdit.getText().toString());
                startActivityForResult(intent, 1000);
                break;
            case R.id.settings_start_time_layout:
                Calendar instance = Calendar.getInstance();
                instance.add(Calendar.YEAR, 10);
                TimePickerView pvTime = new TimePickerView.Builder(mContext, new TimePickerView.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {//选中事件回调
                        startTimeTv.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date));
                    }
                })
                        //年月日时分秒 的显示与否，不设置则默认全部显示
                        .setType(new boolean[]{true, true, true, true, true, false})
                        .setLabel("年", "月", "日", "时", "分", "")
                        .isCenterLabel(false)
                        .setDividerColor(Color.GRAY)
                        .setContentSize(17)
                        .setRangDate(Calendar.getInstance(), instance)
                        .setDate(Calendar.getInstance())
                        .setBackgroundId(0x00FFFFFF) //设置外部遮罩颜色
                        .setDecorView(null)
                        .build();
                pvTime.show();
                break;
            case R.id.settings_end_time_layout:
                Calendar instanceEnd = Calendar.getInstance();
                instanceEnd.add(Calendar.YEAR, 10);
                TimePickerView pvTimeEnd = new TimePickerView.Builder(mContext, new TimePickerView.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {//选中事件回调
                        endTimeTv.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date));
                    }
                })
                        //年月日时分秒 的显示与否，不设置则默认全部显示
                        .setType(new boolean[]{true, true, true, true, true, false})
                        .setLabel("年", "月", "日", "时", "分", "")
                        .isCenterLabel(false)
                        .setDividerColor(Color.GRAY)
                        .setContentSize(17)
                        .setRangDate(Calendar.getInstance(), instanceEnd)
                        .setDate(Calendar.getInstance())
                        .setBackgroundId(0x00FFFFFF) //设置外部遮罩颜色
                        .setDecorView(null)
                        .build();
                pvTimeEnd.show();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == RESULT_OK) {
            finish();
        }
    }
}
