package com.centanet.frameworkdemo;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by vctor2015 on 16/7/18.
 * <p>
 * 描述:请求头信息拦截器
 */

final class HeadersInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder requestBuilder = chain.request().newBuilder();
        requestBuilder
                .addHeader("platform", "android")
                .addHeader("appVersion", String.valueOf(BuildConfig.VERSION_CODE));
        return chain.proceed(requestBuilder.build());
    }

}
