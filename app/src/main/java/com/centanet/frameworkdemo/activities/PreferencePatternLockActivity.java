package com.centanet.frameworkdemo.activities;

import com.centanet.frameworkdemo.R;
import com.centanet.frameworkdemo.fragments.PatternLockPreferenceFragment;

/**
 * 描述:图案偏好
 * <p>
 * Created by vctor2015 on 2016/12/6
 */

public class PreferencePatternLockActivity extends BaseActivity {


    @Override
    protected int layoutResId() {
        return R.layout.activity_preferences;
    }

    @Override
    protected void findViews() {
        setUniversalToolbar(R.string.title_preference_pattern);
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initComplete() {
        getFragmentManager().beginTransaction()
                .replace(R.id.fl_preferences, new PatternLockPreferenceFragment())
                .commit();
    }
}
