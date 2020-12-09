package com.asset.appwork.exception;

import com.asset.appwork.otds.enums.ResponseCode;

/**
 * Created by karim on 10/29/20.
 */
public class AppworkException extends Exception {
    ResponseCode responseCode;

    public AppworkException(ResponseCode code) {
        super();
        this.responseCode = code;
    }

    public AppworkException(String message, Throwable cause, ResponseCode code) {
        super(message, cause);
        this.responseCode = code;
    }

    public AppworkException(String message, ResponseCode code) {
        super(message);
        this.responseCode = code;
    }

    public AppworkException(Throwable cause, ResponseCode code) {
        super(cause);
        this.responseCode = code;
    }

    public ResponseCode getCode() {
        return this.responseCode;
    }
}
