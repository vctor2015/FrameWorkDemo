package com.centanet.frameworkdemo.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.centanet.framework.utils.RxUtil;
import com.centanet.frameworkdemo.R;
import com.trello.rxlifecycle.ActivityEvent;

import rx.functions.Action1;

/**
 * 描述:启动页面
 * <p>
 * Created by vctor2015 on 2016/12/13
 */

public class SplashActivity extends BaseActivity {

    @Override
    protected int layoutResId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void findViews() {

    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initComplete() {
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(
                this);
        RxUtil.countDown(2)
                .compose(this.<Long>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        if (aLong == 1) {
                            if (sharedPreferences.getBoolean(
                                    getString(R.string.key_pattern_lock_switch), false)) {
                                Intent[] intents = new Intent[2];
                                intents[0] = new Intent(SplashActivity.this,
                                        MainActivity.class);
                                intents[1] = new Intent(SplashActivity.this,
                                        PatternUnlockActivity.class);
                                startActivities(intents);
                            } else {
                                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                            }
                            finish();
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {

    }
}
