package com.wbx.merchant.fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.wbx.merchant.R;
import com.wbx.merchant.activity.OrderActivity;
import com.wbx.merchant.activity.OrderDetailActivity;
import com.wbx.merchant.activity.PayActivity;
import com.wbx.merchant.adapter.OrderAdapter;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseAdapter;
import com.wbx.merchant.base.BaseFragment;
import com.wbx.merchant.baseapp.AppConfig;
import com.wbx.merchant.baserx.RxBus;
import com.wbx.merchant.bean.DaDaOrderTrackBean;
import com.wbx.merchant.bean.FengNiaoOrderTrackBean;
import com.wbx.merchant.bean.GoodsInfo;
import com.wbx.merchant.bean.OrderInfo;
import com.wbx.merchant.bean.RxbusBean;
import com.wbx.merchant.dialog.DaDaCancelReasonFragment;
import com.wbx.merchant.utils.ToastUitl;
import com.wbx.merchant.widget.LoadingDialog;
import com.wbx.merchant.widget.iosdialog.AlertDialog;
import com.wbx.merchant.widget.refresh.BaseRefreshListener;
import com.wbx.merchant.widget.refresh.PullToRefreshLayout;
import com.wbx.merchant.widget.refresh.ViewStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import rx.Observable;

/**
 * Created by wushenghui on 2017/6/20.
 * 订单
 */

public class OrderFragment extends BaseFragment implements BaseRefreshListener {
    @Bind(R.id.refresh_layout)
    PullToRefreshLayout mRefreshLayout;
    @Bind(R.id.order_recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.type_layout)
    LinearLayout typeLayout;
    public static final String POSITION = "position";
    private List<OrderInfo> orderInfoList = new ArrayList<>();
    private OrderAdapter mAdapter;
    HashMap<String, Object> mParams = new HashMap<>();
    private int position;
    private boolean canLoadMore = true;
    //声明AMapLocationClientOption对象
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    private int selectPosition;
    private MyHttp myHttp;

    public static OrderFragment newInstance(int position) {
        OrderFragment tabFragment = new OrderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(POSITION, position);
        tabFragment.setArguments(bundle);
        return tabFragment;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.sign_xscroll_view_layout;
    }

    @Override
    public void initPresenter() {
        myHttp = new MyHttp();
    }

    @Override
    protected void initView() {
        typeLayout.setVisibility(View.GONE);
        mRefreshLayout.showView(ViewStatus.LOADING_STATUS);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new OrderAdapter(orderInfoList, getContext(), loginUser.getGrade_id() == AppConfig.StoreType.VEGETABLE_MARKET);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void fillData() {
        initLocation();
        position = getArguments().getInt(POSITION, 0);
        mParams.put("sj_login_token", loginUser.getSj_login_token());
        mParams.put("page", AppConfig.pageNum);
        mParams.put("num", AppConfig.pageSize);
        int status = 0;
        boolean isVegetableMarket = loginUser.getGrade_id() == AppConfig.StoreType.VEGETABLE_MARKET;
        switch (position) {
            case OrderActivity.POSITION_WAIT_SEND:
                status = 1;
                break;
            case OrderActivity.POSITION_SENDING:
                status = 2;
                break;
            case OrderActivity.POSITION_SELF:
                status = 77;
                break;
            case OrderActivity.POSITION_WAIT_REFUND:
                if (isVegetableMarket) {
                    status = 3;
                } else {
                    status = 4;
                }
                break;
            case OrderActivity.POSITION_REFUNDED:
                if (isVegetableMarket) {
                    status = 4;
                } else {
                    status = 5;
                }
                break;
            case OrderActivity.POSITION_COMPLETED:
                status = 8;
                break;
        }
        mParams.put("status", status);
        mParams.put("order_id", 0);
    }

    private void initLocation() {
        //初始化AMapLocationClientOption对象
        AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
        //获取一次定位结果：
        //该方法默认为false。
        mLocationOption.setOnceLocation(true);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //初始化定位
        mLocationClient = new AMapLocationClient(getActivity().getApplicationContext());
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        getServiceData();
    }

    private void getServiceData() {
        myHttp.doPost(Api.getDefault().orderList(mParams), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                if (null != mRefreshLayout) {
                    mRefreshLayout.finishRefresh();
                    mRefreshLayout.finishLoadMore();
                }
                List<OrderInfo> dataList = JSONArray.parseArray(result.getString("data"), OrderInfo.class);
                if (null == dataList && null != mRefreshLayout) {
                    mRefreshLayout.showView(ViewStatus.EMPTY_STATUS);
                    mRefreshLayout.buttonClickNullData(OrderFragment.this, "getServiceData");
                    return;
                }
                if ((int) mParams.get("order_id") == 0) {
                    orderInfoList.clear();
                }
                if (dataList.size() < AppConfig.pageSize) {
                    //说明下次已经没有数据了
                    canLoadMore = false;
                }
                if (null != mRefreshLayout) {
                    mRefreshLayout.showView(ViewStatus.CONTENT_STATUS);
                }
                if (loginUser.getGrade_id() != AppConfig.StoreType.VEGETABLE_MARKET) {
                    //实体店数据赋值到菜市场 兼容
                    for (OrderInfo orderInfo : dataList) {
                        orderInfo.setNeed_pay(orderInfo.getTotal_price());
                        orderInfo.setLogistics(orderInfo.getExpress_price());
                        for (GoodsInfo goodsInfo : orderInfo.getGoods()) {
                            goodsInfo.setProduct_id(goodsInfo.getGoods_id());
                            goodsInfo.setProduct_name(goodsInfo.getTitle());
                            goodsInfo.setDesc(goodsInfo.getIntro());
                        }
                    }
                }
                for (OrderInfo orderInfo : dataList) {
                    orderInfo.setOrder_status((Integer) mParams.get("status"));
                    if (orderInfo.getFengniao() != null && orderInfo.getFengniao().size() > 0) {
                        //将蜂鸟的model中转换成达达
                        List<DaDaOrderTrackBean> lstOrderTrack = new ArrayList<>();
                        for (FengNiaoOrderTrackBean fengNiaoOrderTrackBean : orderInfo.getFengniao()) {
                            DaDaOrderTrackBean daDaOrderTrackBean = new DaDaOrderTrackBean();
                            daDaOrderTrackBean.setDada_status(fengNiaoOrderTrackBean.getFengniao_status());
                            daDaOrderTrackBean.setDm_name(fengNiaoOrderTrackBean.getCarrier_driver_name());
                            daDaOrderTrackBean.setDm_mobile(fengNiaoOrderTrackBean.getCarrier_driver_phone());
                            daDaOrderTrackBean.setLat(fengNiaoOrderTrackBean.getLat());
                            daDaOrderTrackBean.setLng(fengNiaoOrderTrackBean.getLng());
                            daDaOrderTrackBean.setUpdate_time(fengNiaoOrderTrackBean.getCreate_time());
                            lstOrderTrack.add(daDaOrderTrackBean);
                        }
                        orderInfo.setDada(lstOrderTrack);
                    }
                }
                orderInfoList.addAll(dataList);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(int code) {
                if (code == AppConfig.ERROR_STATE.NULLDATA) {
                    if (null != mRefreshLayout) {
                        //无数据情况下
                        if ((int) mParams.get("order_id") == 0) {
                            mRefreshLayout.showView(ViewStatus.EMPTY_STATUS);
                            mRefreshLayout.buttonClickNullData(OrderFragment.this, "getServiceData");
                        } else {
                            canLoadMore = false;
                            showShortToast("没有更多数据了");
                        }

                    } else if (code == AppConfig.ERROR_STATE.NO_NETWORK || code == AppConfig.ERROR_STATE.SERVICE_ERROR) {
                        mRefreshLayout.showView(ViewStatus.ERROR_STATUS);
                        mRefreshLayout.buttonClickError(OrderFragment.this, "getServiceData");
                    } else {

                    }
                    mRefreshLayout.finishRefresh();
                    mRefreshLayout.finishLoadMore();
                }
            }
        });
    }

    @Override
    protected void bindEven() {
        //拨打收货人电话
        mAdapter.setOnItemClickListener(R.id.iv_call_phone, new BaseAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(View view, int position) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + orderInfoList.get(position).getAddr().getTel()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        //地图
        mAdapter.setOnItemClickListener(R.id.iv_location, new BaseAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(View view, int position) {
                if (TextUtils.isEmpty(orderInfoList.get(position).getAddr().getAddr())) {
                    ToastUitl.showShort("收货地址异常，请联系买家");
                    return;
                }
                LoadingDialog.showDialogForLoading(getActivity());
                selectPosition = position;
                mLocationClient.startLocation();
            }
        });
        //订单详情
        mAdapter.setOnItemClickListener(R.id.tv_order_detail, new BaseAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(View view, int position) {
                Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
                intent.putExtra("orderDetail", orderInfoList.get(position));
                startActivity(intent);
            }
        });
        //拒单
        mAdapter.setOnItemClickListener(R.id.tv_refuse_order, new BaseAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(View view, final int position) {
                new AlertDialog(getActivity()).builder()
                        .setTitle("提示")
                        .setMsg("拒绝接单？")
                        .setNegativeButton("不了", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        })
                        .setPositiveButton("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                orderOperation(Api.getDefault().refuseOrder(loginUser.getSj_login_token(), orderInfoList.get(position).getOrder_id()), position);

                            }
                        }).show();
            }
        });
        //发货
        mAdapter.setOnItemClickListener(R.id.tv_send, new BaseAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(View view, int position) {
                selectPosition = position;
                showSendPop();
            }
        });
        //达达配送
        mAdapter.setOnItemClickListener(R.id.tv_send_by_dada, new BaseAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(View view, final int position) {
                new AlertDialog(getActivity()).builder().setTitle("提示")
                        .setMsg("是否确定使用达达配送？")
                        .setNegativeButton("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        }).setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectPosition = position;
                        sendByDaDa(null);
                    }
                }).show();
            }
        });
        //取消达达配送
        mAdapter.setOnItemClickListener(R.id.tv_cancel_send_by_dada, new BaseAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(View view, final int position) {
                new AlertDialog(getActivity()).builder()
                        .setTitle("提示")
                        .setMsg("取消将扣除部分配送费，如有疑问可联系客服")
                        .setNegativeButton("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                            }
                        })
                        .setPositiveButton("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                cancelSendByDaDa(position);
                            }
                        }).show();
            }
        });
        //拒绝退款
        mAdapter.setOnItemClickListener(R.id.tv_refuse_refund, new BaseAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(View view, final int position) {
                new AlertDialog(getActivity()).builder()
                        .setTitle("提示")
                        .setMsg("确认拒绝退款吗？")
                        .setNegativeButton("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                            }
                        })
                        .setPositiveButton("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                orderOperation(Api.getDefault().refuseRefund(loginUser.getSj_login_token(), orderInfoList.get(position).getOrder_id()), position);

                            }
                        }).show();
            }
        });
        //同意退款
        mAdapter.setOnItemClickListener(R.id.tv_refund, new BaseAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(View view, final int position) {
                new AlertDialog(getActivity()).builder()
                        .setTitle("提示")
                        .setMsg("同意退款？")
                        .setNegativeButton("不了", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                            }
                        })
                        .setPositiveButton("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                orderOperation(Api.getDefault().agreeRefund(loginUser.getSj_login_token(), orderInfoList.get(position).getOrder_id()), position);

                            }
                        }).show();
            }
        });
        //打印订单
        mAdapter.setOnItemClickListener(R.id.tv_print_order, new BaseAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(View view, final int position) {
                new AlertDialog(getActivity()).builder()
                        .setTitle("提示")
                        .setMsg("是否确定打印该订单？")
                        .setNegativeButton("不了", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                            }
                        })
                        .setPositiveButton("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                printOrder();
                            }
                        }).show();
            }
        });
        //拨打骑手电话
        mAdapter.setOnItemClickListener(R.id.iv_call_driver, new BaseAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(View view, int position) {
                OrderInfo orderInfo = mAdapter.getItem(position);;
                if (!TextUtils.isEmpty(orderInfo.getDada().get(0).getDm_mobile())) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + orderInfo.getDada().get(0).getDm_mobile()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });
        //一键复制订单信息
        mAdapter.setOnItemClickListener(R.id.tv_copy_order, new BaseAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(View view, int position) {
                OrderInfo orderInfo  = mAdapter.getItem(position);;
                if (orderInfo.getAddr() == null) {
                    return;
                }
                String addr = orderInfo.getAddr().getAddr();
                String xm = orderInfo.getAddr().getXm();
                String tel = orderInfo.getAddr().getTel();
                String lable = xm + tel + addr;
                if (!TextUtils.isEmpty(lable)) {
                    //获取剪贴板管理器：
                    ClipboardManager cm = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                    // 创建普通字符型ClipData
                    ClipData mClipData = ClipData.newPlainText("Label", lable);
                    // 将ClipData内容放到系统剪贴板里。
                    cm.setPrimaryClip(mClipData);
                    ToastUitl.showShort("订单复制成功");
                }
            }
        });
        mRefreshLayout.setRefreshListener(this);
    }

    private void printOrder() {
        myHttp.doPost(Api.getDefault().printOrder(loginUser.getSj_login_token(), String.valueOf(orderInfoList.get(selectPosition).getOrder_id())), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                Toast.makeText(getActivity(), result.getString("msg"), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    private void cancelSendByDaDa(final int position) {
        DaDaCancelReasonFragment reasonFragment = DaDaCancelReasonFragment.newInstance(String.valueOf(orderInfoList.get(position).getOrder_id()));
        reasonFragment.setOnResultListener(new DaDaCancelReasonFragment.OnResultListener() {
            @Override
            public void onSuccess() {
                refresh();
            }
        });
        reasonFragment.show(getChildFragmentManager(), "");
    }

    private void showSendPop() {
        final View popView = getActivity().getLayoutInflater().inflate(R.layout.pop_goods_send_layout, null);
        final PopupWindow mPopUpWindow = new PopupWindow();
        mPopUpWindow.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        mPopUpWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        mPopUpWindow.setAnimationStyle(R.style.ActionSheetDialogAnimation);
        mPopUpWindow.setFocusable(true);
        mPopUpWindow.setOutsideTouchable(true);
        mPopUpWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopUpWindow.setContentView(popView);
        final ImageView ivSendByDaDa = popView.findViewById(R.id.iv_send_by_dada);
        final ImageView ivSendByMerchant = popView.findViewById(R.id.iv_send_by_merchant);
        final TextView tvEstimatePrice = popView.findViewById(R.id.tv_estimate_price);
        ivSendByMerchant.setSelected(true);
        popView.findViewById(R.id.ll_send_by_dada).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivSendByDaDa.setSelected(true);
                ivSendByMerchant.setSelected(false);
                tvEstimatePrice.setVisibility(View.VISIBLE);
                calculateDaDaPrice(tvEstimatePrice);
            }
        });
        popView.findViewById(R.id.ll_send_by_merchant).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivSendByDaDa.setSelected(false);
                ivSendByMerchant.setSelected(true);
                tvEstimatePrice.setVisibility(View.INVISIBLE);
            }
        });
        popView.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopUpWindow.dismiss();
            }
        });
        popView.findViewById(R.id.tv_confirm_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ivSendByDaDa.isSelected()) {
                    sendByDaDa(mPopUpWindow);
                } else {
                    //商家配送
                    orderOperation(Api.getDefault().sendGoods(loginUser.getSj_login_token(), orderInfoList.get(selectPosition).getOrder_id()), selectPosition);
                    mPopUpWindow.dismiss();
                }
            }
        });
        mPopUpWindow.showAtLocation(rootView.findViewById(R.id.root_view), Gravity.CENTER, 0, 0);
        backgroundAlpha(0.6f);
        mPopUpWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
    }
    //获取达达预估价格
    private void calculateDaDaPrice(final TextView tvEstimatePrice) {
        LoadingDialog.showDialogForLoading(getActivity());
        myHttp.doPost(Api.getDefault().getDaDaEstimatePrice(loginUser.getSj_login_token(), String.valueOf(orderInfoList.get(selectPosition).getOrder_id())), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                if (tvEstimatePrice != null) {
                    tvEstimatePrice.setText(result.getJSONObject("data").getString("dada_estimate_price"));
                }
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    private void sendByDaDa(final PopupWindow popupWindow) {
        LoadingDialog.showDialogForLoading(getActivity());
        myHttp.doPost(Api.getDefault().sendByDaDa(loginUser.getSj_login_token(), String.valueOf(orderInfoList.get(selectPosition).getOrder_id())), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                orderInfoList.get(selectPosition).setIs_dada(1);
                mAdapter.notifyDataSetChanged();
                showShortToast("已提交至达达");
                if (popupWindow != null) {
                    popupWindow.dismiss();
                }
            }

            @Override
            public void onError(int code) {
                if (popupWindow != null) {
                    popupWindow.dismiss();
                }
                if (code == AppConfig.ERROR_STATE.SEND_FEE_NO_ENOUGH) {
                    new AlertDialog(getActivity()).builder().setTitle("提示")
                            .setMsg("余额不足请充值").setPositiveButton("充值", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), PayActivity.class);
                            intent.putExtra("isDaDa", true);
                            startActivity(intent);
                        }
                    }).setNegativeButton("取消", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    }).show();
                }
            }
        });
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getActivity().getWindow().setAttributes(lp);
    }

    @Override
    public void refresh() {
        mParams.put("order_id", 0);
        canLoadMore = true;
        getServiceData();
    }

    @Override
    public void loadMore() {
        if (!canLoadMore) {
            mRefreshLayout.finishLoadMore();
            showShortToast("没有更多数据了");
            return;
        }
        if (orderInfoList.size() != 0) {
            mParams.put("order_id", orderInfoList.get(orderInfoList.size() - 1).getOrder_id());
        }
        getServiceData();
    }

    private void orderOperation(Observable<JSONObject> objectObservable, final int position) {
        myHttp.doPost(objectObservable, new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                showShortToast(result.getString("msg"));
                orderInfoList.remove(position);
                mAdapter.notifyDataSetChanged();
                //刷新角标
                rxbusPost();
            }

            @Override
            public void onError(int code) {

            }
        });
    }
    //刷新角标
    private void rxbusPost(){
        mRxManager.post("OrderActivity","updata");
    }

    //是否安装高德地图
    private boolean isInstallMap(String packageName) {
        PackageInfo packageInfo;
        try {
            packageInfo = getActivity().getPackageManager().getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        return null != packageInfo;
    }

    AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(final AMapLocation aMapLocation) {
            if (!TextUtils.isEmpty(orderInfoList.get(selectPosition).getAddr().getLat()) && !TextUtils.isEmpty(orderInfoList.get(selectPosition).getAddr().getLng())) {
                navigation(aMapLocation, Double.valueOf(orderInfoList.get(selectPosition).getAddr().getLat()), Double.valueOf(orderInfoList.get(selectPosition).getAddr().getLng()));
            } else {
                GeocodeSearch geocodeSearch = new GeocodeSearch(getActivity());
                geocodeSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
                    @Override
                    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {

                    }

                    @Override
                    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
                        LatLonPoint latLonPoint = geocodeResult.getGeocodeAddressList().get(0).getLatLonPoint();
                        navigation(aMapLocation, latLonPoint.getLatitude(), latLonPoint.getLongitude());
                    }
                });
                GeocodeQuery query = new GeocodeQuery(orderInfoList.get(selectPosition).getAddr().getAddr(), orderInfoList.get(selectPosition).getAddr().getAddr());
                geocodeSearch.getFromLocationNameAsyn(query);
            }
        }
    };

    /**
     * 导航
     */
    private void navigation(AMapLocation startLocation, double desLatitude, double desLongitude) {
        LoadingDialog.cancelDialogForLoading();
        if (isInstallMap("com.autonavi.minimap")) {
            //有安装高德地图
            Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("amapuri://route/plan/?dlat=" + desLatitude + "&dlon=" + desLongitude + "&dev=0&t=3"));
            intent.setPackage("com.autonavi.minimap");
            startActivity(intent);
        } else {
            //没有有安装高德地图
            String url = String.format("http://uri.amap.com/navigation?from=%f,%f,&to=%f,%f&mode=ride&policy=1&src=mypage&coordinate=gaode&callnative=0"
                    , startLocation.getLongitude()
                    , startLocation.getLatitude()
                    , desLongitude
                    , desLatitude
            );   //指定网址
            Uri uri = Uri.parse(url);
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(uri);
            getActivity().startActivity(intent);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mLocationClient != null) {
            mLocationClient.onDestroy();
            mLocationClient = null;
        }
    }
}
