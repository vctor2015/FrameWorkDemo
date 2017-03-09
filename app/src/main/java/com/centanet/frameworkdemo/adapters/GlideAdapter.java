package com.centanet.frameworkdemo.adapters;

import android.content.Context;
import android.view.View;

import com.bumptech.glide.DrawableRequestBuilder;
import com.centanet.framework.base.BaseAdapter1;
import com.centanet.frameworkdemo.R;
import com.centanet.frameworkdemo.adapters.vh.GlideVH;
import com.centanet.frameworkdemo.model.businessobject.MeiZiBO;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by vctor2015 on 2016/11/17.
 * <p>
 * 描述:图片
 */

public class GlideAdapter extends BaseAdapter1<GlideVH> {

    private final DrawableRequestBuilder<String> mRequestBuilder;
    private final ArrayList<MeiZiBO> mList;

    public GlideAdapter(Context context, DrawableRequestBuilder<String> requestBuilder,
            ArrayList<MeiZiBO> list) {
        super(context);
        mRequestBuilder = requestBuilder;
        mList = list;
    }

    @Override
    protected int inflateByLayoutId() {
        return R.layout.item_glide;
    }

    @Override
    protected GlideVH viewHolder(View view) {
        return new GlideVH(view);
    }

    @Override
    public void onBindViewHolder(GlideVH holder, int position) {
        mRequestBuilder
                .load(String.format(Locale.CHINA, "%s%s",
                        mList.get(position).getUrl(), "?imageView2/0/h/400 "))
                .placeholder(R.drawable.ic_centa_square_logo)
                .error(R.drawable.ic_centa_square_logo)
                .into(holder.mImgGlide);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
