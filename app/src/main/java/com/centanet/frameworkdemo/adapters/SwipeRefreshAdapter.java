package com.centanet.frameworkdemo.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.centanet.framework.adapter.SwipeAdapter1;
import com.centanet.framework.iml.SwipeItemCallback;
import com.centanet.frameworkdemo.R;
import com.centanet.frameworkdemo.adapters.vh.SwipeRefreshVH;
import com.centanet.frameworkdemo.model.businessobject.SearchBO;

import java.util.Locale;

/**
 * Created by vctor2015 on 2016/11/18.
 * <p>
 * 描述:下拉刷新
 */

public class SwipeRefreshAdapter extends SwipeAdapter1<SearchBO, SwipeRefreshVH> {

    public SwipeRefreshAdapter(Context context, SwipeItemCallback<SearchBO> swipeItemCallback) {
        super(context, swipeItemCallback);
    }

    @Override
    protected int userLayoutResId() {
        return R.layout.item_swipe_refresh;
    }

    @Override
    protected SwipeRefreshVH createUserViewHolder(View view) {
        return new SwipeRefreshVH(view);
    }

    @Override
    protected SwipeRefreshVH castVH(RecyclerView.ViewHolder holder) {
        return (SwipeRefreshVH) holder;
    }

    @Override
    public void bindUserViewHolder(final SwipeRefreshVH holder, int position) {
        final SearchBO searchBO = mList.get(position);
        holder.mAtvTitle.setText(searchBO.getDesc());
        holder.mAtvAuthor.setText(String.format(Locale.CHINA, "作者：%s", searchBO.getWho()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSwipeItemCallback.callback(view, holder.getAdapterPosition(), searchBO);
            }
        });
    }
}
