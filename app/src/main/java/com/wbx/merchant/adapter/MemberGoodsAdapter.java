package com.wbx.merchant.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wbx.merchant.R;
import com.wbx.merchant.activity.MemberGoodsActivity;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseApplication;
import com.wbx.merchant.bean.GoodsInfo;
import com.wbx.merchant.bean.SpecInfo;
import com.wbx.merchant.bean.UploadMemberGoodsBean;
import com.wbx.merchant.dialog.SpecDialog;
import com.wbx.merchant.utils.GlideUtils;
import com.wbx.merchant.utils.PriceUtil;
import com.wbx.merchant.utils.ToastUitl;
import com.wbx.merchant.widget.PriceEditText;
import com.wbx.merchant.widget.iosdialog.AlertDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zero on 2017/1/17.
 */

public class MemberGoodsAdapter extends RecyclerView.Adapter<MemberGoodsAdapter.MyViewHolder> {
    private MemberGoodsActivity mContext;

    private List<GoodsInfo> lstData = new ArrayList<>();
    private boolean isBatchDelete = false;

    public MemberGoodsAdapter(MemberGoodsActivity context) {
        mContext = context;
    }

    public void update(List<GoodsInfo> data) {
        lstData = data;
        notifyDataSetChanged();
        mContext.updateGoodsNum(lstData.size());
    }

    public void setBatchDelete(boolean isBatchDelete) {
        this.isBatchDelete = isBatchDelete;
        notifyDataSetChanged();
    }

    public boolean getisBatchDelete() {
        return isBatchDelete;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_member_goods, parent, false);
        MyViewHolder holder = new MyViewHolder(layout);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final GoodsInfo data = lstData.get(position);
        if (isBatchDelete) {
            holder.ivSelect.setVisibility(View.VISIBLE);
            holder.ivSelect.setSelected(data.isSelect());
        } else {
            holder.ivSelect.setVisibility(View.GONE);
        }
        holder.llRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isBatchDelete) {
                    data.setSelect(!data.isSelect());
                    notifyDataSetChanged();
                }
            }
        });
        GlideUtils.showMediumPic(mContext, holder.ivGoods, data.getPhoto());
        holder.tvGoodsName.setText(data.getTitle());
        holder.tvGoodsPrice.setText(data.getMall_price() == 0 ? String.format("¥%.2f", data.getPrice() / 100.00) : String.format("¥%.2f", data.getMall_price() / 100.00));
        if (holder.etMemberPrice.getTag() instanceof TextWatcher) {
            holder.etMemberPrice.removeTextChangedListener((TextWatcher) holder.etMemberPrice.getTag());
        }
        if (data.getShop_member_price() != 0) {
            holder.etMemberPrice.setText(String.format("%.2f", data.getShop_member_price() / 100.00));
        } else {
            holder.etMemberPrice.setText("");
        }
        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog(mContext).builder().setTitle("提示").setMsg("确定删除？")
                        .setNegativeButton("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        }).setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<UploadMemberGoodsBean> lstSelectGoods = new ArrayList<>();
                        if (data.getGoods_attr() != null && data.getGoods_attr().size() > 0) {
                            for (SpecInfo specInfo : data.getGoods_attr()) {
                                UploadMemberGoodsBean uploadMemberGoodsBean = new UploadMemberGoodsBean();
                                uploadMemberGoodsBean.setGoods_id(data.getGoods_id());
                                uploadMemberGoodsBean.setAttr_id(specInfo.getAttr_id());
                                lstSelectGoods.add(uploadMemberGoodsBean);
                            }
                        } else {
                            UploadMemberGoodsBean uploadMemberGoodsBean = new UploadMemberGoodsBean();
                            uploadMemberGoodsBean.setGoods_id(data.getGoods_id());
                            uploadMemberGoodsBean.setAttr_id(0);
                            lstSelectGoods.add(uploadMemberGoodsBean);
                        }
                        new MyHttp().doPost(Api.getDefault().deleteMemberGoods(BaseApplication.getInstance().readLoginUser().getSj_login_token(), JSONArray.toJSONString(lstSelectGoods)), new HttpListener() {
                            @Override
                            public void onSuccess(JSONObject result) {
                                ToastUitl.showShort(result.getString("msg"));
                                lstData.remove(data);
                                notifyDataSetChanged();
                                mContext.updateGoodsNum(lstData.size());
                            }

                            @Override
                            public void onError(int code) {

                            }
                        });
                    }
                }).show();
            }
        });
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!PriceUtil.isCorrectPrice(holder.etMemberPrice, s)) {
                    return;
                }
                if (TextUtils.isEmpty(s.toString())) {
                    data.setShop_member_price(0);
                } else {
                    data.setShop_member_price(Float.parseFloat(s.toString()) * 100);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
        holder.etMemberPrice.addTextChangedListener(textWatcher);
        holder.etMemberPrice.setTag(textWatcher);
        if (data.getGoods_attr() != null && data.getGoods_attr().size() > 0) {
            holder.llSingleSpec.setVisibility(View.GONE);
            holder.tvMultiSpec.setVisibility(View.VISIBLE);
            holder.tvMultiSpec.setText("<--点击设置多规格会员价-->");
            holder.tvMultiSpec.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SpecDialog.newInstance(data).show(mContext.getSupportFragmentManager(), "");
                }
            });
        } else {
            holder.llSingleSpec.setVisibility(View.VISIBLE);
            holder.tvMultiSpec.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return lstData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.ll_root_view)
        LinearLayout llRootView;
        @Bind(R.id.iv_select)
        ImageView ivSelect;
        @Bind(R.id.iv_goods)
        ImageView ivGoods;
        @Bind(R.id.tv_goods_name)
        TextView tvGoodsName;
        @Bind(R.id.tv_goods_price)
        TextView tvGoodsPrice;
        @Bind(R.id.et_member_price)
        PriceEditText etMemberPrice;
        @Bind(R.id.iv_delete)
        ImageView ivDelete;
        @Bind(R.id.ll_single_spec)
        LinearLayout llSingleSpec;
        @Bind(R.id.tv_multi_spec)
        TextView tvMultiSpec;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
