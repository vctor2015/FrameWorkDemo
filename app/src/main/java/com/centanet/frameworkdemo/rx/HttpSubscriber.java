package com.centanet.frameworkdemo.rx;


import com.centanet.framework.http.exception.ApiException;

import rx.Subscriber;

/**
 * Created by vctor2015 on 2016/10/19.
 * <p>
 * 描述:HttpSubscriber
 */

public abstract class HttpSubscriber<T> extends Subscriber<T> {

    @Override
    public void onError(Throwable e) {
        if (e instanceof ApiException) {
            error((ApiException) e);
        } else {
            error(new ApiException(1000, e.getMessage()));
        }
    }

    /**
     * error
     */
    protected abstract void error(ApiException e);
}
