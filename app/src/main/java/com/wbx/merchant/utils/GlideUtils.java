package com.wbx.merchant.utils;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.wbx.merchant.R;

/**
 * Description : 图片加载工具类 使用glide框架封装
 */
public class GlideUtils {

    public static void showSmallPic(Context context, ImageView imageView, String url) {
        if (imageView == null) {
            return;
        }
        if (TextUtils.isEmpty(url)) {
            Glide.with(context).load(R.drawable.loading_logo).centerCrop().into(imageView);
            return;
        }
        if (url.startsWith("http://imgs.wbx365.com/")) {
            url = url + "?imageView2/0/w/100/h/100";
        }
        Glide.with(context).load(url).error(R.drawable.loading_logo).centerCrop().into(imageView);
    }

    public static void showRoundSmallPic(Context context, ImageView imageView, String url) {
        if (imageView == null) {
            return;
        }
        if (TextUtils.isEmpty(url)) {
            Glide.with(context).load(R.drawable.loading_logo).centerCrop().into(imageView);
            return;
        }
        if (url.startsWith("http://imgs.wbx365.com/")) {
            url = url + "?imageView2/0/w/100/h/100";
        }
        Glide.with(context).load(url).error(R.drawable.loading_logo).centerCrop().transform(new GlideRoundImage(context)).into(imageView);
    }

    public static void showMediumPic(Context context, ImageView imageView, String url) {
        if (imageView == null) {
            return;
        }
        if (TextUtils.isEmpty(url)) {
            Glide.with(context).load(R.drawable.loading_logo).centerCrop().into(imageView);
            return;
        }
        if (url.startsWith("http://imgs.wbx365.com/")) {
            url = url + "?imageView2/0/w/200/h/200";
        }
        Glide.with(context).load(url).error(R.drawable.loading_logo).centerCrop().into(imageView);
    }

    public static void showRoundMediumPic(Context context, ImageView imageView, String url) {
        if (imageView == null) {
            return;
        }
        if (TextUtils.isEmpty(url)) {
            Glide.with(context).load(R.drawable.loading_logo).centerCrop().into(imageView);
            return;
        }
        if (url.startsWith("http://imgs.wbx365.com/")) {
            url = url + "?imageView2/0/w/200/h/200";
        }
        Glide.with(context).load(url).error(R.drawable.loading_logo).centerCrop().transform(new GlideRoundImage(context)).into(imageView);
    }

    public static void showBigPic(Context context, ImageView imageView, String url) {
        if (imageView == null) {
            return;
        }
        if (TextUtils.isEmpty(url)) {
            Glide.with(context).load(R.drawable.loading_logo).centerCrop().into(imageView);
            return;
        }
        if (url.startsWith("http://imgs.wbx365.com/")) {
            url = url + "?imageView2/0/w/800/h/800";
        }
        Glide.with(context).load(url).error(R.drawable.loading_logo).centerCrop().into(imageView);
    }

    public static void showRoundBigPic(Context context, ImageView imageView, String url) {
        if (imageView == null) {
            return;
        }
        if (TextUtils.isEmpty(url)) {
            Glide.with(context).load(R.drawable.loading_logo).centerCrop().into(imageView);
            return;
        }
        if (url.startsWith("http://imgs.wbx365.com/")) {
            url = url + "?imageView2/0/w/800/h/800";
        }
        Glide.with(context).load(url).error(R.drawable.loading_logo).centerCrop().transform(new GlideRoundImage(context)).into(imageView);
    }

    public static void displayUri(Context context, ImageView imageView, Uri uri) {
        if (imageView == null) {
            return;
        }
        Glide.with(context).load(uri).error(R.drawable.loading_logo).centerCrop().into(imageView);
    }
}
