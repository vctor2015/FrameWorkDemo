package com.centanet.frameworkdemo;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.centanet.framework.glide.integration.OkHttpGlideModule;
import com.centanet.framework.glide.integration.OkHttpUrlLoader;
import com.centanet.framework.http.okhttpclient.OkHttpClient4Glide;

import java.io.InputStream;

/**
 * Created by vctor2015 on 16/7/11.
 * <p>
 * 描述:替换Glide's default{@link com.bumptech.glide.load.model.ModelLoader}
 */

public class AppGlideModule extends OkHttpGlideModule {

    @Override
    public void registerComponents(Context context, Glide glide) {
        glide.register(GlideUrl.class,
                InputStream.class,
                new OkHttpUrlLoader.Factory(OkHttpClient4Glide.getInstance().getOkHttpClient()));
    }
}