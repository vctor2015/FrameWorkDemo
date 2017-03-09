package com.centanet.frameworkdemo.activities;

import android.support.v7.widget.AppCompatTextView;

import com.centanet.frameworkdemo.BuildConfig;
import com.centanet.frameworkdemo.R;

import java.util.Locale;

/**
 * 描述:关于
 * <p>
 * Created by vctor2015 on 2016/12/6
 */

public class AboutActivity extends BaseActivity {

    private AppCompatTextView mAtvVersion;

    @Override
    protected int layoutResId() {
        return R.layout.activity_about;
    }

    @Override
    protected void findViews() {
        setUniversalToolbar(R.string.title_about);
        mAtvVersion = (AppCompatTextView) findViewById(R.id.atv_version);
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initComplete() {
        mAtvVersion.setText(String.format(Locale.CHINA, "V:%s", BuildConfig.VERSION_NAME));
    }
}
