package com.centanet.framework.utils;

import android.content.Context;

/**
 * app更新util
 */
public final class AppUpdateUtil {

    private static final String APK_NAME = "APK_NAME";
    private static final String UPDATE_VERSION_CODE = "UPDATE_VERSION_CODE";
    private static final String UPDATE_URL = "UPDATE_URL";

    private AppUpdateUtil() {
        // utility class.
    }

    /**
     * 设置下载所需的apk名称
     */
    public static void setApkName(Context context, String apkName) {
        SPUtil.setString(context, APK_NAME, apkName);
    }

    /**
     * 下载所需的apk名称
     */
    public static String getApkName(Context context) {
        return SPUtil.getString(context, APK_NAME, "");
    }

    /**
     * 设置所需下载的apk版本号
     */
    public static void setUpdateVersionCode(Context context, int versionCode) {
        SPUtil.setInt(context, UPDATE_VERSION_CODE, versionCode);
    }

    /**
     * 所需下载的apk版本号
     */
    public static int getUpdateVersionCode(Context context) {
        return SPUtil.getInt(context, UPDATE_VERSION_CODE, 1);
    }

    /**
     * 设置apk下载地址
     */
    public static void setUpdateUrl(Context context, String url) {
        SPUtil.setString(context, UPDATE_URL, url);
    }

    /**
     * apk下载地址
     */
    public static String getUpdateUrl(Context context) {
        return SPUtil.getString(context, UPDATE_URL, "");
    }

}
