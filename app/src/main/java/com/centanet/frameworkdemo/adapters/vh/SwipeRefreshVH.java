package com.centanet.frameworkdemo.adapters.vh;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.centanet.frameworkdemo.R;

/**
 * Created by vctor2015 on 2016/11/18.
 * <p>
 * 描述:下拉刷新
 */

public class SwipeRefreshVH extends RecyclerView.ViewHolder {

    public AppCompatTextView mAtvTitle;
    public AppCompatTextView mAtvAuthor;

    public SwipeRefreshVH(View itemView) {
        super(itemView);
        mAtvTitle = (AppCompatTextView) itemView.findViewById(R.id.atv_title);
        mAtvAuthor = (AppCompatTextView) itemView.findViewById(R.id.atv_author);
    }
}
