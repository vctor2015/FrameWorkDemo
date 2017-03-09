package com.centanet.frameworkdemo.activities;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.centanet.framework.iml.SwipeItemCallback;
import com.centanet.frameworkdemo.R;
import com.centanet.frameworkdemo.adapters.MainAdapter;
import com.taobao.hotfix.HotFixManager;

import java.util.ArrayList;

/**
 * Created by vctor2015 on 2016/11/17.
 * <p>
 * 描述:主页 main
 */

public class MainActivity extends BaseActivity {

    private RecyclerView mRvMain;

    @Override
    protected int layoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void findViews() {
        setUniversalToolbar(R.string.app_name);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
        mRvMain = (RecyclerView) findViewById(R.id.rv_main);
    }

    @Override
    protected void initViews() {
        mRvMain.setLayoutManager(new LinearLayoutManager(this));
        mRvMain.setAdapter(new MainAdapter(this, initList(), new SwipeItemCallback<String>() {
            @Override
            public void callback(View view, int position, String s) {
                switch (position) {
                    case 0:
                        startActivity(new Intent(MainActivity.this, HttpActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(MainActivity.this, HttpOperatorsActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(MainActivity.this, GlideActivity.class));
                        break;
                    case 3:
                        startActivity(new Intent(MainActivity.this, JsBridgeActivity.class));
                        break;
                    case 4:
                        startActivity(
                                new Intent(MainActivity.this, SwipeRecyclerViewActivity.class));
                        break;
                    case 5:
                        startActivity(
                                new Intent(MainActivity.this, PreferenceSettingActivity.class));
                        break;
                    case 6:
                        startActivity(
                                new Intent(MainActivity.this, PreferencePatternLockActivity.class));
                        break;
                    case 7:
                        startActivity(
                                new Intent(MainActivity.this, CalendarActivity.class));
                        break;
                    case 8:
                        startActivity(
                                new Intent(MainActivity.this, FlowLayoutManagerActivity.class));
                        break;
                    case 9:
                        startActivity(
                                new Intent(MainActivity.this, LeakCanaryActivity.class));
                        break;
                    case 10:
                        startActivity(
                                new Intent(MainActivity.this, StateLayoutActivity.class));
                        break;
                    default:
                        break;
                }
            }
        }));
    }

    @Override
    protected void initComplete() {
        HotFixManager.getInstance().queryNewHotPatch();
    }

    private ArrayList<String> initList() {
        ArrayList<String> list = new ArrayList<>();
        list.add(getString(R.string.title_http));
        list.add(getString(R.string.title_http_operators));
        list.add(getString(R.string.title_glide));
        list.add(getString(R.string.title_js_bridge));
        list.add(getString(R.string.title_swipe_refresh));
        list.add(getString(R.string.title_preference));
        list.add(getString(R.string.title_preference_pattern));
        list.add(getString(R.string.title_calendar));
        list.add(getString(R.string.title_flow_layout_manager));
        list.add(getString(R.string.title_leak_canary));
        list.add(getString(R.string.title_state_layout));
        return list;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (R.id.about == item.getItemId()) {
            startActivity(new Intent(this, AboutActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
