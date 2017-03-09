package com.centanet.framework.iml;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by vctor2015 on 16/7/15.
 * <p>
 * 描述:列表刷新帮助类
 */

public interface SwipeHelper<DVH extends RecyclerView.ViewHolder,
        MVH extends RecyclerView.ViewHolder, UVH extends RecyclerView.ViewHolder> {

    /**
     * 默认ViewHolder
     */
    DVH defViewHolder(ViewGroup parent);

    /**
     * 更多ViewHolder
     */
    MVH moreViewHolder(ViewGroup parent);

    /**
     * 用户ViewHolder
     */
    UVH userViewHolder(ViewGroup parent, int viewType);

    /**
     * 绑定默认ViewHolder
     */
    void bindDefViewHolder(DVH holder);

    /**
     * 绑定更多ViewHolder
     */
    void bindMoreViewHolder(MVH holder);

    /**
     * 绑定用户ViewHolder
     */
    void bindUserViewHolder(UVH holder, int position);

    /**
     * 用户数据数量
     */
    int userDataCount();
}
