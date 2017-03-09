package com.centanet.frameworkdemo.activities;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.centanet.framework.glide.ImageLoad;
import com.centanet.framework.http.exception.ApiException;
import com.centanet.framework.iml.SimpleRequestIml;
import com.centanet.frameworkdemo.R;
import com.centanet.frameworkdemo.adapters.GlideAdapter;
import com.centanet.frameworkdemo.api.ApiCreate;
import com.centanet.frameworkdemo.enums.GHCategory;
import com.centanet.frameworkdemo.model.businessobject.MeiZiBO;
import com.centanet.frameworkdemo.rx.HttpSubscriber;

import java.util.ArrayList;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by vctor2015 on 2016/11/17.
 * <p>
 * 描述:图片加载
 */

public class GlideActivity extends BaseActivity implements SimpleRequestIml {

    private RecyclerView mRvGlide;

    private GlideAdapter mGlideAdapter;
    private ArrayList<MeiZiBO> mList = new ArrayList<>();

    @Override
    protected int layoutResId() {
        return R.layout.activity_glide;
    }

    @Override
    protected void findViews() {
        setUniversalToolbar(R.string.title_glide);
        mRvGlide = (RecyclerView) findViewById(R.id.rv_glide);
    }

    @Override
    protected void initViews() {
        mRvGlide.setLayoutManager(new GridLayoutManager(this, 2));
        mGlideAdapter = new GlideAdapter(this, ImageLoad.get(this), mList);
        mRvGlide.setAdapter(mGlideAdapter);
    }

    @Override
    protected void initComplete() {
        request();
    }

    @Override
    public void request() {
        ApiCreate
                .gh()
                .category(GHCategory.FULI.getSource(), 20, 1)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        showLoadingDialog();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<ArrayList<MeiZiBO>>bindHttpTransformer2())
                .subscribe(new HttpSubscriber<ArrayList<MeiZiBO>>() {
                    @Override
                    protected void error(ApiException e) {
                        cancelLoadingDialog();
                        netWorkUnable(e);
                    }

                    @Override
                    public void onCompleted() {
                        cancelLoadingDialog();
                    }

                    @Override
                    public void onNext(ArrayList<MeiZiBO> meiZiBOs) {
                        mList.addAll(meiZiBOs);
                        mGlideAdapter.notifyItemRangeInserted(0, 10);
                    }
                });
    }
}
