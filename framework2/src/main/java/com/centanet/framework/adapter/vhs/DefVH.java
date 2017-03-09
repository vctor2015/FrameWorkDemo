package com.centanet.framework.adapter.vhs;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.centanet.framework.R;

/**
 * Created by vctor2015 on 16/6/23.
 * <p>
 * 描述:默认VH
 */
@SuppressWarnings("all")
public class DefVH extends RecyclerView.ViewHolder {

    public ImageView mImgSwipeDef;
    public AppCompatTextView mAtvSwipeDef;

    public DefVH(View itemView) {
        super(itemView);
        mImgSwipeDef = (ImageView) itemView.findViewById(R.id.img_swipe_def);
        mAtvSwipeDef = (AppCompatTextView) itemView.findViewById(R.id.atv_swipe_def);
    }
}
