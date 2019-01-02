package com.wbx.merchant.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wbx.merchant.R;
import com.wbx.merchant.adapter.BookSeatRefundAdapter;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.base.BaseAdapter;
import com.wbx.merchant.baseapp.AppConfig;
import com.wbx.merchant.bean.BookSeatInfo;
import com.wbx.merchant.widget.LoadingDialog;
import com.wbx.merchant.widget.iosdialog.AlertDialog;
import com.wbx.merchant.widget.refresh.BaseRefreshListener;
import com.wbx.merchant.widget.refresh.PullToRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;

/**
 * Created by wushenghui on 2017/12/28.
 */

public class BookSeatRefundActivity extends BaseActivity implements BaseRefreshListener {
    @Bind(R.id.refresh_layout)
    PullToRefreshLayout mRefreshLayout;
    @Bind(R.id.order_recycler_view)
    RecyclerView mRecyclerView;
    private BookSeatRefundAdapter mAdapter;
    private List<BookSeatInfo> mDataList = new ArrayList<>();
    private HashMap<String, Object> mParams = new HashMap<>();
    private int pageNum = AppConfig.pageNum;
    private int pageSize = AppConfig.pageSize;
    private int selectPosition = 0;
    private boolean isAgree;
    private boolean canLoadMore = true;

    @Override
    public int getLayoutId() {
        return R.layout.activity_book_seat_refund;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new BookSeatRefundAdapter(mDataList, mContext);
        mRecyclerView.setAdapter(mAdapter);
        mParams.put("sj_login_token", userInfo.getSj_login_token());
        mParams.put("num", pageSize);
    }

    @Override
    public void fillData() {
        LoadingDialog.showDialogForLoading(mActivity, "数据加载中...", true);
        mParams.put("page", pageNum);
        new MyHttp().doPost(Api.getDefault().getBookSeatRefund(mParams), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                mRefreshLayout.finishRefresh();
                mRefreshLayout.finishLoadMore();
                List<BookSeatInfo> dataList = JSONArray.parseArray(result.getString("data"), BookSeatInfo.class);
                if (null == dataList) return;
                if (pageNum == AppConfig.pageNum) mDataList.clear();
                if (dataList.size() < AppConfig.pageSize) {
                    //说明下次已经没有数据了
                    canLoadMore = false;
                }
                mDataList.addAll(dataList);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(int code) {
                mRefreshLayout.finishRefresh();
                mRefreshLayout.finishLoadMore();
            }
        });
    }

    @Override
    public void setListener() {
        mRefreshLayout.setRefreshListener(this);
        mAdapter.setOnItemClickListener(R.id.call_im, new BaseAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(View view, int position) {
                if (TextUtils.isEmpty(mDataList.get(position).getMobile())) {
                    showShortToast("未获取到客户电话");
                } else {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mDataList.get(position).getMobile()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });
        mAdapter.setOnItemClickListener(R.id.order_agree_btn, new BaseAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(View view, int position) {
                //同意接单
                selectPosition = position;
                isAgree = true;
                orderOperation();
            }
        });
        mAdapter.setOnItemClickListener(R.id.order_repulse_btn, new BaseAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(View view, int position) {
                //拒绝接单
                selectPosition = position;
                isAgree = false;
                orderOperation();
            }
        });
    }

    //订单操作
    private void orderOperation() {
        new AlertDialog(mContext).builder()
                .setTitle("提示")
                .setMsg(isAgree ? "是否同意退款？" : "是否拒绝退款？")
                .setNegativeButton("再想想", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                })
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        submit();
                    }
                }).show();
    }

    //提交操作
    private void submit() {
        new MyHttp().doPost(isAgree ? Api.getDefault().bookSeatRefundAgree(userInfo.getSj_login_token(), mDataList.get(selectPosition).getReserve_table_id())
                : Api.getDefault().bookSeatRefundRefuse(userInfo.getSj_login_token(), mDataList.get(selectPosition).getReserve_table_id()), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                showShortToast(result.getString("msg"));
                mDataList.remove(selectPosition);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    @Override
    public void refresh() {
        canLoadMore = true;
        pageNum = AppConfig.pageNum;
        fillData();
    }

    @Override
    public void loadMore() {
        if (!canLoadMore) {
            mRefreshLayout.finishLoadMore();
            showShortToast("没有更多数据了");
            return;
        }
        pageNum += AppConfig.pageNum;
        fillData();
    }
}
