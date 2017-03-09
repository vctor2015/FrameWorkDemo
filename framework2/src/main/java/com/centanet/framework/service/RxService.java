package com.centanet.framework.service;

import android.app.Service;
import android.support.annotation.CallSuper;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

import com.trello.rxlifecycle.LifecycleTransformer;
import com.trello.rxlifecycle.RxLifecycle;

import rx.subjects.BehaviorSubject;

/**
 * Created by vctor2015 on 16/7/28.
 * <p>
 * 描述:RxService 生命周期管理
 */
@SuppressWarnings("unused")
public abstract class RxService extends Service {

    private final BehaviorSubject<ServiceEvent> mLifecycleSubject = BehaviorSubject.create();

    /**
     * 绑定Destroy生命周期
     */
    protected final <T> LifecycleTransformer<T> bindDestroyEvent() {
        return bindUntilEvent(ServiceEvent.DESTROY);
    }

    /**
     * 绑定生命周期
     */
    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindUntilEvent(@NonNull ServiceEvent event) {
        return RxLifecycle.bindUntilEvent(mLifecycleSubject, event);
    }

    @Override
    @CallSuper
    public void onDestroy() {
        mLifecycleSubject.onNext(ServiceEvent.DESTROY);
        super.onDestroy();
    }

    /**
     * Rx服务订阅
     */
    public enum ServiceEvent{

        DESTROY
    }
}
