package com.centanet.framework.adapter.vhs;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.centanet.framework.R;

/**
 * Created by vctor2015 on 16/6/23.
 * <p>
 * 描述:更多VH
 */

public class MoreVH extends RecyclerView.ViewHolder {

    public AppCompatTextView mAtvSwipeMore;
    public ProgressBar mPbSwipeMore;

    public MoreVH(View itemView) {
        super(itemView);
        mAtvSwipeMore = (AppCompatTextView) itemView.findViewById(R.id.atv_swipe_more);
        mPbSwipeMore = (ProgressBar) itemView.findViewById(R.id.pb_swipe_more);
    }
}
