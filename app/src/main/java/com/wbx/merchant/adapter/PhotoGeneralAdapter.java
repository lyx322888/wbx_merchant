package com.wbx.merchant.adapter;


import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wbx.merchant.R;
import com.wbx.merchant.utils.GlideUtils;
import com.wbx.merchant.widget.MyImagView;

import java.util.List;

//图片通用
public class PhotoGeneralAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public PhotoGeneralAdapter() {
        super(R.layout.item_photo_general);
        getData().add("");
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        MyImagView imagView = helper.getView(R.id.iv_photo);
        ImageView ivDelete = helper.getView(R.id.iv_delete);
        if (helper.getLayoutPosition()==0){
            imagView.setImageResource(R.mipmap.ic_sctpmr);
            ivDelete.setVisibility(View.GONE);
        }else {
            GlideUtils.showBigPic(mContext,imagView,item);
            ivDelete.setVisibility(View.VISIBLE);
        }


        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(helper.getLayoutPosition());
            }
        });
    }

    public List<String> getPhotoList(){
        return mData.subList(1,mData.size());
    }
}
