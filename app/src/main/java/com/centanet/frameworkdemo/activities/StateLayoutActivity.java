package com.centanet.frameworkdemo.activities;

import com.centanet.framework.utils.RxUtil;
import com.centanet.framework.widgets.StateLayout;
import com.centanet.frameworkdemo.R;
import com.trello.rxlifecycle.ActivityEvent;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * 描述:状态布局
 * <p>
 * Created by vctor2015 on 2016/12/21
 */

public class StateLayoutActivity extends BaseActivity {

    private StateLayout mStateLayout;

    @Override
    protected int layoutResId() {
        return R.layout.activity_state_layout;
    }

    @Override
    protected void findViews() {
        setUniversalToolbar(R.string.title_state_layout);
        mStateLayout = (StateLayout) findViewById(R.id.stateLayout);
    }

    @Override
    protected void initViews() {
        mStateLayout.setStateCallback(new StateLayout.StateCallback() {
            @Override
            public void reload() {
                request();
            }
        });
    }

    @Override
    protected void initComplete() {
        RxUtil.countDown(3)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<Long>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        if (aLong == 2) {
                            mStateLayout.error();
                        }
                    }
                });
    }

    private void request() {
        RxUtil.countDown(3)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<Long>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        if (aLong == 2) {
                            mStateLayout.success();
                        }
                    }
                });
    }
}
