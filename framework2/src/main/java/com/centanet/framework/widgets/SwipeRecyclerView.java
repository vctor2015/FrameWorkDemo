package com.centanet.framework.widgets;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.centanet.framework.R;
import com.centanet.framework.adapter.SwipeAdapter;
import com.centanet.framework.iml.ScrollStateChangedCallback;
import com.centanet.framework.iml.SwipeRefreshCallback;

/**
 * 描述:下拉刷新控件
 * <p>
 * Created by vctor2015 on 16/6/23.
 * <p>
 * 由{@link MSwipeRefreshLayout}和{@link RecyclerView}组合而成
 */
@SuppressWarnings("unused")
public class SwipeRecyclerView extends MSwipeRefreshLayout implements
        SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private RefreshCallback mRefreshCallback;
    private SwipeRefreshCallback mSwipeRefreshCallback;
    private ScrollStateChangedCallback mScrollStateChangedCallback;
    private SwipeAdapter mSwipeAdapter;

    public SwipeRecyclerView(Context context) {
        this(context, null);
    }

    public SwipeRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.recyclerview, this);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        init();
    }

    private void init() {
        int colorSchemeResources = colorSchemeResources();
        setColorSchemeResources(
                colorSchemeResources,
                colorSchemeResources,
                colorSchemeResources,
                colorSchemeResources);

        setOnRefreshListener(this);
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int loadStatus = mSwipeAdapter.getLoadStatus();
                if (loadStatus == 0 &&
                        mSwipeAdapter.isHasMore() &&
                        mSwipeAdapter.userDataCount() ==
                                mLinearLayoutManager.findLastCompletelyVisibleItemPosition()) {
                    mSwipeAdapter.setLoadStatus(2);
                    mRefreshCallback.upRefresh(mSwipeAdapter.userDataCount());
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (mScrollStateChangedCallback != null) {
                    if (newState == 1) {
                        mScrollStateChangedCallback.scroll();
                    }
                    if (newState == 0) {
                        mScrollStateChangedCallback.stop();
                    }
                }
            }
        });

        mSwipeRefreshCallback = new SwipeRefreshCallback() {
            @Override
            public void downRefresh() {
                setRefreshing(true);
                mRefreshCallback.downRefresh();
            }

            @Override
            public void upRefresh(int count) {
                mRefreshCallback.upRefresh(count);
            }

            @Override
            public void refreshComplete() {
                setRefreshing(false);
            }
        };
    }

    /**
     * 加载圈色值
     */
    @ColorRes
    protected int colorSchemeResources() {
        return R.color.colorAccent;
    }

    @Override
    public void onRefresh() {
        mSwipeAdapter.setLoadStatus(1);
        mRefreshCallback.downRefresh();
    }

    /**
     * 刷新回调
     */
    public void setRefreshCallback(RefreshCallback refreshCallback) {
        mRefreshCallback = refreshCallback;
    }

    /**
     * 滚动状态变化回调
     */
    public void setScrollStateChangeCallback(ScrollStateChangedCallback scrollStateChangeCallback) {
        mScrollStateChangedCallback = scrollStateChangeCallback;
    }

    /**
     * 开启下拉刷新
     */
    public void startDownRefresh() {
        mSwipeAdapter.setLoadStatus(1);
        setRefreshing(true);
        mRecyclerView.smoothScrollToPosition(0);
        mRefreshCallback.downRefresh();
    }

    /**
     * 设置Adapter
     */
    public void setAdapter(SwipeAdapter swipeAdapter) {
        mSwipeAdapter = swipeAdapter;
        mSwipeAdapter.setSwipeRefreshCallback(mSwipeRefreshCallback);
        mRecyclerView.setAdapter(mSwipeAdapter);
    }

    /**
     * 分割线
     */
    public void add(RecyclerView.ItemDecoration itemDecoration) {
        mRecyclerView.addItemDecoration(itemDecoration);
    }

    /**
     * 获取RecyclerView
     */
    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    /**
     * 刷新回调
     */
    public abstract static class RefreshCallback {
        /**
         * 下拉刷新
         */
        public abstract void downRefresh();

        /**
         * 加载更多
         *
         * @param count 用户数据
         */
        public abstract void upRefresh(int count);
    }
}
