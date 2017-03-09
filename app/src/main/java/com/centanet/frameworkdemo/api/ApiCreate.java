package com.centanet.frameworkdemo.api;

import com.centanet.framework.http.okhttpclient.OkHttpClient4Api;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by vctor2015 on 2016/11/17.
 * <p>
 * 描述:
 */

public final class ApiCreate {

    private ApiCreate() {
        //utility class
    }

    /**
     * 干货
     *
     * @return GHApi
     */
    public static GHApi gh() {
        return retrofit("http://gank.io/api/").create(GHApi.class);
    }

    /**
     * 获取 Retrofit
     *
     * @param baseUrl host地址
     * @return Retrofit
     */
    private static Retrofit retrofit(String baseUrl) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(OkHttpClient4Api.getInstance().getOkHttpClient())
                .build();
    }
}
