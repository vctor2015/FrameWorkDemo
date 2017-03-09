package com.centanet.frameworkdemo.activities;

import com.centanet.frameworkdemo.R;

/**
 * 描述:日历控件
 * <p>
 * Created by vctor2015 on 2016/12/14
 */

public class CalendarActivity extends BaseActivity {

    @Override
    protected int layoutResId() {
        return R.layout.activity_calendar;
    }

    @Override
    protected void findViews() {
        setUniversalToolbar(R.string.title_calendar);
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initComplete() {

    }
}
