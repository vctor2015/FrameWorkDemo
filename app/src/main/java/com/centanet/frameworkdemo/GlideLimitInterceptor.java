package com.centanet.frameworkdemo;

import com.centanet.framework.http.GlideInterceptor;

/**
 * Created by vctor2015 on 16/7/11.
 * <p>
 * 描述:图片加载限制拦截器,例如:图片仅在wifi下加载
 */

final class GlideLimitInterceptor extends GlideInterceptor {

    @Override
    protected boolean cancelRequest() {
        return false;
    }
}
