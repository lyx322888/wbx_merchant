package com.wbx.merchant.update;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import com.wbx.merchant.base.BaseApplication;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;


public class UpdateService extends Service {
    public static final String TAG = "UpdateService";

    /**
     * 进度变化达到多大时更新
     */
    public static final int UPDATE_NUMBER_SIZE = 1;
    public static final String URL = "downloadUrl";
    public static final String STORE_DIR = "storeDir";
    private String downloadUrl;
    /**
     * 保存的路径，默认sdcard/Android/package/update
     */
    private static String storeDir;
    private UpdateProgressListener updateProgressListener;
    private LocalBinder localBinder = new LocalBinder();

    /**
     * Class used for the client Binder.
     */
    public class LocalBinder extends Binder {
        public UpdateService getService() {
            return UpdateService.this;
        }

        /**
         * set update progress call back
         *
         * @param listener
         */
        public void setUpdateProgressListener(UpdateProgressListener listener) {
            UpdateService.this.setUpdateProgressListener(listener);
        }
    }

    /**
     * 开始下载，防止重复下载
     */
    private boolean startDownload;
    private DownloadApk downloadApkTask;

    /**
     * 点击通知栏去进行安装
     *
     * @param path
     * @return
     */
    private static Intent installIntent(String path) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        File file = new File(path);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri apkUri = FileProvider.getUriForFile(BaseApplication.getInstance(), "com.wbx.merchant.fileProvider", file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri uri = Uri.fromFile(file);
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
        }
        return intent;
    }

    private static String getSaveFileName(String downloadUrl) {
        if (downloadUrl == null || TextUtils.isEmpty(downloadUrl)) {
            return "noName.apk";
        }
        return downloadUrl.substring(downloadUrl.lastIndexOf("/"));
    }

    private static File getDownloadDir(UpdateService service) {
        File downloadDir = null;
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            if (storeDir != null) {
                downloadDir = new File(Environment.getExternalStorageDirectory(), storeDir);
            } else {
                downloadDir = new File(service.getExternalCacheDir(), "update");
            }
        } else {
            downloadDir = new File(service.getCacheDir(), "update");
        }
        if (!downloadDir.exists()) {
            downloadDir.mkdirs();
        }
        return downloadDir;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        if (!startDownload && intent != null) {
            startDownload = true;
            downloadUrl = intent.getStringExtra(URL);
            storeDir = intent.getStringExtra(STORE_DIR);
            downloadApkTask = new DownloadApk(this);
            downloadApkTask.execute(downloadUrl);
        }
        return localBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return true;
    }

    public void setUpdateProgressListener(UpdateProgressListener updateProgressListener) {
        this.updateProgressListener = updateProgressListener;
    }

    @Override
    public void onDestroy() {
        if (downloadApkTask != null) {
            downloadApkTask.cancel(true);
        }

        if (updateProgressListener != null) {
            updateProgressListener = null;
        }
        super.onDestroy();
    }

    private void start() {
        if (updateProgressListener != null) {
            updateProgressListener.start();
        }
    }

    private void update(int progress) {
        if (updateProgressListener != null) {
            updateProgressListener.update(progress);
        }
    }

    private void success(String path) {
        if (FileHelper.checkFileIsExists(path)) {
            Intent i = installIntent(path);
            if (updateProgressListener != null) {
                updateProgressListener.success();
            }
            startActivity(i);
        } else {
            DataCleanManager.deleteFilesByDirectory2(storeDir);
        }
        stopSelf();
    }

    private void error() {
        if (updateProgressListener != null) {
            updateProgressListener.error();
        }
        stopSelf();
    }

    private static class DownloadApk extends AsyncTask<String, Integer, String> {

        private WeakReference<UpdateService> updateServiceWeakReference;

        public DownloadApk(UpdateService service) {
            updateServiceWeakReference = new WeakReference<>(service);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            UpdateService service = updateServiceWeakReference.get();
            if (service != null) {
                service.start();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            DataCleanManager.deleteFilesByDirectory2("" +
                    UpdateService.getDownloadDir(updateServiceWeakReference.get()).getAbsolutePath());
            final String downloadUrl = params[0];

            final File file = new File(UpdateService.getDownloadDir(updateServiceWeakReference.get()),
                    UpdateService.getSaveFileName(downloadUrl));
            File dir = file.getParentFile();
            if (!dir.exists()) {
                dir.mkdirs();
            }

            HttpURLConnection httpConnection = null;
            InputStream is = null;
            FileOutputStream fos = null;
            int updateTotalSize;
            java.net.URL url;
            try {
                url = new URL(downloadUrl);
                httpConnection = (HttpURLConnection) url.openConnection();
                httpConnection.setConnectTimeout(5000);
                httpConnection.setReadTimeout(5000);
                if (httpConnection.getResponseCode() != 200) {
                    return null;
                }

                updateTotalSize = httpConnection.getContentLength();
                if (file.exists()) {
                    if (updateTotalSize == file.length()) {
                        // 下载完成
                        return file.getAbsolutePath();
                    } else {
                        file.delete();
                    }
                }
                file.createNewFile();
                is = httpConnection.getInputStream();
                fos = new FileOutputStream(file, false);
                byte[] buffer = new byte[4096];

                int readSize = 0;
                long currentSize = 0;

                int lastProgressNumber = 0;
                int currentProgressNumber = 0;
                while ((readSize = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, readSize);
                    currentSize += readSize;
                    currentProgressNumber = (int) (currentSize * 100 / updateTotalSize);
                    if (currentProgressNumber - lastProgressNumber >= UPDATE_NUMBER_SIZE) {
                        lastProgressNumber = currentProgressNumber;
                        publishProgress(currentProgressNumber);
                    }
                }
                // download success
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            } finally {
                if (httpConnection != null) {
                    httpConnection.disconnect();
                }
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return file.getAbsolutePath();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            UpdateService service = updateServiceWeakReference.get();
            if (service != null) {
                service.update(values[0]);
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            UpdateService service = updateServiceWeakReference.get();
            if (service != null) {
                if (s != null) {
                    service.success(s);
                } else {
                    service.error();
                }
            }
        }
    }
}
