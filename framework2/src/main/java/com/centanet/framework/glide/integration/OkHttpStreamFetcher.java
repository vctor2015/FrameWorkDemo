package com.centanet.framework.glide.integration;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.util.ContentLengthInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Fetches an {@link InputStream} using the okhttp3 library.
 */
class OkHttpStreamFetcher implements DataFetcher<InputStream> {

    private final Call.Factory mFactory;
    private final GlideUrl mGlideUrl;
    private InputStream mInputStream;
    private ResponseBody mResponseBody;
    private volatile Call mCall;

    OkHttpStreamFetcher(Call.Factory factory, GlideUrl url) {
        this.mFactory = factory;
        this.mGlideUrl = url;
    }

    @Override
    public InputStream loadData(Priority priority) throws Exception {
        Request.Builder requestBuilder = new Request.Builder().url(mGlideUrl.toStringUrl());
        for (Map.Entry<String, String> headerEntry : mGlideUrl.getHeaders().entrySet()) {
            String key = headerEntry.getKey();
            requestBuilder.addHeader(key, headerEntry.getValue());
        }
        Request request = requestBuilder.build();

        Response response;
        mCall = mFactory.newCall(request);
        response = mCall.execute();
        mResponseBody = response.body();
        if (!response.isSuccessful()) {
             throw new IOException("Request failed with code: " + response.code());
        }

        long contentLength = mResponseBody.contentLength();
        mInputStream = ContentLengthInputStream.obtain(mResponseBody.byteStream(), contentLength);
        return mInputStream;
    }

    @Override
    public void cleanup() {
        try {
            if (mInputStream != null) {
                mInputStream.close();
            }
        } catch (IOException e) {
            // Ignored
        }
        if (mResponseBody != null) {
            mResponseBody.close();
        }
    }

    @Override
    public String getId() {
        return mGlideUrl.getCacheKey();
    }

    @Override
    public void cancel() {
        Call local = mCall;
        if (local != null) {
            local.cancel();
        }
    }
}