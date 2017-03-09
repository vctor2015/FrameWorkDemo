package com.centanet.frameworkdemo.activities;

import com.centanet.frameworkdemo.R;
import com.centanet.frameworkdemo.fragments.SettingPreferenceFragment;

/**
 * 描述:设置
 * <p>
 * Created by vctor2015 on 2016/12/6
 */

public class PreferenceSettingActivity extends BaseActivity {


    @Override
    protected int layoutResId() {
        return R.layout.activity_preferences;
    }

    @Override
    protected void findViews() {
        setUniversalToolbar(R.string.title_preference);
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initComplete() {
        getFragmentManager().beginTransaction()
                .replace(R.id.fl_preferences, new SettingPreferenceFragment())
                .commit();
    }
}
