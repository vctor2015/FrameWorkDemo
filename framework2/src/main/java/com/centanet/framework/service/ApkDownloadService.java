package com.centanet.framework.service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;

import com.centanet.framework.R;
import com.centanet.framework.utils.AppUpdateUtil;
import com.centanet.framework.utils.ExternalDirUtil;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by vctor2015 on 16/7/14.
 * <p>
 * 描述:apk下载服务
 */

public class ApkDownloadService extends Service {

    private static final String TAG = "ApkDownloadService";

    private NotificationManagerCompat mNotificationManagerCompat;
    private NotificationCompat.Builder mBuilder;
    private int mNotificationId;

    private String mUpdateUrl;
    private File mFile;
    private OkHttpClient mOkHttpClient;

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.t(TAG).d("onCreate");
        download();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.t(TAG).d("onDestroy");
    }

    /**
     * 下载逻辑
     */
    private void download() {
        String appName = AppUpdateUtil.getApkName(this);
        int versionCode = AppUpdateUtil.getUpdateVersionCode(this);
        mUpdateUrl = AppUpdateUtil.getUpdateUrl(this);
        if (TextUtils.isEmpty(appName) ||
                versionCode < 1 ||
                TextUtils.isEmpty(mUpdateUrl)) {
            throw new IllegalArgumentException();
        } else {
            initNotification();
            mNotificationId = getApplicationInfo().packageName.hashCode();
            String downloadDir = ExternalDirUtil.apkCacheDir();
            File dir = new File(downloadDir);
            if (dir.canWrite()) {
                mFile = new File(dir,
                        String.format(Locale.CHINA, "%s-%d.apk", appName, versionCode));
                if (mFile.exists()) {
                    //最新版本已经下载
                    successAndInstall();
                    stopSelf();
                } else {
                    mOkHttpClient = new OkHttpClient.Builder()
                            .connectTimeout(10000, TimeUnit.MILLISECONDS)
                            .build();
                    new ApkDownloadTask().execute();
                }
            } else {
                //SD卡不可用
                mBuilder.setContentText(getString(R.string.sd_unable))
                        .setAutoCancel(true);
                notification();
                stopSelf();
            }
        }
    }

    /**
     * 通知初始化
     */
    private void initNotification() {
        mNotificationManagerCompat = NotificationManagerCompat.from(this);
        mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentTitle(getString(getApplicationInfo().labelRes))
                .setSmallIcon(R.drawable.ic_file_download)
                .setWhen(System.currentTimeMillis());
    }

    /**
     * 下载成功、安装
     */
    private void successAndInstall() {
        Intent intent = getInstallIntent();
        mBuilder.setContentText(getString(R.string.app_update_complete))
                .setContentInfo("")
                .setProgress(0, 0, false)
                .setContentIntent(PendingIntent.getActivity(this, 0, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT))
                .setAutoCancel(true);
        notification();
        startActivity(intent);
    }

    /**
     * 安装的Intent
     */
    private Intent getInstallIntent() {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(mFile), "application/vnd.android.package-archive");
        return intent;
    }

    /**
     * 通知
     */
    private void notification() {
        mNotificationManagerCompat.notify(mNotificationId, mBuilder.build());
    }

    private class ApkDownloadTask extends AsyncTask<Void, Long, Boolean> {

        private int mProgress;

        @Override
        protected Boolean doInBackground(Void... voids) {
            Call call = mOkHttpClient.newCall(new Request.Builder().url(mUpdateUrl).get().build());
            FileOutputStream fos = null;
            InputStream inputStream = null;
            try {
                Response response = call.execute();
                if (200 == response.code()) {
                    inputStream = response.body().byteStream();
                    fos = new FileOutputStream(mFile);
                    byte[] buff = new byte[1024 * 4];
                    long downloaded = 0;
                    long target = response.body().contentLength();
                    publishProgress(0L, target);
                    while (true) {
                        int read = inputStream.read(buff);
                        if (read == -1) {
                            break;
                        }
                        fos.write(buff, 0, read);
                        fos.flush();
                        downloaded += read;
                        publishProgress(downloaded, target);
                    }
                    return downloaded == target;
                } else {
                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        @Override
        protected void onProgressUpdate(Long... values) {
            Logger.t(TAG).d("mProgress : %d/%d", values[0], values[1]);
            int current = (int) (values[0] * 100 / values[1]);
            if (mProgress == 0 || current > mProgress) {
                mProgress = current;
                mBuilder.setContentText(getString(R.string.app_update_progress))
                        .setContentInfo(mProgress + "%")
                        .setProgress(100, mProgress, false)
                        .setAutoCancel(false);
                notification();
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean) {
                successAndInstall();
                stopSelf();
            } else {
                mFile.deleteOnExit();
                mBuilder.setContentText(getString(R.string.app_update_error))
                        .setContentInfo(getString(R.string.app_update_retry))
                        .setProgress(0, 0, false)
                        .setContentIntent(PendingIntent.getService(ApkDownloadService.this, 0,
                                new Intent(getApplicationContext(), ApkDownloadService.class),
                                PendingIntent.FLAG_CANCEL_CURRENT))
                        .setAutoCancel(true);
                notification();
                stopSelf();
            }
        }
    }
}
