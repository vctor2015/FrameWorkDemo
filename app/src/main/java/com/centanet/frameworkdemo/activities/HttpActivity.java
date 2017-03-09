package com.centanet.frameworkdemo.activities;

import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.RadioGroup;

import com.centanet.framework.http.exception.ApiException;
import com.centanet.framework.iml.SimpleRequestIml;
import com.centanet.frameworkdemo.R;
import com.centanet.frameworkdemo.api.ApiCreate;
import com.centanet.frameworkdemo.enums.GHCategory;
import com.centanet.frameworkdemo.model.GHResponse;
import com.centanet.frameworkdemo.model.businessobject.MeiZiBO;
import com.centanet.frameworkdemo.rx.HttpSubscriber;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.trello.rxlifecycle.ActivityEvent;

import java.util.ArrayList;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by vctor2015 on 2016/11/17.
 * <p>
 * 描述:httpDemo
 */

public class HttpActivity extends BaseActivity implements SimpleRequestIml,
        View.OnClickListener {

    private static final String TAG = "HttpActivity";

    private RadioGroup mRgHttp;
    private AppCompatTextView mAtvResponse;

    @Override
    protected int layoutResId() {
        return R.layout.activity_http;
    }

    @Override
    protected void findViews() {
        setUniversalToolbar(R.string.title_http);
        mRgHttp = (RadioGroup) findViewById(R.id.rg_http);
        mAtvResponse = (AppCompatTextView) findViewById(R.id.atv_response);
    }

    @Override
    protected void initViews() {
        mRgHttp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                Logger.t(TAG).d("onCheckedChanged : %d", i);
            }
        });
    }

    @Override
    protected void initComplete() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_request:
                request();
                break;
            default:
                break;
        }
    }

    @Override
    public void request() {
        switch (mRgHttp.getCheckedRadioButtonId()) {
            case R.id.rb_1:
                ApiCreate
                        .gh()
                        .category(GHCategory.FULI.getSource(), 1)
                        .subscribeOn(Schedulers.io())
                        .doOnSubscribe(new Action0() {
                            @Override
                            public void call() {
                                //可以显示加载对话框
                                showLoadingDialog();
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .compose(this.<GHResponse<ArrayList<MeiZiBO>>>bindUntilEvent(
                                ActivityEvent.DESTROY))
                        .doOnNext(new Action1<GHResponse<ArrayList<MeiZiBO>>>() {
                            @Override
                            public void call(GHResponse<ArrayList<MeiZiBO>> arrayListGHResponse) {
                                //可以把数据存储到数据库
                            }
                        })
                        .subscribe(new Subscriber<GHResponse<ArrayList<MeiZiBO>>>() {
                            @Override
                            public void onCompleted() {
                                cancelLoadingDialog();
                            }

                            @Override
                            public void onError(Throwable e) {
                                cancelLoadingDialog();
                                netWorkUnable(e);
                            }

                            @Override
                            public void onNext(GHResponse<ArrayList<MeiZiBO>> arrayListGHResponse) {
                                mAtvResponse.setText(new Gson().toJson(arrayListGHResponse));
                            }
                        });
                break;
            case R.id.rb_2:
                ApiCreate
                        .gh()
                        .category(GHCategory.FULI.getSource(), 1)
                        .subscribeOn(Schedulers.io())
                        .doOnSubscribe(new Action0() {
                            @Override
                            public void call() {
                                showLoadingDialog();
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .compose(this.<ArrayList<MeiZiBO>>bindHttpTransformer())
                        .subscribe(new HttpSubscriber<GHResponse<ArrayList<MeiZiBO>>>() {
                            @Override
                            protected void error(ApiException e) {
                                cancelLoadingDialog();
                                switch (e.code) {
                                    case 1000:
                                        netWorkUnable(e);
                                        break;
                                    default:
                                        toast("请求出错");
                                        break;
                                }
                            }

                            @Override
                            public void onCompleted() {
                                cancelLoadingDialog();
                            }

                            @Override
                            public void onNext(GHResponse<ArrayList<MeiZiBO>> arrayListGHResponse) {
                                mAtvResponse.setText(new Gson().toJson(arrayListGHResponse));
                            }
                        });
                break;
            case R.id.rb_3:
                ApiCreate
                        .gh()
                        .category(GHCategory.FULI.getSource(), 1)
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
                                        toast("请求出错");
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
                break;
            default:
                break;
        }
    }
}
