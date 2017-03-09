package com.centanet.framework.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.text.TextUtils;

import com.centanet.framework.R;
import com.centanet.framework.receiver.ImageDownloadReceiver;
import com.centanet.framework.utils.EncryptUtil;
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
 * 描述:图片下载服务
 */
@SuppressWarnings("unused")
public class ImageDownloadService extends IntentService {

    private static final String TAG = "ImageDownloadService";

    /**
     * 网络图片地址
     */
    private static final String IMAGE_URL = "IMAGE_URL";

    private OkHttpClient mOkHttpClient;

    public ImageDownloadService() {
        super("ImageDownloadService");
    }

    /**
     * @param imgUrl 网络图片地址
     */
    public static void startDownloadImage(Context context, String imgUrl) {
        context.startService(new Intent(context, ImageDownloadService.class)
                .putExtra(IMAGE_URL, imgUrl));
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.t(TAG).d("onCreate");
        mOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10000, TimeUnit.MILLISECONDS)
                .readTimeout(10000, TimeUnit.MILLISECONDS)
                .build();
    }

    @Override
    public void onDestroy() {
        Logger.t(TAG).d("onDestroy");
        super.onDestroy();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Logger.t(TAG).d("onHandleIntent");
        if (intent != null) {
            String url = intent.getStringExtra(IMAGE_URL);
            if (!TextUtils.isEmpty(url)) {
                String downloadDir = ExternalDirUtil.imageDownloadDir();
                File dir = new File(downloadDir);
                if (dir.canWrite()) {
                    File file = new File(dir,
                            String.format(Locale.CHINA, "%s.jpg", EncryptUtil.md5(url)));
                    if (file.exists()) {
                        sendMsg(getString(R.string.image_download_success));
                    } else {
                        Call call = mOkHttpClient.newCall(new Request.Builder().url(
                                url).get().build());
                        FileOutputStream fos = null;
                        InputStream inputStream = null;
                        try {
                            Response response = call.execute();
                            if (200 == response.code()) {
                                inputStream = response.body().byteStream();
                                fos = new FileOutputStream(file);
                                byte[] buff = new byte[1024 * 4];
                                while (true) {
                                    int read = inputStream.read(buff);
                                    if (read == -1) {
                                        break;
                                    }
                                    fos.write(buff, 0, read);
                                    fos.flush();
                                }
                                sendMsg(getString(R.string.image_download_success));
                                MediaScannerConnection.scanFile(getApplicationContext(),
                                        new String[]{file.getAbsolutePath()},
                                        null,
                                        null);
                            } else {
                                sendMsg(getString(R.string.image_download_failed));
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            file.deleteOnExit();
                            sendMsg(getString(R.string.image_download_failed));
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
                } else {
                    sendMsg(getString(R.string.sd_unable));
                }
            }
        }
    }

    private void sendMsg(String msg) {
        Intent intent = new Intent(ImageDownloadReceiver.ACTION);
        intent.putExtra(ImageDownloadReceiver.MSG, msg);
        sendBroadcast(intent);
    }
}
