package com.wbx.merchant.adapter;

import android.widget.ImageView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wbx.merchant.R;
import com.wbx.merchant.bean.ActivityManagerBean;
import com.wbx.merchant.utils.GlideUtils;

//管理活动
public class ActivityManagerAdpapter  extends BaseQuickAdapter<ActivityManagerBean.DataBean, BaseViewHolder>{
    public ActivityManagerAdpapter( ) {
        super(R.layout.item_myactivity);
    }

    @Override
    protected void convert(BaseViewHolder helper, ActivityManagerBean.DataBean item) {
        helper.setText(R.id.tv_title,item.getTitle())
                .setText(R.id.tv_content,item.getDescribe())
                .setText(R.id.tv_number,item.getShop_use_num());
        if (item.getHas_create()==1){
            helper.setText(R.id.btn_cj,"管理活动");
        }else {
            helper.setText(R.id.btn_cj,"立即创建");
        }
        GlideUtils.showBigPic(mContext, (ImageView) helper.getView(R.id.riv_photo),item.getPhoto());
        helper.addOnClickListener(R.id.btn_cj);
    }
}
