package com.centanet.framework.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

/**
 * Created by vctor2015 on 16/7/14.
 * <p>
 * 描述:网络图片下载接收
 */

public class ImageDownloadReceiver extends BroadcastReceiver {

    private static final String TAG = "ImageDownloadReceiver";
    public static final String MSG = "MSG";
    public static final String ACTION = "android.intent.action.ACTION_IMAGE_DOWNLOAD";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            String msg = intent.getStringExtra(MSG);
            if (!TextUtils.isEmpty(msg)) {
                Logger.t(TAG).d(msg);
                Toast.makeText(context.getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
