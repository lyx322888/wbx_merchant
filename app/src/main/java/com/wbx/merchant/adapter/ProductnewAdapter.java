package com.wbx.merchant.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.wbx.merchant.R;
import com.wbx.merchant.base.BaseAdapter;
import com.wbx.merchant.base.BaseViewHolder;
import com.wbx.merchant.bean.CateInfo;

import java.util.List;

/**
 * Created by Administrator on 2017/11/14 0014.
 */

public class ProductnewAdapter  extends BaseAdapter<CateInfo,Context>{


    public ProductnewAdapter(List<CateInfo> dataList, Context context) {
        super(dataList, context);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_recycler_product;
    }

    @Override
    public void convert(BaseViewHolder holder, CateInfo cateInfo, int position) {
        ImageView imageView = holder.getView(R.id.item_recycler_img);
        TextView title = holder.getView(R.id.item_recycler_tv);
        title.setText(cateInfo.getCate_name());
        boolean select = cateInfo.isSelect();
        if (select){
            imageView.setImageResource(R.drawable.ic_ok);
        }else {
         imageView.setImageResource(R.drawable.ic_round);
        }
    }
}
