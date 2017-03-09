package com.centanet.frameworkdemo.adapters.vh;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.centanet.frameworkdemo.R;

/**
 * 描述:flow
 * <p>
 * Created by vctor2015 on 2016/12/15
 */

public class FlowVH extends RecyclerView.ViewHolder {

    public AppCompatTextView mAtvText;

    public FlowVH(View itemView) {
        super(itemView);
        mAtvText = (AppCompatTextView) itemView.findViewById(R.id.atv_text);
    }
}
