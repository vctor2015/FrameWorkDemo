package com.centanet.frameworkdemo.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatTextView;

import com.centanet.framework.utils.SPUtil;
import com.centanet.frameworkdemo.R;
import com.centanet.frameworkdemo.constants.REQCode;
import com.centanet.frameworkdemo.constants.SPConst;
import com.jakewharton.rxbinding.view.RxView;
import com.orhanobut.logger.Logger;

import java.util.List;

import me.zhanghai.android.patternlock.PatternUtils;
import me.zhanghai.android.patternlock.PatternView;
import rx.functions.Action1;

/**
 * 描述:解锁
 * <p>
 * Created by vctor2015 on 2016/12/8
 */

public class PatternUnlockActivity extends BaseActivity {

    private AppCompatTextView mAtvTips;
    private PatternView mPatternView;
    private AppCompatTextView mAtvClear;

    private String mPatternLock;//设置的手势记录
    private int mRetryCount;//尝试次数

    @Override
    protected int layoutResId() {
        return R.layout.activity_pattern_unlock;
    }

    @Override
    protected void findViews() {
        setUniversalToolbar(R.string.title_pattern_unlock);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
        mAtvTips = (AppCompatTextView) findViewById(R.id.atv_tips);
        mPatternView = (PatternView) findViewById(R.id.patternView);
        mAtvClear = (AppCompatTextView) findViewById(R.id.atv_clear);
    }

    @Override
    protected void initViews() {
        mPatternView.setOnPatternListener(new PatternView.OnPatternListener() {
            @Override
            public void onPatternStart() {

            }

            @Override
            public void onPatternCleared() {

            }

            @Override
            public void onPatternCellAdded(List<PatternView.Cell> pattern) {

            }

            @Override
            public void onPatternDetected(List<PatternView.Cell> pattern) {
                mPatternView.setInputEnabled(false);
                switch (pattern.size()) {
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                        patternError();
                        mPatternView.setDisplayMode(PatternView.DisplayMode.Wrong);
                        break;
                    default:
                        if (PatternUtils.patternToSha1String(pattern)
                                .equals(mPatternLock)) {
                            mPatternView.setDisplayMode(PatternView.DisplayMode.Correct);
                            patternMatchCorrect();
                        } else {
                            mPatternView.setDisplayMode(PatternView.DisplayMode.Wrong);
                            patternMatchError();
                        }
                        break;
                }
            }
        });
        RxView.clicks(mAtvClear)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        clearPattern();
                    }
                });
    }

    @Override
    protected void initComplete() {
        initLock();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Logger.t("PatternUnlockActivity").d("onNewIntent");
        initLock();
    }

    @SuppressWarnings("deprecation")
    private void initLock() {
        mPatternLock = SPUtil.getString(this, SPConst.PATTERN_LOCK, null);
        mRetryCount = SPUtil.getInt(this, SPConst.PATTERN_ERROR_COUNT, 5);
        if (mRetryCount < 1) {
            mAtvTips.setTextColor(getResources().getColor(
                    R.color.colorAccent));
            mAtvTips.setText(
                    getString(R.string.text_pattern_unlock_error, mRetryCount));
            mPatternView.clearPattern();
            mPatternView.setInputEnabled(false);
        }
    }

    /**
     * 绘制少于4个点
     */
    @SuppressWarnings("deprecation")
    private void patternError() {
        mAtvTips.setText(R.string.text_pattern_error_too_short);
        mAtvTips.setTextColor(getResources().getColor(R.color.colorAccent));
        mPatternView.clearPattern();
        mPatternView.setInputEnabled(true);
    }

    /**
     * 验证错误
     */
    @SuppressWarnings("deprecation")
    private void patternMatchError() {
        mRetryCount--;
        SPUtil.setInt(this, SPConst.PATTERN_ERROR_COUNT, mRetryCount);
        if (mRetryCount > 0) {
            mAtvTips.setText(
                    getString(R.string.text_pattern_unlock_error, mRetryCount));
            mAtvTips.setTextColor(getResources().getColor(
                    R.color.colorAccent));
            mPatternView.clearPattern();
            mPatternView.setInputEnabled(true);
        } else {
            mAtvTips.setText(
                    getString(R.string.text_pattern_unlock_error, mRetryCount));
            mPatternView.clearPattern();
        }
    }

    /**
     * 验证成功
     */
    @SuppressWarnings("deprecation")
    private void patternMatchCorrect() {
        SPUtil.setInt(this, SPConst.PATTERN_ERROR_COUNT, 5);
        toast(R.string.text_pattern_unlock_correct);
        finish();
    }

    /**
     * 清除解锁密码
     */
    private void clearPattern() {
        new AlertDialog.Builder(this)
                .setMessage(R.string.dialog_pattern_reset)
                .setPositiveButton(R.string.reset, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivityForResult(new Intent(PatternUnlockActivity.this,
                                SettingPatternLockActivity.class), REQCode.SETTRING_PATTERN);
                    }
                })
                .setNegativeButton(R.string.dialog_cancel, null)
                .create()
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK) {
            switch (requestCode){
                case REQCode.SETTRING_PATTERN:
                    SPUtil.setInt(this, SPConst.PATTERN_ERROR_COUNT, 5);
                    toast(R.string.text_pattern_unlock_correct);
                    finish();
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onBackPressed() {

    }
}
