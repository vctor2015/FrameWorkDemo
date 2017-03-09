package com.centanet.framework.widgets;

import android.animation.LayoutTransition;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.centanet.framework.R;

/**
 * 描述:加载状态Layout
 * <p>
 * Created by vctor2015 on 2016/12/20
 */
@SuppressWarnings("unused")
public class StateLayout extends FrameLayout {

    private View mLoadingView;//加载中view
    private View mErrorView;//加载错误view
    private View mContentView;//正常显示的view

    private StateCallback mStateCallback;

    public StateLayout(@NonNull Context context) {
        this(context, null);
    }

    public StateLayout(@NonNull Context context,
            @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StateLayout(@NonNull Context context,
            @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.StateLayout);
        int loadingViewId = typedArray.getResourceId(R.styleable.StateLayout_loadingLayout,
                R.layout.state_loading);
        int errorViewId = typedArray.getResourceId(R.styleable.StateLayout_errorLayout,
                R.layout.state_error);
        typedArray.recycle();

        mLoadingView = inflate(context, loadingViewId, null);
        mErrorView = inflate(context, errorViewId, null);
        setLayoutTransition(new LayoutTransition());

        mErrorView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoadingView.setVisibility(VISIBLE);
                mErrorView.setVisibility(GONE);
                if (mStateCallback != null) {
                    mStateCallback.reload();
                }
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mContentView == null) {
            int count = getChildCount();
            if (count > 1) {
                throw new IllegalStateException("can host only one direct child");
            }
            mContentView = getChildAt(0);
            addView(mErrorView);
            addView(mLoadingView);
            mContentView.setVisibility(GONE);
            mErrorView.setVisibility(GONE);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 设置回调
     */
    public void setStateCallback(StateCallback stateCallback) {
        mStateCallback = stateCallback;
    }

    /**
     * 显示内容页面
     */
    public void success() {
        removeViewAt(2);
        removeViewAt(1);
        mContentView.setVisibility(VISIBLE);
    }

    /**
     * 显示错误页面
     */
    public void error() {
        mLoadingView.setVisibility(GONE);
        mErrorView.setVisibility(VISIBLE);
    }

    /**
     * 状态回调
     */
    public interface StateCallback {

        /**
         * 重新加载
         */
        void reload();
    }
}
