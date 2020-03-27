package com.wbx.merchant.activity;

import android.app.Dialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wbx.merchant.R;
import com.wbx.merchant.adapter.ReleaseSecKillAdapter;
import com.wbx.merchant.adapter.SecKillAttrAdapter;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.base.BaseAdapter;
import com.wbx.merchant.bean.GoodsInfo;
import com.wbx.merchant.bean.SpecInfo;
import com.wbx.merchant.utils.FormatUtil;
import com.wbx.merchant.widget.PriceEditText;
import com.wbx.merchant.widget.iosdialog.AlertDialog;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by wushenghui on 2017/12/12.
 * 发布秒杀
 */
public class ReleaseSecKillActivity extends BaseActivity {
    @Bind(R.id.recycler_view)
    RecyclerView mRecycler;
    private ReleaseSecKillAdapter mAdapter;
    private List<GoodsInfo> goodsInfoList = new ArrayList<>();
    @Bind(R.id.goods_num_tv)
    TextView goodsNumTv;
    private HashMap<String, Object> mParams = new HashMap<>();
    private String seckill_start_time, seckill_end_time, limitations_num;
    private Dialog mDialog;
    private View inflate;
    private RecyclerView attrRecyclerView;
    private SecKillAttrAdapter mAttrAdapter;
    private GoodsInfo selectGoodsInfo;
    private int selectPosition;
    private Dialog dialog;
    private Button cancelBtn;
    private Button completeBtn;
    private PriceEditText price;
    private boolean isShow;
    private List<GoodsInfo> selectDeleteGoodsList = new ArrayList<>();
    @Bind(R.id.batch_delete_seckill_bt)
    Button batchDeleteBtn;

    @Override
    public int getLayoutId() {
        return R.layout.activity_release_seckill;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        seckill_start_time = getIntent().getStringExtra("seckill_start_time");
        seckill_end_time = getIntent().getStringExtra("seckill_end_time");
        limitations_num = getIntent().getStringExtra("limitations_num");
        goodsInfoList = (List<GoodsInfo>) getIntent().getSerializableExtra("selectGoodsList");
        for (GoodsInfo goodsInfo : goodsInfoList) {
            goodsInfo.setSelect(false);
        }
        goodsNumTv.setText(String.format("秒杀商品(共%d件)", goodsInfoList.size()));
        mRecycler.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new ReleaseSecKillAdapter(goodsInfoList, mContext);
        mRecycler.setAdapter(mAdapter);
    }

    @Override
    public void fillData() {

    }

    @Override
    public void setListener() {
        mAdapter.setOnItemClickListener(R.id.root_view, new BaseAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(View view, int position) {
                if (isShow) {
                    GoodsInfo goodsInfo = goodsInfoList.get(position);
                    goodsInfo.setSelect(!goodsInfo.isSelect());
                    if (goodsInfo.isSelect()) {
                        selectDeleteGoodsList.add(goodsInfo);
                    } else {
                        selectDeleteGoodsList.remove(goodsInfo);
                    }
                    mAdapter.notifyDataSetChanged();
                }

            }
        });
        mAdapter.setOnItemClickListener(R.id.delete_im, new BaseAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(View view, int position) {
                selectPosition = position;
                new AlertDialog(mContext).builder()
                        .setTitle("提示")
                        .setMsg("删除此商品？")
                        .setNegativeButton("再想想", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        })
                        .setPositiveButton("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                goodsInfoList.remove(selectPosition);
                                mAdapter.notifyDataSetChanged();
                                goodsNumTv.setText(String.format("秒杀商品(共%d件)", goodsInfoList.size()));


                            }
                        }).show();
            }
        });
        mAdapter.setOnItemClickListener(R.id.is_attr_tv, new BaseAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(View view, int position) {
                selectGoodsInfo = mAdapter.getItem(position);
                showAttrPop();
            }
        });

    }

    private void showAttrPop() {
        //seckill_attr_recycler
        if (mDialog == null) {
            mDialog = new Dialog(this, R.style.ActionSheetDialogStyle);
            inflate = getLayoutInflater().inflate(R.layout.seckill_attr_dialog, null);
            Window dialogWindow = mDialog.getWindow();
            mDialog.setContentView(inflate);
            dialogWindow.setGravity(Gravity.BOTTOM);
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            WindowManager m = getWindowManager();
            Display d = m.getDefaultDisplay();
            lp.width = (d.getWidth()); // 宽度设置为屏幕的宽度
            lp.height = (int) (d.getHeight() * 0.6);
            dialogWindow.setAttributes(lp);
        }
        attrRecyclerView = (RecyclerView) inflate.findViewById(R.id.seckill_attr_recycler);
        attrRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAttrAdapter = new SecKillAttrAdapter(selectGoodsInfo.getGoods_attr(), mContext);
        attrRecyclerView.setAdapter(mAttrAdapter);
        inflate.findViewById(R.id.confirm_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });
        if (!mDialog.isShowing()) {
            mDialog.show();
        }
        mAttrAdapter.setOnItemClickListener(R.id.delete_seckill_attr_im, new BaseAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(View view, int position) {
                selectGoodsInfo.getGoods_attr().remove(mAttrAdapter.getItem(position));
                mAttrAdapter.notifyDataSetChanged();
            }
        });
    }

    public void submit(View view) {
        if (isShow) {
            if (selectDeleteGoodsList.size() == 0) {
                showShortToast("请先选中需删除的商品");
                return;
            }
            goodsInfoList.removeAll(selectDeleteGoodsList);
            mAdapter.notifyDataSetChanged();
            return;
        }
        for (GoodsInfo goodsInfo : goodsInfoList) {
            if (goodsInfo.getIs_attr() == 0) {
                if (goodsInfo.getSeckill_price() == 0) {
                    showShortToast(String.format("请输入商品(%s)的秒杀价格", goodsInfo.getProduct_name()));
                    return;
                }
                double price = goodsInfo.getMall_price() == 0 ? (goodsInfo.getPrice() / 100.00) : (goodsInfo.getMall_price() / 100.00);
                if (goodsInfo.getSeckill_price() >= price) {
                    showShortToast(String.format("商品(%s)的秒杀价格需低于原价格", goodsInfo.getProduct_name()));
                    return;
                }
                if (TextUtils.isEmpty(goodsInfo.getSeckill_num()) || "0".equals(goodsInfo.getSeckill_num())) {
                    showShortToast(String.format("请输入商品(%s)的秒杀库存", goodsInfo.getProduct_name()));
                    return;
                }
            } else {
                List<SpecInfo> goods_attr = goodsInfo.getGoods_attr();
                for (SpecInfo specInfo : goods_attr) {
                    if (specInfo.getSeckill_price() == 0) {
                        showShortToast(String.format("请输入多规格商品%s(%s)的秒杀价格", goodsInfo.getProduct_name(), specInfo.getAttr_name()));
                        return;
                    }
                    double price = specInfo.getMall_price() == 0 ? (specInfo.getPrice() / 100.00) : (specInfo.getMall_price() / 100.00);
                    if (specInfo.getSeckill_price() >= price) {
                        showShortToast(String.format("多规格商品%s(%s)的秒杀价格需低于原价格", goodsInfo.getProduct_name(), specInfo.getAttr_name()));
                        return;
                    }
                    if (specInfo.getSeckill_num() == 0) {
                        showShortToast(String.format("请输入多规格商品%s(%s)的秒杀库存", goodsInfo.getProduct_name(), specInfo.getAttr_name()));
                        return;
                    }
                }
            }
        }
        mParams.put("sj_login_token", userInfo.getSj_login_token());
        try {
            mParams.put("seckill_start_time", FormatUtil.mydateToStamp2(seckill_start_time, "yyyy-MM-dd HH:mm"));
            mParams.put("seckill_end_time", FormatUtil.mydateToStamp2(seckill_end_time, "yyyy-MM-dd HH:mm"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        mParams.put("limitations_num", limitations_num);
        mParams.put("seckill_goods", JSON.toJSONString(goodsInfoList));
        new MyHttp().doPost(Api.getDefault().addSecKillGoods(mParams), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                showShortToast("添加成功");
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onError(int code) {
            }
        });
    }

    @OnClick({R.id.batch_settings_seckill_price, R.id.batch_delete_seckill_bt})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.batch_settings_seckill_price:
                showBatchDialog();
                break;
            case R.id.batch_delete_seckill_bt:
                isShow = !isShow;
                if (isShow) {
                    batchDeleteBtn.setText("取消删除");
                } else {
                    batchDeleteBtn.setText("批量删除");
                }
                mAdapter.setIsShow(isShow);
                break;
        }
    }

    private void showBatchDialog() {
        View inflate = getLayoutInflater().inflate(R.layout.settings_seckill_price_dialog, null);
        if (null == dialog) {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mContext);
            builder.setView(inflate);
            dialog = builder.create();
            TextView nickName = inflate.findViewById(R.id.user_nick_name_tv);
            nickName.setText(userInfo.getNickname());
            cancelBtn = inflate.findViewById(R.id.dialog_cancel_btn);
            completeBtn = inflate.findViewById(R.id.dialog_complete_btn);
            price = inflate.findViewById(R.id.price_edit);
        }
        price.setText("");
        completeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doBatchSettingsPrice(price.getText().toString());
                dialog.dismiss();
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void doBatchSettingsPrice(String batchPrice) {
        if (TextUtils.isEmpty(batchPrice)) {
            return;
        }
        for (GoodsInfo goodsInfo : goodsInfoList) {
            if (goodsInfo.getIs_attr() == 0) {
                goodsInfo.setFloatSeckill_price(Float.valueOf(batchPrice));
            } else {
                List<SpecInfo> goods_attr = goodsInfo.getGoods_attr();
                for (SpecInfo specInfo : goods_attr) {
                    specInfo.setFloatSeckill_price(Float.valueOf(batchPrice));
                }
            }
        }
        mAdapter.notifyDataSetChanged();
    }
}
