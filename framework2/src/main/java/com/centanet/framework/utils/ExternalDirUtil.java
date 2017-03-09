package com.centanet.framework.utils;

import android.os.Environment;

import com.orhanobut.logger.Logger;

import java.io.File;

/**
 * ExternalCacheDirUtil SD卡目录工具
 * <p>
 */
@SuppressWarnings("unused")
public final class ExternalDirUtil {

    private static final String TAG = "ExternalCacheDirUtil";

    private ExternalDirUtil() {
        // Utility class.
    }

    /**
     * 图片下载目录[拍照目录]
     */
    public static String imageDownloadDir() {
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                Logger.t(TAG).e("Unable to create external cache directory");
            }
        }
        return dir.getAbsolutePath();
    }

    /**
     * apk保存目录[downloads文件夹]
     */
    public static String apkCacheDir() {
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                Logger.t(TAG).e("Unable to create external cache directory");
            }
        }
        return dir.getAbsolutePath();
    }
}
