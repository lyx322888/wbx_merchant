package com.wbx.merchant.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wbx.merchant.R;
import com.wbx.merchant.activity.SeatActivity;
import com.wbx.merchant.adapter.BookSeatOrderAdapter;
import com.wbx.merchant.adapter.ChooseSeatAdapter;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseAdapter;
import com.wbx.merchant.base.BaseFragment;
import com.wbx.merchant.baseapp.AppConfig;
import com.wbx.merchant.bean.BookSeatInfo;
import com.wbx.merchant.bean.SeatInfo;
import com.wbx.merchant.dialog.BookSeatOrderDetailDialog;
import com.wbx.merchant.widget.LoadingDialog;
import com.wbx.merchant.widget.iosdialog.AlertDialog;
import com.wbx.merchant.widget.refresh.BaseRefreshListener;
import com.wbx.merchant.widget.refresh.PullToRefreshLayout;
import com.wbx.merchant.widget.refresh.ViewStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;

/**
 * Created by wushenghui on 2017/12/26.
 */

public class BookSeatOrderFragment extends BaseFragment implements BaseRefreshListener {
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    BookSeatOrderAdapter mAdapter;
    public List<BookSeatInfo> orderInfoList = new ArrayList<>();
    @Bind(R.id.refresh_layout)
    PullToRefreshLayout refreshLayout;
    private HashMap<String, Object> mParams = new HashMap<>();
    public int pageNum = AppConfig.pageNum;
    public int pageSize = AppConfig.pageSize;
    public boolean canLoadMore = true;
    private int selectPosition;
    private PopupWindow mPopWindow;
    private int seatId = 0;
    private ChooseSeatAdapter productnewAdapter;

    public static BookSeatOrderFragment newInstance(long timestamp) {
        BookSeatOrderFragment tabFragment = new BookSeatOrderFragment();
        Bundle bundle = new Bundle();
        bundle.putLong("timestamp", timestamp);
        tabFragment.setArguments(bundle);
        return tabFragment;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_book_seat_order;
    }

    @Override
    public void initPresenter() {
    }

    @Override
    protected void initView() {
        mParams.put("sj_login_token", loginUser.getSj_login_token());
        mParams.put("day_time", getArguments().getLong("timestamp", 0L));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new BookSeatOrderAdapter(orderInfoList, getActivity());
        mRecyclerView.setAdapter(mAdapter);
        refreshLayout.setRefreshListener(this);
    }

    @Override
    protected void fillData() {
        getServiceData();
    }

    public void getServiceData() {
        if (!canLoadMore) {
            showShortToast("没有更多数据了");
            refreshLayout.finishLoadMore();
            return;
        }
        mParams.put("page", pageNum);
        mParams.put("num", pageSize);
        new MyHttp().doPost(Api.getDefault().getBookSeatList(mParams), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                if (refreshLayout == null) {
                    return;
                }
                refreshLayout.finishLoadMore();
                refreshLayout.finishRefresh();
                if (pageNum == AppConfig.pageNum) orderInfoList.clear();
                List<BookSeatInfo> dataList = JSONArray.parseArray(result.getJSONObject("data").getString("list"), BookSeatInfo.class);
                if (dataList == null) return;
                orderInfoList.addAll(dataList);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(int code) {
                if (refreshLayout == null) {
                    return;
                }
                refreshLayout.finishLoadMore();
                refreshLayout.finishRefresh();
                if (code == AppConfig.ERROR_STATE.NULLDATA) {
                    canLoadMore = false;
                } else {
                    refreshLayout.showView(ViewStatus.ERROR_STATUS);
                }
            }
        });
    }

    @Override
    protected void bindEven() {
        mAdapter.setOnItemClickListener(R.id.receive_order_btn, new BaseAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(View view, int position) {
                //接单
                selectPosition = position;
                getSeatList();
            }
        });
        mAdapter.setOnItemClickListener(R.id.ll_order_info, new BaseAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(View view, int position) {
                //订单详情
                List<BookSeatInfo.OrderGoodsBean> order_goods = orderInfoList.get(position).getOrder_goods();
                if (order_goods != null && order_goods.size() > 0) {
                    BookSeatOrderDetailDialog dialog = BookSeatOrderDetailDialog.newInstance(orderInfoList.get(position));
                    dialog.show(getFragmentManager(), "");
                }
            }
        });
        mAdapter.setOnItemClickListener(R.id.refuse_order_btn, new BaseAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(View view, int position) {
                //拒单
                selectPosition = position;
                new AlertDialog(getActivity()).builder()
                        .setTitle("提示")
                        .setMsg("确认拒单？")
                        .setNegativeButton("再想想", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                            }
                        })
                        .setPositiveButton("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                refuseOrder();
                            }
                        }).show();
            }
        });
        mAdapter.setOnItemClickListener(R.id.call_custom_btn, new BaseAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(View view, int position) {
                //联系客户
                if (TextUtils.isEmpty(orderInfoList.get(position).getMobile())) {
                    showShortToast("未获取到客户电话");
                } else {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + orderInfoList.get(position).getMobile()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getActivity().startActivity(intent);
                }
            }
        });
    }

    /**
     * 获取座位列表
     */
    private void getSeatList() {
        LoadingDialog.showDialogForLoading(getActivity(), "数据获取中...", true);
        new MyHttp().doPost(Api.getDefault().getSeatInfo(loginUser.getSj_login_token()), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                List<SeatInfo> dataList = JSONArray.parseArray(result.getString("data"), SeatInfo.class);
                showSeatList(dataList);
            }

            @Override
            public void onError(int code) {
                if (code == AppConfig.ERROR_STATE.NULLDATA) {
                    showShortToast("请先设置座位");
                    SeatActivity.actionStart(getActivity());
                }
            }
        });
    }

    private void showSeatList(final List<SeatInfo> dataList) {
        View inflate = LayoutInflater.from(getActivity()).inflate(R.layout.pop_seat_list, null);
        mPopWindow = new PopupWindow();
        mPopWindow.setAnimationStyle(R.style.ActionSheetDialogAnimation);
        mPopWindow.setContentView(inflate);
        mPopWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mPopWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopWindow.setFocusable(true);
        mPopWindow.setOutsideTouchable(true);
        mPopWindow.showAtLocation(rootView.findViewById(R.id.root_view), Gravity.BOTTOM | Gravity
                .CENTER_HORIZONTAL, 0, 0);
        Button cancel = (Button) inflate.findViewById(R.id.abolish_item_product);//取消
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopWindow.dismiss();
            }
        });
        Button ensure = (Button) inflate.findViewById(R.id.confirm_item_product);//确定
        ensure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (seatId == 0) {
                    showShortToast("请选择选中商品所属分类");
                    return;
                }
                setSeatIdForUser();
            }
        });
        //新建分类
        inflate.findViewById(R.id.classify_item_product).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopWindow.dismiss();
                SeatActivity.actionStart(getActivity());
            }
        });
        final RecyclerView recyclerView = inflate.findViewById(R.id
                .recycler_item_new);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        productnewAdapter = new ChooseSeatAdapter(dataList, getActivity());
        recyclerView.setAdapter(productnewAdapter);
        productnewAdapter.setOnItemClickListener(R.id.root_view, new BaseAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(View view, int position) {
                //先还原 == 全部设置未选中
                for (SeatInfo seatInfo : dataList) {
                    seatInfo.setSelect(false);
                }
                //再根据 选中的那个
                SeatInfo cateInfo = dataList.get(position);
                //设置选中
                cateInfo.setSelect(!cateInfo.isSelect());
                //如果是选中 把选中的cateId赋值上去
                if (cateInfo.isSelect()) {
                    seatId = cateInfo.getId();
                } else {
                    //否则 为反选 把id置为0 为了点击下一步的时候判断 用户是否有选择分类
                    seatId = 0;
                }
                productnewAdapter.notifyDataSetChanged();
            }
        });
    }

    //分配座位给客户
    private void setSeatIdForUser() {
        new MyHttp().doPost(Api.getDefault().receiveBookSeat(loginUser.getSj_login_token(), orderInfoList.get(selectPosition).getReserve_table_id(), seatId), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                showShortToast(result.getString("msg"));
                orderInfoList.get(selectPosition).setStatus(2);
                mAdapter.notifyDataSetChanged();
                mPopWindow.dismiss();
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    private void refuseOrder() {
        new MyHttp().doPost(Api.getDefault().refuseBookSeat(loginUser.getSj_login_token(), orderInfoList.get(selectPosition).getReserve_table_id()), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                showShortToast(result.getString("msg"));
                orderInfoList.get(selectPosition).setStatus(9);
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
        getServiceData();
    }

    @Override
    public void loadMore() {
        pageNum += AppConfig.pageNum;
        getServiceData();
    }
}
