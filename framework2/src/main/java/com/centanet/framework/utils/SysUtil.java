package com.centanet.framework.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import java.util.Locale;

/**
 * Created by vctor2015 on 16/7/18.
 * <p>
 * 描述:SysUtil
 */
@SuppressWarnings("unused")
public final class SysUtil {

    private SysUtil() {
        //Utility Class
    }

    /**
     * 设备id
     */
    @SuppressWarnings("deprecation")
    public static String deviceId() {
        return String.format(Locale.CHINA, "35%d%d%d%d%d%d%d%d%d%d%d%d%d",
                Build.BOARD.length() % 10,
                Build.BRAND.length() % 10,
                Build.CPU_ABI.length() % 10,
                Build.DEVICE.length() % 10,
                Build.DISPLAY.length() % 10,
                Build.HOST.length() % 10,
                Build.ID.length() % 10,
                Build.MANUFACTURER.length() % 10,
                Build.MODEL.length() % 10,
                Build.PRODUCT.length() % 10,
                Build.TAGS.length() % 10,
                Build.TYPE.length() % 10,
                Build.USER.length() % 10);
    }

    /**
     * 当前进程名
     */
    public static String currentProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(
                Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    /**
     * 短信
     */
    public static void sendMsg(Context context, String mobile, String msg) {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + mobile));
        intent.putExtra("sms_body", msg);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
