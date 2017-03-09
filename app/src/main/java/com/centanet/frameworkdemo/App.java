package com.centanet.frameworkdemo;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.centanet.framework.http.LoggerInterceptor;
import com.centanet.framework.http.okhttpclient.OkHttpClient4Api;
import com.centanet.framework.http.okhttpclient.OkHttpClient4Glide;
import com.centanet.framework.utils.SPUtil;
import com.centanet.framework.utils.SysUtil;
import com.centanet.frameworkdemo.activities.PatternUnlockActivity;
import com.centanet.frameworkdemo.constants.SPConst;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.squareup.leakcanary.LeakCanary;
import com.taobao.hotfix.HotFixManager;
import com.taobao.hotfix.PatchLoadStatusListener;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;

/**
 * Created by vctor2015 on 2016/11/17. <p> 描述:Application
 */

public class App extends Application {

    private static final String TAG_ACTIVITY_LIFE = "TAG_ACTIVITY_LIFE";

    private long mActivityOperateTime;//页面最后操作时间「create、pause、stop」

    private ActivityLifecycleCallbacks mActivityLifecycleCallbacks =
            new ActivityLifecycleCallbacks() {

                @Override
                public void onActivityCreated(Activity activity, Bundle bundle) {
                    Logger.t(TAG_ACTIVITY_LIFE).d("onActivityCreated : %s",
                            activity.getClass().getSimpleName());
                    mActivityOperateTime = System.currentTimeMillis();
                }

                @Override
                public void onActivityStarted(Activity activity) {
                    Logger.t(TAG_ACTIVITY_LIFE).d("onActivityStarted : %s",
                            activity.getClass().getSimpleName());
                    long currentTime = System.currentTimeMillis();
                    Logger.d("onActivityStarted check operate[time : %d]",
                            currentTime - mActivityOperateTime);
                    if (currentTime - mActivityOperateTime > 9999) {
                        //符合1秒条件再进行锁定时间比较
                        SharedPreferences sharedPreferences =
                                PreferenceManager.getDefaultSharedPreferences(
                                        getApplicationContext());
                        if (sharedPreferences.getBoolean(
                                getString(R.string.key_pattern_lock_switch), false) &&
                                !TextUtils.isEmpty(
                                        SPUtil.getString(activity, SPConst.PATTERN_LOCK, null))) {
                            //已开启验证并设置了图案
                            String patternLockLimit = sharedPreferences.getString(
                                    getString(R.string.key_pattern_lock_limit), "1");
                            if (currentTime - mActivityOperateTime > Integer.parseInt(
                                    patternLockLimit) * 10000) {
                                activity.startActivity(
                                        new Intent(activity, PatternUnlockActivity.class));
                            }
                        }
                    }
                }

                @Override
                public void onActivityResumed(Activity activity) {
                    Logger.t(TAG_ACTIVITY_LIFE).d("onActivityResumed : %s",
                            activity.getClass().getSimpleName());
                }

                @Override
                public void onActivityPaused(Activity activity) {
                    Logger.t(TAG_ACTIVITY_LIFE).d("onActivityPaused : %s",
                            activity.getClass().getSimpleName());
                    mActivityOperateTime = System.currentTimeMillis();
                }

                @Override
                public void onActivityStopped(Activity activity) {
                    Logger.t(TAG_ACTIVITY_LIFE).d("onActivityStopped : %s",
                            activity.getClass().getSimpleName());
                    mActivityOperateTime = System.currentTimeMillis();
                }

                @Override
                public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

                }

                @Override
                public void onActivityDestroyed(Activity activity) {
                    Logger.t(TAG_ACTIVITY_LIFE).d("onActivityDestroyed : %s",
                            activity.getClass().getSimpleName());
                }
            };

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.init("Logger")//TAG标签
                .methodCount(0)//方法数量
                .hideThreadInfo()//隐藏线程信息
                .logLevel(LogLevel.FULL);//日志层级

        String currentProgressName = SysUtil.currentProcessName(this);
        if (BuildConfig.APPLICATION_ID.equals(currentProgressName)) {
            LeakCanary.install(this);
            registerActivityLifecycleCallbacks(mActivityLifecycleCallbacks);
            initRetrofit();
            initGlide();
        }
        hotfix();
    }

    /**
     * 初始化Retrofit <p> 连接超时：10s<br> 读取超时：10s<br> 请求头信息{@link HeadersInterceptor}<br>
     * 日志输出{@link
     * LoggerInterceptor}<br> 请求缓存
     */
    private void initRetrofit() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10000, TimeUnit.MILLISECONDS)
                .readTimeout(10000, TimeUnit.MILLISECONDS)
                .addInterceptor(new HeadersInterceptor())
                .addInterceptor(new LoggerInterceptor(LoggerInterceptor.Level.HEADERS))
                .cache(new Cache(new File(getCacheDir(), "http"), 10 * 1024 * 1024))
                .build();
        OkHttpClient4Api.initClient(okHttpClient);
    }

    /**
     * 初始化图片加载 <p> 连接超时：10s<br> 读取超时：10s<br> 网络图片请求拦截器{@link GlideLimitInterceptor}
     */
    private void initGlide() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10000, TimeUnit.MILLISECONDS)
                .readTimeout(10000, TimeUnit.MILLISECONDS)
                .addInterceptor(new GlideLimitInterceptor())
                .build();
        OkHttpClient4Glide.initClient(okHttpClient);
    }

    /**
     * hotfix
     */
    private void hotfix() {
        HotFixManager.getInstance().initialize(this,
                BuildConfig.VERSION_NAME,
                getString(R.string.ali_id),
                BuildConfig.DEBUG,
                new PatchLoadStatusListener() {
                    @Override
                    public void onload(int i, int i1, String s, int i2) {
                        Logger.t("HotFixManager").d("onLoad : %d\n%d\n%s\n%d", i, i1, s,
                                i2);
                    }
                });
    }
}
