package com.wbx.merchant.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wbx.merchant.R;
import com.wbx.merchant.adapter.ProductnewAdapter;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.base.BaseAdapter;
import com.wbx.merchant.bean.CateInfo;
import com.wbx.merchant.fragment.GoodsManagerFragment;
import com.wbx.merchant.widget.LoadingDialog;
import com.wbx.merchant.widget.iosdialog.AlertDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import rx.Observable;

/**
 * Created by wushenghui on 2017/6/20.
 * 商品管理
 */
public class GoodsManagerActivity extends BaseActivity {
    public static final int REQUEST_ADD_GOODS = 1000;
    public static final int REQUEST_UPDATE_GOODS = 1001;
    @Bind(R.id.lab_layout)
    TabLayout mTabLayout;
    @Bind(R.id.view_pager)
    ViewPager mOrderViewPager;
    private String[] mTitles = new String[]{"已上架", "已下架"};
    private GoodsFragmentStateAdapter mPagerAdapter;
    private List<GoodsManagerFragment> mFragment = new ArrayList<>();
    private boolean isEdit;
    @Bind(R.id.do_edit_layout)
    LinearLayout doEditLayout;
    @Bind(R.id.edited_layout)
    LinearLayout editedLayout;
    @Bind(R.id.rl_right)
    public RelativeLayout rlRight;
    @Bind(R.id.tv_all_cb)
    TextView selectAllCb;
    @Bind(R.id.sold_out_tv)
    TextView soldOutTv;
    @Bind(R.id.tv_menu)
    TextView tvMenu;
    private int cateId;

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, GoodsManagerActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_goods_manager;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mPagerAdapter = new GoodsFragmentStateAdapter(getSupportFragmentManager(), mTitles);
        mOrderViewPager.setAdapter(mPagerAdapter);
        mTabLayout.setupWithViewPager(mOrderViewPager);
    }

    @Override
    public void fillData() {
    }

    @Override
    public void setListener() {
        mOrderViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                isEdit = false;
                selectAllCb.setSelected(false);
                if (mFragment.size() >= mOrderViewPager.getCurrentItem() + 1) {
                    mFragment.get(mOrderViewPager.getCurrentItem()).cancelSelectAll();
                    mFragment.get(mOrderViewPager.getCurrentItem()).batchManager(false);
                }
//                editedLayout.startAnimation(hidAnim());
                editedLayout.setVisibility(View.GONE);
                tvMenu.setText("添加分类");
//                doEditLayout.startAnimation(showAnim());
                doEditLayout.setVisibility(View.VISIBLE);
                if (position == 0) {
                    soldOutTv.setText("下架");
                    Drawable leftDrawable = getResources().getDrawable(R.drawable.ic_shelves);
                    leftDrawable.setBounds(0, 0, leftDrawable.getMinimumWidth(), leftDrawable.getMinimumHeight());
                    soldOutTv.setCompoundDrawables(leftDrawable, null, null, null);

                } else {
                    soldOutTv.setText("上架");
                    Drawable leftDrawable = getResources().getDrawable(R.drawable.ic_upgoods);
                    leftDrawable.setBounds(0, 0, leftDrawable.getMinimumWidth(), leftDrawable.getMinimumHeight());
                    soldOutTv.setCompoundDrawables(leftDrawable, null, null, null);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @OnClick({R.id.publish_goods_btn, R.id.batch_manager, R.id.rl_right, R.id.rl_all_check, R.id.rl_sold_out, R.id.rl_goods_delete, R.id.rl_classify})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.publish_goods_btn:
                startActivityForResult(new Intent(mContext, ReleaseActivity.class), REQUEST_ADD_GOODS);
                break;
            case R.id.batch_manager:
                if (mFragment.size() < mOrderViewPager.getCurrentItem() + 1) {
                    return;
                }
                isEdit = !isEdit;
                mFragment.get(mOrderViewPager.getCurrentItem()).batchManager(isEdit);
                doEditLayout.startAnimation(hidAnim());
                doEditLayout.setVisibility(View.GONE);
                editedLayout.startAnimation(showAnim());
                editedLayout.setVisibility(View.VISIBLE);
                tvMenu.setText("取消");
                break;
            case R.id.rl_right:
                if (tvMenu.getText().toString().trim().equals("添加分类")) {
                    startActivity(new Intent(mContext, GoodsClassifyActivity.class));
                } else {
                    selectAllCb.setSelected(false);
                    mFragment.get(mOrderViewPager.getCurrentItem()).cancelSelectAll();
                    isEdit = !isEdit;
                    mFragment.get(mOrderViewPager.getCurrentItem()).batchManager(isEdit);
                    editedLayout.startAnimation(hidAnim());
                    editedLayout.setVisibility(View.GONE);
                    tvMenu.setText("添加分类");
                    doEditLayout.startAnimation(showAnim());
                    doEditLayout.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.rl_all_check:
                selectAllCb.setSelected(!selectAllCb.isSelected());
                if (selectAllCb.isSelected()) {
                    mFragment.get(mOrderViewPager.getCurrentItem()).setSelectAll();
                } else {
                    mFragment.get(mOrderViewPager.getCurrentItem()).cancelSelectAll();
                }
                break;
            case R.id.rl_sold_out:
                if (mOrderViewPager.getCurrentItem() == 0) {
                    new AlertDialog(mContext).builder()
                            .setTitle("提示")
                            .setMsg("确定下架？")
                            .setNegativeButton("再想想", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            })
                            .setPositiveButton("确定", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    mFragment.get(mOrderViewPager.getCurrentItem()).soldOut();
                                }
                            }).show();
                } else {
                    new AlertDialog(mContext).builder()
                            .setTitle("提示")
                            .setMsg("确定上架？")
                            .setNegativeButton("再想想", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            })
                            .setPositiveButton("确定", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    mFragment.get(mOrderViewPager.getCurrentItem()).soldIn();
                                }
                            }).show();
                }
                break;
            case R.id.rl_goods_delete:
                new AlertDialog(mContext).builder()
                        .setTitle("提示")
                        .setMsg("删除商品？")
                        .setNegativeButton("再想想", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        })
                        .setPositiveButton("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mFragment.get(mOrderViewPager.getCurrentItem()).deleteGoods();
                            }
                        }).show();

                break;
            case R.id.rl_classify:
                getCateInfo();
                break;
        }
    }

    /**
     * 获取用户添加的分类列表
     */
    private void getCateInfo() {
        LoadingDialog.showDialogForLoading(this, "获取分类中...", true);
        Observable<JSONObject> cateList = Api.getDefault().getCateList(userInfo.getSj_login_token());
        new MyHttp().doPost(cateList, new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                List<CateInfo> dataList = JSONArray.parseArray(result.getString("data"), CateInfo.class);
                showCatePop(dataList);
            }

            @Override
            public void onError(int code) {
                List<CateInfo> cateInfoList = new ArrayList<CateInfo>();
                showCatePop(cateInfoList);
            }
        });
    }

    //弹出pop显示分类列表
    private void showCatePop(final List<CateInfo> dataList) {
        View inflate = getLayoutInflater().inflate(R.layout.pop_product_type, null);
        final PopupWindow popupWindow = new PopupWindow();
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
                if (cateId == 0) {
                    showShortToast("请选择选中商品所属分类");
                    return;
                }
                mFragment.get(mOrderViewPager.getCurrentItem()).batchClassify(cateId);
                popupWindow.dismiss();
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
        final RecyclerView recyclerView = inflate.findViewById(R.id.recycler_item_new);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        final ProductnewAdapter productnewAdapter = new ProductnewAdapter(dataList, mContext);
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
                } else {
                    //否则 为反选 把id置为0 为了点击下一步的时候判断 用户是否有选择分类
                    cateId = 0;
                }
                productnewAdapter.notifyDataSetChanged();
            }
        });
    }

    public TranslateAnimation showAnim() {
        //显示动画
        TranslateAnimation mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mShowAction.setDuration(500);
        return mShowAction;
    }

    public TranslateAnimation hidAnim() {
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -1.0f);
        mHiddenAction.setDuration(500);
        return mHiddenAction;
    }

    class GoodsFragmentStateAdapter extends FragmentStatePagerAdapter {
        private String[] mTitles;

        public GoodsFragmentStateAdapter(FragmentManager fm, String[] mTitles) {
            super(fm);
            this.mTitles = mTitles;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            mFragment.add(GoodsManagerFragment.newInstance(position));
            return mFragment.get(position);
        }

        @Override
        public int getCount() {
            return mTitles.length;
        }
    }

    public void setSelectAll(boolean selectAll) {
        selectAllCb.setSelected(selectAll);
    }

    public void setOtherItemDataChange() {
        int currentItem = mOrderViewPager.getCurrentItem();
        for (int i = 0; i < mFragment.size(); i++) {
            if (i != currentItem) {
                mFragment.get(i).setDataChange();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mFragment.isEmpty()) {
            return;
        }
        if (resultCode == RESULT_OK) {
            mFragment.get(mOrderViewPager.getCurrentItem()).onActivityResult(requestCode, resultCode, data);
        }
    }
}
