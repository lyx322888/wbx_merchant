package com.wbx.merchant.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.wbx.merchant.R;
import com.wbx.merchant.activity.MaterialCenterActivity;
import com.wbx.merchant.adapter.MaterialCenterAdapter;
import com.wbx.merchant.base.BaseAdapter;
import com.wbx.merchant.base.BaseFragment;
import com.wbx.merchant.bean.MaterialInfoBean;
import com.wbx.merchant.utils.DisplayUtil;
import com.wbx.merchant.widget.decoration.GridDivider;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class MaterialCenterFragment extends BaseFragment {
    @Bind(R.id.et_search)
    EditText etSearch;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    private MaterialCenterAdapter adapter;
    private int position;
    private List<MaterialInfoBean.ProductBean> lstProduct = new ArrayList<>();
    private List<MaterialInfoBean.ProductBean> lstSearchProduct = new ArrayList<>();
    private MaterialCenterActivity activity;

    public static MaterialCenterFragment newInstance(int position) {
        MaterialCenterFragment fragment = new MaterialCenterFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        fragment.setArguments(bundle);
        return fragment;
    }

    public MaterialCenterFragment() {
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_material_center;
    }

    @Override
    public void initPresenter() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                lstSearchProduct.clear();
                if (TextUtils.isEmpty(charSequence)) {
                    adapter.setData(lstProduct);
                } else {
                    for (MaterialInfoBean.ProductBean productInfo : lstProduct) {
                        if (productInfo.getName().contains(charSequence.toString())) {
                            lstSearchProduct.add(productInfo);
                        }
                    }
                    adapter.setData(lstSearchProduct);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    @Override
    protected void initView() {
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        adapter = new MaterialCenterAdapter(lstProduct, getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new GridDivider(DisplayUtil.dp2Px(getContext(), 20), Color.WHITE));
        adapter.setOnItemClickListener(R.id.root_view, new BaseAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(View view, int position) {
                MaterialInfoBean.ProductBean productInfo;
                if (lstSearchProduct.size() != 0) {
                    productInfo = lstSearchProduct.get(position);
                } else {
                    productInfo = lstProduct.get(position);
                }
                productInfo.setIsCheck(!productInfo.getIsCheck());
                adapter.notifyDataSetChanged();
                if (productInfo.getIsCheck()) {
                    boolean hasContain = false;
                    for (MaterialInfoBean.ProductBean productBean : activity.getCheckedMaterial()) {
                        if (productInfo.getName().equals(productBean.getName())) {
                            hasContain = true;
                            break;
                        }
                    }
                    if (!hasContain) {
                        activity.getCheckedMaterial().add(productInfo);
                    }
                } else {
                    activity.getCheckedMaterial().remove(productInfo);
                }
            }
        });
    }

    @Override
    protected void fillData() {
        position = getArguments().getInt("position");
        activity = (MaterialCenterActivity) getActivity();
        List<MaterialInfoBean> lstAllMaterial = activity.getAllMaterial();
        if (lstAllMaterial != null && lstAllMaterial.size() >= position) {
            lstProduct = lstAllMaterial.get(position).getProduct();
            adapter.setData(lstProduct);
        }
    }

    @Override
    protected void bindEven() {

    }
}
