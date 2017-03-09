package com.centanet.framework.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.centanet.framework.iml.SwipeItemCallback;

/**
 * Created by vctor2015 on 16/7/15.
 * <p>
 * 描述:一种ViewType基类
 */

public abstract class SwipeAdapter1<E, UVH extends RecyclerView.ViewHolder> extends
        SwipeAdapter<E, UVH> {

    public SwipeAdapter1(Context context, SwipeItemCallback<E> swipeItemCallback) {
        super(context, swipeItemCallback);
    }

    /**
     * 用户item布局id
     */
    @LayoutRes
    protected abstract int userLayoutResId();

    /**
     * 创建用户ViewHolder
     */
    protected abstract UVH createUserViewHolder(View view);

    @Override
    public UVH userViewHolder(ViewGroup parent, int viewType) {
        return createUserViewHolder(mLayoutInflater.inflate(userLayoutResId(), parent, false));
    }
}
