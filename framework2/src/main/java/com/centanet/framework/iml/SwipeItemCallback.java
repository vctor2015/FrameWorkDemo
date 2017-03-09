package com.centanet.framework.iml;

import android.view.View;

/**
 * Created by vctor2015 on 16/7/15.
 * <p>
 * 描述:swipe列表点击回调
 */

public interface SwipeItemCallback<E> {

    /**
     * 点击回调
     */
    void callback(View view, int position, E e);
}
