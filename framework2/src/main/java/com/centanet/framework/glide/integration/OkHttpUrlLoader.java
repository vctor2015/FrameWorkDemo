package com.centanet.framework.glide.integration;

import android.content.Context;

import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.GenericLoaderFactory;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;

import java.io.InputStream;

import okhttp3.Call;
import okhttp3.OkHttpClient;

/**
 * A simple model loader for fetching media over http/https using OkHttp.
 */
public final class OkHttpUrlLoader implements ModelLoader<GlideUrl, InputStream> {

    private final Call.Factory mFactory;

    private OkHttpUrlLoader(Call.Factory factory) {
        this.mFactory = factory;
    }

    @Override
    public DataFetcher<InputStream> getResourceFetcher(GlideUrl model, int width, int height) {
        return new OkHttpStreamFetcher(mFactory, model);
    }

    /**
     * The default factory for {@link OkHttpUrlLoader}s.
     */
    public static class Factory implements ModelLoaderFactory<GlideUrl, InputStream> {

        private static volatile Call.Factory sFactory;
        private Call.Factory mFactory1;

        /**
         * Constructor for a new Factory that runs requests using a static singleton mFactory.
         */
        Factory() {
            this(getFactory());
        }

        /**
         * Constructor for a new Factory that runs requests using given mFactory.
         *
         * @param factory this is typically an instance of {@code OkHttpClient}.
         */
        public Factory(Call.Factory factory) {
            this.mFactory1 = factory;
        }

        private static Call.Factory getFactory() {
            if (sFactory == null) {
                synchronized (Factory.class) {
                    if (sFactory == null) {
                        sFactory = new OkHttpClient();
                    }
                }
            }
            return sFactory;
        }

        @Override
        public ModelLoader<GlideUrl, InputStream> build(Context context,
                GenericLoaderFactory factories) {
            return new OkHttpUrlLoader(mFactory1);
        }

        @Override
        public void teardown() {
            // Do nothing, this instance doesn't own the mFactory.
        }
    }
}