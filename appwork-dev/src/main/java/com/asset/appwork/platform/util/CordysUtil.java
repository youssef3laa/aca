package com.asset.appwork.platform.util;

import com.asset.appwork.dto.Account;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.util.Http;
import com.fasterxml.jackson.core.JsonProcessingException;

public class CordysUtil {
    String cordysUrl;
    public CordysUtil(String cordysUrl){
        this.cordysUrl = cordysUrl;
    }


    public String sendRequest(String message){
        Http http = new Http().setContentType(Http.ContentType.XML_REQUEST)
                .setData(message)
                .post(cordysUrl);
        return http.getResponse();
    }


    public String sendRequest(Account account, String message) throws JsonProcessingException, AppworkException {
        // @TODO Add URL
        Http http = new Http().setDoAuthentication(true).setContentType(Http.ContentType.XML_REQUEST).setData(message).post(cordysUrl+"?SAMLart="+account.getTicket());
        // @TODO re-login
        return http.getResponse();
    }
}
