package com.wbx.merchant.activity;

import android.app.Activity;
import android.content.Intent;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.wbx.merchant.R;
import com.wbx.merchant.adapter.SelectSubSpecAdapter;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.bean.GoodsInfo;

import java.util.ArrayList;
import java.util.Iterator;

import butterknife.Bind;
import butterknife.OnClick;

import static com.wbx.merchant.activity.SelectNatureActivity.REQUEST_SELECT_NATURE;
import static com.wbx.merchant.activity.SelectNatureAttrActivity.REQUEST_SELECT_NATURE_ATTR;

public class SelectSubSpecActivity extends BaseActivity {
    public static final int REQUEST_SELECT_SUB_SPEC = 1005;//选择商品规格项目和属性
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    private ArrayList<GoodsInfo.Nature> lstData = new ArrayList<>();
    private SelectSubSpecAdapter adapter;
    private int clickPosition;

    public static void actionStart(Activity activity, ArrayList<GoodsInfo.Nature> nature) {
        Intent intent = new Intent(activity, SelectSubSpecActivity.class);
        intent.putExtra("nature", nature);
        activity.startActivityForResult(intent, REQUEST_SELECT_SUB_SPEC);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_select_sub_spec;
    }

    @Override
    public void initPresenter() {
        ArrayList<GoodsInfo.Nature> nature = (ArrayList<GoodsInfo.Nature>) getIntent().getSerializableExtra("nature");
        if (nature != null) {
            lstData.addAll(nature);
        }
    }

    @Override
    public void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new SelectSubSpecAdapter(R.layout.item_select_sub_spec, lstData);
        recyclerView.setAdapter(adapter);
        View footView = LayoutInflater.from(this).inflate(R.layout.foot_select_sub_spec, null);
        footView.findViewById(R.id.ll_add_sub_spec).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoodsInfo.Nature nature = new GoodsInfo.Nature();
                lstData.add(nature);
                adapter.notifyDataSetChanged();
            }
        });
        adapter.addFooterView(footView);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                clickPosition = position;
                switch (view.getId()) {
                    case R.id.iv_delete:
                        lstData.remove(position);
                        adapter.notifyDataSetChanged();
                        break;
                    case R.id.ll_project:
                        SelectNatureActivity.actionStart(SelectSubSpecActivity.this);
                        break;
                    case R.id.ll_attr:
                        if (TextUtils.isEmpty(lstData.get(position).getItem_id())) {
                            Toast.makeText(SelectSubSpecActivity.this, "请先选择规格项目", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        SelectNatureAttrActivity.actionStart(SelectSubSpecActivity.this, lstData.get(position));
                        break;
                }
            }
        });
    }

    @Override
    public void fillData() {

    }

    @Override
    public void setListener() {

    }

    @OnClick(R.id.tv_complete)
    public void onViewClicked() {
        Intent intent = new Intent();
        if (lstData.size() > 0) {
            for (GoodsInfo.Nature lstDatum : lstData) {
                if (TextUtils.isEmpty(lstDatum.getItem_id()) || lstDatum.getNature_arr() == null) {
                    Toast.makeText(mContext, "请填写完整规格信息", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }
        intent.putExtra("nature", lstData);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        GoodsInfo.Nature subSpecBean = lstData.get(clickPosition);
        switch (requestCode) {
            case REQUEST_SELECT_NATURE:
                GoodsInfo.Nature selectProject = (GoodsInfo.Nature) data.getSerializableExtra("selectNature");
                if (selectProject != null) {
                    boolean isExit = false;
                    for (int i = 0; i < lstData.size(); i++) {
                        if (i != clickPosition && selectProject.getItem_id().equals(lstData.get(i).getItem_id())) {
                            isExit = true;
                            break;
                        }
                    }
                    if (isExit) {
                        Toast.makeText(mContext, "该规格项目已存在，请重新选择", Toast.LENGTH_SHORT).show();
                    } else {
                        if (!TextUtils.isEmpty(subSpecBean.getItem_id()) && !subSpecBean.getItem_id().equals(selectProject.getItem_id())) {
                            subSpecBean.setNature_arr(null);
                        }
                        subSpecBean.setItem_id(selectProject.getItem_id());
                        subSpecBean.setItem_name(selectProject.getItem_name());
                    }
                }
                ArrayList<GoodsInfo.Nature> lstDelete = (ArrayList<GoodsInfo.Nature>) data.getSerializableExtra("lstDelete");
                if (lstDelete != null) {
                    Iterator<GoodsInfo.Nature> iterator = lstData.iterator();
                    while (iterator.hasNext()) {
                        GoodsInfo.Nature next = iterator.next();
                        Iterator<GoodsInfo.Nature> iterator1 = lstDelete.iterator();
                        while (iterator1.hasNext()) {
                            GoodsInfo.Nature next1 = iterator1.next();
                            if (!TextUtils.isEmpty(next.getItem_id()) && next.getItem_id().equals(next1.getItem_id())) {
                                iterator.remove();
                            }
                        }
                    }
                }
                ArrayList<GoodsInfo.Nature> lstModify = (ArrayList<GoodsInfo.Nature>) data.getSerializableExtra("lstModify");
                if (lstModify != null) {
                    for (GoodsInfo.Nature lstDatum : lstData) {
                        for (GoodsInfo.Nature natureBean : lstModify) {
                            if (!TextUtils.isEmpty(lstDatum.getItem_id()) && lstDatum.getItem_id().equals(natureBean.getItem_id())) {
                                lstDatum.setItem_name(natureBean.getItem_name());
                            }
                        }
                    }
                }
                adapter.notifyDataSetChanged();
                break;
            case REQUEST_SELECT_NATURE_ATTR:
                ArrayList<GoodsInfo.Nature_attr> selectAttr = (ArrayList<GoodsInfo.Nature_attr>) data.getSerializableExtra("selectAttr");
                subSpecBean.setNature_arr(selectAttr);
                adapter.notifyDataSetChanged();
                break;
        }
    }
}
