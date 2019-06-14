package com.wbx.merchant.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.autonavi.amap.mapcore.interfaces.IText;
import com.wbx.merchant.R;
import com.wbx.merchant.bean.BindUserBean;
import com.wbx.merchant.bean.OpenRadarBean;
import com.wbx.merchant.common.LoginUtil;
import com.wbx.merchant.utils.GlideUtils;
import com.wbx.merchant.utils.RetrofitUtils;
import com.wbx.merchant.utils.SPUtils;

import org.w3c.dom.Text;

import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class OpenRadarAdapter extends RecyclerView.Adapter<OpenRadarAdapter.VH> {

    Context context;
    List<OpenRadarBean.DataBean> list;

    public OpenRadarAdapter(Context context, List<OpenRadarBean.DataBean> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_radar, parent, false);
        VH vh = new VH(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final VH holder, final int position) {
        if (list.size() == 0) {
            holder.radar.setVisibility(View.VISIBLE);
            holder.ll_user.setVisibility(View.GONE);
        } else {
            holder.radar.setVisibility(View.GONE);
            holder.ll_user.setVisibility(View.VISIBLE);
        }
        holder.tv_name.setText(list.get(position).getNickname());
        if (list.get(position).getIs_shop_member() == 1) {
            holder.iv_vip.setVisibility(View.VISIBLE);
        } else {
            holder.iv_vip.setVisibility(View.GONE);
        }
        holder.tv_distance.setText(list.get(position).getDistance());
        if (TextUtils.isEmpty(list.get(position).getFace())) {
            holder.iv_user.setImageResource(R.drawable.loading_logo);
        } else {
            GlideUtils.showSmallPic(context, holder.iv_user, list.get(position).getFace());
        }
        if (list.get(position).getIs_bind() == 1) {
            holder.tv_bind.setText("已绑定");
            holder.tv_bind.setBackgroundResource(R.drawable.shape_onbind);
        }

        holder.tv_bind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RetrofitUtils.getInstance().server().getBindUser(LoginUtil.getLoginToken(), list.get(position).getUser_id() + "")
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Observer<BindUserBean>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(BindUserBean bindUserBean) {
                                if (bindUserBean.getState() == 1 && list.get(position).getIs_bind() == 0) {
                                    holder.tv_bind.setText("已绑定");
                                    holder.tv_bind.setBackgroundResource(R.drawable.shape_onbind);
                                }
                            }
                        });
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class VH extends RecyclerView.ViewHolder {
        ImageView iv_user;
        ImageView iv_vip;
        TextView tv_name;
        TextView tv_bind;
        TextView tv_distance;
        ImageView radar;
        LinearLayout ll_user;

        public VH(View itemView) {
            super(itemView);
            iv_user = itemView.findViewById(R.id.iv_user);
            tv_name = itemView.findViewById(R.id.tv_name);
            iv_vip = itemView.findViewById(R.id.iv_vip);
            tv_bind = itemView.findViewById(R.id.tv_bind);
            tv_distance = itemView.findViewById(R.id.tv_distance);

            radar = itemView.findViewById(R.id.img_radar);
            ll_user = itemView.findViewById(R.id.ll_user);
        }
    }
}
