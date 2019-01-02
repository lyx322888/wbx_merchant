package com.wbx.merchant.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXMiniProgramObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wbx.merchant.R;
import com.wbx.merchant.baseapp.AppConfig;

/**
 * 分享工具类
 *
 * @author Zero
 * @date 2018/6/22
 */
public class ShareUtils {
    private static ShareUtils instance;

    public static ShareUtils getInstance() {
        if (instance == null) {
            instance = new ShareUtils();
        }
        return instance;
    }

    public void share(final Context context, final String title, final String descrption, final String imageUrl, final String clickUrl) {
        final Dialog shareDialog = new Dialog(context, R.style.ActionSheetDialogStyle);
        View shareInflate = LayoutInflater.from(context).inflate(R.layout.dialog_share_view, null);
        shareDialog.setContentView(shareInflate);
        //获取当前Activity所在的窗体
        Window dialogWindow = shareDialog.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager windowManager = dialogWindow.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (display.getWidth()); //设置宽度
        dialogWindow.setAttributes(lp);
        shareDialog.show();//显示对话框

        //微信好友
        shareInflate.findViewById(R.id.share_friend_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doShare(context, title, descrption, imageUrl, clickUrl, SendMessageToWX.Req.WXSceneSession);
                shareDialog.dismiss();
            }
        });
        //微信朋友圈
        shareInflate.findViewById(R.id.share_circle_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doShare(context, title, descrption, imageUrl, clickUrl, SendMessageToWX.Req.WXSceneTimeline);
                shareDialog.dismiss();
            }
        });
        //微信收藏
        shareInflate.findViewById(R.id.share_collection_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doShare(context, title, descrption, imageUrl, clickUrl, SendMessageToWX.Req.WXSceneFavorite);
                shareDialog.dismiss();
            }
        });
        shareInflate.findViewById(R.id.share_cancel_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareDialog.dismiss();
            }
        });
    }

    private void doShare(final Context context, final String title, final String descrption, String imageUrl, final String clickUrl, final int scene) {
        if (TextUtils.isEmpty(imageUrl)) {
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.logo);
            commitShare(context, title, descrption, bitmap, clickUrl, scene);
        } else {
            if (imageUrl.startsWith("http://imgs.wbx365.com/")) {
                imageUrl = imageUrl + "?imageView2/0/w/200/h/200";
            }
            Glide.with(context).load(imageUrl).asBitmap().into(new SimpleTarget<Bitmap>(200, 200) {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    commitShare(context, title, descrption, resource, clickUrl, scene);
                }
            });
        }
    }

    private void commitShare(Context context, String title, String descrption, Bitmap resource, String clickUrl, int scene) {
        IWXAPI wxapi = WXAPIFactory.createWXAPI(context, AppConfig.WX_APP_ID);
        wxapi.registerApp(AppConfig.WX_APP_ID);
        WXWebpageObject wxWebpageObject = new WXWebpageObject();
        wxWebpageObject.webpageUrl = clickUrl;
        WXMediaMessage msg = new WXMediaMessage(wxWebpageObject);
        msg.title = title;
        msg.description = descrption;
        msg.thumbData = PictureUtil.compressWxShareImage(resource, 32);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = scene;
        wxapi.sendReq(req);
    }

    public void shareMiniProgram(final Context context, final String title, final String descrption, String imageUrl, final String path, final String clickUrl) {
        if (TextUtils.isEmpty(imageUrl)) {
            Bitmap resource = BitmapFactory.decodeResource(context.getResources(), R.drawable.logo);
            commitShareMiniProgram(context, title, descrption, resource, path, clickUrl);
        } else {
            if (imageUrl.startsWith("http://imgs.wbx365.com/")) {
                imageUrl = imageUrl + "?imageView2/0/w/500/h/500";
            }
            Glide.with(context).load(imageUrl).asBitmap().into(new SimpleTarget<Bitmap>(500, 500) {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    commitShareMiniProgram(context, title, descrption, resource, path, clickUrl);
                }
            });
        }
    }

    public void commitShareMiniProgram(final Context context, final String title, final String descrption, Bitmap resource, String path, final String clickUrl) {
        IWXAPI wxapi = WXAPIFactory.createWXAPI(context, AppConfig.WX_APP_ID);
        wxapi.registerApp(AppConfig.WX_APP_ID);
        WXMiniProgramObject miniProgramObj = new WXMiniProgramObject();
        miniProgramObj.webpageUrl = clickUrl; // 兼容低版本的网页链接
        miniProgramObj.miniprogramType = WXMiniProgramObject.MINIPTOGRAM_TYPE_RELEASE;// 正式版:0，测试版:1，体验版:2
        miniProgramObj.userName = "gh_c90fe5b5ba40";     // 小程序原始id
        miniProgramObj.path = path;            //小程序页面路径
        WXMediaMessage msg = new WXMediaMessage(miniProgramObj);
        msg.title = title;                    // 小程序消息title
        msg.description = descrption;               // 小程序消息desc
        msg.thumbData = PictureUtil.compressWxShareImage(resource, 128);                      // 小程序消息封面图片，小于128k

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneSession;  // 目前支持会话
        wxapi.sendReq(req);
    }
}
