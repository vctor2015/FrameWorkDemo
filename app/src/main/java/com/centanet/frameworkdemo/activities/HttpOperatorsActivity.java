package com.centanet.frameworkdemo.activities;

import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.centanet.framework.http.exception.ApiException;
import com.centanet.frameworkdemo.R;
import com.centanet.frameworkdemo.api.ApiCreate;
import com.centanet.frameworkdemo.enums.GHCategory;
import com.centanet.frameworkdemo.model.GHResponse;
import com.centanet.frameworkdemo.model.businessobject.MeiZiBO;
import com.centanet.frameworkdemo.model.businessobject.SearchBO;
import com.centanet.frameworkdemo.model.zipobject.ZipBO2;
import com.centanet.frameworkdemo.rx.HttpSubscriber;
import com.google.gson.Gson;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.orhanobut.logger.Logger;
import com.trello.rxlifecycle.ActivityEvent;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Created by vctor2015 on 2016/11/17.
 * <p>
 * 描述:RxJava操作符在http请求中的使用
 */

public class HttpOperatorsActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "HttpOperatorsActivity";

    private AppCompatEditText mEditQuery;
    private AppCompatTextView mAtvResponse;

    @Override
    protected int layoutResId() {
        return R.layout.activity_http_operators;
    }

    @Override
    protected void findViews() {
        setUniversalToolbar(R.string.title_http_operators);
        mEditQuery = (AppCompatEditText) findViewById(R.id.edit_query);
        mAtvResponse = (AppCompatTextView) findViewById(R.id.atv_response);
    }

    @Override
    protected void initViews() {
        RxTextView
                .textChanges(mEditQuery)
                .debounce(400, TimeUnit.MILLISECONDS)//输入间隔小于400ms，则不会发送数据
                .subscribe(new Action1<CharSequence>() {
                    @Override
                    public void call(CharSequence charSequence) {
                        Logger.t(TAG).d("textChanges : %s", charSequence);
                        if (charSequence.length() > 0) {
                            query(charSequence.toString());
                        }
                    }
                });
    }

    @Override
    protected void initComplete() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_request1:
                combineLast();
                break;
            case R.id.btn_request2:
                zip();
                break;
            case R.id.btn_request3:
                depends();
                break;
            default:
                break;
        }
    }

    /**
     * 搜索
     */
    private void query(String query) {
        ApiCreate
                .gh()
                .search(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<ArrayList<SearchBO>>bindHttpTransformer2())
                .subscribe(new HttpSubscriber<ArrayList<SearchBO>>() {
                    @Override
                    protected void error(ApiException e) {
                        netWorkUnable(e);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(ArrayList<SearchBO> searchBOs) {
                        mAtvResponse.setText(new Gson().toJson(searchBOs));
                    }
                });
    }

    /**
     * 联合查询，所有的接口调用响应后，集中处理数据
     */
    private void combineLast() {
        Observable
                .combineLatest(
                        ApiCreate
                                .gh()
                                .category(GHCategory.ANDROID.getSource(), 5, 1),
                        ApiCreate
                                .gh()
                                .category(GHCategory.iOS.getSource(), 5, 1),
                        new Func2<GHResponse<ArrayList<MeiZiBO>>, GHResponse<ArrayList<MeiZiBO>>,
                                GHResponse<ArrayList<MeiZiBO>>>() {
                            @Override
                            public GHResponse<ArrayList<MeiZiBO>> call(
                                    GHResponse<ArrayList<MeiZiBO>> arrayListGHResponse,
                                    GHResponse<ArrayList<MeiZiBO>> arrayListGHResponse2) {
                                //如果第一个请求失败，则使用第二个接口数据
                                if (arrayListGHResponse.isError()) {
                                    return arrayListGHResponse2;
                                } else {
                                    return arrayListGHResponse;
                                }
                            }
                        })
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
                        switch (e.code) {
                            case 1000:
                                netWorkUnable(e);
                                break;
                            default:
                                toast("没有数据返回");
                                break;
                        }
                    }

                    @Override
                    public void onCompleted() {
                        cancelLoadingDialog();
                    }

                    @Override
                    public void onNext(ArrayList<MeiZiBO> meiZiBOs) {
                        mAtvResponse.setText(new Gson().toJson(meiZiBOs));
                    }
                });

    }

    /**
     * 需要从多个数据源获取数据，可以从缓存、数据库、网络
     */
    private void zip() {
        Observable
                .zip(
                        ApiCreate
                                .gh()
                                .category(GHCategory.ANDROID.getSource(), 5, 1),
                        ApiCreate
                                .gh()
                                .category(GHCategory.iOS.getSource(), 5, 1),
                        new Func2<GHResponse<ArrayList<MeiZiBO>>,
                                GHResponse<ArrayList<MeiZiBO>>,
                                ZipBO2<GHResponse<ArrayList<MeiZiBO>>,
                                        GHResponse<ArrayList<MeiZiBO>>>>() {
                            @Override
                            public ZipBO2<GHResponse<ArrayList<MeiZiBO>>,
                                    GHResponse<ArrayList<MeiZiBO>>> call(
                                    GHResponse<ArrayList<MeiZiBO>> arrayListGHResponse,
                                    GHResponse<ArrayList<MeiZiBO>> arrayListGHResponse2) {
                                return new ZipBO2<>(arrayListGHResponse, arrayListGHResponse2);
                            }
                        })
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        showLoadingDialog();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .compose(
                        this.<ZipBO2<GHResponse<ArrayList<MeiZiBO>>,
                                GHResponse<ArrayList<MeiZiBO>>>>bindUntilEvent(
                                ActivityEvent.DESTROY))
                .subscribe(
                        new HttpSubscriber<ZipBO2<GHResponse<ArrayList<MeiZiBO>>,
                                GHResponse<ArrayList<MeiZiBO>>>>() {
                            @Override
                            protected void error(ApiException e) {
                                cancelLoadingDialog();
                                switch (e.code) {
                                    case 1000:
                                        netWorkUnable(e);
                                        break;
                                    default:
                                        toast("没有数据返回");
                                        break;
                                }
                            }

                            @Override
                            public void onCompleted() {
                                cancelLoadingDialog();
                            }

                            @Override
                            public void onNext(ZipBO2<GHResponse<ArrayList<MeiZiBO>>,
                                    GHResponse<ArrayList<MeiZiBO>>> ghResponseGHResponseZipBO2) {
                                mAtvResponse.setText(new Gson().toJson(ghResponseGHResponseZipBO2));
                            }
                        });
    }

    /**
     * 第二个接口数据参数依赖第一个接口响应值
     */
    private void depends() {
        ApiCreate
                .gh()
                .search("ListView")
                .flatMap(
                        new Func1<GHResponse<ArrayList<SearchBO>>,
                                Observable<GHResponse<ArrayList<MeiZiBO>>>>() {
                            @Override
                            public Observable<GHResponse<ArrayList<MeiZiBO>>> call(
                                    GHResponse<ArrayList<SearchBO>> arrayListGHResponse) {
                                if (arrayListGHResponse.isError()) {
                                    return Observable.error(new ApiException(-1));
                                } else {
                                    return ApiCreate
                                            .gh()
                                            .category(arrayListGHResponse.getResults().get(
                                                    0).getType(), 1);
                                }
                            }
                        })
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
                        switch (e.code) {
                            case 1000:
                                netWorkUnable(e);
                                break;
                            default:
                                toast("没有数据返回");
                                break;
                        }
                    }

                    @Override
                    public void onCompleted() {
                        cancelLoadingDialog();
                    }

                    @Override
                    public void onNext(ArrayList<MeiZiBO> meiZiBOs) {
                        mAtvResponse.setText(new Gson().toJson(meiZiBOs));
                    }
                });
    }
}
