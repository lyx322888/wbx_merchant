package com.wbx.merchant.activity;


import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wbx.merchant.R;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.base.BaseAdapter;
import com.wbx.merchant.base.BaseViewHolder;
import com.wbx.merchant.bean.SpecialSupplyBean;
import com.wbx.merchant.utils.GlideUtils;
import com.wbx.merchant.utils.ToastUitl;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 特供产品
 */

public class SpecialSupplyActivity extends BaseActivity {
    @Bind(R.id.produt_view_release)
    RecyclerView mReleaseView;
    @Bind(R.id.release_bt)
    Button btnRelease;
    @Bind(R.id.product_title)
    TextView tvTitle;

    private SpecialSupplyAdapter mReleaseAdapter;
    private List<SpecialSupplyBean> mSupplyList = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.product_release;
    }

    @Override
    public void initPresenter() {
    }

    @Override
    public void initView() {
        tvTitle.setText("特供免单");
        mReleaseView.setLayoutManager(new GridLayoutManager(mContext, 2));
        mReleaseView.setHasFixedSize(true);
        mReleaseAdapter = new SpecialSupplyAdapter(mSupplyList, mContext);
        mReleaseView.setAdapter(mReleaseAdapter);
    }

    @Override
    public void fillData() {
        new MyHttp().doPost(Api.getDefault().getSpecialSupply(userInfo.getSj_login_token()), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                List<SpecialSupplyBean> data = JSONArray.parseArray(result.getString("data"), SpecialSupplyBean.class);
                mSupplyList.addAll(data);
                mReleaseAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(int code) {
            }
        });
    }

    @Override
    public void setListener() {
    }

    private String isNext() {
        if (mSupplyList != null && mSupplyList.size() > 0) {
            StringBuffer sb = new StringBuffer();
            for (SpecialSupplyBean bean : mSupplyList) {
                if (bean.isStatus()) {
                    sb.append(bean.getGoods_id() + ",");
                }
            }
            if (!TextUtils.isEmpty(sb)) {
                return sb.substring(0, sb.length() - 1);
            } else {
                return "";
            }
        }
        return "";
    }

    @OnClick(R.id.release_bt)
    public void onViewClicked(View view) {
        String ids = isNext();
        if (TextUtils.isEmpty(ids)) {
            ToastUitl.showShort("请选择特供产品");
            return;
        }
        new MyHttp().doPost(Api.getDefault().sendSpecial(userInfo.getSj_login_token(), ids), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                String state = result.getString("state");
                String msg = result.getString("msg");
                if (!state.equals("0")) {
                    finish();
                }
                ToastUitl.showShort(msg);
            }

            @Override
            public void onError(int code) {
            }
        });
    }

    public class SpecialSupplyAdapter extends BaseAdapter<SpecialSupplyBean, Context> {

        SpecialSupplyAdapter(List<SpecialSupplyBean> saveList, Context context) {
            super(saveList, context);
        }

        @Override
        public int getLayoutId(int viewType) {
            return R.layout.item_special_supply;
        }

        @Override
        public void convert(BaseViewHolder holder, final SpecialSupplyBean productInfo, final int position) {
            ImageView recycler_img = holder.getView(R.id.item_recycler_img);
            GlideUtils.showMediumPic(mContext, recycler_img, productInfo.getPhoto());
            TextView tvTitle = holder.getView(R.id.tv_product_title);
            TextView tvMoney = holder.getView(R.id.tv_product_money);
            tvTitle.setText(productInfo.getTitle());
            tvMoney.setText(productInfo.getPrice() / 100 + "");
            ImageView ivCheck = holder.getView(R.id.iv_check);
            if (productInfo.isStatus()) {
                ivCheck.setBackgroundResource(R.drawable.ic_ok);
            } else {
                ivCheck.setBackgroundResource(R.drawable.ic_round);
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    productInfo.setStatus(!productInfo.isStatus());
                    notifyItemChanged(position);
                }
            });
        }
    }
}
