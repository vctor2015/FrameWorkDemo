package com.centanet.framework.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.centanet.framework.R;
import com.centanet.framework.iml.ISnack;
import com.centanet.framework.widgets.CProgressDialog;
import com.orhanobut.logger.Logger;
import com.trello.rxlifecycle.components.RxFragment;

/**
 * Created by vctor2015 on 16/7/18.
 * <p>
 * 描述:基类Fragment
 */
@SuppressWarnings("unused")
public abstract class AbsFragment extends RxFragment {

    /**
     * 页面是否可见
     */
    protected boolean mVisible = true;
    /**
     * 页面准备完毕
     */
    protected boolean mPrepared;
    /**
     * {@link ISnack}
     */
    protected ISnack mISnack;
    private CProgressDialog mDialog;

    public void setISnack(ISnack iSnack) {
        this.mISnack = iSnack;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            mVisible = true;
            onVisible();
        } else {
            mVisible = false;
            onInvisible();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(layoutResId(), container, false);
        preInit(savedInstanceState);
        findViews(view);
        initViews();
        mPrepared = true;
        lazyLoad();
        return view;
    }

    /**
     * 布局id
     */
    @LayoutRes
    protected abstract int layoutResId();

    /**
     * 在{@link #findViews(View)}前执行
     *
     * @param savedInstanceState {@link Bundle}
     */
    protected void preInit(Bundle savedInstanceState) {
    }

    /**
     * 放置findView方法
     */
    protected abstract void findViews(View view);

    /**
     * view初始化
     */
    protected abstract void initViews();

    /**
     * {@link android.support.v4.app.Fragment}可见
     */
    protected void onVisible() {
        lazyLoad();
    }

    /**
     * {@link android.support.v4.app.Fragment}不可见
     */
    protected void onInvisible() {

    }

    private void lazyLoad() {
        if (!mPrepared || !mVisible) {
            return;
        }
        loadData();
    }

    /**
     * 初始化完毕且页面可见
     */
    protected abstract void loadData();

    /**
     * 显示进度Dialog
     */
    protected void showLoadingDialog() {
        if (mDialog == null) {
            mDialog = new CProgressDialog(getActivity());
        }
        mDialog.show();
    }

    /**
     * 取消进度Dialog
     */
    protected void cancelLoadingDialog() {
        if (mDialog != null &&
                mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    /**
     * 网络不可用
     */
    protected void netWorkUnable(Throwable e) {
        Logger.e(e, "netWorkUnable");
        toast(R.string.network_unable);
    }

    /**
     * 网络不可用
     */
    @Deprecated
    protected void netWorkUnable() {
        toast(R.string.network_unable);
    }

    /**
     * Toast统一显示入口
     */
    protected void toast(@StringRes int id) {
        Toast.makeText(getActivity().getApplicationContext(), id, Toast.LENGTH_SHORT).show();
    }

    /**
     * Toast统一显示入口
     */
    protected void toast(String text) {
        Toast.makeText(getActivity().getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

    /**
     * snack
     *
     * @param text 显示内容
     */
    protected void snack(CharSequence text) {
        if (mISnack != null) {
            mISnack.showSnack(text);
        }
    }
}
