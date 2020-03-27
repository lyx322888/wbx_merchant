package com.wbx.merchant.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.wbx.merchant.R;
import com.wbx.merchant.adapter.MaterailaCenterDrawerAdapter;
import com.wbx.merchant.adapter.ProductnewAdapter;
import com.wbx.merchant.adapter.viewpageadapter.MaterialCenterFragmentStateAdapter;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.base.BaseAdapter;
import com.wbx.merchant.bean.CateInfo;
import com.wbx.merchant.bean.MaterialInfoBean;
import com.wbx.merchant.widget.LoadingDialog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import rx.Observable;

/**
 * Created by Administrator on 2017/11/6 0006.
 */

public class MaterialCenterActivity extends BaseActivity {
    @Bind(R.id.tab_layout)
    TabLayout tabLayout;
    @Bind(R.id.view_pager)
    ViewPager viewPager;
    @Bind(R.id.btn_next)
    Button btnNext;
    @Bind(R.id.root_view)
    LinearLayout rootView;
    @Bind(R.id.rv_all_classify)
    RecyclerView rvAllClassify;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    private PopupWindow popupWindow;
    /**
     * 这个id是 商品分类id
     */
    private int firstCateId;
    private List<MaterialInfoBean> lstAllMaterial = new ArrayList<>();
    private MaterialCenterFragmentStateAdapter mPagerAdapter;
    private List<MaterialInfoBean.ProductBean> lstCheckMaterial = new ArrayList<>();
    private ProductnewAdapter productnewAdapter;
    private MaterailaCenterDrawerAdapter drawerAdapter;

    public static void actionStart(Context context, int cateId) {
        Intent intent = new Intent(context, MaterialCenterActivity.class);
        intent.putExtra("cateId", cateId);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.acitivity_material_center;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void initView() {
        rvAllClassify.setLayoutManager(new LinearLayoutManager(this));
        drawerAdapter = new MaterailaCenterDrawerAdapter(lstAllMaterial);
        rvAllClassify.setAdapter(drawerAdapter);
    }


    @Override
    public void fillData() {
        //这个id是 商品分类id  不能和用户自己添加分类的id混合
        firstCateId = getIntent().getIntExtra("cateId", 0);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        LoadingDialog.showDialogForLoading(this);
        new MyHttp().doPost(Api.getDefault().getMaterialList(userInfo.getSj_login_token(), firstCateId),
                new HttpListener() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        lstAllMaterial.clear();
                        lstAllMaterial.addAll(JSONObject.parseArray(result.getString("data"), MaterialInfoBean.class));
                        updateUI();
                    }

                    @Override
                    public void onError(int code) {

                    }
                });
    }

    private void updateUI() {
        if (lstAllMaterial == null || lstAllMaterial.size() == 0) {
            return;
        }
        List<String> lstTitle = new ArrayList<>();
        for (MaterialInfoBean lstAllGood : lstAllMaterial) {
            lstTitle.add(lstAllGood.getName());
        }
        mPagerAdapter = new MaterialCenterFragmentStateAdapter(getSupportFragmentManager(), lstTitle);
        viewPager.setAdapter(mPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        lstAllMaterial.get(0).setCheck(true);
        drawerAdapter.notifyDataSetChanged();
    }

    @Override
    public void setListener() {
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                for (int i = 0; i < lstAllMaterial.size(); i++) {
                    if (lstAllMaterial.get(i).isCheck()) {
                        viewPager.setCurrentItem(i);
                        break;
                    }
                }
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < lstAllMaterial.size(); i++) {
                    if (i == position) {
                        lstAllMaterial.get(i).setCheck(true);
                    } else {
                        lstAllMaterial.get(i).setCheck(false);
                    }
                }
                drawerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        drawerAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.rl_classify:
                        for (int i = 0; i < lstAllMaterial.size(); i++) {
                            if (i == position) {
                                lstAllMaterial.get(i).setCheck(true);
                            } else {
                                lstAllMaterial.get(i).setCheck(false);
                            }
                        }
                        drawerAdapter.notifyDataSetChanged();
                        drawerLayout.closeDrawers();
                        break;
                }
            }
        });
    }

    public List<MaterialInfoBean> getAllMaterial() {
        return lstAllMaterial;
    }

    public List<MaterialInfoBean.ProductBean> getCheckedMaterial() {
        return lstCheckMaterial;
    }

    //弹出pop显示分类列表
    int cateId = -1;

    private void showCatePop(final List<CateInfo> dataList) {
        View inflate = getLayoutInflater().inflate(R.layout.pop_product_type, null);
        popupWindow = new PopupWindow();
        popupWindow.setAnimationStyle(R.style.ActionSheetDialogAnimation);
        popupWindow.setContentView(inflate);
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(findViewById(R.id.root_view), Gravity.BOTTOM | Gravity
                .CENTER_HORIZONTAL, 0, 0);
        //取消
        Button cancel = inflate.findViewById(R.id.abolish_item_product);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        //确定
        Button ensure = inflate.findViewById(R.id.confirm_item_product);
        ensure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cateId == -1) {
                    showShortToast("请选择选中商品所属分类");
                    return;
                }
                Intent intent = new Intent(mContext, ProductReleaseActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("checkedList", (Serializable) lstCheckMaterial);
                bundle.putInt("cateId", cateId);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });
        if (null == dataList || 0 != dataList.size()) {
            inflate.findViewById(R.id.new_item_product).setVisibility(View.GONE);
        } else {
            inflate.findViewById(R.id.new_item_product).setVisibility(View.VISIBLE);
        }
        //新建分类
        inflate.findViewById(R.id.new_item_product).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                startActivity(new Intent().setClass(mContext, GoodsClassifyActivity.class));
            }
        });
        //管理分类
        inflate.findViewById(R.id.classify_item_product).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                startActivity(new Intent().setClass(mContext, GoodsClassifyActivity.class));
            }
        });
        final RecyclerView recyclerView = inflate.findViewById(R.id
                .recycler_item_new);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        productnewAdapter = new ProductnewAdapter(dataList, mContext);
        recyclerView.setAdapter(productnewAdapter);
        productnewAdapter.setOnItemClickListener(R.id.root_view, new BaseAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(View view, int position) {
                //先还原 == 全部设置未选中
                for (CateInfo cateInfo : dataList) {
                    cateInfo.setSelect(false);
                }
                //再根据 选中的那个
                CateInfo cateInfo = dataList.get(position);
                //设置选中
                cateInfo.setSelect(!cateInfo.isSelect());
                //如果是选中 把选中的cateId赋值上去
                if (cateInfo.isSelect()) {
                    cateId = cateInfo.getCate_id();
                } else { //否则 为反选 把id置为-1 为了点击下一步的时候判断 用户是否有选择分类
                    cateId = -1;
                }
                productnewAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * 获取用户添加的分类列表
     */
    private void getCateInfo() {
        LoadingDialog.showDialogForLoading(MaterialCenterActivity.this, "获取分类中...", true);
        Observable<JSONObject> cateList = Api.getDefault().getCateList(userInfo.getSj_login_token());
        new MyHttp().doPost(cateList, new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                List<CateInfo> dataList = JSONArray.parseArray(result.getString("data"), CateInfo.class);
                cateId = -1;
                showCatePop(dataList);
            }

            @Override
            public void onError(int code) {
                List<CateInfo> cateInfoList = new ArrayList<>();
                cateId = -1;
                showCatePop(cateInfoList);
            }
        });
    }

    @OnClick({R.id.tv_see_all, R.id.btn_next, R.id.iv_close_drawer})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_see_all:
                drawerLayout.openDrawer(Gravity.END);
                break;
            case R.id.iv_close_drawer:
                drawerLayout.closeDrawers();
            case R.id.btn_next:
                next();
                break;
        }
    }

    private void next() {
        if (lstCheckMaterial.size() == 0) {
            showShortToast("请先选择所需模版商品");
            return;
        }
        getCateInfo();
    }
}
