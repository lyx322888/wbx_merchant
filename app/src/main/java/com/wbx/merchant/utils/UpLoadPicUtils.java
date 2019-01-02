package com.wbx.merchant.utils;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.utils.UrlSafeBase64;
import com.wbx.merchant.baseapp.AppConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by wushenghui on 2017/6/23.
 * 图片上传到七牛的工具类
 */

public class UpLoadPicUtils {
    private static final String AccessKey = "L_kSa1gvGrbPOzPMFvvA7RhO4L7Ux4z4-R4qXull";
    private static final String SecretKey = "goHUc06JheQLH9ESXjfE1avmJI8LJVR3bRsOIjc_";
    private static final String MAC_NAME = "HmacSHA1";
    private static final String ENCODING = "UTF-8";

    /**
     * 批量上传图片
     *
     * @param lstPic
     * @return
     */
    public static void batchUpload(final List<String> lstPic, final BatchUpLoadPicListener listener) {
        if (lstPic == null || lstPic.size() == 0) {
            new IllegalArgumentException("please select pictures");
        }
        final HashMap<Integer, String> hmQiNiuPath = new HashMap<>();
        for (int i = 0; i < lstPic.size(); i++) {
            String path = lstPic.get(i);
            if (path.startsWith("http")) {
                //七牛图片
                hmQiNiuPath.put(i, path);
                if (hmQiNiuPath.size() == lstPic.size()) {
                    //所有图片上传完了
                    if (listener != null) {
                        final List<String> lstQiNiuPath = new ArrayList<>();
                        for (Integer integer : hmQiNiuPath.keySet()) {
                            lstQiNiuPath.add(hmQiNiuPath.get(integer));
                        }
                        listener.success(lstQiNiuPath);
                    }
                }
            } else {
                //本地图片
                final int finalI = i;
                UpLoadPicUtils.upOnePic(path, new UpLoadPicUtils.UpLoadPicListener() {
                    @Override
                    public void success(String qiNiuPath) {
                        hmQiNiuPath.put(finalI, qiNiuPath);
                        if (hmQiNiuPath.size() == lstPic.size()) {
                            //所有图片上传完了
                            if (listener != null) {
                                final List<String> lstQiNiuPath = new ArrayList<>();
                                for (Integer integer : hmQiNiuPath.keySet()) {
                                    lstQiNiuPath.add(hmQiNiuPath.get(integer));
                                }
                                listener.success(lstQiNiuPath);
                            }
                        }
                    }

                    @Override
                    public void error() {
                        if (listener != null) {
                            listener.error();
                        }
                    }
                });
            }
        }
    }

    public static void upOnePic(String photoPath, final UpLoadPicListener listener) {
        try {
            // 1 构造上传策略
            JSONObject _json = new JSONObject();
            long _dataline = System.currentTimeMillis() / 1000 + 3600;
            _json.put("deadline", _dataline);// 有效时间为一个小时
            _json.put("scope", "wbx365888");
            String _encodedPutPolicy = UrlSafeBase64.encodeToString(_json
                    .toString().getBytes());
            byte[] _sign = HmacSHA1Encrypt(_encodedPutPolicy, SecretKey);
            String _encodedSign = UrlSafeBase64.encodeToString(_sign);
            String _uploadToken = AccessKey + ':' + _encodedSign + ':'
                    + _encodedPutPolicy;

            UploadManager uploadManager = new UploadManager();
            uploadManager.put(photoPath, null, _uploadToken,
                    new UpCompletionHandler() {
                        @Override
                        public void complete(String key, ResponseInfo info,
                                             JSONObject response) {
                            try {
                                if (info.isOK()) {
                                    listener.success(AppConfig.IMAGES + response.getString("hash"));
                                } else {
                                    listener.error();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    }, null);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static byte[] HmacSHA1Encrypt(String encryptText, String encryptKey)
            throws Exception {
        byte[] data = encryptKey.getBytes(ENCODING);
        // 根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称
        SecretKeySpec secretKeySpec = new SecretKeySpec(data, MAC_NAME);
        // 生成一个指定 Mac 算法 的 Mac 对象
        Mac mac = Mac.getInstance(MAC_NAME);
        // 用给定密钥初始化 Mac 对象
        mac.init(secretKeySpec);
        byte[] text = encryptText.getBytes(ENCODING);
        // 完成 Mac 操作
        return mac.doFinal(text);
    }

    public interface UpLoadPicListener {
        void success(String qiNiuPath);

        void error();
    }

    public interface BatchUpLoadPicListener {
        void success(List<String> qiNiuPath);

        void error();
    }
}
