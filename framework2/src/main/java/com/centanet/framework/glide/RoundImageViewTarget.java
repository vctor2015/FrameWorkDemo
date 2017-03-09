package com.centanet.framework.glide;

import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.ImageView;

import com.bumptech.glide.request.target.ImageViewTarget;

/**
 * Created by vctor2015 on 16/5/30.
 * <p>
 * 描述:圆形图片
 */
@SuppressWarnings("unused")
public class RoundImageViewTarget extends ImageViewTarget<Bitmap> {

    public RoundImageViewTarget(ImageView view) {
        super(view);
    }

    @Override
    protected void setResource(Bitmap resource) {
        RoundedBitmapDrawable mRoundedBitmapDrawable = RoundedBitmapDrawableFactory.create(
                view.getResources(), resource);
        mRoundedBitmapDrawable.setCircular(true);
        view.setImageDrawable(mRoundedBitmapDrawable);
    }
}
