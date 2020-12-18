package com.wbx.merchant.activity;

import android.app.Activity;
import android.content.Intent;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import com.flyco.tablayout.SlidingTabLayout;
import com.wbx.merchant.R;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.fragment.ItemBusinessDistrictFragment;
import com.wbx.merchant.fragment.ItemCsxqFragment;

import java.util.ArrayList;

import butterknife.Bind;

//数据统计
public class ReportFormsSetMealActivity extends BaseActivity {

    @Bind(R.id.lab_layout)
    SlidingTabLayout labLayout;
    @Bind(R.id.order_view_pager)
    ViewPager orderViewPager;

    //跳转
    public static void actionStart(Activity activity, String shop_set_meal_id) {
        Intent intent = new Intent(activity, ReportFormsSetMealActivity.class);
        intent.putExtra("shop_set_meal_id", shop_set_meal_id);
        activity.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_report_forms_set_meal;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        String shop_set_meal_id = getIntent().getStringExtra("shop_set_meal_id");
        String[] mTitles = new String[]{"出售详情", "使用详情"};
        ArrayList<Fragment> arrayList = new ArrayList<>();
        arrayList.add(ItemCsxqFragment.newInstance(0,shop_set_meal_id));
        arrayList.add(ItemCsxqFragment.newInstance(1,shop_set_meal_id));
        labLayout.setViewPager(orderViewPager,mTitles, (FragmentActivity) mActivity,arrayList);
    }

    @Override
    public void fillData() {

    }

    @Override
    public void setListener() {

    }
}