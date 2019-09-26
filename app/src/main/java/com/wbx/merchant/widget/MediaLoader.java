package com.wbx.merchant.widget;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.wbx.merchant.R;
import com.yanzhenjie.album.AlbumFile;
import com.yanzhenjie.album.AlbumLoader;

public class MediaLoader implements AlbumLoader {
    @Override
    public void load(ImageView imageView, AlbumFile albumFile) {
        load(imageView, albumFile.getPath());
    }

    @Override
    public void load(ImageView imageView, String url) {
        Glide.with(imageView.getContext())
                .load(url)
                .error(R.drawable.loading_logo)
                .placeholder(R.drawable.loading_logo)
                .crossFade()
                .into(imageView);
    }
}
