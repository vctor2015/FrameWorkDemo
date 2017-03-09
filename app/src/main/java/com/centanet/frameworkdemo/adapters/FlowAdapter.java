package com.centanet.frameworkdemo.adapters;

import android.content.Context;
import android.view.View;

import com.centanet.framework.base.BaseAdapter1;
import com.centanet.frameworkdemo.R;
import com.centanet.frameworkdemo.adapters.vh.FlowVH;

import java.util.ArrayList;

/**
 * 描述:FlowAdapter
 * <p>
 * Created by vctor2015 on 2016/12/15
 */

public class FlowAdapter extends BaseAdapter1<FlowVH> {

    private final ArrayList<String> mList;

    public FlowAdapter(Context context, ArrayList<String> list) {
        super(context);
        this.mList = list;
    }

    @Override
    protected int inflateByLayoutId() {
        return R.layout.item_tag_flow;
    }

    @Override
    protected FlowVH viewHolder(View view) {
        return new FlowVH(view);
    }

    @Override
    public void onBindViewHolder(final FlowVH holder, int position) {
        holder.mAtvText.setText(mList.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mList.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
