package com.centanet.frameworkdemo.activities;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.centanet.frameworkdemo.R;
import com.centanet.frameworkdemo.adapters.FlowAdapter;
import com.centanet.frameworkdemo.widget.FlowLayoutManager;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

/**
 * 描述:流式布局「FlowLayoutManager」
 * <p>
 * Created by vctor2015 on 2016/12/15
 */

public class FlowLayoutManagerActivity extends BaseActivity implements View.OnClickListener {

    private RecyclerView mRvList;

    private FlowAdapter mFlowAdapter;
    private ArrayList<String> mList = new ArrayList<>();

    @Override
    protected int layoutResId() {
        return R.layout.activity_recyclerview;
    }

    @Override
    protected void findViews() {
        setUniversalToolbar(R.string.title_flow_layout_manager);
        mRvList = (RecyclerView) findViewById(R.id.rv_list);
    }

    @Override
    protected void initViews() {
        mRvList.setLayoutManager(new FlowLayoutManager());
        mFlowAdapter = new FlowAdapter(this, mList);
        mRvList.setAdapter(mFlowAdapter);
    }

    @Override
    protected void initComplete() {
        reload();
    }

    /**
     * 重新加载
     */
    private void reload() {
        mList.clear();
        for (int i = 0; i < 10; i++) {
            mList.add(String.format(Locale.CHINA, "item %d", i));
        }
        mRvList.smoothScrollToPosition(0);
        mFlowAdapter.notifyDataSetChanged();
    }

    /**
     * 随机增加
     */
    private void addRandom() {
        int random = mList.size() > 0 ?
                new Random().nextInt(mList.size()) : 0;
        mList.add(random, String.format(Locale.CHINA, "item add %d", random));
        mFlowAdapter.notifyItemInserted(random);
    }

    /**
     * 随机移除
     */
    private void removeRandom() {
        if (mList.size() == 0) {
            return;
        }
        int random = mList.size() > 1 ? new Random().nextInt(mList.size() - 1) : 0;
        mList.remove(random);
        mFlowAdapter.notifyItemRemoved(random);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_reload:
                reload();
                break;
            case R.id.btn_add_random:
                addRandom();
                break;
            case R.id.btn_remove_random:
                removeRandom();
                break;
            default:
                break;
        }
    }
}
