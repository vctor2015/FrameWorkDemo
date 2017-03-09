package com.centanet.frameworkdemo.fragments;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;

import com.centanet.framework.base.AbsFragment;
import com.centanet.frameworkdemo.R;
import com.centanet.frameworkdemo.model.GHResponse;
import com.centanet.frameworkdemo.rx.HttpTransformer;
import com.centanet.frameworkdemo.rx.HttpTransformer2;
import com.orhanobut.logger.Logger;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.trello.rxlifecycle.FragmentEvent;

import java.util.Locale;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * 描述:Fragment基类
 * <p>
 * Created by vctor2015 on 2016/12/7
 */
@SuppressWarnings("unused")
public abstract class BaseFragment extends AbsFragment {

    /**
     * 处理找房API错误逻辑<p>
     * 绑定生命周期<p>
     * 返回完整实体
     */
    protected final <T> HttpTransformer<T> bindHttpTransformer() {
        return new HttpTransformer<>(this.<GHResponse<T>>bindUntilEvent(FragmentEvent.DESTROY));
    }

    /**
     * 处理找房API错误逻辑<p>
     * 绑定生命周期<p>
     * 只返回results
     */
    protected final <T> HttpTransformer2<T> bindHttpTransformer2() {
        return new HttpTransformer2<>(this.<T>bindUntilEvent(FragmentEvent.DESTROY));
    }

    /**
     * 拨打电话
     * <p>
     * 6.0以上RuntimePermission，使用敏感权限需要检查授权
     */
    protected Observable<Void> dial(final String phoneNumber) {
        return RxPermissions.getInstance(getActivity())
                .request(Manifest.permission.CALL_PHONE)
                .flatMap(new Func1<Boolean, Observable<Void>>() {
                    @Override
                    public Observable<Void> call(Boolean aBoolean) {
                        Logger.d("dial : %b", aBoolean);
                        if (aBoolean) {
                            return dialDialog(phoneNumber);
                        } else {
                            return Observable.empty();
                        }
                    }
                });
    }

    /**
     * 拨打
     */
    @SuppressWarnings("ResourceType")
    private Observable<Void> dialDialog(final String phoneNumber) {
        return Observable.create(new Observable.OnSubscribe<Void>() {
            @Override
            public void call(final Subscriber<? super Void> subscriber) {
                new AlertDialog.Builder(getActivity())
                        .setMessage(phoneNumber)
                        .setPositiveButton(R.string.dialog_dial_positive,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(Intent.ACTION_CALL,
                                                Uri.parse(String.format(Locale.CHINA, "tel:%s",
                                                        phoneNumber)));
                                        startActivity(intent);
                                        subscriber.onNext(null);
                                        subscriber.onCompleted();
                                    }
                                })
                        .setNegativeButton(R.string.dialog_cancel,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        subscriber.onCompleted();
                                    }
                                })
                        .create()
                        .show();
            }
        });
    }
}
