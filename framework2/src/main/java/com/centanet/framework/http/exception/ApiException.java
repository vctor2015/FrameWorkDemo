package com.centanet.framework.http.exception;

/**
 * Created by vctor2015 on 2016/10/19.
 * <p>
 * 描述:API Exception
 */

public final class ApiException extends RuntimeException {

    public final int code;
    public final String message;

    public ApiException(int code) {
        this(code, null);
    }

    public ApiException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
}
