package com.centanet.frameworkdemo.enums;

/**
 * Created by vctor2015 on 2016/11/17.
 * <p>
 * 描述:干货资源
 */

public enum GHCategory {

    FULI(0,"福利"),
    ANDROID(1,"Android"),
    iOS(2,"iOS");

    private int mCode;
    private String mSource;

    GHCategory(int code, String source) {
        this.mCode = code;
        this.mSource = source;
    }

    public int getCode() {
        return mCode;
    }

    public String getSource() {
        return mSource;
    }
}
