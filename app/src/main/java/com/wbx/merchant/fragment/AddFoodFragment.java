package com.wbx.merchant.fragment;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wbx.merchant.R;
import com.wbx.merchant.activity.AddFoodActivity;
import com.wbx.merchant.adapter.AddFoodAdapter;
import com.wbx.merchant.base.BaseFragment;
import com.wbx.merchant.bean.ScanOrderGoodsBean;

import java.util.List;

import butterknife.Bind;

public class AddFoodFragment extends BaseFragment {
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    private AddFoodAdapter adapter;
    private int position;

    public AddFoodFragment() {
    }

    public static AddFoodFragment newInstance(int position) {
        AddFoodFragment fragment = new AddFoodFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_add_food;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    protected void initView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new AddFoodAdapter(getActivity());
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void fillData() {
        position = getArguments().getInt("position");
        List<ScanOrderGoodsBean> allGoods = ((AddFoodActivity) getActivity()).getAllGoods();
        if (allGoods != null && allGoods.size() >= position) {
            adapter.update(allGoods.get(position).getGoods());
        }
    }

    @Override
    protected void bindEven() {
    }
}
