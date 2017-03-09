package com.centanet.frameworkdemo.adapters;

import android.content.Context;
import android.view.View;

import com.centanet.framework.base.BaseAdapter1;
import com.centanet.framework.iml.SwipeItemCallback;
import com.centanet.frameworkdemo.R;
import com.centanet.frameworkdemo.adapters.vh.MainVH;

import java.util.ArrayList;

/**
 * Created by vctor2015 on 2016/11/17.
 * <p>
 * 描述:主页面
 */

public class MainAdapter extends BaseAdapter1<MainVH> {

    private final ArrayList<String> mList;
    private final SwipeItemCallback<String> mSwipeItemCallback;

    public MainAdapter(Context context, ArrayList<String> list,
            SwipeItemCallback<String> swipeItemCallback) {
        super(context);
        mList = list;
        mSwipeItemCallback = swipeItemCallback;
    }

    @Override
    protected int inflateByLayoutId() {
        return R.layout.item_main;
    }

    @Override
    protected MainVH viewHolder(View view) {
        return new MainVH(view);
    }

    @Override
    public void onBindViewHolder(final MainVH holder, int position) {
        final String title = mList.get(position);
        holder.mAtvTitle.setText(title);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSwipeItemCallback.callback(view, holder.getAdapterPosition(), title);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
