package com.centanet.framework.widgets;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.centanet.framework.R;


/**
 * 自定义对话框
 * Created by vctor2015 on 16/5/13.
 */
public class CProgressDialog extends Dialog {

    public CProgressDialog(Context context) {
        super(context, R.style.DefaultProgressDialog);
        this.setCancelable(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_progress_dialog);
    }
}
