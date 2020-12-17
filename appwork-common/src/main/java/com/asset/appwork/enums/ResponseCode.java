package com.asset.appwork.enums;

import org.springframework.http.HttpStatus;

/**
 * Created by karim on 10/22/20.
 */
/**
 *  Define HTTP Status Codes In 100:149, 200:249, 300:349, 400:449, 500:549
 *  Define Customs Status Codes In 150:199, 250:299, 350:399, 450:499, 550:599
 */
public enum ResponseCode {
    /** Success **/
    SUCCESS(200, "Success", HttpStatus.OK),
    CREATED(201, "Created", HttpStatus.CONFLICT),
    NO_CONTENT(204, "No Content", HttpStatus.NO_CONTENT),
    // Define all custom success response, Start from 250 To 299
    ALREADY_EXIST(250, "Is Already Exist", HttpStatus.CONFLICT),


    /** Client errors **/
    BAD_REQUEST(400, "Bad Request", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED(401, "UNAUTHORIZED", HttpStatus.UNAUTHORIZED),
    PAYMENT_REQUIRED(402, "Payment Required", HttpStatus.PAYMENT_REQUIRED),
    FORBIDDEN(403, "Forbidden", HttpStatus.FORBIDDEN),
    NOT_FOUND(404, "Not Found", HttpStatus.NOT_FOUND),
    METHOD_NOT_ALLOWED(405, "Method Not Allowed", HttpStatus.METHOD_NOT_ALLOWED),
    REQUEST_TIMEOUT(408, "Request Timeout", HttpStatus.REQUEST_TIMEOUT),
    UNSUPPORTED_MEDIA_TYPE(415, "Unsupported Media Type", HttpStatus.UNSUPPORTED_MEDIA_TYPE),
    LOGIN_ERROR(499, "Method Not Allowed", HttpStatus.METHOD_NOT_ALLOWED),


    // Define all custom Profile errors request, Start from 450 To 499
    INVALID_AUTH(450, "INVALID_CREDENTIALS", HttpStatus.FORBIDDEN),
    INVALID_TOKEN(451, "INVALID_TOKEN", HttpStatus.FORBIDDEN),
    DUPRICATED_TOKEN(452, "DEPRECATED_TOKEN_YOU_HAVE_TO_LOGIN", HttpStatus.FORBIDDEN),
    UNSUPPORTED_FILE_TYPE(453, "UNSUPPORTED_FILE_TYPE", HttpStatus.FORBIDDEN),
    EXCEED_MAX_SIZE(454, "EXCEED_MAX_SIZE", HttpStatus.FORBIDDEN),
    NOT_EXIST(455, "NOT_EXIST", HttpStatus.NOT_FOUND),

    /** Server errors **/
    INTERNAL_SERVER_ERROR(500, "INTERNAL_SERVER_ERROR", HttpStatus.INTERNAL_SERVER_ERROR),
    NOT_IMPLEMENTED(501, "NOT_IMPLEMENTED", HttpStatus.NOT_IMPLEMENTED),
    DATABASE_ERROR(503, "SERVICE_UNAVAILABLE", HttpStatus.SERVICE_UNAVAILABLE),

    // Define More Detailed Failure Response, Start from 550 To 599
    NO_DATA_SAVED(501, "INTERNAL_SERVER_ERROR", HttpStatus.INTERNAL_SERVER_ERROR),
    GENERAL_FAILURE(555, "INTERNAL_SERVER_ERROR", HttpStatus.INTERNAL_SERVER_ERROR);


    private int code;
    private String message;
    private HttpStatus httpStatus;

    ResponseCode(int code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    /**
     * Gets the HTTP status code
     * @return the status code number
     */
    public int getCode() {
        return code;
    }

    /**
     * Get the description
     * @return the description of the status code
     */
    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }


    /**
     * Get the description
     * @return the description of the status code
     */
    public String getMessageWithPrefix(String prefix) {
        return (prefix != null ? " " + prefix : "") + message;
    }

    /**
     * Get the description
     * @return the description of the status code
     */
    public String getMessageWithPostfix(String postfix) {
        return  message + (postfix != null ? " " + postfix : "");
    }
}
