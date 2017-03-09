package com.centanet.frameworkdemo.activities;

import android.os.AsyncTask;
import android.os.SystemClock;
import android.view.View;

import com.centanet.frameworkdemo.R;

/**
 * 描述:检查内存泄漏测试
 * <p>
 * Created by vctor2015 on 2016/12/16
 */

public class LeakCanaryActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected int layoutResId() {
        return R.layout.activity_leak_canary;
    }

    @Override
    protected void findViews() {
        setUniversalToolbar(R.string.title_leak_canary);
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initComplete() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_task:
                startAsyncTask();
                break;
            default:
                break;
        }
    }

    void startAsyncTask() {
        // This async task is an anonymous class and therefore has a hidden reference to the outer
        // class MainActivity. If the activity gets destroyed before the task finishes (e.g.
        // rotation),
        // the activity instance will leak.
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                // Do some slow work in background
                SystemClock.sleep(10000);
                return null;
            }
        }.execute();
    }
}
