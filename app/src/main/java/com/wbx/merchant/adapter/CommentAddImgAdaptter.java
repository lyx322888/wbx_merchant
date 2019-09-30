package com.wbx.merchant.adapter;

import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wbx.merchant.R;
import com.wbx.merchant.utils.GlideUtils;
import com.yanzhenjie.album.AlbumFile;

import java.util.List;
//添加图片
public class CommentAddImgAdaptter extends BaseQuickAdapter<AlbumFile, BaseViewHolder> {
    private boolean  editable = false;
    public CommentAddImgAdaptter(int layoutResId, @Nullable List<AlbumFile> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AlbumFile item) {
        ImageView imageView = helper.getView(R.id.iv_comment_img);
        GlideUtils.showMediumPic(mContext, imageView, item.getPath());
        helper.addOnClickListener(R.id.iv_comment_delete);
        helper.setVisible(R.id.iv_comment_delete,editable);
    }
    //是否可编辑  删除按钮的显示隐藏
    public void setEditable(boolean editable){
            this.editable = editable;
            notifyDataSetChanged();
    }

}
