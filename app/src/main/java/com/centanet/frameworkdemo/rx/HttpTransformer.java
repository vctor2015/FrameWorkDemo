package com.centanet.frameworkdemo.rx;

import com.centanet.framework.http.exception.ApiException;
import com.centanet.frameworkdemo.model.GHResponse;
import com.trello.rxlifecycle.LifecycleTransformer;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by vctor2015 on 2016/10/18.
 * <p>
 * 描述:自定义http Transformer
 */

public final class HttpTransformer<T> implements
        Observable.Transformer<GHResponse<T>, GHResponse<T>> {

    private final LifecycleTransformer<GHResponse<T>> mLifecycleTransformer;

    public HttpTransformer(LifecycleTransformer<GHResponse<T>> lifecycleTransformer) {
        mLifecycleTransformer = lifecycleTransformer;
    }

    @Override
    public Observable<GHResponse<T>> call(Observable<GHResponse<T>> observable) {
        return observable
                .map(new Func1<GHResponse<T>, GHResponse<T>>() {
                    @Override
                    public GHResponse<T> call(GHResponse<T> ghResponse) {
                        if (ghResponse.isError()) {
                            throw new ApiException(-1);
                        }
                        return ghResponse;
                    }
                })
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends GHResponse<T>>>() {
                    @Override
                    public Observable<? extends GHResponse<T>> call(Throwable throwable) {
                        throwable.printStackTrace();
                        return Observable.error(throwable);
                    }
                })
                .compose(mLifecycleTransformer);

    }
}
