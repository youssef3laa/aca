package com.asset.appwork.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.*;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.HttpHeaders;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by karim on 10/25/20.
 */
@Slf4j
public class Http {
    HttpClient client;
    Set<Header> headers;
    Object data;
    ContentType contentType;
    Integer statusCode;
    String response;
    boolean doAuthentication;

    public Http() {
        this.client = new HttpClient();
        headers = new HashSet<>();
    }

    public Http basicAuthentication(String username, String password) {
        String auth = username + ":" + password;
        byte[] encodedAuth = Base64.encodeBase64(
                auth.getBytes(StandardCharsets.ISO_8859_1));
        String authHeader = "Basic " + new String(encodedAuth);
        this.setHeader(HttpHeaders.AUTHORIZATION, authHeader);
        return this;
    }

    public String getResponse() {
        return this.response;
    }

    public Http setHeader(String key, String value) {
        Header tempHeader = new Header();
        tempHeader.setName(key);
        tempHeader.setValue(value);
        headers.add(tempHeader);
        return this;
    }

    public Http setData(Object data) {
        this.data = data;
        return this;
    }

    public Http get(String url) {
        GetMethod method = new GetMethod(url);
        if (!headers.isEmpty()) headers.forEach(headerVar -> {
            method.addRequestHeader(headerVar);
        });

        try {
            statusCode = this.client.executeMethod(method);
            response = method.getResponseBodyAsString();

        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return this;
    }

    public Http delete(String url) {
        DeleteMethod method = new DeleteMethod(url);
        if (!headers.isEmpty()) headers.forEach(headerVar -> {
            method.addRequestHeader(headerVar);
        });

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

            if (!headers.isEmpty()) headers.forEach(headerVar -> {
                method.addRequestHeader(headerVar);
            });

            method.setRequestEntity(setRequestEntity());
            statusCode = this.client.executeMethod(method);
            System.out.println("******************************");
            System.out.println(statusCode);
            System.out.println("******************************");
            response = method.getResponseBodyAsString();

        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return this;
    }

    public Http put(String url){
        PutMethod method = new PutMethod(url);
        if (doAuthentication) method.setDoAuthentication(doAuthentication);

        try {

            if (!headers.isEmpty()) headers.forEach(headerVar -> {
                method.addRequestHeader(headerVar);
            });

            method.setRequestEntity(setRequestEntity());
            statusCode = this.client.executeMethod(method);
            response = method.getResponseBodyAsString();

        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return this;
    }

    private RequestEntity setRequestEntity() throws UnsupportedEncodingException {
        switch (this.contentType) {
            case JSON_REQUEST:
            case XML_REQUEST:
                return new StringRequestEntity((String) this.data, this.contentType.getContentType(), StandardCharsets.UTF_8.name());
            case FORM_REQUEST:
                return new MultipartRequestEntity((Part[]) this.data, new HttpMethodParams());
            default:
                return null;
        }
    }

    public Http setContentType(ContentType requestType) {
        this.contentType = requestType;
        return this;
    }


    public enum ContentType {
        XML_REQUEST("text/xml"),
        FORM_REQUEST("application/x-www-form-urlencoded"),
        JSON_REQUEST("application/json");

        private final String contentType;

        ContentType(String contentType) {
            this.contentType = contentType;
        }

        public String getContentType() {
            return contentType;
        }
    }


}
