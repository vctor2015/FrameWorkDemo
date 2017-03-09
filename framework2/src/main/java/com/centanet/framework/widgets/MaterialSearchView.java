package com.centanet.framework.widgets;

import android.content.Context;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.centanet.framework.R;
import com.jakewharton.rxbinding.widget.RxTextView;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * 自定义SearchView
 * Created by vctor2015 on 16/2/18.
 */
@SuppressWarnings("unused")
public class MaterialSearchView extends RelativeLayout {

    private EditText mSearchTextView;
    private ImageButton mBtnActionVoice;
    private ImageButton mBtnActionEmpty;
    private String mOldQueryText = "";
    private boolean mClearingFocus;
    private OnQueryTextListener mOnQueryTextListener;
    private VoiceClick mVoiceClick;
    private Runnable mShowImeRunnable = new Runnable() {
        public void run() {
            showKeyboard();
        }
    };
    private OnClickListener mOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == mBtnActionEmpty) {
                mSearchTextView.setText("");
                mSearchTextView.requestFocus();
                setImeVisibility(true);
            } else if (v == mBtnActionVoice) {
                setImeVisibility(false);
                if (mVoiceClick != null) {
                    mVoiceClick.voiceClick();
                }
            }
        }
    };

    public MaterialSearchView(Context context) {
        this(context, null);
    }

    public MaterialSearchView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MaterialSearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView();
    }

    private void initView() {
        inflate(getContext(), R.layout.search_view, this);
        mSearchTextView = (EditText) findViewById(R.id.searchTextView);
        mBtnActionVoice = (ImageButton) findViewById(R.id.btn_action_voice);
        mBtnActionVoice.setOnClickListener(mOnClickListener);
        mBtnActionEmpty = (ImageButton) findViewById(R.id.btn_action_empty);
        mBtnActionEmpty.setOnClickListener(mOnClickListener);

        mSearchTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                onSubmitQuery();
                return true;
            }
        });

        Observable<CharSequence> textChangeObservable = RxTextView.textChanges(mSearchTextView);

        textChangeObservable.subscribe(new Action1<CharSequence>() {
            @Override
            public void call(CharSequence charSequence) {
                if (TextUtils.isEmpty(charSequence)) {
                    showClear(false);
                    showVoice(true);
                } else {
                    showClear(true);
                    showVoice(false);
                }
            }
        });

        textChangeObservable.debounce(400, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<CharSequence>() {
                    @Override
                    public void call(CharSequence charSequence) {
                        textChanged(charSequence);
                    }
                });

        mSearchTextView.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });

        setFocusable(true);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (hasWindowFocus && hasFocus()) {
            setImeVisibility(true);
        }
    }

    private void textChanged(CharSequence newText) {
        if (mOnQueryTextListener != null &&
                !TextUtils.equals(newText, mOldQueryText)) {
            mOnQueryTextListener.onQueryTextChange(newText.toString());
        }
        mOldQueryText = newText.toString();
    }

    private void onSubmitQuery() {
        CharSequence query = mSearchTextView.getText();
        if (query != null &&
                TextUtils.getTrimmedLength(query) > 0) {
            if (mOnQueryTextListener == null || mOnQueryTextListener.onQueryTextSubmit(
                    query.toString())) {
                setImeVisibility(false);
            }
        }
    }


    @Override
    public boolean requestFocus(int direction, Rect previouslyFocusedRect) {
        return !mClearingFocus && isFocusable() && super.requestFocus(direction,
                previouslyFocusedRect);
    }

    @Override
    public void clearFocus() {
        mClearingFocus = true;
        setImeVisibility(false);
        super.clearFocus();
        mSearchTextView.clearFocus();
        mClearingFocus = false;
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getWindowToken(), 0);
        }
    }

    private void showKeyboard() {
        if (mSearchTextView.hasFocus()) {
            mSearchTextView.clearFocus();
        }
        mSearchTextView.requestFocus();
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(mSearchTextView, 0);
        }
    }

    private void showClear(boolean show) {
        mBtnActionEmpty.setVisibility(show ? VISIBLE : GONE);
    }

    private void showVoice(boolean show) {
        mBtnActionVoice.setVisibility(show ? VISIBLE : GONE);
    }

    /**
     * 设置输入法状态
     */
    public void setImeVisibility(final boolean visible) {
        if (visible) {
            post(mShowImeRunnable);
        } else {
            removeCallbacks(mShowImeRunnable);
            hideKeyboard();
        }
    }

    /**
     * 搜索默认文本
     */
    public void setQueryHint(CharSequence hint) {
        mSearchTextView.setHint(hint == null ? "" : hint);
    }

    /**
     * 搜索内容
     */
    public void setQuery(CharSequence query, boolean submit) {
        mSearchTextView.setText(query);
        if (query != null) {
            mSearchTextView.setSelection(mSearchTextView.length());
        }

        // If the query is not empty and submit is requested, submit the query
        if (submit && !TextUtils.isEmpty(query)) {
            onSubmitQuery();
        }
    }

    /**
     * 立即执行textChanged
     */
    public void textChangedImmediately() {
        CharSequence query = mSearchTextView.getText();
        if (!TextUtils.isEmpty(query) &&
                (mOnQueryTextListener != null)) {
            mOnQueryTextListener.onQueryTextChange(query.toString());
        }
    }

    public void setOnQueryTextListener(OnQueryTextListener onQueryTextListener) {
        this.mOnQueryTextListener = onQueryTextListener;
    }

    public void setVoiceClick(VoiceClick voiceClick) {
        this.mVoiceClick = voiceClick;
    }

    /**
     * 搜索监听
     */
    public interface OnQueryTextListener {

        /**
         * 搜索内容提交
         */
        boolean onQueryTextSubmit(String query);

        /**
         * 搜索内容变化
         */
        boolean onQueryTextChange(String newText);
    }

    /**
     * 语音点击监听
     */
    public interface VoiceClick {

        /**
         * 点击语音按钮
         */
        void voiceClick();
    }
}
