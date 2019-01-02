package com.wbx.merchant.utils;

/**
 * Created by wushenghui on 2017/8/1.
 * 图片处理工具类
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class PictureUtil {

    /**
     * 得到临时图片路径
     *
     * @param filePath
     * @return
     * @throws IOException
     */
    public static String bitmapToPath(String filePath) throws IOException {

        Bitmap bm = BitmapFactory.decodeFile(filePath);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 40, baos);

        //得到文件名
        String imgName = getfilepath(filePath);
        //得到存放路径
        String sdPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ImgTmp";
        //获取 sdcard的跟目录

        File parent = new File(sdPath);
        if (!parent.exists()) {
            //创建路径
            parent.mkdirs();
        }
        //写入 临时文件
        File file = new File(parent, imgName);
        file.createNewFile();
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(baos.toByteArray());
        fos.flush();
        fos.close();
        baos.close();
        //返回图片路径
        return sdPath + "/" + imgName;

    }

    /**
     * 保存图片，得到缓存地址
     */
    public static String saveBitmap(Bitmap bitmap, String fileName) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 40, baos);

        //得到文件名
        String imgName = getfilepath(fileName);
        //得到存放路径
        String sdPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ImgTmp";
        //获取 sdcard的跟目录

        File parent = new File(sdPath);
        if (!parent.exists()) {
            //创建路径
            parent.mkdirs();
        }
        //写入 临时文件
        File file = new File(parent, imgName);
        file.createNewFile();
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(baos.toByteArray());
        fos.flush();
        fos.close();
        baos.close();
        //返回图片路径
        return sdPath + "/" + imgName;

    }

    /**
     * @param path
     * @return
     */
    private static String getfilepath(String path) {
        return System.currentTimeMillis() + getExtensionName(path);
    }


    /*
     * 获取文件扩展名
     */
    private static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot, filename.length());
            }
        }
        return filename;
    }


    /**
     * 删除临时文件
     *
     * @param imgs
     */
    public static void deleteImgTmp(List<String> imgs) {

        for (String string : imgs) {
            File file = new File(string);
            if (file.exists()) {
                file.delete();
            }
        }
    }

    //保存文件到指定路径
    public static boolean saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        String storePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "wbx";
        File appDir = new File(storePath);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            //通过io流的方式来压缩保存图片
            boolean isSuccess = bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();

            //把文件插入到系统图库
            //MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);

            //保存图片后发送广播通知更新数据库
            Uri uri = Uri.fromFile(file);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            if (isSuccess) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 压缩图片到指定大小
     *
     * @param image
     * @return
     */
    public static byte[] compressWxShareImage(Bitmap image, int maxSize) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > maxSize) {  //循环判断如果压缩后图片是否大于128kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        return baos.toByteArray();
    }
}
