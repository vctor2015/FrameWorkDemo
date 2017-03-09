package com.centanet.frameworkdemo.activities;

import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;

import com.centanet.framework.utils.SPUtil;
import com.centanet.frameworkdemo.R;
import com.centanet.frameworkdemo.constants.SPConst;
import com.orhanobut.logger.Logger;

import java.util.List;

import me.zhanghai.android.patternlock.PatternUtils;
import me.zhanghai.android.patternlock.PatternView;

/**
 * 描述:设置图案锁
 * <p>
 * Created by vctor2015 on 2016/12/7
 */

public class SettingPatternLockActivity extends BaseActivity {

    private static final String TAG = "SettingPatternLockActivity";

    private AppCompatTextView mAtvTips;
    private PatternView mPatternView;
    private String mFirstPattern;//第一次绘制结果

    @Override
    protected int layoutResId() {
        return R.layout.activity_setting_pattern_lock;
    }

    @Override
    protected void findViews() {
        setUniversalToolbar(R.string.title_setting_pattern);
        mAtvTips = (AppCompatTextView) findViewById(R.id.atv_tips);
        mPatternView = (PatternView) findViewById(R.id.patternView);
    }

    @Override
    protected void initViews() {
        mPatternView.setOnPatternListener(new PatternView.OnPatternListener() {
            @Override
            public void onPatternStart() {
                Logger.t(TAG).d("onPatternStart");
            }

            @Override
            public void onPatternCleared() {
                Logger.t(TAG).d("onPatternCleared");
            }

            @Override
            public void onPatternCellAdded(List<PatternView.Cell> pattern) {
                Logger.t(TAG).d("onPatternCellAdded : %s", pattern.toString());
            }

            @Override
            public void onPatternDetected(List<PatternView.Cell> pattern) {
                Logger.t(TAG).d("onPatternDetected : %s", pattern.toString());
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
                        if (TextUtils.isEmpty(mFirstPattern)) {
                            //第一次绘制
                            mPatternView.setDisplayMode(PatternView.DisplayMode.Correct);
                            mFirstPattern = PatternUtils.patternToSha1String(pattern);
                            patternCorrect();
                        } else if (PatternUtils.patternToSha1String(pattern)
                                .equals(mFirstPattern)) {
                            //2次绘制匹配成功
                            mPatternView.setDisplayMode(PatternView.DisplayMode.Correct);
                            patternConfirm();
                        } else {
                            //2次绘制不匹配
                            mPatternView.setDisplayMode(PatternView.DisplayMode.Wrong);
                            patternMatchError();
                        }
                        break;
                }
            }
        });
    }

    @Override
    protected void initComplete() {

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
     * 2次绘制不一致
     */
    @SuppressWarnings("deprecation")
    private void patternMatchError() {
        mAtvTips.setText(R.string.text_pattern_error_match);
        mAtvTips.setTextColor(getResources().getColor(R.color.colorAccent));
        mPatternView.clearPattern();
        mPatternView.setInputEnabled(true);
    }

    /**
     * 第一次绘制正确
     */
    @SuppressWarnings("deprecation")
    private void patternCorrect() {
        mAtvTips.setText(R.string.text_pattern_second);
        mAtvTips.setTextColor(getResources().getColor(
                R.color.material_default_deep_teal_500));
        mPatternView.clearPattern();
        mPatternView.setInputEnabled(true);
    }

    /**
     * 第二次绘制确认
     */
    @SuppressWarnings("deprecation")
    private void patternConfirm() {
        SPUtil.setString(SettingPatternLockActivity.this, SPConst.PATTERN_LOCK, mFirstPattern);
        mAtvTips.setText(R.string.text_pattern_complete);
        mAtvTips.setTextColor(getResources().getColor(
                R.color.material_default_deep_teal_500));
        toast(R.string.text_pattern_complete);
        setResult(RESULT_OK);
        finish();
    }
}
