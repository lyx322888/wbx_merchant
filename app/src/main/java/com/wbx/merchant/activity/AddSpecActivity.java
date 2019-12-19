package com.wbx.merchant.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.wbx.merchant.R;
import com.wbx.merchant.adapter.SpecAdapter;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.base.BaseAdapter;
import com.wbx.merchant.bean.SpecInfo;
import com.wbx.merchant.utils.ToastUitl;
import com.wbx.merchant.widget.iosdialog.AlertDialog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by wushenghui on 2017/7/21.
 * 添加多规格
 */
public class AddSpecActivity extends BaseActivity {
    @Bind(R.id.spec_recycler_view)
    RecyclerView mRecyclerView;
    private List<SpecInfo> specInfoList = new ArrayList<>();
    private SpecAdapter mAdapter;
    private boolean isInventory;
    private boolean isSales;
    private int selectPosition;
    private List<SpecInfo> specList;
    private boolean isShop;
    private boolean isFoodStreet;

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_spec;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        isInventory = getIntent().getBooleanExtra("isInventory", false);
        isSales = getIntent().getBooleanExtra("isSales", false);
        isShop = getIntent().getBooleanExtra("isShop", false);
        isFoodStreet = getIntent().getBooleanExtra("isFoodStreet", false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new SpecAdapter(specInfoList, mContext);
        mAdapter.setShowing(isInventory, isSales, isShop, isFoodStreet);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void fillData() {
        specList = (List<SpecInfo>) getIntent().getSerializableExtra("specList");
        if (null != specList) {
            specInfoList.addAll(specList);
            mAdapter.notifyDataSetChanged();
        } else {
            addSpec();
        }
    }

    @Override
    public void setListener() {
        mAdapter.setOnItemClickListener(R.id.delete_im, new BaseAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(View view, final int position) {
                if (specInfoList.size() == 1) {
                    showShortToast("至少存在一种规格！");
                    return;
                }
                selectPosition = position;
                new AlertDialog(mContext).builder()
                        .setTitle("提示")
                        .setMsg("删除此规格？")
                        .setNegativeButton("再想想", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                            }
                        })
                        .setPositiveButton("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                specInfoList.remove(selectPosition);
                                mAdapter.setListData(specInfoList);
                            }
                        }).show();
            }
        });
    }

    private void addSpec() {
        SpecInfo specInfo = new SpecInfo();
        specInfoList.add( 0,specInfo);
        mAdapter.notifyDataSetChanged();
        mRecyclerView.scrollToPosition(0);
    }

    @OnClick({R.id.add_new_spec_layout, R.id.save_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_new_spec_layout:
                addSpec();
                break;
            case R.id.save_btn:
                save();
                break;
        }
    }

    private void save() {
        if (canSave()) {
            Intent intent = new Intent();
            Collections.reverse(specInfoList);
            intent.putExtra("specList", (Serializable)specInfoList);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    private boolean canSave() {
        for (SpecInfo specInfo : specInfoList) {
            if (TextUtils.isEmpty(specInfo.getAttr_name()) || specInfo.getPrice() == 0
                    || (isSales && specInfo.getSales_promotion_price() == 0)) {
                ToastUitl.showShort("请输入完整的规格信息");
                return false;
            }
        }
        return true;
    }
}
