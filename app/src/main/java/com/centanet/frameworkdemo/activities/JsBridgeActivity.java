package com.centanet.frameworkdemo.activities;

import android.view.View;
import android.webkit.WebView;

import com.centanet.frameworkdemo.R;
import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.BridgeWebViewClient;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.orhanobut.logger.Logger;

import java.util.Locale;

/**
 * Created by vctor2015 on 2016/11/18.
 * <p>
 * 描述:JavaScriptBridge与WebView互调
 */

public class JsBridgeActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "JsBridgeActivity";

    private BridgeWebView mWvBridge;

    @Override
    protected int layoutResId() {
        return R.layout.activity_jsbridge;
    }

    @Override
    protected void findViews() {
        setUniversalToolbar(R.string.title_js_bridge);
        mWvBridge = (BridgeWebView) findViewById(R.id.wv_bridge);
    }

    @Override
    protected void initViews() {
        mWvBridge.setWebViewClient(new BridgeWebViewClient(mWvBridge) {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Logger.t(TAG).d("shouldOverrideUrlLoading : %s", url);
                return super.shouldOverrideUrlLoading(view, url);
            }
        });

        mWvBridge.setDefaultHandler(new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                Logger.t(TAG).d("defaultHandler data : %s", data);
                toast(String.format(Locale.CHINA, "receiver from js : %s", data));
                function.onCallBack("defaultHandler");
            }
        });

        mWvBridge.registerHandler("getDeviceId", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                Logger.t(TAG).d("registerHandler [getDeviceId] : %s", data);
                toast(String.format(Locale.CHINA, "js request native : %s", data));
                function.onCallBack("\"DeviceId\":\"1234567890]\"");
            }
        });
    }

    @Override
    protected void initComplete() {
        mWvBridge.loadUrl("file:///android_asset/js.html");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_send:
                mWvBridge.callHandler("functionInJs", "native button click",
                        new CallBackFunction() {
                            @Override
                            public void onCallBack(String data) {
                                toast(String.format(Locale.CHINA, "native调用js,返回内容：%s", data));
                                Logger.t(TAG).d("callHandler onCallBack: %s", data);
                            }
                        });
                break;
            default:
                break;
        }
    }
}
