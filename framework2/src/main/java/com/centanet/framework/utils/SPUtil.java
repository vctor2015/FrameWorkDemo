package com.centanet.framework.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * {@link SharedPreferences}工具类
 */
@SuppressWarnings("all")
public final class SPUtil {

    private static final String FILE_NAME = "AppShared";

    private SPUtil() {
        // utility class.
    }

    /**
     * SharedPreferences
     */
    private static SharedPreferences preferences(Context context) {
        return context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    }

    /**
     * Editor
     */
    private static SharedPreferences.Editor editor(Context context) {
        return preferences(context).edit();
    }

    /**
     * 设置
     */
    public static void setInt(Context context, String key, int value) {
        editor(context).putInt(key, value).apply();
    }

    /**
     * 获取
     */
    public static int getInt(Context context, String key, int defVale) {
        return preferences(context).getInt(key, defVale);
    }

    /**
     * 设置
     */
    public static void setString(Context context, String key, String value) {
        editor(context).putString(key, value).apply();
    }

    /**
     * 获取
     */
    public static String getString(Context context, String key, String defVale) {
        return preferences(context).getString(key, defVale);
    }

    /**
     * 设置
     */
    public static void setBoolean(Context context, String key, boolean value) {
        editor(context).putBoolean(key, value).apply();
    }

    /**
     * 获取
     */
    public static boolean getBoolean(Context context, String key, boolean defVale) {
        return preferences(context).getBoolean(key, defVale);
    }

    /**
     * 设置
     */
    public static void setLong(Context context, String key, long value) {
        editor(context).putLong(key, value).apply();
    }

    /**
     * 获取
     */
    public static long getLong(Context context, String key, long value) {
        return preferences(context).getLong(key, value);
    }

    /**
     * 设置
     */
    public static void setFloat(Context context, String key, float value) {
        editor(context).putFloat(key, value).apply();
    }

    /**
     * 获取
     */
    public static float getFloat(Context context, String key, float defVale) {
        return preferences(context).getFloat(key, defVale);
    }
}
