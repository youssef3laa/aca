package com.asset.appwork.response;

import com.asset.appwork.otds.enums.ResponseCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Created by karim on 10/22/20.
 */
@Getter
@ToString
public class Error {
    private int status;
    private String message;

    public Error(ResponseCode responseCode) {

        if(responseCode != null && responseCode != ResponseCode.SUCCESS) {
            this.status = responseCode.getCode();
            this.message = responseCode.getMessage();
        }

    }

    public Error(ResponseCode responseCode, String message) {


        if(responseCode != null && responseCode != ResponseCode.SUCCESS) {
            this.status = responseCode.getCode();
            this.message = message;
        }

    }
}
