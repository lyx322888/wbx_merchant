package com.wbx.merchant.utils;

import android.text.TextUtils;

/**
 * Created by Administrator on 2018/3/15 0015.
 */

public class UrlUtis {
    /**
     * 获取七牛云上图片的缩略url
     *
     * @param url
     * @return
     */
    public static String getCompressUrl(String url) {
        if (!TextUtils.isEmpty(url) && url.startsWith("http://imgs.wbx365.com/")) {
            url = url + "?imageView2/0/w/200/h/200";
        }
        return url;
    }
}
