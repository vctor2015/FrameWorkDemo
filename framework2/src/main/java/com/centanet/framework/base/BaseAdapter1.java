package com.centanet.framework.base;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * {@link RecyclerView.Adapter}基类<p>
 * viewType的类型只有一种情况下使用,简化{@link LayoutInflater#inflate(int, ViewGroup, boolean)}过程<p>
 * Created by vctor2015 on 16/1/29.
 */

public abstract class BaseAdapter1<VH extends RecyclerView.ViewHolder> extends
        RecyclerView.Adapter<VH> {

    private final LayoutInflater mLayoutInflater;

    public BaseAdapter1(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = mLayoutInflater.inflate(inflateByLayoutId(), parent, false);
        return viewHolder(view);
    }

    /**
     * 返回布局id
     *
     * @return 布局id
     */
    @LayoutRes
    protected abstract int inflateByLayoutId();

    /**
     * 返回自定义{@link android.support.v7.widget.RecyclerView.ViewHolder}
     *
     * @param view 由{@link LayoutInflater#inflate(int, ViewGroup, boolean)}生产的View
     * @return 返回自定义ViewHolder
     */
    protected abstract VH viewHolder(View view);
}
