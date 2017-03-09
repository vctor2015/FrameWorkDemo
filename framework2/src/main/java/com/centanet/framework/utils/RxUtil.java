package com.centanet.framework.utils;

import java.util.concurrent.TimeUnit;

import rx.Observable;

/**
 * 描述:Rx工具类
 * <p>
 * Created by vctor2015 on 2016/12/8
 */
@SuppressWarnings("unused")
public final class RxUtil {

    private RxUtil() {
        //utility class
    }

    /**
     * 倒计时「输出结果为0～count，假设count==1000，第1个输出：0，最后输出：999」
     *
     * @param count 倒计时时间「秒」
     */
    public static Observable<Long> countDown(int count) {
        return Observable
                .interval(0, 1, TimeUnit.SECONDS)
                .take(count);
    }
}
