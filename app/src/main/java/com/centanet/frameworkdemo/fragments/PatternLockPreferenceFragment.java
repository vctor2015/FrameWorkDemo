package com.centanet.frameworkdemo.fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;

import com.centanet.frameworkdemo.R;

/**
 * 描述:图案解锁偏好
 * <p>
 * Created by vctor2015 on 2016/12/7
 */

public class PatternLockPreferenceFragment extends PreferenceFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences_pattern_lock);
    }

}
