package com.wbx.merchant.activity;

import android.content.Intent;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wbx.merchant.R;
import com.wbx.merchant.adapter.FullAdapter;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.base.BaseAdapter;
import com.wbx.merchant.bean.FullDataInfo;
import com.wbx.merchant.bean.FullJsonBean;
import com.wbx.merchant.widget.LoadingDialog;
import com.wbx.merchant.widget.iosdialog.AlertDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import rx.Observable;

/**
 * Created by Administrator on 2017/10/27 0027.
 */

public class FullActivity extends BaseActivity {
    @Bind(R.id.full_add)
    TextView full_add;
    @Bind(R.id.coupon_full_recycler_view)
    RecyclerView mRecyclerView_full;
    @Bind(R.id.coupon_full_bt)
    Button coupon_full_bt;
    private List<FullDataInfo> fullList = new ArrayList<>();
    private List<FullDataInfo> addFullList = new ArrayList<>();//新添加的数据
    private FullAdapter mfullAdapter;
    private int mSelectPosition = 0;
    private boolean canSubmit = true;

    @Override
    public int getLayoutId() {
        return R.layout.coupon_full;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        mRecyclerView_full.setLayoutManager(new LinearLayoutManager(mContext));
        mfullAdapter = new FullAdapter(fullList, mContext);
        mRecyclerView_full.setAdapter(mfullAdapter);
    }

    @Override
    public void fillData() {
        LoadingDialog.showDialogForLoading(mActivity, "加载中...", true);
        Observable<JSONObject> getfull = Api.getDefault().getfull(userInfo.getSj_login_token());
        new MyHttp().doPost(getfull, new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                fullList.addAll(JSONArray.parseArray(result.getString("data"),
                        FullDataInfo.class));
                mfullAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(int code) {
            }
        });
    }

    @Override
    public void setListener() {
        mfullAdapter.setOnItemClickListener(R.id.full_delete, new BaseAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(View view, int position) {
                mSelectPosition = position;
                new AlertDialog(mContext).builder()
                        .setTitle("提示")
                        .setMsg("删除当前项？")
                        .setNegativeButton("再想想", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        })
                        .setPositiveButton("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (mfullAdapter.getItem(mSelectPosition).getId() == 0) {
                                    //说明选中项为本地添加的 并未提交到服务器  这时候直接删除即可
                                    addFullList.remove(mfullAdapter.getItem(mSelectPosition));
                                    fullList.remove(mSelectPosition);
                                    mfullAdapter.notifyDataSetChanged();
                                } else {
                                    delete();
                                }
                            }
                        }).show();
            }
        });
        full_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转回执
                startActivityForResult(new Intent(mContext, AddFullActivity.class), 10045);

            }
        });

        findViewById(R.id.coupon_full_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (canSubmit) {
                    upDateFull();
                }
            }
        });
    }
    //删除选中项

    /**
     * id 满减活动id  token
     */
    private void delete() {
        Observable<JSONObject> id = Api.getDefault().delfull(userInfo.getSj_login_token(), fullList.get(mSelectPosition).getId());
        new MyHttp().doPost(id, new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                showShortToast("删除成功");
                fullList.remove(mSelectPosition);
                mfullAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        FullDataInfo fullData = (FullDataInfo) data.getSerializableExtra("fullData");
        addFullList.add(fullData);
        fullList.add(fullData);
        mfullAdapter.notifyDataSetChanged();
    }

    private void upDateFull() {
        canSubmit = false;
        if (addFullList.size() == 0) {
            finish();
            return;
        }
        List<FullJsonBean> lstFull = new ArrayList<>();
        for (FullDataInfo fullDataInfo : addFullList) {
            FullJsonBean fullJsonBean = new FullJsonBean();
            fullJsonBean.setReduce_money(fullDataInfo.getReduce_money() / 100.00f);
            fullJsonBean.setFull_money(fullDataInfo.getFull_money() / 100.00f);
            lstFull.add(fullJsonBean);
        }
        String jsonString = JSON.toJSONString(lstFull);
        LoadingDialog.showDialogForLoading(this, "保存中...", true);
        new MyHttp().doPost(Api.getDefault().addfull(userInfo.getSj_login_token(),
                jsonString), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                canSubmit = true;
                showShortToast("添加成功");
                finish();
            }

            @Override
            public void onError(int code) {
                canSubmit = true;
            }
        });
    }
}
