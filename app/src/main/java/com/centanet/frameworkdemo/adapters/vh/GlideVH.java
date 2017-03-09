package com.centanet.frameworkdemo.adapters.vh;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.centanet.frameworkdemo.R;

/**
 * Created by vctor2015 on 2016/11/17.
 * <p>
 * 描述:图片
 */

public class GlideVH extends RecyclerView.ViewHolder {

    public ImageView mImgGlide;

    public GlideVH(View itemView) {
        super(itemView);
        mImgGlide = (ImageView) itemView.findViewById(R.id.img_glide);
    }
}
