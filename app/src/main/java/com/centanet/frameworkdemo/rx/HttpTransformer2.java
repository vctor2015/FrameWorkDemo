package com.centanet.frameworkdemo.rx;

import com.centanet.framework.http.exception.ApiException;
import com.centanet.frameworkdemo.model.GHResponse;
import com.trello.rxlifecycle.LifecycleTransformer;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by vctor2015 on 2016/10/18.
 * <p>
 * 描述:自定义http Transformer,直接返回result实体
 */

public final class HttpTransformer2<T> implements Observable.Transformer<GHResponse<T>, T> {

    private final LifecycleTransformer<T> mLifecycleTransformer;

    public HttpTransformer2(LifecycleTransformer<T> lifecycleTransformer) {
        mLifecycleTransformer = lifecycleTransformer;
    }

    @Override
    public Observable<T> call(Observable<GHResponse<T>> observable) {
        return observable
                .map(new Func1<GHResponse<T>, GHResponse<T>>() {
                    @Override
                    public GHResponse<T> call(GHResponse<T> ghResponse) {
                        if (ghResponse.isError())
                            throw new ApiException(-1);
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
                .map(new Func1<GHResponse<T>, T>() {
                    @Override
                    public T call(GHResponse<T> ghResponse) {
                        return ghResponse.getResults();
                    }
                })
                .compose(mLifecycleTransformer);

    }
}
