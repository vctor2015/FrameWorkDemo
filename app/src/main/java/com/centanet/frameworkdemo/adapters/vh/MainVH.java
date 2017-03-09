package com.centanet.frameworkdemo.adapters.vh;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.centanet.frameworkdemo.R;

/**
 * Created by vctor2015 on 2016/11/17.
 * <p>
 * 描述:主页面
 */

public class MainVH extends RecyclerView.ViewHolder {

    public AppCompatTextView mAtvTitle;

    public MainVH(View itemView) {
        super(itemView);
        mAtvTitle = (AppCompatTextView) itemView.findViewById(R.id.atv_title);
    }
}
