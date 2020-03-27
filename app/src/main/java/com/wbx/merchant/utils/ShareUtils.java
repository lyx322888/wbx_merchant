package com.wbx.merchant.utils;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

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
    //分享窗口
    public void shareHb(final Context context, final View hb, final String title, final String descrption, final Bitmap bitmap, final String clickUrl) {
        final Dialog shareDialog = new Dialog(context, R.style.DialogTheme);
        View shareInflate = LayoutInflater.from(context).inflate(R.layout.share_pop_save_view, null);
        shareDialog.setContentView(shareInflate);
        //获取当前Activity所在的窗体
        Window dialogWindow = shareDialog.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager windowManager = dialogWindow.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = display.getWidth(); //设置宽度
        dialogWindow.setAttributes(lp);
        dialogWindow.setWindowAnimations(R.style.main_menu_animStyle);
        shareDialog.show();//显示对话框
        shareDialog.setCanceledOnTouchOutside(true);
        //微信好友
        shareInflate.findViewById(R.id.share_wechat_friends_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commitShare(context, title, descrption, bitmap, clickUrl, SendMessageToWX.Req.WXSceneSession);
                shareDialog.dismiss();
            }
        });
        shareInflate.findViewById(R.id.share_bctp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //保存图片
                shareDialog.dismiss();
                ScannerUtils.saveImageToGallery(context, FormatUtil.loadBitmapFromView(hb), ScannerUtils.ScannerType.RECEIVER);
            }
        });

        shareInflate.findViewById(R.id.poop_share_cancel_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareDialog.dismiss();
            }
        });
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
    //分享图片到朋友圈
    public  void shareToTimeLine(Bitmap drawable,Context context) {
        Intent intent = new Intent();
        ComponentName comp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI");
        intent.setComponent(comp);
        intent.setAction("android.intent.action.SEND");
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_TEXT, "");
        final Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(
                context.getContentResolver(), drawable, null, null));
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        context.startActivity(intent);
    }
    //分享图片到微信
    public static void shareImgUI(Bitmap drawable,Context context) {
        Intent intent = new Intent();
        ComponentName comp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI");
        intent.setComponent(comp);
        intent.setAction("android.intent.action.SEND");
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_TEXT, "");
        final Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(
                context.getContentResolver(), drawable, null, null));
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        context.startActivity(intent);
    }

    public void doShare(final Context context, final String title, final String descrption, String imageUrl, final String clickUrl, final int scene) {
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
