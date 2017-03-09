package com.centanet.framework.glide;

import android.app.Activity;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.Fragment;

import com.bumptech.glide.BitmapTypeRequest;
import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;

/**
 * Created by vctor2015 on 16/7/8.
 * <p>
 * 描述:图片加载
 */
@SuppressWarnings("unused")
public final class ImageLoad {

    private ImageLoad() {
        //Utility Class
    }

    /**
     * 初始化
     */
    public static DrawableRequestBuilder<String> get(Activity activity) {
        return Glide.with(activity)
                .fromString()
                .listener(new GlideLogListener<String, GlideDrawable>())
                .crossFade();
    }

    /**
     * 初始化
     */
    public static DrawableRequestBuilder<String> get(Fragment fragment) {
        return Glide.with(fragment)
                .fromString()
                .listener(new GlideLogListener<String, GlideDrawable>())
                .crossFade();
    }

    /**
     * 初始化圆形加载
     */
    public static BitmapTypeRequest<String> getRound(Fragment fragment) {
        return Glide
                .with(fragment)
                .from(String.class)
                .asBitmap();
    }

    /**
     * 初始化圆形加载
     */
    public static BitmapTypeRequest<String> getRound(Activity activity) {
        return Glide
                .with(activity)
                .from(String.class)
                .asBitmap();
    }

    /**
     * 初始化Uri类型
     */
    public static DrawableRequestBuilder<Uri> getUri(Activity activity) {
        return Glide.with(activity)
                .fromUri()
                .skipMemoryCache(Build.VERSION.SDK_INT < 21)
                .crossFade();
    }
}
