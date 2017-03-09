package com.centanet.frameworkdemo.activities;

import android.view.View;

import com.centanet.framework.http.exception.ApiException;
import com.centanet.framework.iml.SimpleRequestIml;
import com.centanet.framework.iml.SwipeItemCallback;
import com.centanet.framework.widgets.SwipeRecyclerView;
import com.centanet.frameworkdemo.R;
import com.centanet.frameworkdemo.adapters.SwipeRefreshAdapter;
import com.centanet.frameworkdemo.api.ApiCreate;
import com.centanet.frameworkdemo.model.businessobject.SearchBO;
import com.centanet.frameworkdemo.rx.HttpSubscriber;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by vctor2015 on 2016/11/18.
 * <p>
 * 描述:下拉刷新
 */

public class SwipeRecyclerViewActivity extends BaseActivity implements SimpleRequestIml {

    private static final String TAG = "SwipeRecyclerViewActivity";

    private SwipeRecyclerView mSwipeRecyclerView;

    private SwipeRefreshAdapter mSwipeRefreshAdapter;
    private int mPage;

    @Override
    protected int layoutResId() {
        return R.layout.activity_swiperecyclerview;
    }

    @Override
    protected void findViews() {
        setUniversalToolbar(R.string.title_swipe_refresh);
        mSwipeRecyclerView = (SwipeRecyclerView) findViewById(R.id.swipeRecyclerView);
    }

    @Override
    protected void initViews() {
        mSwipeRecyclerView.setRefreshCallback(new SwipeRecyclerView.RefreshCallback() {
            @Override
            public void downRefresh() {
                mPage = 1;
                request();
            }

            @Override
            public void upRefresh(int count) {
                mPage = count / 10 + 1;
                request();
            }
        });
        mSwipeRefreshAdapter = new SwipeRefreshAdapter(this, new SwipeItemCallback<SearchBO>() {
            @Override
            public void callback(View view, int position, SearchBO searchBO) {
                Logger.t(TAG).d("SwipeItemCallback[view id : %d,position : %d]", view.getId(),
                        position);
            }
        });
        mSwipeRecyclerView.setAdapter(mSwipeRefreshAdapter);
    }

    @Override
    protected void initComplete() {
        mSwipeRecyclerView.startDownRefresh();
    }

    @Override
    public void request() {
        ApiCreate
                .gh()
                .search(mPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<ArrayList<SearchBO>>bindHttpTransformer2())
                .subscribe(new HttpSubscriber<ArrayList<SearchBO>>() {
                    @Override
                    protected void error(ApiException e) {
                        mSwipeRefreshAdapter.error();
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(ArrayList<SearchBO> searchBOs) {
                        mSwipeRefreshAdapter.load(searchBOs,
                                !(searchBOs == null || searchBOs.size() < 10));
                    }
                });
    }
}
