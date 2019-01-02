package com.wbx.merchant.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import com.wbx.merchant.R;
import com.wbx.merchant.base.BaseAdapter;
import com.wbx.merchant.base.BaseViewHolder;
import com.wbx.merchant.utils.GlideUtils;

import java.io.File;
import java.util.List;

/**
 * Created by wushenghui on 2017/6/21.
 */

public class PhotoAdapter extends BaseAdapter<String, Context> {
    public PhotoAdapter(List<String> dataList, Context context) {
        super(dataList, context);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_photo;
    }

    @Override
    public void convert(BaseViewHolder holder, String s, int position) {
        ImageView picIm = holder.getView(R.id.iv_pic);
        if (position == 0) {
            picIm.setImageResource(R.drawable.p_add);
            holder.getView(R.id.iv_delete).setVisibility(View.GONE);
        } else {
            holder.getView(R.id.iv_delete).setVisibility(View.VISIBLE);
            if (s.contains("http://")) {
                GlideUtils.showMediumPic(mContext, picIm, s);
            } else {
                GlideUtils.displayUri(mContext, picIm, Uri.fromFile(new File(s)));
            }
        }
    }
}
