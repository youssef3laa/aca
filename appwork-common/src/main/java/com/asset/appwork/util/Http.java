package com.asset.appwork.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;

import java.io.IOException;
import java.util.Optional;

/**
 * Created by karim on 10/25/20.
 */
@Slf4j
public class Http {
    HttpClient client;
    Header header;
    String data;
    String contentType;
    Integer statusCode;
    String response;
    boolean doAuthentication;

    public Http(){
        this.client = new HttpClient();
    }

    public String getResponse(){
        return this.response;
    }

    public Http setHeader(String key, String value ){
        Optional.of(this.header).orElseGet(()-> new Header() );
        header.setName(key);
        header.setValue(value);
        return this;
    }

    public Http setData(String data){
        this.data = data;
        return this;
    }

    public Http get(String url){
        GetMethod method = new GetMethod(url);
        method.setRequestHeader(header);
        try {
            statusCode = this.client.executeMethod(method);
            response = method.getResponseBodyAsString();

        } catch (IOException e) {
           log.error(e.getMessage());
        }
        return this;
    }

    public Http setDoAuthentication(boolean doAuthentication){
        this.doAuthentication = doAuthentication;
        return this;
    }

    public Http post(String url){
        PostMethod method = new PostMethod(url);
        if (doAuthentication) method.setDoAuthentication(doAuthentication);

        try {
            if (header != null) method.setRequestHeader(header);
            contentType = Optional.of(contentType).orElseGet(()-> ContentType.JSON_REQUEST.getContentType());
            method.setRequestEntity(new StringRequestEntity(data, contentType, "UTF-8"));
            statusCode = this.client.executeMethod(method);
            response = method.getResponseBodyAsString();

        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return this;
    }

    public Http setContentType(ContentType requestType){
        this.contentType = requestType.getContentType();
        return this;
    }


    public enum ContentType{
        XML_REQUEST("text/xml"),
        FORM_REQUEST("application/x-www-form-urlencoded"),
        JSON_REQUEST("application/json");

        private final String contentType;
        private ContentType(String contentType) { this.contentType = contentType; }
        public String getContentType() { return contentType; }
    }

}
